package fr.kahlouch.coding._common.geometry;

public record Point2D(long x, long y) {
    public static Point2D of(String x, String y) {
        return new Point2D(Long.parseLong(x), Long.parseLong(y));
    }

    public Point2D east(long amount) {
        return new Point2D(x + amount, y);
    }

    public Point2D west(long amount) {
        return new Point2D(x - amount, y);
    }

    public Point2D north(long amount) {
        return new Point2D(x, y + amount);
    }

    public Point2D south(long amount) {
        return new Point2D(x, y - amount);
    }

    public Point2D applyDirection(long amount, Direction direction) {
        return switch (direction) {
            case EAST -> east(amount);
            case WEST -> west(amount);
            case NORTH -> north(amount);
            case SOUTH -> south(amount);
        };
    }

    public Point2D applyDirection(Direction direction) {
        return applyDirection(1, direction);
    }
}
