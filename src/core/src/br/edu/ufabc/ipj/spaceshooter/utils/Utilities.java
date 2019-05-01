package br.edu.ufabc.ipj.spaceshooter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class Utilities {
    public enum ScreenAxis{
        X,
        Y
    };
    
    public static final int GAME_WIDTH  = 1366;
    public static final int GAME_HEIGHT = 768;
	
    public static Vector3 convertCoordinates(float x, float y) {
	Vector3 pos = new Vector3();
	pos.x = ((x / (float)Gdx.graphics.getWidth())*GAME_WIDTH);
	pos.y = GAME_HEIGHT - ((y / (float)Gdx.graphics.getHeight())*GAME_HEIGHT);	
        return pos;
    }
	
    public static float getAngle(Vector3 pos1, Vector3 pos2) {
	return (float)Math.toDegrees(Math.atan((pos1.x-pos2.x)/(pos1.z-pos2.z)));
    }
    
    /**
     * Converts real screen coordinate to the relative game screen position
     * @param axis  : Axis of the current coordinate
     * @param value : Value of the current coordinate
     * @return      : Relative value of the current coordinate
     */
    public static int toGameCoordinates(ScreenAxis axis, int value){
        int newCoord;
        if (axis == ScreenAxis.X)
            newCoord = (int)(value * Utilities.GAME_WIDTH / (float)Gdx.graphics.getWidth());
        else 
            newCoord = (Utilities.GAME_HEIGHT) 
                    - (int)(value * Utilities.GAME_HEIGHT / (float)Gdx.graphics.getHeight());
        return newCoord;
    }
}
