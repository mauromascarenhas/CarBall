package br.edu.ufabc.ipj.spaceshooter.model;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.core.GameObject;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class Pedestal extends AbstractModel{
    
    private final GameObject object;
    
    public Pedestal(){
        super(true, false);
        
        Model mIdle = SpaceShooterGame.assetManager.get("three_dimensional/pedestal/tech_pedestal.g3db");
        object = new GameObject(mIdle, true);
        object.transform.scale(0.95f, 0.4f, 0.95f);
        
        for (Material m : object.materials)
            m.remove(ColorAttribute.Emissive);
    }
    
    @Override
    public void update(float delta){
        object.update(delta);
    }

    @Override
    public GameObject getGameObject() {
        return object;
    }
}
