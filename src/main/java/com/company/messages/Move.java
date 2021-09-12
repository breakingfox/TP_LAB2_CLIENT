package com.company.messages;

//класс для джейсона в котором описывается как сходил игрок
public class Move {

    private boolean isBelieve;
    private String card;
    private String toldCard;

    public String getToldCard() {
        return toldCard;
    }

    public void setToldCard(String toldCard) {
        this.toldCard = toldCard;
    }

    public boolean isBelieve() {
        return isBelieve;
    }

    public void setBelieve(boolean believe) {
        isBelieve = believe;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

}
