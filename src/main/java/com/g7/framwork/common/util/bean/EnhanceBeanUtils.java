package com.g7.framwork.common.util.bean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dreamyao
 * @title BeanUtils 增强版
 * @date 2018/9/7 下午9:56
 * @since 1.0.0
 */
public final class EnhanceBeanUtils {

    private static final Logger logger = LoggerFactory.getLogger(EnhanceBeanUtils.class);

    private EnhanceBeanUtils() {

    }

    /**
     * 拷贝源对象到目标对象
     * @param source      源对象
     * @param targetClass 目标对象Class
     * @param <F>         源对象类型
     * @param <T>         目标对象类型
     * @return 目标对象
     */
    public static <F, T> T convert(F source, Class<T> targetClass) {
        return convert(source, targetClass, (String[]) null);
    }

    /**
     * 拷贝源对象到目标对象,可以忽略某些属性不拷贝
     * @param source           源对象
     * @param targetClass      目标对象Class
     * @param ignoreProperties 忽略属性集合
     * @param <F>              源对象类型
     * @param <T>              目标对象类型
     * @return 目标对象
     */
    public static <F, T> T convert(F source, Class<T> targetClass, String... ignoreProperties) {
        Assert.notNull(source, "source must not be null");
        Assert.notNull(targetClass, "target class must not be null");

        T t = BeanUtils.instantiateClass(targetClass);
        copyProperties4Heighten(source, t, ignoreProperties);
        return t;
    }

    /**
     * 将source集合里的元素转换为clazz的实例, 存入新list返回
     * @param source      源对象集合
     * @param targetClass 目标对象Class
     * @param <T>         目标对象类型
     * @return 目标对象集合
     */
    public static <T> List<T> convert(List<?> source, Class<T> targetClass) {
        return convert(source, targetClass, (String) null);
    }

    /**
     * 将source集合里的元素转换为clazz的实例, 存入新list返回，可以忽略某些属性不拷贝
     * @param source           源对象集合
     * @param targetClass      目标对象Class
     * @param ignoreProperties 忽略属性集合
     * @param <T>              目标对象类型
     * @return 目标对象集合
     */
    public static <T> List<T> convert(List<?> source, Class<T> targetClass, String... ignoreProperties) {
        Assert.notNull(source, "source must not be null");
        Assert.notNull(targetClass, "target class must not be null");

        List<T> dest = new ArrayList<>(source.size());
        copy(source, targetClass, dest, ignoreProperties);
        return dest;
    }

    private static <F, T> void copyProperties4Heighten(F source, T target, String... ignoreProperties) {

        PropertyDescriptor[] sourcePds = BeanUtils.getPropertyDescriptors(source.getClass());
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(target.getClass());

        // 源对象属性名称集合
        List<String> sourcePropertyNames = Arrays.stream(sourcePds).map(FeatureDescriptor::getName).collect(Collectors.toList());

        for (PropertyDescriptor targetPd : targetPds) {

            String targetPropertyName = targetPd.getName();

            // 需要忽略拷贝的属性集合
            List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);
            if (Boolean.FALSE.equals(CollectionUtils.isEmpty(ignoreList)) && ignoreList.contains(targetPropertyName)) {
                continue;
            }
            boolean contains = sourcePropertyNames.contains(targetPropertyName);
            // 属性拷贝规范，只有当目标对象所有属性完全包括在源对象的属性中是才能拷贝
            // Assert.isTrue(contains, "目标对象: " + targetPropertyName + " 属性，不存在于源对象属性中");
            if (!contains) {
                logger.warn("目标对象: {} 属性，不存在于源对象属性中", targetPropertyName);
                continue;
            }

            // 目标属性类型
            Class<?> targetPropertyType = targetPd.getPropertyType();

            PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPropertyName);
            // 源属性类型
            Class<?> sourcePropertyType = sourcePd.getPropertyType();

