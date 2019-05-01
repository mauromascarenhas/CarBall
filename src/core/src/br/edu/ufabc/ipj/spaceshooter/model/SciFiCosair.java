package br.edu.ufabc.ipj.spaceshooter.model;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.core.GameObject;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

public class SciFiCosair extends AbstractModel{
        
    public static final float DEFAULT_SCALE = 0.006f;
    public static final float SHOT_RELOAD_TIME = 1.5f;
    
    public static boolean USES_MISSILE = false;
    
    private GameObject object;
    
    public SciFiCosair(){
        this(true);
    }
    
    public SciFiCosair(boolean visible){
        super(true, true);
        
        Model mIdle = SpaceShooterGame.assetManager.get("three_dimensional/scifi_cosair/cosair.g3db");
        object = new GameObject(mIdle, visible);
        
        object.updateBoxScale(DEFAULT_SCALE);
        object.transform.scale(DEFAULT_SCALE, DEFAULT_SCALE, DEFAULT_SCALE);
        object.transform.rotate(Vector3.Y, 180);
        
        for (Material m : object.materials)
            m.remove(ColorAttribute.Emissive);
    }
    
    @Override
    public void update(float delta){
        object.update(delta);
        
        if (this.isMoveable())
            object.updateBoundingBox();
    }

    @Override
    public GameObject getGameObject() {
        return object;
    }
    
    public void rotateSelection(float delta){
        object.transform.rotate(Vector3.Y, 30 * delta);
        object.setAngle(30 * delta);
    }
    
    public static float getDefaultSpeed(){
        return - 15.0f;
    }
}
