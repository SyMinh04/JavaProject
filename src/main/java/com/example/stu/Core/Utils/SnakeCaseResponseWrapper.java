package com.example.stu.Core.Utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessorFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper class for converting object properties to snake_case format
 */
public class SnakeCaseResponseWrapper {

    /**
     * Converts an object's properties from camelCase to snake_case
     * @param source The source object with camelCase properties
     * @param targetClass The target class to create
     * @return A new instance of the target class with snake_case properties
     */
    public static <T> T wrapToSnakeCase(Object source, Class<T> targetClass) {
        try {
            // Create a new instance of the target class
            T target = targetClass.getDeclaredConstructor().newInstance();
            
            // Get property descriptors from the source object
            BeanWrapper sourceBeanWrapper = new BeanWrapperImpl(source);
            PropertyDescriptor[] propertyDescriptors = sourceBeanWrapper.getPropertyDescriptors();
            
            // Get the bean wrapper for the target object
            BeanWrapper targetBeanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(target);
            
            // Copy properties from source to target, converting names to snake_case
            for (PropertyDescriptor pd : propertyDescriptors) {
                String propertyName = pd.getName();
                
                // Skip "class" property
                if ("class".equals(propertyName)) {
                    continue;
                }
                
                // Get the property value from source
                Object value = sourceBeanWrapper.getPropertyValue(propertyName);
                
                // Convert property name to snake_case
                String snakeCasePropertyName = CaseConverter.camelToSnakeCase(propertyName);
                
                // Check if the target has this property
                if (hasProperty(targetClass, snakeCasePropertyName)) {
                    // Set the property in the target object
                    targetBeanWrapper.setPropertyValue(snakeCasePropertyName, value);
                }
            }
            
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Error creating snake_case response", e);
        }
    }
    
    /**
     * Checks if a class has a property with the given name
     */
    private static boolean hasProperty(Class<?> clazz, String propertyName) {
        try {
            return clazz.getDeclaredField(propertyName) != null;
        } catch (NoSuchFieldException e) {
            // If the field doesn't exist in this class, check the superclass
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && superClass != Object.class) {
                return hasProperty(superClass, propertyName);
            }
            return false;
        }
    }
    
    /**
     * Converts an object to a Map with snake_case keys
     * @param source The source object with camelCase properties
     * @return A Map with snake_case keys and corresponding values
     */
    public static Map<String, Object> toSnakeCaseMap(Object source) {
        Map<String, Object> result = new HashMap<>();
        BeanWrapper sourceBeanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = sourceBeanWrapper.getPropertyDescriptors();
        
        for (PropertyDescriptor pd : propertyDescriptors) {
            String propertyName = pd.getName();
            
            // Skip "class" property
            if ("class".equals(propertyName)) {
                continue;
            }
            
            // Get the property value
            Object value = sourceBeanWrapper.getPropertyValue(propertyName);
            
            // Convert property name to snake_case
            String snakeCasePropertyName = CaseConverter.camelToSnakeCase(propertyName);
            
            // Add to result map
            result.put(snakeCasePropertyName, value);
        }
        
        return result;
    }
}