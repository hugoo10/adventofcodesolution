package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;
import lombok.Builder;
import org.apache.commons.lang3.Range;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Problem05 extends Problem {
    private Set<Seed> seeds = new HashSet<>();
    private Set<Seed> rangeSeeds = new HashSet<>();
    private UltiMapper ultiMapper;

    public static void main(String[] args) {
        Problem.solve(Problem05.class);
    }

    @Override
    public void setupData() {
        if (lines.isEmpty()) return;
        Map<String, Set<SpecialMapping>> specialMappings = new HashMap<>();
        String mapperName = null;
        for (var line : lines) {
            if (line.isEmpty()) {
                mapperName = null;
            } else if (line.startsWith("seeds:")) {
                this.seeds = parseSimpleSeeds(line);
                this.rangeSeeds = parseRangeSeeds(line);
            } else if (line.contains("map:")) {
                mapperName = line;
                specialMappings.put(mapperName, new HashSet<>());
            } else {
                specialMappings.get(mapperName).add(parseSpecialMapping(line));
            }
        }

        this.ultiMapper = UltiMapper.builder()
                .seedToSoilMapper(new Mapper<>("seed-to-soil", specialMappings.get("seed-to-soil map:"), Soil::new))
                .soilToFertilizerMapper(new Mapper<>("soil-to-fertilizer", specialMappings.get("soil-to-fertilizer map:"), Fertilizer::new))
                .fertilizerToWaterMapper(new Mapper<>("fertilizer-to-water", specialMappings.get("fertilizer-to-water map:"), Water::new))
                .waterToLightMapper(new Mapper<>("water-to-light", specialMappings.get("water-to-light map:"), Light::new))
                .lightToTemperatureMapper(new Mapper<>("light-to-temperature", specialMappings.get("light-to-temperature map:"), Temperature::new))
                .temperatureToHumidityMapper(new Mapper<>("temperature-to-humidity", specialMappings.get("temperature-to-humidity map:"), Humidity::new))
                .humidityToLocationMapper(new Mapper<>("humidity-to-location", specialMappings.get("humidity-to-location map:"), Location::new))
                .build();
    }

    private SpecialMapping parseSpecialMapping(String line) {
        final var split = Arrays.stream(line.split(" ")).map(Long::parseLong).toList();
        return SpecialMapping.from(split.get(0), split.get(1), split.get(2));
    }

    private Set<Seed> parseSimpleSeeds(String line) {
        final var sub = line.replace("seeds: ", "");
        return Arrays.stream(sub.split(" "))
                .map(Long::parseLong)
                .map(i -> new Seed(i, i))
                .collect(Collectors.toCollection(HashSet::new));
    }

    private Set<Seed> parseRangeSeeds(String line) {
        final var sub = line.replace("seeds: ", "").split(" ");
        final var set = new HashSet<Seed>();
        for (var i = 0; i < sub.length; i += 2) {
            final var from = Long.parseLong(sub[i]);
            final var nb = Long.parseLong(sub[i + 1]);
            set.add(new Seed(from, from + nb - 1));
        }
        return set;
    }

    @Override
    public Object rule1() {
        return ultiMapper.map(this.seeds)
                .stream()
                .mapToLong(HukRange::from)
                .min();
    }

    @Override
    public Object rule2() {
        final var r = ultiMapper.map(this.rangeSeeds);
        return ultiMapper.map(this.rangeSeeds)
                .stream()
                .mapToLong(HukRange::from)
                .min();
    }

    @Builder
    record UltiMapper(Mapper<Seed, Soil> seedToSoilMapper,
                      Mapper<Soil, Fertilizer> soilToFertilizerMapper,
                      Mapper<Fertilizer, Water> fertilizerToWaterMapper,
                      Mapper<Water, Light> waterToLightMapper,
                      Mapper<Light, Temperature> lightToTemperatureMapper,
                      Mapper<Temperature, Humidity> temperatureToHumidityMapper,
                      Mapper<Humidity, Location> humidityToLocationMapper
    ) {
        UltiMapper {
            Objects.requireNonNull(seedToSoilMapper);
            Objects.requireNonNull(soilToFertilizerMapper);
            Objects.requireNonNull(fertilizerToWaterMapper);
            Objects.requireNonNull(waterToLightMapper);
            Objects.requireNonNull(lightToTemperatureMapper);
            Objects.requireNonNull(temperatureToHumidityMapper);
            Objects.requireNonNull(humidityToLocationMapper);
        }

        Collection<Location> map(Collection<Seed> seed) {
            final Function<Collection<Seed>, Set<Location>> mappingChain = ((Function<Collection<Seed>, Set<Soil>>) seedToSoilMapper::map)
                    .andThen(soilToFertilizerMapper::map)
                    .andThen(fertilizerToWaterMapper::map)
                    .andThen(waterToLightMapper::map)
                    .andThen(lightToTemperatureMapper::map)
                    .andThen(temperatureToHumidityMapper::map)
                    .andThen(humidityToLocationMapper::map);
            return mappingChain.apply(seed);
        }

    }

    record Mapper<I extends HukRange, O extends HukRange>(String name, Set<SpecialMapping> specialMappings,
                                                          BiFunction<Long, Long, O> constructor) {
        Mapper {
            Objects.requireNonNull(name);
            Objects.requireNonNull(specialMappings);
        }

        public Set<O> map(Collection<I> input) {
            return input.stream().map(this::mapOne)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toCollection(HashSet::new));
        }

        private Set<O> mapOne(I input) {
            final var completeTranslations = new HashSet<Translation>();
            final var inCompleteTranslations = new HashSet<Translation>();
            inCompleteTranslations.add(new Translation(new SimpleHukRange(input.from(), input.to())));

            for (var specialMapping : specialMappings) {
                final var toMap = new HashSet<>(inCompleteTranslations);
                inCompleteTranslations.clear();
                for (var t : toMap) {
                    specialMapping.mapIfPossible(t).forEach(subTrans -> {
                        if (subTrans.destination().isPresent()) {
                            completeTranslations.add(subTrans);
                        } else {
                            inCompleteTranslations.add(subTrans);
                        }
                    });
                }

            }
            return Stream.concat(inCompleteTranslations.stream().map(Translation::source), completeTranslations.stream().map(Translation::destination).map(Optional::get))
                    .map(range -> constructor.apply(range.from, range.to))
                    .collect(Collectors.toSet());
        }

    }

    record SpecialMapping(long from, long to, long diff) implements HukRange {
        static SpecialMapping from(long destinationStart, long sourceStart, long range) {
            final var diff = destinationStart - sourceStart;
            return new SpecialMapping(sourceStart, sourceStart + range - 1, diff);
        }

        Set<Translation> mapIfPossible(Translation translation) {
            if (translation.destination().isPresent()) {
                throw new RuntimeException("pas besoin de mapper");
            }


            final var range = Range.between(translation.source.from, translation.source.to);
            final var thisRange = Range.between(this.from, this.to);
            if (!thisRange.isOverlappedBy(range)) {
                return Set.of(translation);
            }
            final var set = new HashSet<Translation>();
            final var intersect = thisRange.intersectionWith(range);
            final var source = new SimpleHukRange(intersect.getMinimum(), intersect.getMaximum());
            set.add(new Translation(source, source.shift(diff)));
            if (thisRange.isAfter(range.getMinimum())) {
                final var notMapped = new SimpleHukRange(range.getMinimum(), thisRange.getMinimum() - 1);
                set.add(new Translation(notMapped));
            }
            if (thisRange.isBefore(range.getMaximum())) {
                final var notMapped = new SimpleHukRange(thisRange.getMaximum() + 1, range.getMaximum());
                set.add(new Translation(notMapped));
            }
            return set;
        }
    }

    static class Translation {
        private final SimpleHukRange source;
        private SimpleHukRange destination;

        public Translation(SimpleHukRange source, SimpleHukRange destination) {
            Objects.requireNonNull(source);
            this.source = source;
            this.destination = destination;
        }

        public Translation(SimpleHukRange source) {
            this(source, null);
        }

        Optional<SimpleHukRange> destination() {
            return Optional.ofNullable(this.destination);
        }

        SimpleHukRange source() {
            return this.source;
        }

    }

    interface HukRange {
        long from();

        long to();
    }

    record SpecialMappingResult(SimpleHukRange mappedSource, SimpleHukRange mappedDestination,
                                Set<SimpleHukRange> notMappedSources) {

    }

    record SimpleHukRange(long from, long to) implements HukRange {
        SimpleHukRange shift(long diff) {
            return new SimpleHukRange(from + diff, to + diff);
        }
    }

    record Seed(long from, long to) implements HukRange {
    }

    record Soil(long from, long to) implements HukRange {
    }

    record Fertilizer(long from, long to) implements HukRange {
    }

    record Water(long from, long to) implements HukRange {
    }

    record Light(long from, long to) implements HukRange {
    }

    record Temperature(long from, long to) implements HukRange {
    }

    record Humidity(long from, long to) implements HukRange {
    }

    record Location(long from, long to) implements HukRange {
    }
}
