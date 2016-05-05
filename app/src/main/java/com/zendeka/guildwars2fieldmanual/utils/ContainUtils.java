package com.zendeka.guildwars2fieldmanual.utils;

/**
 * Created by lawrence on 3/21/15.
 */
public class ContainUtils {
    public static boolean contains(final int[] values, final int value) {
        for (final int v : values) {
            if (v == value) {
                return true;
            }
        }

        return false;
    }
}
