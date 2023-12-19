package fr.kahlouch.coding._common.math;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BinaryOperations {
    public static long or(long a, long b) {
        return a | b;
    }

    public static long and(long a, long b) {
        return a & b;
    }

    public static long xor(long a, long b) {
        return a ^ b;
    }

    public static long complement(long a) {
        return ~a;
    }

    public static long leftShift(long a, long shift) {
        return a << shift;
    }

    public static long signedRightShift(long a, long shift) {
        return a >> shift;
    }

    public static long unsignedRightShift(long a, long shift) {
        return a >>> shift;
    }

}
