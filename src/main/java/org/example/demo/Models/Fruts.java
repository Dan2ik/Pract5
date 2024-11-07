package org.example.demo.Models;

public class Fruts extends food{
    public Boolean isFresh;// свежий ли фрукт

    public Fruts(int kkal, String title, Boolean isFresh) {
        super(kkal, title);
        this.isFresh = isFresh;
    }

    @Override
    public String getDescription() {
        String isFreshString = this.isFresh ? "свежий" : "не свежий";
        return String.format("Фрукт %s", isFreshString);
    }

}
