package com.g7.framwork.common.util.reflect;

import java.util.HashMap;

/**
 * @author dreamyao
 * @title 基础类型检查
 * @date 2018/9/7 下午9:56
 * @since 1.0.0
 */
public class PrimitiveUtils {


    private static HashMap<String, String> basePrimitiveType = new HashMap<>();

    static {
        basePrimitiveType.put("java.lang.Character", "char");
        basePrimitiveType.put("java.lang.Byte", "byte");
        basePrimitiveType.put("java.lang.Short", "short");
        basePrimitiveType.put("java.lang.Integer", "int");
        basePrimitiveType.put("java.lang.Long", "long");
        basePrimitiveType.put("java.lang.Float", "float");
        basePrimitiveType.put("java.lang.Double", "double");
        basePrimitiveType.put("java.lang.Void", "void");
    }

    /**
     * 是否基础类型（包装类和基础类）
     * @param clazz
     * @return
     */
    public static boolean isPrimitive(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        boolean is = isPackagedPrimitive(clazz);
        return is || isBasePrimitive(clazz);
    }

    /**
     * 是否基础类型（包装类和基础类）
     * @param fullType
     * @return
     */
    public static boolean isPrimitive(String fullType) {
        if (fullType == null) {
            return false;
        }
        boolean is = isPackagedPrimitive(fullType);
        return is || isBasePrimitive(fullType);
    }

    /**
     * 是否基础类型的包装类
     * @param clazz
     * @return
     */
    public static boolean isPackagedPrimitive(Class<?> clazz) {
        return clazz != null && isPackagedPrimitive(clazz.getName());
    }

    /**
     * 是否基础类型的包装类
     * @param fullType
     * @return
     */
    public static boolean isPackagedPrimitive(String fullType) {
        return fullType != null && basePrimitiveType.containsKey(fullType);
    }

    /**
     * 是否基础类型
     * @param clazz
     * @return
     */
    public static boolean isBasePrimitive(Class<?> clazz) {
        return clazz != null && clazz.isPrimitive();
    }

    /**
     * 是否基础类型
     * @param type
     * @return
     */
    public static boolean isBasePrimitive(String type) {
        return type != null && basePrimitiveType.containsValue(type);
    }

}
