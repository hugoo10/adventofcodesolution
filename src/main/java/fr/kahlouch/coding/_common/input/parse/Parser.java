package fr.kahlouch.coding._common.input.parse;

public interface Parser<T> {

    T parse(String input, long idx);
}
