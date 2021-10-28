package com.company;

import com.company.messages.GameMsg;

import java.util.HashSet;

public class Player {
    private String name;


    private String strength;

    public Player(String name) {
        this.name = name;
        this.strength = "";
    }

    public void setStrengthFromGame(GameMsg game) {
        if (name.equalsIgnoreCase("1"))
            this.strength = game.getPlayerFirst();
        else
            this.strength = game.getPlayerSecond();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }
}
