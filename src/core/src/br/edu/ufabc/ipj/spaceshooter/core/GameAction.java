package br.edu.ufabc.ipj.spaceshooter.core;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.model.AbstractModel;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiFighter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.utils.Array;

public class GameAction {
    protected Array<AbstractModel> objects;
    
    public GameAction(){
        objects = new Array<AbstractModel>();
        
        //Load models in here
        objects.add(new SciFiFighter());
        
        //TODO: Implement scenario here!

        objects.first().getGameObject().transform.translate(0, 0, 0);
        
        for (AbstractModel obj: objects)
            for (Material mat : obj.getGameObject().materials)
                mat.remove(ColorAttribute.Emissive);
    }
    
    public void update(float delta){
        for (AbstractModel o : objects)
            o.update(delta);
        
        for (AbstractModel o : objects)
            if (o instanceof SciFiFighter)
                ((SciFiFighter) o).rotateSelection(delta);
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.D))
            SpaceShooterGame.DEBUG = !SpaceShooterGame.DEBUG;
    }
}
