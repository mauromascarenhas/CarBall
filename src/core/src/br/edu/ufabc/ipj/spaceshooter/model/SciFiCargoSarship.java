package br.edu.ufabc.ipj.spaceshooter.model;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.core.GameObject;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

public class SciFiCargoSarship extends AbstractModel{
        
    private GameObject object;
    
    public SciFiCargoSarship(){
        this(true);
    }
    
    public SciFiCargoSarship(boolean visible){
        super(true, true);
        
        Model mIdle = SpaceShooterGame.assetManager.get("three_dimensional/scifi_cargostarship/scifi_cargostarship.g3db");
        object = new GameObject(mIdle, visible);
        object.transform.scale(0.05f, 0.05f, 0.05f);
        
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
    
}
