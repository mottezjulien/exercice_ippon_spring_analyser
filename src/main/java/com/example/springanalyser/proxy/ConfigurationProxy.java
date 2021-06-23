package com.example.springanalyser.proxy;

import com.example.springanalyser.resources.MyProperty;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@ComponentScan("com.example.springanalyser.resources")
@SpringBootApplication
public class ConfigurationProxy {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS) // or @RequestScope
    public MyProperty myParameter() {
        return new MyProperty();
    }

}
