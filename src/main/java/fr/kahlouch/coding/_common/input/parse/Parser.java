package fr.kahlouch.coding._common.input.parse;

@FunctionalInterface
public interface Parser<T> {

    T parse(String input, long idx);
}
