package br.edu.ufabc.ipj.spaceshooter.model;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.core.GameObject;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

public class Missile extends AbstractModel{
    
    public static float DEFAULT_SCALE = 0.5f;
    
    private GameObject object;
    
    public Missile(){
        this(true);
    }
    
    public Missile(boolean visible){
        super(true, true);
        
        Model mIdle = SpaceShooterGame.assetManager.get("three_dimensional/agm_missile/Missile_AGM-65.g3db");
        object = new GameObject(mIdle, visible);
        object.transform.rotate(Vector3.Y, 270);
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
        return 10.0f;
    }
}
