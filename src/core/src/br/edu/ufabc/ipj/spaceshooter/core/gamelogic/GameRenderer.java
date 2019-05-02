package br.edu.ufabc.ipj.spaceshooter.core.gamelogic;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.model.AbstractModel;
import br.edu.ufabc.ipj.spaceshooter.utils.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;

public class GameRenderer {
    private float startTimer;
    private float switchTimer;
    private float overlay_alpha;
    
    private final GameAction gameAction;
    private final ModelBatch modelBatch;
    private final Environment environment;
    private final PerspectiveCamera camera;
    
    // Static content
    private Sprite black_sprite;
    private final Texture texture;
    private final Texture life_symb;
    private final Texture shot_symb;
    private final Texture black_ovl;
    private final Texture pause_symb;
    private final Texture aimTexture;
    private final Texture nxtTexture;
    private final Texture prvTexture;
    private final Matrix4 viewMatrix;
    private final Matrix4 transMatrix;
    private final BitmapFont bitmapFont;
    private final BitmapFont bitmapTitleFont;
    private final SpriteBatch spriteBatch;
    
    public GameRenderer(GameAction action){
        this.gameAction = action;
        
        this.startTimer = 0;
        this.switchTimer = 0;
        this.overlay_alpha = 0;
        
        //Loads static content
        texture = new Texture("static_images/space_background_dark_2.jpg");
        prvTexture = new Texture("static_images/left_arrow_icon.png");
        nxtTexture = new Texture("static_images/right_arrow_icon.png");
        aimTexture = new Texture("static_images/target_marker.png");
        
        shot_symb = new Texture("static_images/target_icon.png");
        black_ovl = new Texture("static_images/black_overlay.jpg");
        life_symb = new Texture("static_images/spacecraft_icon.png");
        pause_symb = new Texture("static_images/pause_icon.png");
        
        bitmapFont = new BitmapFont(Gdx.files.internal("fonts/space_age/big/space_age.fnt"));
        bitmapTitleFont = new BitmapFont(Gdx.files.internal("fonts/space_age/big_bold/space_age.fnt"));
        
        viewMatrix = new Matrix4();
        transMatrix = new Matrix4();
        spriteBatch = new SpriteBatch();
        
        modelBatch = new ModelBatch();
        environment = new Environment();
        
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,  0.6f, 0.6f, 0.6f,0));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f,-0.2f, -0.8f, 1));
        
        camera = new PerspectiveCamera(67.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.far = 300.0f;
        camera.position.set(0, 15, 25);
        camera.lookAt(0, 3, 0);
        camera.update();
        
        SpaceShooterGame.particleBatch.setCamera(camera);
    }
    
    public void draw(float delta){startTimer += delta;
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);		
        
        viewMatrix.setToOrtho2D(0, 0, Utilities.GAME_WIDTH, Utilities.GAME_HEIGHT);
        spriteBatch.setProjectionMatrix(viewMatrix);
        spriteBatch.setTransformMatrix(transMatrix);
        
        spriteBatch.begin();
        // Background drawing
        spriteBatch.draw(texture, 0, 0, Utilities.GAME_WIDTH, Utilities.GAME_HEIGHT, 0, 0,
                            2048, 1152, false, false);
        spriteBatch.end();
        
        // Render 3D objects
        modelBatch.begin(camera);
        for (AbstractModel o : gameAction.shots)
            if (o.getGameObject().isVisible()) modelBatch.render(o.getGameObject(), environment);
        for (AbstractModel o : gameAction.objects)
            if (o.getGameObject().isVisible()) modelBatch.render(o.getGameObject(), environment);
        
        if (!gameAction.canShowScore && gameAction.lives < 1){
            SpaceShooterGame.particleSystem.update(delta);
            SpaceShooterGame.particleSystem.begin();
            SpaceShooterGame.particleSystem.draw();
            SpaceShooterGame.particleSystem.end();
            modelBatch.render(SpaceShooterGame.particleSystem, environment);
        }        
        modelBatch.end();
        
        // HUD drawing
        spriteBatch.begin();
        if (gameAction.canShot && gameAction.lives > 0)
            spriteBatch.draw(shot_symb, 1200, 50, 100.0f, 100.0f, 0, 0, 512, 512, false, false);
        
        for (int i = 0; i < gameAction.lives; ++i)
            spriteBatch.draw(life_symb, (50 + i * 102f), 50, 100.0f, 100.0f, 0, 0, 512, 512, false, false);
        
        if (startTimer < 5){
            startTimer += delta;
            switchTimer += delta;
            
            if (switchTimer <= 0.5){
                spriteBatch.setColor(1.0f, 1.0f, 1.0f, 0.25f);
                
                spriteBatch.draw(aimTexture,
                        (Utilities.GAME_WIDTH - 512) * 0.5f, (Utilities.GAME_HEIGHT - 512) * 0.5f,
                        512, 512, 0, 0, 512, 512, false, false);
                spriteBatch.draw(prvTexture, 0, Utilities.GAME_HEIGHT * 0.1f,
                        Utilities.GAME_WIDTH * 0.30f, Utilities.GAME_HEIGHT * 0.8f,
                        0, 0, 512, 512, false, false);
                spriteBatch.draw(nxtTexture, 10 + Utilities.GAME_WIDTH * 0.7f, Utilities.GAME_HEIGHT * 0.1f,
                        Utilities.GAME_WIDTH * 0.30f, Utilities.GAME_HEIGHT * 0.8f,
                        0, 0, 512, 512, false, false);
                
                spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
            else if (switchTimer > 1) switchTimer = 0;
        }

        if (gameAction.lives > 0 && !gameAction.isPaused
                && gameAction.pauseTimer <= 0){
            overlay_alpha = 0;
            
            spriteBatch.draw(pause_symb, 1210, 630, 100.0f, 100.0f, 0, 0, 512, 512, false, false);
            
            bitmapFont.getData().setScale(1.0f, 1.5f);
            bitmapFont.draw(spriteBatch, String.format("SCORE : %d", gameAction.score), 50.0f, 710.0f);
        }
        else {
            if (overlay_alpha < 0.7f) overlay_alpha += (0.3f * delta);
            spriteBatch.enableBlending();
            
            black_sprite = new Sprite(black_ovl, Utilities.GAME_WIDTH - 100, Utilities.GAME_HEIGHT - 80);
            black_sprite.setColor(0, 0, 0, overlay_alpha);
            black_sprite.setPosition(50, 40);
            black_sprite.draw(spriteBatch);
            
            if (gameAction.canShowScore){
                bitmapTitleFont.getData().setScale(0.75f, 1);
                bitmapTitleFont.draw(spriteBatch, "CURRENT SCORE", 400, 600);
                bitmapTitleFont.getData().setScale(1, 1.25f);
                bitmapTitleFont.draw(spriteBatch, "HIGHEST SCORE", 350, 400);
                bitmapTitleFont.draw(spriteBatch, String.format("%019d", SpaceShooterGame.highestScore),
                                                                150, 300);

                bitmapFont.getData().setScale(1, 1.5f);
                bitmapFont.draw(spriteBatch, String.format("%019d", gameAction.score), 170, 500);
            }
            else if (gameAction.isPaused){
                bitmapTitleFont.getData().setScale(1, 1.5f);
                bitmapTitleFont.draw(spriteBatch, "GAME PAUSED...", 375, 400);
            }
            else if (gameAction.pauseTimer > 0){
                bitmapTitleFont.getData().setScale(1, 1.5f);
                bitmapTitleFont.draw(spriteBatch, "RESTARTING...", 380, 450);
                bitmapTitleFont.draw(spriteBatch, String.format("%02d", (int)gameAction.pauseTimer + 1),
                                                                610, 380);
            }
            
            if (gameAction.canProceed)
                bitmapFont.draw(spriteBatch, "<- SHOT TO CONTINUE ->", 200, 100);
        }
        
        spriteBatch.end();
        
        camera.update();
    }
}
