package com.example.simulateur.black.box;

import com.example.ReflexivityUtils;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RequestScopeHandler<Param> implements InvocationHandler {

    private final Class<Param> clazz;
    private Map<Long, Param> proxied = new HashMap<>();

    public RequestScopeHandler(Class<Param> clazz) {
        this.clazz = clazz;
    }

    public void malloc() {
        proxied.put(Thread.currentThread().getId(), (Param) ReflexivityUtils.yoloNewInstance(clazz));
    }

    public void free() {
        proxied.remove(Thread.currentThread().getId());
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxied.get(Thread.currentThread().getId()), args);
    }

}
