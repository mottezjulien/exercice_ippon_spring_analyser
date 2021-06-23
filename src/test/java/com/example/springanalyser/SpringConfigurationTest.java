package com.example.springanalyser;

import com.example.springanalyser.noproxy.ConfigurationNoProxy;
import com.example.springanalyser.proxy.ConfigurationProxy;
import com.example.springanalyser.resources.MyProperty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpringConfigurationTest {

    @Test
    public void verifyValidConfigurationAndBuildBeanWithFactoryPattern() {
        ConfigurableApplicationContext context = SpringApplication.run(ConfigurationProxy.class, new String[0]);
        Object bean = context.getBean("myParameter");
        assertThat(bean).isInstanceOf(MyProperty.class);
        assertThat(bean.getClass().getSimpleName()).startsWith("MyProperty$$EnhancerBySpringCGLIB$$");
        SpringApplication.exit(context);
    }


    @Test
    public void throwExceptionWithNoProxyRequestParameter() {
        assertThatThrownBy(() -> SpringApplication.run(ConfigurationNoProxy.class, new String[0]))
                .isInstanceOf(UnsatisfiedDependencyException.class);
    }

}
