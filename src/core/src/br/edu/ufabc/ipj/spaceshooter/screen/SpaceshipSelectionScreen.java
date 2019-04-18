package br.edu.ufabc.ipj.spaceshooter.screen;

import br.edu.ufabc.ipj.spaceshooter.core.GameAction;
import br.edu.ufabc.ipj.spaceshooter.core.Renderer;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.Matrix4;

public class SpaceshipSelectionScreen extends BaseScreen{

    private GameAction gameAction;
    private Renderer renderer;
    
//    private Texture texture;
//    private Matrix4 viewMatrix;
//    private Matrix4 transMatrix;
//    private SpriteBatch spriteBatch;
    
    public SpaceshipSelectionScreen(String id){
        super(id);
        
        gameAction = new GameAction();
        renderer   = new Renderer(gameAction);
        
//        texture = new Texture("static_images/space_background_dark_1.jpg");
//        viewMatrix = new Matrix4();
//        transMatrix = new Matrix4();
//        spriteBatch = new SpriteBatch();
    }

    @Override
    public void dispose() {
//        texture.dispose();
//        spriteBatch.dispose();
        
        super.dispose();
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
