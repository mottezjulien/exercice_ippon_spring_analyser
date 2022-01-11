package com.example.simulateur.black.box.model;

public class SimuServer {

    private SimuSpringContext springContext;

    public SimuServer(SimuSpringConfig config) {
        init(config);
    }

    private void init(SimuSpringConfig config) {
        springContext = new SimuSpringContext(config);
        springContext.init();
    }

    public SimuHttpResponse call(SimuHttpRequest request) {
        return new SimuHttpResponse(springContext.call(request).orElse("404"));
    }

}
