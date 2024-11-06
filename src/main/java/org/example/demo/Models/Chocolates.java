package org.example.demo.Models;

public class Chocolates extends food {
    public enum Type {white, black, milk;} // какие типы шоколада бывают

    private Type type;// а это собственно тип шоколада

    public Chocolates(int kkal, String title, Type type) {
        super(kkal, title);
        this.type = type;
    }

    @Override
    public String getDescription() {
        String typeString = "";
        switch (this.type) {
            case white:
                typeString = "белый";
                break;
            case black:
                typeString = "черный";
                break;
            case milk:
                typeString = "молочный";
                break;
        }
        return String.format("Шоколад %s", typeString);

    }
}
