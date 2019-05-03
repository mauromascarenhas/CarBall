package br.edu.ufabc.ipj.spaceshooter.model;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.core.GameObject;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

public class SciFiIntergalactic extends AbstractModel{
    
    public static float DEFAULT_SCALE = 0.013f;
    public static float SHOT_RELOAD_TIME = 2;
    
    public static boolean USES_MISSILE = true;
    
    private GameObject object;
    
    public SciFiIntergalactic(){
        this(true);
    }
    
    public SciFiIntergalactic(boolean visible){
        super(true, true);
        
        Model mIdle = SpaceShooterGame.assetManager.get("three_dimensional/scifi_intergalactic/SciFi_Intergalactic.g3db");
        object = new GameObject(mIdle, visible);
        object.transform.scale(DEFAULT_SCALE, DEFAULT_SCALE, DEFAULT_SCALE);
        object.updateBoxScale(DEFAULT_SCALE);
        
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
        return 13.0f;
    }
}
