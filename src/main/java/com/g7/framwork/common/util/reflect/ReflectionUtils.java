package com.g7.framwork.common.util.reflect;

import org.springframework.asm.*;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author dreamyao
 * @title
 * @date 2017/12/25 下午8:59
 * @since 1.0.0
 */
public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    /**
     * 实例化Class对象
     * @param cls Class对象
     * @param <T> T
     * @return T
     */
    public static <T> T initFromClass(Class<? extends T> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 实例化Class对象
     * @param classes Class对象
     */
    public static <T> void initializeClass(Class<? extends T>... classes) {
        for (Class<?> clazz : classes) {
            try {
                Class.forName(clazz.getName(), true, clazz.getClassLoader());
            } catch (ClassNotFoundException e) {
                throw new AssertionError(e);
            }
        }
    }

    /**
     * 获取所有属性，包括父类对象的属性
     * @param clazz  Class对象
     * @param fields Field对象
     */
    public static void getAllFields(Class clazz, List<Field> fields) {
        if (clazz == null || fields == null || clazz == Object.class) {
            return;
        }
        Field[] fs = clazz.getDeclaredFields();
        fields.addAll(Arrays.asList(fs));
        getAllFields(clazz.getSuperclass(), fields);
    }

    /**
     * 获取所有属性，包括父类对象的属性
     * @param clazz Class对象
     */
    public static List<Field> getAllFieldsList(final Class<?> clazz) {
        final List<Field> fieldList = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(fieldList, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return fieldList;
    }

    /**
     * 获取单个类的属性
     * @param clazz
     * @param fields
     */
    public static void getFields(Class clazz, List<Field> fields) {
        if (clazz == null || fields == null || clazz == Object.class) {
            return;
        }
        Field[] newFields = clazz.getDeclaredFields();
        fields.addAll(Arrays.asList(newFields));
    }

    /**
     * 获取属性值
     * @param bean        实例
     * @param targetClass 属性所属class
     * @param fieldName   属性名
     * @return Object
     * @throws IllegalAccessException .{@link IllegalAccessException}
     */
    public static Object getFieldValue(Object bean, Class targetClass, String fieldName) throws IllegalAccessException {
        Optional<Field> fieldOptional = Arrays.stream(targetClass.getDeclaredFields())
                .filter(field -> field.getName().equals(fieldName))
                .findFirst();
        if (fieldOptional.isPresent()) {
            fieldOptional.get().setAccessible(true);
            return fieldOptional.get().get(bean);
        }
        return null;
    }

    /**
     * 获取属性值
     * @param bean        实例
     * @param targetClass 属性所属class
     * @param fieldName   属性名
     * @return Field
     * @throws IllegalAccessException .{@link IllegalAccessException}
     */
    public static Field getField(Object bean, Class targetClass, String fieldName) throws IllegalAccessException {
        List<Field> fields = new ArrayList<>();
        getAllFields(bean.getClass(), fields);
        // 第一次类型和属性名都满足才返回
        Optional<Field> oneNewFields = fields.stream()
                .filter(field -> field.getName().equals(fieldName) && field.getType() == targetClass)
                .findFirst();
        // 第2次类型和属性名满足一个即返回
        Optional<Field> twoNewFields = fields.stream()
                .filter(field -> field.getName().equals(fieldName) || field.getType() == targetClass)
                .findFirst();
        return oneNewFields.orElseGet(() -> twoNewFields.orElse(null));
    }

    /**
     * 从类的Class包含父类中获取给定名称的属性
     * @param clazz     Class对象
     * @param fieldName 字段名称
     * @return 字段属性
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        List<Field> fields = getAllFieldsList(clazz);
        Optional<Field> optional = fields.stream()
                .filter(field -> fieldName.equalsIgnoreCase(field.getName()))
                .findFirst();
        return optional.orElse(null);
    }

    /**
     * 获取属性值
     * @param bean           实例
     * @param targetClass    属性所属class
     * @param fieldClassName 属性名或属性所属类名
     * @return
     * @throws IllegalAccessException .{@link IllegalAccessException}
     */
    public static Object getFieldInstance(Object bean, Class<?> targetClass, String fieldClassName) throws IllegalAccessException {
        Field[] fields = targetClass.getDeclaredFields();
        Optional<Field> filterField = Arrays.stream(fields)
                .filter(field -> field.getName().equals(fieldClassName) || field.getType().toString().toLowerCase().endsWith("." + fieldClassName.toLowerCase()))
                .findFirst();
        if (filterField.isPresent()) {
            filterField.get().setAccessible(true);
            return filterField.get().get(bean);
        }
        return null;
    }

    /**
     * 只根据方法名称来获取method，有重载函数的不要调用
     * @param clazz      Class对象
     * @param methodName 方法名称
     * @return
     */
    public static Method getMethod(Class<?> clazz, String methodName) {
        Optional<Method> method = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().equalsIgnoreCase(methodName))
                .findFirst();
        return method.orElse(null);
    }

    /**
     * 在指定Class对象中获取指定方法
     * @param clazz          Class对象
     * @param methodName     方法名称
     * @param parameterTypes 入参Type
     * @return Method对象
     * @throws Exception .{@link Exception}
     */
    public static Method getMethod(Class<?> clazz, String methodName, final Class<?>... parameterTypes) throws Exception {
        Method method;
        try {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() == null) {
                throw new NoSuchMethodException(String.format("方法[%s]不存在！", methodName));
            } else {
                method = getMethod(clazz.getSuperclass(), methodName, parameterTypes);
            }
        }
        return method;
    }

    /**
     * 比较参数类型是否一致
     * @param types   asm的类型({@link Type})
     * @param clazzes java 类型({@link Class})
     * @return true or false
     */
    private static boolean sameType(Type[] types, Class<?>[] clazzes) {
        // 个数不同
        if (types.length != clazzes.length) {
            return false;
        }
        for (int i = 0; i < types.length; i++) {
            if (!Type.getType(clazzes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取方法的参数名
     * @param method 方法
     * @return 方法名称数组
     */
    public static String[] getParamNames(Method method) {
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        return u.getParameterNames(method);
    }

    /**
     * 获取方法的参数名
     * @param m 方法
     * @return 方法名称数组
     */
    public static String[] getMethodParamNames(final Method m) throws IOException {
        final String[] paramNames = new String[m.getParameterTypes().length];
        final String n = m.getDeclaringClass().getName();
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassReader cr = new ClassReader(n);
        cr.accept(new ClassVisitor(Opcodes.ASM4, cw) {
            @Override
            public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
                final Type[] args = Type.getArgumentTypes(desc);
                // 方法名相同并且参数个数相同
                if (!name.equals(m.getName())
                        || !sameType(args, m.getParameterTypes())) {
                    return super.visitMethod(access, name, desc, signature,
                            exceptions);
                }
                MethodVisitor v = cv.visitMethod(access, name, desc, signature,
                        exceptions);
                return new MethodVisitor(Opcodes.ASM4, v) {
                    @Override
                    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                        int i = index - 1;
                        // 如果是静态方法，则第一就是参数
                        // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
                        if (Modifier.isStatic(m.getModifiers())) {
                            i = index;
                        }
                        if (i >= 0 && i < paramNames.length) {
                            paramNames[i] = name;
                        }
                        super.visitLocalVariable(name, desc, signature, start, end, index);
                    }
                };
            }
        }, 0);
        return paramNames;
    }

    public static void set(Object obj, String propertyName, Object value) {
        try {
            Field f = getField(obj.getClass(), propertyName);
            f.setAccessible(true);
            f.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
