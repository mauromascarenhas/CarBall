package br.edu.ufabc.ipj.spaceshooter.screen;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.core.spaceshipselection.SpaceShipSelectionAction;
import br.edu.ufabc.ipj.spaceshooter.core.spaceshipselection.SpaceShipSelectionRenderer;
import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import br.edu.ufabc.ipj.spaceshooter.utils.ModelSelector;
import br.edu.ufabc.ipj.spaceshooter.utils.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class SpaceshipSelectionScreen extends BaseScreen{

    private final Music ssSelectionBeep;
    private ModelSelector selected;
    
    private final SpaceShipSelectionAction gameAction;
    private final SpaceShipSelectionRenderer renderer;
    
    public SpaceshipSelectionScreen(String id){
        super(id);
        
        selected = ModelSelector.SCIFI_FIGHTER;
        
        ssSelectionBeep = Gdx.audio.newMusic(Gdx.files.internal("songs/selection/spacecraft_selection.wav"));
        
        gameAction = new SpaceShipSelectionAction();
        renderer   = new SpaceShipSelectionRenderer(gameAction);
    }
    
    @Override
    public void update(float delta) {
        if (Commands.hasCommand(Commands.Command.SHOT)){
            ssSelectionBeep.play();
            this.setSelected(gameAction.currentSelection);
            this.setDone(true);
        }
        else if (Commands.hasCommand(Commands.Command.ESCAPE)){
            SpaceShooterGame.playMenuSelectionBeep();
            this.setSelected(ModelSelector.SCIFI_UNDFINED);
            this.setDone(true);
        }
        
        if (Gdx.input.justTouched()){
            int xCoord = Utilities.toGameCoordinates(Utilities.ScreenAxis.X, Gdx.input.getX()),
                yCoord = Utilities.toGameCoordinates(Utilities.ScreenAxis.Y, Gdx.input.getY());
        
            if (yCoord >= 630  && yCoord <= 730
                            && xCoord <= 150 && xCoord >= 50){
                SpaceShooterGame.playMenuSelectionBeep();
                this.setSelected(ModelSelector.SCIFI_UNDFINED);
                this.setDone(true);
            }
            else if (xCoord >= Utilities.GAME_WIDTH * 0.3f
                    && xCoord <= Utilities.GAME_WIDTH * 0.7f){
                ssSelectionBeep.play();
                this.setSelected(gameAction.currentSelection);
                this.setDone(true);            
            }
        }
        
        gameAction.update(delta);
    }

    @Override
    public void draw(float delta) {
        renderer.draw(delta);
    }
    
    private void setSelected(ModelSelector selected){
        this.selected = selected;
    }
    
    public ModelSelector getSelected(){
        return this.selected;
    }
}
