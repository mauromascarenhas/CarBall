package br.edu.ufabc.ipj.spaceshooter.screen;

import br.edu.ufabc.ipj.spaceshooter.core.SpaceShipSelectionAction;
import br.edu.ufabc.ipj.spaceshooter.core.SpaceShipSelectionRenderer;

public class SpaceshipSelectionScreen extends BaseScreen{

    private SpaceShipSelectionAction gameAction;
    private SpaceShipSelectionRenderer renderer;
    
    public SpaceshipSelectionScreen(String id){
        super(id);
        
        gameAction = new SpaceShipSelectionAction();
        renderer   = new SpaceShipSelectionRenderer(gameAction);
    }
    
    @Override
    public void update(float delta) {
        gameAction.update(delta);
    }

    @Override
    public void draw(float delta) {
        renderer.draw(delta);
    }   
}
