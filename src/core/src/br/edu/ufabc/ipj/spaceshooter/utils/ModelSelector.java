package br.edu.ufabc.ipj.spaceshooter.utils;

public enum ModelSelector {
    SCIFI_UNDFINED(0),
    SCIFI_FIGHTER(1),
    SCIFI_COSAIR(2),
    SCIFI_STARSHIP(3),
    SCIFI_INTERGALACTIC(4);

    private final int value;

    ModelSelector(final int value){
        this.value = value;
    }

    public int getValue() {return value;}
    
    public static ModelSelector fromInteger(int i){
        switch(i){
            case 1: return SCIFI_FIGHTER;
            case 2: return SCIFI_COSAIR;
            case 3: return SCIFI_STARSHIP;
            case 4: return SCIFI_INTERGALACTIC;
        }
        return SCIFI_UNDFINED;
    }
}