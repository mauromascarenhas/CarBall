package br.edu.ufabc.ipj.spaceshooter.core.gamelogic;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.model.AbstractModel;
import br.edu.ufabc.ipj.spaceshooter.model.Pedestal;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiCargoSarship;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiCosair;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiFighter;
import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import br.edu.ufabc.ipj.spaceshooter.utils.ModelSelector;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.utils.Array;

public class GameAction {
    
    protected Array<AbstractModel> objects;
    
    public GameAction(ModelSelector selected){
        objects = new Array<AbstractModel>();
        
        //Load models in here
        switch(selected){
            case SCIFI_COSAIR   : objects.add(new SciFiCosair());       break;
            case SCIFI_FIGHTER  : objects.add(new SciFiFighter());      break;
            case SCIFI_STARSHIP : objects.add(new SciFiCargoSarship()); break;
        }
        
        for (AbstractModel obj: objects)
            for (Material mat : obj.getGameObject().materials)
                mat.remove(ColorAttribute.Emissive);
    }
    
    public void update(float delta){
        for (AbstractModel o : objects)
            o.update(delta);
        
        SpaceShooterGame.DEBUG = Commands.set[Commands.Command.DEBUG.getValue()];
    }
}
