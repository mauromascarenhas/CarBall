package br.edu.ufabc.ipj.spaceshooter.screen;

import br.edu.ufabc.ipj.spaceshooter.core.gamelogic.GameAction;
import br.edu.ufabc.ipj.spaceshooter.core.gamelogic.GameRenderer;
import br.edu.ufabc.ipj.spaceshooter.utils.DifficultySelector;
import br.edu.ufabc.ipj.spaceshooter.utils.ModelSelector;

public class GameScreen extends BaseScreen{

    private final GameAction gameAction;
    private final GameRenderer renderer;
    
    public GameScreen(String id, ModelSelector selected,
                        DifficultySelector difficulty){
        super(id);
        
        gameAction = new GameAction(selected, difficulty);
        renderer   = new GameRenderer(gameAction);
    }
    
    @Override
    public void update(float delta) {
        gameAction.update(delta);
        this.setDone(gameAction.isDone);
    }

    @Override
    public void draw(float delta) {
        renderer.draw(delta);
    }
    
}
