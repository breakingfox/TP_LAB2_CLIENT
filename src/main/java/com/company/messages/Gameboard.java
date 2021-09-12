package com.company.messages;

import java.util.Map;

public class Gameboard {
    private String curPlayer;
    private String lastCard;
    private String lastToldCard;
    private Map<String, Integer> cards;
    private Map<String, Integer> playerFirstCards;
    private Map<String, Integer> playerSecondCards;

    public String getCurPlayer() {
        return curPlayer;
    }

    public void setCurPlayer(String curPlayer) {
        this.curPlayer = curPlayer;
    }

    public String getLastToldCard() {
        return lastToldCard;
    }

    public void setLastToldCard(String lastToldCard) {
        this.lastToldCard = lastToldCard;
    }

    public Map<String, Integer> getPlayerFirstCards() {
        return playerFirstCards;
    }

    public void setPlayerFirstCards(Map<String, Integer> playerFirstCards) {
        this.playerFirstCards = playerFirstCards;
    }

    public Map<String, Integer> getPlayerSecondCards() {
        return playerSecondCards;
    }

    public void setPlayerSecondCards(Map<String, Integer> playerSecondCards) {
        this.playerSecondCards = playerSecondCards;
    }


    public String getLastCard() {
        return lastCard;
    }

    public void setLastCard(String lastCard) {
        this.lastCard = lastCard;
    }

    public Map<String, Integer> getCards() {
        return cards;
    }

    public void setCards(Map<String, Integer> cards) {
        this.cards = cards;
    }


}
