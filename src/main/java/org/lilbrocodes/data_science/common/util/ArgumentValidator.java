package org.lilbrocodes.data_science.common.util;

import java.util.ArrayList;
import java.util.List;

public class ArgumentValidator {
    public static List<Object> validateArgs(Object[] args, Class<?>... expected) {
        return validateArgs(args, true, expected);
    }

    public static List<Object> validateArgs(Object[] args, boolean strict, Class<?>... expected) {
        if (args.length < expected.length) {
            return new ArrayList<>();
        }

        if (strict && args.length > expected.length) {
            return new ArrayList<>();
        }

        List<Object> castedArgs = new ArrayList<>(expected.length);

        for (int i = 0; i < expected.length; i++) {
            Object arg = args[i];
            Class<?> type = expected[i];

            if (arg == null) {
                return new ArrayList<>();
            }

            Class<?> boxedType = boxPrimitive(type);

            if (!boxedType.isInstance(arg)) {
                return new ArrayList<>();
            }

            castedArgs.add(boxedType.cast(arg));
        }

        return castedArgs;
    }

    private static Class<?> boxPrimitive(Class<?> clazz) {
        if (!clazz.isPrimitive()) return clazz;
        if (clazz == int.class) return Integer.class;
        if (clazz == long.class) return Long.class;
        if (clazz == float.class) return Float.class;
        if (clazz == double.class) return Double.class;
        if (clazz == boolean.class) return Boolean.class;
        if (clazz == byte.class) return Byte.class;
        if (clazz == char.class) return Character.class;
        if (clazz == short.class) return Short.class;
        return clazz;
    }
}
