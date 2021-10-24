package com.company;

import com.company.messages.GameMsg;

import java.util.HashSet;

public class Player {
    private String name;


    private HashSet<String> strength;

    public Player(String name) {
        this.name = name;
        this.strength = new HashSet<>();
    }

    public void setStrengthFromGame(GameMsg game) {
        gameStrngth = game.getPlayerFirst()
        for (char x:game.)
        game.getPlayerSecond()
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashSet<String> getStrength() {
        return strength;
    }

    public void setStrength(HashSet<String> strength) {
        this.strength = strength;
    }
}
