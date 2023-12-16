package fr.kahlouch.coding._common.cryptography;

import org.apache.commons.codec.digest.DigestUtils;

public final class Hash {
    private Hash() {
    }

    public static String md5(String input) {
        return DigestUtils.md5Hex(input).toUpperCase();
    }
}
