package com.example.springanalyser.noproxy;

import com.example.springanalyser.resources.MyProperty;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@ComponentScan("com.example.springanalyser.resources")
@SpringBootApplication
public class ConfigurationNoProxy {

	@Bean
	@Scope(value="request", proxyMode = ScopedProxyMode.NO)
	public MyProperty myParameter() {
		return new MyProperty();
	}

}
