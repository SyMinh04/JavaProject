package com.example.stu.Core.Auth;

import com.example.stu.Core.Enums.UserType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify which user types are allowed to access a controller or method
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireUserType {
    /**
     * The user types allowed to access the endpoint
     * @return Array of allowed user types
     */
    UserType[] value();
}