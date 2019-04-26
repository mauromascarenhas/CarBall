package br.edu.ufabc.ipj.spaceshooter.utils;

public enum DifficultySelector {
    UNDEFINED(0),
    EASY(1),
    MEDIUM(2),
    HARD(3);

    private final int value;

    DifficultySelector(final int value){
        this.value = value;
    }

    public int getValue() {return value;}
    
    public static DifficultySelector fromInteger(int i){
        switch(i){
            case 1: return EASY;
            case 2: return MEDIUM;
            case 3: return HARD;
        }
        return UNDEFINED;
    }
}