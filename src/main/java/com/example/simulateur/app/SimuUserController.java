package com.example.simulateur.app;

import com.example.simulateur.black.box.annotation.SimuController;
import com.example.simulateur.black.box.annotation.SimuMapping;

@SimuController
public class SimuUserController {

    private SimuUserRequestScope user;

    public SimuUserController(SimuUserRequestScope user) {
        this.user = user;
    }

    @SimuMapping("/login")
    public String value(String userName) {
        user.setUserName(userName);
        randomWait();
        return welcomeMessage();
    }

    private void randomWait() {
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String welcomeMessage() {
        SimuUserRequestScope user = this.user;
        return "Bienvenue " + user.getUserName();
    }
}








