package com.example;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

public class ReflexivityUtils {

    public static boolean hasAnnotation(Class<?> clazz, Class annotationClazz) {
        return Arrays.stream(clazz.getAnnotations()).anyMatch(annotation -> annotation.annotationType().equals(annotationClazz));
    }

    public static boolean hasAnnotation(Method method, Class annotationClazz) {
        return Arrays.stream(method.getAnnotations()).anyMatch(annotation -> annotation.annotationType().equals(annotationClazz));
    }

    public static Stream<Class> findByPackageNames(String packageName) throws IOException {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return ClassPath.from(loader).getTopLevelClasses().stream()
                .filter(classInfo -> classInfo.getName().startsWith(packageName))
                .map(classInfo -> classInfo.load());
    }


    //TODO
    public static Constructor yoloEasiestConstructor(Class clazz) {
        return clazz.getConstructors()[0];
    }

    //TODO
    public static Object yoloNewInstance(Constructor constructor, Stream<Object> args) {
        try {
            return constructor.newInstance(args.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO
    public static Object yoloNewInstance(Class clazz) {
        try {
            return clazz.getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
