package br.edu.ufabc.ipj.spaceshooter.screen;

import br.edu.ufabc.ipj.spaceshooter.core.spaceshipselection.SpaceShipSelectionAction;
import br.edu.ufabc.ipj.spaceshooter.core.spaceshipselection.SpaceShipSelectionRenderer;
import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import br.edu.ufabc.ipj.spaceshooter.utils.ModelSelector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class SpaceshipSelectionScreen extends BaseScreen{

    private Music ssSelectionBeep;
    private ModelSelector selected;
    
    private SpaceShipSelectionAction gameAction;
    private SpaceShipSelectionRenderer renderer;
    
    public SpaceshipSelectionScreen(String id){
        super(id);
        
        selected = ModelSelector.SCIFI_FIGHTER;
        
        ssSelectionBeep = Gdx.audio.newMusic(Gdx.files.internal("songs/selection/spacecraft_selection.wav"));
        
        gameAction = new SpaceShipSelectionAction();
        renderer   = new SpaceShipSelectionRenderer(gameAction);
    }
    
    @Override
    public void update(float delta) {
        gameAction.update(delta);
        
        if (Commands.hasCommand(Commands.Command.SHOT)){
            ssSelectionBeep.play();
            
            this.setDone(true);
            this.setSelected(gameAction.currentSelection);
        }
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
