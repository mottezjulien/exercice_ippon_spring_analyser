package com.example.springanalyser.resources;

import java.util.UUID;

public class MyProperty {

    private UUID id = UUID.randomUUID();

    public String id() {
        return id.toString();
    }

}