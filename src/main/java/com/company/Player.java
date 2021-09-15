package com.company;

import com.company.messages.Gameboard;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private String name;
    private Map<String, Integer> cards;

    public Player(String name) {
        this.name = name;
        cards = new HashMap<>();
        cards.put("1", 3);
        cards.put("2", 2);
        cards.put("3", 1);
    }

    public void setCardsFromBoard(Gameboard gameboard) {
        if (name.equalsIgnoreCase("1"))
            cards = gameboard.getPlayerFirstCards();
        else
            cards = gameboard.getPlayerSecondCards();
    }

    /**
     * проверка может ли пользователь выложить свою карту
     */
    public boolean isPossibleToDecrease(String type) {
        return cards.get(type) - 1 >= 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getCards() {
        return cards;
    }

    public void setCards(Map<String, Integer> cards) {
        this.cards = cards;
    }
}
