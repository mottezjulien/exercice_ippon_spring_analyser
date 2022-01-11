package com.example.simulateur.black.box.model;

import com.example.ReflexivityUtils;
import com.example.simulateur.black.box.annotation.SimuController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class SimuSpringContext {

    private final SimuSpringConfig config;

    private List<SimuControllerBean> controllers = new ArrayList<>();

    public SimuSpringContext(SimuSpringConfig config) {
        this.config = config;
    }

    public void init() {
        try {
            controllers = ReflexivityUtils
                    .findByPackageNames(config.componentScan())
                    .filter(clazz -> ReflexivityUtils.hasAnnotation(clazz, SimuController.class))
                    .map(clazz -> SimuControllerBean.Builder.create(clazz))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<String> call(SimuHttpRequest request) {
        return findOneControllerWithUrlMatch(request)
                .map(controllerBean -> controllerBean.call(request));
    }

    private Optional<SimuControllerBean> findOneControllerWithUrlMatch(SimuHttpRequest request) {
        return controllers.stream()
                .filter(controller -> controller.isMatch(request))
                .findAny();
    }
}
