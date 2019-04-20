package br.edu.ufabc.ipj.spaceshooter.core.gamelogic;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.model.AbstractModel;
import br.edu.ufabc.ipj.spaceshooter.model.Asteroid;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiCargoSarship;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiCosair;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiFighter;
import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import br.edu.ufabc.ipj.spaceshooter.utils.ModelSelector;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class GameAction {
    
    protected Array<AbstractModel> objects;
    
    public GameAction(ModelSelector selected){
        objects = new Array<AbstractModel>();
        
        //Load models in here
        switch(selected){
            case SCIFI_COSAIR:
                objects.add(new SciFiCosair());
                objects.first().getGameObject().transform.rotate(Vector3.Y, 180);
                break;
            case SCIFI_FIGHTER:
                objects.add(new SciFiFighter());
                objects.first().getGameObject().transform.rotate(Vector3.Y, 180);
                break;
            case SCIFI_STARSHIP:
                objects.add(new SciFiCargoSarship());
                objects.first().getGameObject().transform.rotate(Vector3.Y, 180);
                break;
        }
        
        objects.add(new Asteroid());
        
        for (AbstractModel obj: objects)
            for (Material mat : obj.getGameObject().materials)
                mat.remove(ColorAttribute.Emissive);
        
        objects.get(1).getGameObject().transform.translate(0, 0, -300.0f / objects.get(1).getGameObject().transform.getScaleZ());
    }
    
    public void update(float delta){
        for (AbstractModel o : objects)
            o.update(delta);
        
        Vector3 cPos = new Vector3();
        if (objects.size > 1){
            objects.get(1).getGameObject().transform.translate(0, 0, +1 / objects.get(1).getGameObject().transform.getScaleZ());
            objects.get(1).getGameObject().transform.getTranslation(cPos);
            System.out.println(cPos);
            if (cPos.z > 26.0f){
                objects.removeIndex(1);
                
                objects.add(new Asteroid());
                for (Material mat : objects.get(objects.size - 1).getGameObject().materials)
                    mat.remove(ColorAttribute.Emissive);
                objects.get(objects.size - 1).getGameObject().transform.translate(((float)Math.random() * 80) - 40,
                                                                                    0, -300.0f / objects.get(1).getGameObject().transform.getScaleZ());
            }
        }
        
        if (Commands.hasCommand(Commands.Command.LEFT)) 
            objects.first().getGameObject().transform.translate(10.0f * delta / objects.first().getGameObject().transform.getScaleX(), 0, 0);
        else if (Commands.hasCommand(Commands.Command.RIGHT)) 
            objects.first().getGameObject().transform.translate(-10.0f * delta / objects.first().getGameObject().transform.getScaleX(), 0, 0);
        
        SpaceShooterGame.DEBUG = Commands.set[Commands.Command.DEBUG.getValue()];
    }
}
