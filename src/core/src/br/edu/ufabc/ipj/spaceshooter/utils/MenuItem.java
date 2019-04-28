package br.edu.ufabc.ipj.spaceshooter.utils;

public enum MenuItem {
    UNDEFINED(0),
    PLAY(1),
    CREDITS(2),
    QUIT(3);

    private final int value;

    MenuItem(final int value){
        this.value = value;
    }

    public int getValue() {return value;}
    
    public static MenuItem fromInteger(int i){
        switch(i){
            case 1: return PLAY;
            case 2: return CREDITS;
            case 3: return QUIT;
        }
        return UNDEFINED;
    }
}