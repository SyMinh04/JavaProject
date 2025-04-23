package com.example.stu.Core.Utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for mapping between objects
 */
public class ObjectMapper {

    /**
     * Maps properties from source object to target object
     * @param source Source object
     * @param target Target object
     */
    public static void map(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    /**
     * Maps properties from source object to target object, ignoring null properties
     * @param source Source object
     * @param target Target object
     */
    public static void mapNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * Creates a new instance of the target class and maps properties from source object
     * @param source Source object
     * @param targetClass Target class
     * @return New instance of target class with properties mapped from source
     */
    public static <T> T map(Object source, Class<T> targetClass) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping object to " + targetClass.getName(), e);
        }
    }

    /**
     * Gets the names of null properties in the source object
     * @param source Source object
     * @return Array of property names that are null
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}