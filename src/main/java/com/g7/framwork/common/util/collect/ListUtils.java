package com.g7.framwork.common.util.collect;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author dreamyao
 * @title
 * @date 2017/12/25 下午9:02
 * @since 1.0.0
 */
public final class ListUtils {

    private ListUtils() {
    }

    /**
     * 去除List中重复的对象
     * @param list       list集合
     * @param comparator 比较器
     * @param <T>        类型
     * @return 去重后的List集合
     */
    public static <T> List<T> distinct(List<T> list, Comparator<T> comparator) {
        if (CollectionUtils.isEmpty(list) || comparator == null) {
            return list;
        }
        List<T> newList = new ArrayList<>(list.size());
        list.stream().filter(t -> !contains(newList, t, comparator)).forEach(newList::add);
        return newList;
    }

    /**
     * 去除List中重复的Map
     * @param mapList list集合
     * @return 去重后的List集合
     */
    public static List<Map<String, Object>> distinctMap(List<Map<String, Object>> mapList) {
        return distinct(mapList, (o1, o2) -> {
            for (int i = 0; i < o1.keySet().size(); i++) {
                Iterator<String> keys = o1.keySet().iterator();
                while (keys.hasNext()) {
                    if (o1.get(keys.next()).toString().compareTo(o2.get(keys.next()).toString()) == 0) {
                        return 0;
                    }
                }
            }
            return 1;
        });
    }

    /**
     * 去除List中重复的Map,此方法效率有点低，后续重新实现
     * @param allTestCases 待去重待 List<Map<String, Object>> 结合
     */
    public static void toHeavy4ListMap(List<Map<String, Object>> allTestCases) {
        for (int i = 0; i < allTestCases.size(); i++) {
            for (int j = i + 1; j < allTestCases.size(); j++) {
                boolean isNoEqual = false;
                for (String key : allTestCases.get(i).keySet()) {
                    if (!allTestCases.get(i).get(key).equals(allTestCases.get(j).get(key))) {
                        isNoEqual = true;
                        break;
                    }
                }
                if (!isNoEqual) {
                    allTestCases.remove(j);
                    toHeavy4ListMap(allTestCases);
                }
            }
        }
    }

    /**
     * 判断List中是否包含此对象
     * @param list       list集合
     * @param target     目标对象
     * @param comparator 比较器
     * @param <T>        对象
     * @return true 或 false
     */
    private static <T> boolean contains(List<T> list, T target, Comparator<T> comparator) {
        for (T t : list) {
            if (comparator.compare(t, target) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据多属性进行list转map分组
     * @param list    要转换的集合
     * @param strings 作为key的string数组
     * @param <T>     集合里对象的泛型
     * @return list集合
     */
    public static <T> Map<String, List<T>> list2Map(List<? extends T> list, String... strings) {
        Map<String, List<T>> returnMap = new HashMap<>(16);
        try {
            for (T t : list) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : strings) {
                    // 通过反射获得私有属性,这里捕获获取不到属性异常
                    Field name1 = t.getClass().getDeclaredField(s);
                    // 获得访问和修改私有属性的权限
                    name1.setAccessible(true);
                    // 获得key值
                    String key = name1.get(t).toString();
                    stringBuilder.append(key);
                }
                String keyName = stringBuilder.toString();

                List<T> tempList = returnMap.get(keyName);
                if (tempList == null) {
                    tempList = new ArrayList<>();
                    tempList.add(t);
                    returnMap.put(keyName, tempList);
                } else {
                    tempList.add(t);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return returnMap;
    }

    /**
     * 通用的从结果集中取第一条的数据
     * @param list 结果集
     * @param <T>  结果集中的数据类型
     * @return 其中的一条数据
     */
    public static <T> T selectOne(List<T> list) {
        if (Boolean.FALSE.equals(CollectionUtils.isEmpty(list))) {
            return list.get(0);
        }
        return null;
    }
}
