package net.craftingstore.core.util;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayUtil {

    public static <T> T[][] splitArray(T[] arrayToSplit, int chunkSize) {
        if (chunkSize <= 0) {
            return (T[][]) Array.newInstance(arrayToSplit.getClass(), 0);
        }
        int rest = arrayToSplit.length % chunkSize;
        int chunks = arrayToSplit.length / chunkSize + (rest > 0 ? 1 : 0);
        T[][] arrays = (T[][]) Array.newInstance(arrayToSplit.getClass(), chunks);

        for (int i = 0; i < (rest > 0 ? chunks - 1 : chunks); i++) {
            arrays[i] = Arrays.copyOfRange(arrayToSplit, i * chunkSize, i * chunkSize + chunkSize);
        }
        if (rest > 0) {
            arrays[chunks - 1] = Arrays.copyOfRange(arrayToSplit, (chunks - 1) * chunkSize, (chunks - 1) * chunkSize + rest);
        }
        return arrays;
    }
}
