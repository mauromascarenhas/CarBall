package br.edu.ufabc.ipj.spaceshooter.screen;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import br.edu.ufabc.ipj.spaceshooter.utils.Utilities;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class LoadingScreen extends BaseScreen{

    private int progress;
    private float time;
    private boolean loaded;
    private boolean visible;
    
    private final String gameTitle;
    
    private Texture texture;
    private Matrix4 viewMatrix;
    private Matrix4 transMatrix;
    private BitmapFont bitmapFont;
    private SpriteBatch spriteBatch;
    
    public LoadingScreen(String id){
        super(id);
        
        progress = 0;
        time = 0.0f;
        
        loaded = false;
        visible = false;
        
        gameTitle = "} Shooter";
        
        texture = new Texture("static_images/space_background_dark_1.jpg");
        viewMatrix = new Matrix4();
        transMatrix = new Matrix4();
        spriteBatch = new SpriteBatch();
        
        bitmapFont = new BitmapFont(Gdx.files.internal("fonts/space_age.fnt"));
    }

    @Override
    public void dispose() {
        texture.dispose();
        spriteBatch.dispose();
        
        super.dispose();
    }
    
    @Override
    public void update(float delta) {
        if (!SpaceShooterGame.assetManager.isFinished()){
            SpaceShooterGame.assetManager.update();
            progress = (int) (SpaceShooterGame.assetManager.getProgress() * 100);
        }
        else loaded = true;
        
        if (loaded && Gdx.input.justTouched()){
            setDone(true);
        }
    }

    @Override
    public void draw(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glClearColor(0, 0, 0, 0);
        
        viewMatrix.setToOrtho2D(0, 0, 1366, 768);
        spriteBatch.setProjectionMatrix(viewMatrix);
        spriteBatch.setTransformMatrix(transMatrix);
        
        spriteBatch.begin();
        spriteBatch.draw(texture, 0, 0, Utilities.GAME_WIDTH, Utilities.GAME_HEIGHT, 0, 0,
                            2000, 1500, false, false);
        
        /*
         * Escala e desenha a fonte na tela
         */
        
        bitmapFont.getData().setScale(2);
        bitmapFont.draw(spriteBatch, gameTitle, 375, 600);
        
        bitmapFont.getData().setScale(1);
        if (!loaded) bitmapFont.draw(spriteBatch,
                String.format("< %03d / 100 > Loading...", progress), 455, 100);
        else {
            if (visible) bitmapFont.draw(spriteBatch, "<- CLICK TO START ->", 490, 100);
            time += Gdx.graphics.getDeltaTime();
            if (time >= 0.2f){
                visible = !visible;
                time = 0;
            }
        }
        
        // Termina a operação de desenho
        spriteBatch.end();
    }
    
}