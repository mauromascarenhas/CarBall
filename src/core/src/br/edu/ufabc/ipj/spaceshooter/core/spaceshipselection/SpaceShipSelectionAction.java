package br.edu.ufabc.ipj.spaceshooter.core.spaceshipselection;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.model.AbstractModel;
import br.edu.ufabc.ipj.spaceshooter.model.Pedestal;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiCargoSarship;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiCosair;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiFighter;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiIntergalactic;
import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import br.edu.ufabc.ipj.spaceshooter.utils.ModelSelector;
import br.edu.ufabc.ipj.spaceshooter.utils.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.utils.Array;

public class SpaceShipSelectionAction {
    
    public float currentSpeed;
    public float currentReload;
    public boolean isMissile;
    
    public ModelSelector currentSelection;
    
    private boolean hadCommand;
    
    protected Array<AbstractModel> objects;
    
    public SpaceShipSelectionAction(){
        this.currentSpeed = SciFiFighter.getDefaultSpeed();
        this.currentReload = SciFiFighter.SHOT_RELOAD_TIME;
        this.isMissile = SciFiFighter.USES_MISSILE;
        
        this.hadCommand = Commands.hasCommand();
        
        this.currentSelection = ModelSelector.SCIFI_FIGHTER;
        
        objects = new Array<AbstractModel>();
        
        //Load models in here
        objects.add(new Pedestal());
        objects.add(new SciFiFighter());
        objects.add(new SciFiCosair(false));
        objects.add(new SciFiCargoSarship(false));
        objects.add(new SciFiIntergalactic(false));
        
        for (AbstractModel obj: objects){
            for (Material mat : obj.getGameObject().materials)
                mat.remove(ColorAttribute.Emissive);
            if (!(obj instanceof Pedestal))
                obj.getGameObject().transform.translate(0, 8 / obj.getGameObject().transform.getScaleY(), 0);
        }
    }
    
    public void update(float delta){
        for (AbstractModel o : objects)
            o.update(delta);
        
        // Giver preference to keyboard instead of mouse or touch
        if (!hadCommand && Commands.set[Commands.Command.LEFT.getValue()])
            swapSpaceship(Commands.Command.LEFT);
        else if (!hadCommand && Commands.set[Commands.Command.RIGHT.getValue()])
            swapSpaceship(Commands.Command.RIGHT);
        else hadCommand = Commands.hasCommand();
        
        if (Gdx.input.justTouched()){
            int xCoord = Utilities.toGameCoordinates(Utilities.ScreenAxis.X, Gdx.input.getX());

            if (xCoord < Utilities.GAME_WIDTH * 0.3f)
                swapSpaceship(Commands.Command.LEFT);
            else if (xCoord > Utilities.GAME_WIDTH * 0.7f)
                swapSpaceship(Commands.Command.RIGHT);
        }
        
        for (AbstractModel o : objects)
            if (o instanceof SciFiFighter)
                ((SciFiFighter) o).rotateSelection(delta);
            else if (o instanceof SciFiCosair)
                ((SciFiCosair) o).rotateSelection(delta);
            else if (o instanceof SciFiCargoSarship)
                ((SciFiCargoSarship) o).rotateSelection(delta);
            else if (o instanceof SciFiIntergalactic)
                ((SciFiIntergalactic) o).rotateSelection(delta);
    }
    
    private void swapSpaceship(Commands.Command command){
        SpaceShooterGame.playMenuHoveringBeep();
        
        hadCommand = true;
        
        int i;
        for (i = 1; i < objects.size; ++i)
            if (objects.get(i).getGameObject().isVisible()) break;
        
        objects.get(i).getGameObject().setVisible(false);
        if (command == Commands.Command.RIGHT) currentSelection = ModelSelector.fromInteger(++i >= objects.size ? 1 : i);
        else currentSelection = ModelSelector.fromInteger(--i == 0 ? objects.size - 1 : i);
        objects.get(currentSelection.getValue()).getGameObject().setVisible(true);
        
        switch(currentSelection){
            case SCIFI_FIGHTER:
                this.currentSpeed = SciFiFighter.getDefaultSpeed();
                this.currentReload = SciFiFighter.SHOT_RELOAD_TIME;
                this.isMissile = SciFiFighter.USES_MISSILE;
                break;
            case SCIFI_COSAIR:
                this.currentSpeed = SciFiCosair.getDefaultSpeed();
                this.currentReload = SciFiCosair.SHOT_RELOAD_TIME;
                this.isMissile = SciFiCosair.USES_MISSILE;
                break;
            case SCIFI_STARSHIP:
                this.currentSpeed = SciFiCargoSarship.getDefaultSpeed();
                this.currentReload = SciFiCargoSarship.SHOT_RELOAD_TIME;
                this.isMissile = SciFiCargoSarship.USES_MISSILE;
                break;
            case SCIFI_INTERGALACTIC:
                this.currentSpeed = SciFiIntergalactic.getDefaultSpeed();
                this.currentReload = SciFiIntergalactic.SHOT_RELOAD_TIME;
                this.isMissile = SciFiIntergalactic.USES_MISSILE;
        }
    }
}
