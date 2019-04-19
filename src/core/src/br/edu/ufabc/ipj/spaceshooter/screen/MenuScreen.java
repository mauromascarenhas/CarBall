package br.edu.ufabc.ipj.spaceshooter.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class MenuScreen extends BaseScreen {

    private final Texture texture;
    private final Matrix4 viewMatrix;
    private final Matrix4 transMatrix;
    private final SpriteBatch spriteBatch;
    
    public MenuScreen(String id){
        super(id);
        
        texture = new Texture("static_images/space_background_dark_1.jpg");
        viewMatrix = new Matrix4();
        transMatrix = new Matrix4();
        spriteBatch = new SpriteBatch();
    }
    
    @Override
    public void dispose() {
        texture.dispose();
        spriteBatch.dispose();
        
        super.dispose();
    }
    
    @Override
    public void update(float delta) {
        
    }

    @Override
    public void draw(float delta) {
        
    }
    
}
