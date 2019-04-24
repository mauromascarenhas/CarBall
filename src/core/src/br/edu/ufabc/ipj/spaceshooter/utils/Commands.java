package br.edu.ufabc.ipj.spaceshooter.utils;

public class Commands{
    public static enum Command {
        UP(0),
        DOWN(1),
        LEFT(2),
        RIGHT(3),
        SHOT(4);

        private final int value;

        Command(final int value){
            this.value = value;
        }

        public int getValue() {return value;}
    }
    
    public static boolean set[]  = 
        {   false,      // UP
            false,      // DOWN
            false,      // LEFT
            false,      // RIGHT
            false   };  // SHOT
    
    public static boolean noCommands(){
        return !hasCommand();
    }
    
    public static boolean hasCommand(){
        boolean res = false;
        for (int i = 0; i < set.length; ++i) res |= set[i];
        return res;
    }
    
    public static boolean hasCommand(Command command){
        return set[command.getValue()];
    }
}
