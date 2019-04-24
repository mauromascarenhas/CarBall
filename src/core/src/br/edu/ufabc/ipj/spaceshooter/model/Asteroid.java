package br.edu.ufabc.ipj.spaceshooter.model;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.core.GameObject;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

public class Asteroid extends AbstractModel{
        
    public static final float DEFAULT_SCALE = 0.008f;
    
    private GameObject object;
    
    public Asteroid(){
        this(true);
    }
    
    public Asteroid(boolean visible){
        super(true, true);
        
        Model mIdle = SpaceShooterGame.assetManager.get("three_dimensional/asteroid/Asteroid.g3db");
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
        return 1;
    }
}