            // 如果目标属性的类型为基本类型，则返回true
            boolean targetPropertyTypeIsBasicType = isBasicTypeOrEnum(targetPropertyType);
            // 如果源属性的类型为基本类型，则返回true
            boolean sourcePropertyTypeIsBasicType = isBasicTypeOrEnum(sourcePropertyType);

            // 判断属性是否为 Iterable 的子类或子接口、Map的子类或子接口、数组对象
            boolean sourcePropertyTypeIsIterableOrMap = isIterableOrMapOrArray(sourcePropertyType);
            boolean targetPropertyTypesIsIterableOrMap = isIterableOrMapOrArray(targetPropertyType);

            boolean notIterableOrMap = Boolean.FALSE.equals(sourcePropertyTypeIsIterableOrMap) && Boolean.FALSE.equals(targetPropertyTypesIsIterableOrMap);

            Assert.isTrue(notIterableOrMap, "属性: " + targetPropertyName + " 不能为集合、Map、数组对象");

            // 如果目标对象和源对象都为自定义类型
            boolean isCustomizeObj = Boolean.FALSE.equals(targetPropertyTypeIsBasicType) && Boolean.FALSE.equals(sourcePropertyTypeIsBasicType) && Boolean.FALSE.equals("class".equals(targetPd.getName()));
            if (isCustomizeObj) {

                try {

                    Object sourceObj = FieldUtils.readDeclaredField(source, sourcePd.getName(), true);
                    Object targetObj = BeanUtils.instantiateClass(targetPropertyType);

                    if (Objects.nonNull(sourceObj)) {

                        // 检查嵌套自定义对象是否也满足此规范要求，如果满足这进行属性拷贝
                        copyProperties4Heighten(sourceObj, targetObj);

                        // 把拷贝属性后的嵌套对象，设置到外层目标对象的属性中
                        targetPd.getWriteMethod().invoke(target, targetObj);
                    }

                } catch (Exception e) {
                    throw new FatalBeanException("根据属性名称从对象中获取属性对象异常", e);
                }
            }

            // 枚举属性拷贝
            copyEnum2Enum(source, target, sourcePd, targetPd);

