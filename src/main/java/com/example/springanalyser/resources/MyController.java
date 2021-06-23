package com.example.springanalyser.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Autowired
    private MyProperty myRequestParameter;

    @GetMapping("/id")
    public String value() {
        return myRequestParameter.id();
    }

    @GetMapping("/className")
    public String className() {
        return myRequestParameter.getClass().getSimpleName();
    }





}
