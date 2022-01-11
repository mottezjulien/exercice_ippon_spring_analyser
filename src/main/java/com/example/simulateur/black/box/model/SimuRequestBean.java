package com.example.simulateur.black.box.model;

import com.example.ReflexivityUtils;
import com.example.simulateur.black.box.RequestScopeHandler;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SimuRequestBean {

    private final Class clazz;
    private final Object proxy;
    private final Map<Thread, Object> proxiedByThread = new HashMap<>();

    public SimuRequestBean(Class clazz) {
        this.clazz = clazz;
        InvocationHandler callBack = (obj, method, args) -> method.invoke(proxied(), args);
        proxy = Enhancer.create(clazz, callBack);
    }

    private Object proxied() {
        return proxiedByThread.get(Thread.currentThread());
    }

    public Object proxy() {
        return proxy;
    }

    public void malloc() {
        proxiedByThread.put(Thread.currentThread(), ReflexivityUtils.yoloNewInstance(clazz));
    }

    public void free() {
        proxiedByThread.remove(Thread.currentThread());
    }
}