            // 特定属性转换
            convertProperties(source, target, sourcePd, targetPd);
        }
    }

    private static Boolean isIterableOrMapOrArray(Class<?> propertyType) {
        boolean isIterable = Iterable.class.isAssignableFrom(propertyType);
        boolean isMap = Map.class.isAssignableFrom(propertyType);
        boolean isArray = propertyType.isArray();
        return isIterable || isMap || isArray;
    }

    private static <F, T> void copyEnum2Enum(F source, T target, PropertyDescriptor sourcePd, PropertyDescriptor targetPd) throws BeansException {

        // enum to enum，原生BeanUtils的copyProperties无法在相同属性名称且类型不同的，枚举对象之间进行Copy
        if (targetPd.getPropertyType().isEnum() && sourcePd.getPropertyType().isEnum()) {
            String targetPropertyName = targetPd.getName();
            try {
                Object enumValue = sourcePd.getReadMethod().invoke(source);
                // 把枚举值转换为字符串
                String stringValue = enumValue == null ? null : enumValue.toString();

                Method method = targetPd.getPropertyType().getMethod("valueOf", String.class);

                // 把字符串转换为枚举
                Object value = stringValue == null ? null : method.invoke(null, stringValue);
                targetPd.getWriteMethod().invoke(target, value);

            } catch (Exception e) {
                throw new FatalBeanException("枚举属性：" + targetPropertyName + " 拷贝异常", e);
            }
        }
    }


    private static <F, T> void convertProperties(F source, T target, PropertyDescriptor sourcePd, PropertyDescriptor targetPd) throws BeansException {

        Class<?> targetPropertyType = targetPd.getPropertyType();
        Class<?> sourcePropertyType = sourcePd.getPropertyType();

        // Date时间转换毫秒时间
        if (sourcePropertyType.equals(Date.class) && targetPropertyType.equals(Long.class)) {
            convertDate2Long(source, target, sourcePd, targetPd);
            return;
        }

        // 毫秒时间转换Date时间
        if (sourcePropertyType.equals(Long.class) && targetPropertyType.equals(Date.class)) {
            convertLong2Date(source, target, sourcePd, targetPd);
            return;
        }

        // Enum对象转换为Number
        if (sourcePropertyType.isEnum() && Number.class.isAssignableFrom(targetPropertyType)) {
            convertEnum2Number(source, target, sourcePd, targetPd);
            return;
        }

        // Number转换为Enum对象
        if (Number.class.isAssignableFrom(sourcePropertyType) && targetPropertyType.isEnum()) {
            convertNumber2Enum(source, target, sourcePd, targetPd);
            return;
        }

        // Enum对象转换为String
        if (sourcePropertyType.isEnum() && targetPropertyType.equals(String.class)) {
            convertEnum2String(source, target, sourcePd, targetPd);
            return;
        }

        // String转换为Enum对象
        if (sourcePropertyType.equals(String.class) && targetPropertyType.isEnum()) {
            convertString2Enum(source, target, sourcePd, targetPd);
            return;
        }

        // Boolean转换为Number对象
        if (sourcePropertyType.equals(Boolean.class) && Number.class.isAssignableFrom(targetPropertyType)) {
            convertBoolean2Number(source, target, sourcePd, targetPd);
            return;
        }

        // Number转换为Boolean对象
        if (Number.class.isAssignableFrom(sourcePropertyType) && targetPropertyType.equals(Boolean.class)) {
            convertNumber2Boolean(source, target, sourcePd, targetPd);
            return;
        }

        // 如果目标属性的类型为基本类型，则返回true
        boolean targetPropertyTypeIsBasicType = isBasicTypeOrEnum(targetPropertyType);
        // 如果源属性的类型为基本类型，则返回true
        boolean sourcePropertyTypeIsBasicType = isBasicTypeOrEnum(sourcePropertyType);
        // 是否为自定义对象
        boolean isCustomizeObj = Boolean.FALSE.equals(targetPropertyTypeIsBasicType) && Boolean.FALSE.equals(sourcePropertyTypeIsBasicType) && Boolean.FALSE.equals("class".equals(targetPd.getName()));
        // 如果目标属性类型和源属性类型相同，，则返回true
        boolean targetTypeIsEqualsSourceType = Objects.equals(targetPropertyType, sourcePropertyType);

        Assert.isTrue(targetTypeIsEqualsSourceType || isCustomizeObj, "属性名称：" + targetPd.getName() + " 在源对象中和目标对象中类型不一致");

        // 拷贝类型相同的基本类型属性
        copyGeneralProperties(source, target, sourcePd, targetPd);
    }

    private static <F, T> void convertDate2Long(F source, T target, PropertyDescriptor sourcePd, PropertyDescriptor targetPd) throws BeansException {

        // Date to Long
        try {
            Object dateValue = sourcePd.getReadMethod().invoke(source);

            if (Objects.isNull(dateValue)) {
                return;
            }

            Long value = ((Date) dateValue).getTime();
            targetPd.getWriteMethod().invoke(target, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FatalBeanException("Date时间转换为毫秒时间时异常", e);
        }
    }

    private static <F, T> void convertLong2Date(F source, T target, PropertyDescriptor sourcePd, PropertyDescriptor targetPd) throws BeansException {

        // Long to Date
        try {
            Object longValue = sourcePd.getReadMethod().invoke(source);

            if (Objects.isNull(longValue)) {
                return;
            }

            // 毫秒 to Date
            Date value = getTime((Long) longValue);
            targetPd.getWriteMethod().invoke(target, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FatalBeanException("毫秒时间转换为Date时间时异常", e);
        }
    }

    private static <F, T> void convertEnum2Number(F source, T target, PropertyDescriptor sourcePd, PropertyDescriptor targetPd) throws BeansException {
        try {
            // 返回枚举属性的return对象
            Object enumObj = sourcePd.getReadMethod().invoke(source);
            // 获取枚举对象中的Integer code值
            Method getCodeMethod = sourcePd.getPropertyType().getMethod("getCode");

            // 把枚举值转换为Integer
            Object integerValue = Objects.isNull(enumObj) ? null : getCodeMethod.invoke(enumObj);

            if (Objects.isNull(integerValue)) {
                return;
            }

            if (targetPd.getPropertyType().equals(Integer.class)) {

                targetPd.getWriteMethod().invoke(target, integerValue);

            } else if (targetPd.getPropertyType().equals(Long.class)) {

                targetPd.getWriteMethod().invoke(target, Long.valueOf(integerValue.toString()));

            } else if (targetPd.getPropertyType().equals(Byte.class)) {

                targetPd.getWriteMethod().invoke(target, Byte.valueOf(integerValue.toString()));
            }

        } catch (NoSuchMethodException ne) {
            // 枚举中没有此方法不做任何处理
        } catch (Exception e) {
            throw new FatalBeanException("枚举对象转换为Integer值时异常", e);
        }
    }

    private static <F, T> void convertNumber2Enum(F source, T target, PropertyDescriptor sourcePd, PropertyDescriptor targetPd) throws BeansException {
        try {
            Object numberValue = sourcePd.getReadMethod().invoke(source);

            if (Objects.isNull(numberValue)) {
                return;
            }

            Method method = targetPd.getPropertyType().getMethod("valueOf", Integer.class);
            Object value = method.invoke(null, Integer.valueOf(numberValue.toString()));
            targetPd.getWriteMethod().invoke(target, value);
        } catch (NoSuchMethodException ne) {
            // 枚举中没有此方法不做任何处理
        } catch (Exception e) {
            throw new FatalBeanException("Integer值转换为枚举对象时异常", e);
        }
    }

    private static <F, T> void convertString2Enum(F source, T target, PropertyDescriptor sourcePd, PropertyDescriptor targetPd) {
        try {
            Object stringValue = sourcePd.getReadMethod().invoke(source);

            if (Objects.isNull(stringValue)) {
                return;
            }

            Method method = targetPd.getPropertyType().getMethod("valueOf", String.class);
            Object value = method.invoke(null, stringValue);
            targetPd.getWriteMethod().invoke(target, value);
        } catch (Exception e) {
            throw new FatalBeanException("String值转换为枚举对象时异常", e);
        }
    }

    private static <F, T> void convertEnum2String(F source, T target, PropertyDescriptor sourcePd, PropertyDescriptor targetPd) {
        try {
            Object enumValue = sourcePd.getReadMethod().invoke(source);

            if (Objects.isNull(enumValue)) {
                return;
            }

            String value = enumValue.toString();
            targetPd.getWriteMethod().invoke(target, value);
        } catch (Exception e) {
            throw new FatalBeanException("枚举对象转换为String值时异常", e);
        }
    }

    private static <F, T> void convertBoolean2Number(F source, T target, PropertyDescriptor sourcePd, PropertyDescriptor targetPd) {

        // Boolean to Number
        try {
            Object booleanValue = sourcePd.getReadMethod().invoke(source);

            if (Objects.isNull(booleanValue)) {
                return;
            }

            Integer value = (Boolean) booleanValue ? 1 : 0;

            if (targetPd.getPropertyType().equals(Integer.class)) {

                targetPd.getWriteMethod().invoke(target, value);

            } else if (targetPd.getPropertyType().equals(Long.class)) {

                targetPd.getWriteMethod().invoke(target, Long.valueOf(value.toString()));

            } else if (targetPd.getPropertyType().equals(Byte.class)) {

                targetPd.getWriteMethod().invoke(target, Byte.valueOf(value.toString()));
            }

        } catch (Exception e) {
            throw new FatalBeanException("Boolean转换为Number值时异常", e);
        }
    }

    private static <F, T> void convertNumber2Boolean(F source, T target, PropertyDescriptor sourcePd, PropertyDescriptor targetPd) {

        // Number to Boolean
        try {
            Object numberValue = sourcePd.getReadMethod().invoke(source);

            if (Objects.isNull(numberValue)) {
                return;
            }

            targetPd.getWriteMethod().invoke(target, toBoolean(numberValue.toString()));
        } catch (Exception e) {
            throw new FatalBeanException("Number转换为Boolean值时异常", e);
        }
    }

    /**
     * 表示为真的字符串,事实不可变，线程安全的
     */
    private static final List<String> TRUE_ARRAY = new ArrayList<>(Arrays.asList("true", "yes", "y", "t", "ok", "1", "on", "是", "对", "真"));

    /**
     * 转换字符串为boolean值
     *
     * @param valueStr 字符串
     * @return boolean值
     */
    private static boolean toBoolean(String valueStr) {
        if (StringUtils.isNotBlank(valueStr)) {
            valueStr = valueStr.trim().toLowerCase();
            return TRUE_ARRAY.contains(valueStr);
        }
        return false;
    }

    private static <F, T> void copyGeneralProperties(F source, T target, PropertyDescriptor sourcePd, PropertyDescriptor targetPd) throws BeansException {

        Class<?> targetPropertyType = targetPd.getPropertyType();

        // 拷贝除枚举之外的普通属性
        if (Boolean.FALSE.equals(targetPropertyType.isEnum()) && Boolean.FALSE.equals(sourcePd.getPropertyType().isEnum())) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {
                Method readMethod = sourcePd.getReadMethod();
                if (readMethod != null &&
                        ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                    try {
                        // 判断getter方法访问权限是否为public
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        // 判断setter方法访问权限是否为public
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                        writeMethod.invoke(target, value);
                    } catch (Throwable ex) {
                        throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                    }
                }
            }
        }
    }

    private static Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    private static boolean isBasicTypeOrEnum(Class clz) {
        try {

            if (clz.isEnum()) {
                return true;
            }

            // 是否基础类或者其包装类，String、Date、BigDecimal类型
            boolean isBasicType = clz.isPrimitive() || Arrays.asList("String", "Date", "BigDecimal").contains(clz.getSimpleName()) || ((Class) clz.getField("TYPE").get(null)).isPrimitive();

            // 如果当前类不是基本类型或包装器类型，则判断他的父类是否是基本类型或包装器类型
            if (Boolean.FALSE.equals(isBasicType)) {
                Class superclass = clz.getSuperclass();
                // 如果为null 说明当前类没有父类
                if (Objects.nonNull(superclass)) {
                    isBasicType = isBasicTypeOrEnum(superclass);
                }
            }
            return isBasicType;
        } catch (Exception e) {
            return false;
        }
    }

    private static <E> void copy(List<?> source, Class<E> clazz, List<E> dest, String... ignoreProperties) {
        Iterator<?> iterator = source.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            E e = convert(o, clazz, ignoreProperties);
            dest.add(e);
        }
    }

    /**
     * JavaBean转换为Map
     * @param obj JavaBean
     * @return Map
     */
    public static <T> Map<String, Object> object2Map(T obj) {

        if (obj == null) {
            return null;
        }

        try {
            Map<String, Object> map = new LinkedHashMap<>(32);
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(obj) : null;
                if (Objects.nonNull(value)) {
                    map.put(key, value);
                }
            }
            return map;
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Java bean to map error.", e);
        }
    }

    /**
     * Map转换为JavaBean
     * @param map       Map
     * @param beanClass JavaBean
     * @return
     */
    public static <T> T map2Object(Map<String, Object> map, Class<T> beanClass) {

        if (map == null) {
            return null;
        }

        try {

            T obj = beanClass.newInstance();

            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                Method setter = property.getWriteMethod();
                Object value = map.get(property.getName());
                if (setter != null && value != null) {

                    setter.invoke(obj, value);
                }
            }
            return obj;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IntrospectionException e) {
            throw new RuntimeException("Map to java bean error.", e);
        }
    }
}
