package com.example.simulateur.black.box.model;

import com.example.ReflexivityUtils;
import com.example.simulateur.black.box.annotation.SimuMapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimuControllerBean {


    public static final class Builder {

        public static SimuControllerBean create(Class clazz) {
            Constructor selectedConstructor = ReflexivityUtils.yoloEasiestConstructor(clazz);
            List<SimuRequestBean> requestBeans = Arrays.stream(selectedConstructor.getParameters())
                    .map(param -> new SimuRequestBean((Class) param.getParameterizedType()))
                    .collect(Collectors.toList());
            Object value = ReflexivityUtils.yoloNewInstance(selectedConstructor, requestBeans.stream().map(requestBean -> requestBean.proxy()));
            return new SimuControllerBean(value, requestBeans);
        }
    }

    private final Object value;

    private final List<SimuRequestBean> requestBeans;

    public SimuControllerBean(Object value, List<SimuRequestBean> requestBeans) {
        this.value = value;
        this.requestBeans = requestBeans;
    }


    public boolean isMatch(SimuHttpRequest request) {
        return methods()
                .anyMatch(method -> isUrlMatch(method, request));
    }

    public String call(SimuHttpRequest request) {
        requestBeans.forEach(bean -> bean.malloc());
        String call = call(findMatch(request), request);
        requestBeans.forEach(bean -> bean.free());
        return call;
    }

    private String call(Optional<Method> optMethod, SimuHttpRequest request) {
        return optMethod.map(method -> {
            try {
                return (String) method.invoke(value, request.data());
            } catch (IllegalAccessException | InvocationTargetException e) {
                return "500";
            }
        }).orElse("404");
    }

    private Optional<Method> findMatch(SimuHttpRequest request) {
        return methods()
                .filter(method -> isUrlMatch(method, request)).findAny();
    }

    private boolean isUrlMatch(Method method, SimuHttpRequest request) {
        return ReflexivityUtils.hasAnnotation(method, SimuMapping.class) && method.getAnnotation(SimuMapping.class).value().equals(request.url());
    }

    private Stream<Method> methods() {
        return Arrays.stream(value.getClass().getMethods());
    }


}
