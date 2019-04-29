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
    private final Matrix4 viewMatrix;
    private final Matrix4 transMatrix;
    private final BitmapFont bitmapFont;
    private final BitmapFont bitmapTitleFont;
    private final SpriteBatch spriteBatch;
    
    public GameRenderer(GameAction action){
        this.gameAction = action;
        
        overlay_alpha = 0;
        
        //Loads static content
        texture = new Texture("static_images/space_background_dark_2.jpg");
        shot_symb = new Texture("static_images/target_icon.png");
        black_ovl = new Texture("static_images/black_overlay.jpg");
        life_symb = new Texture("static_images/spacecraft_icon.png");
        
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
    }
    
    public void draw(float delta){
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 0);
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
        
        /*if (!gameAction.canShowScore && gameAction.lives < 1){
            SpaceShooterGame.particleSystem.update(delta/20);
            SpaceShooterGame.particleSystem.begin();
            SpaceShooterGame.particleSystem.draw();
            SpaceShooterGame.particleSystem.end();
            modelBatch.render(SpaceShooterGame.particleSystem, environment);
        }*/
        
        modelBatch.end();
        
        
        // HUD drawing
        spriteBatch.begin();
        if (gameAction.canShot && gameAction.lives > 0)
            spriteBatch.draw(shot_symb, 1250, 40, 60.0f, 60.0f, 0, 0, 512, 512, false, false);
        
        for (int i = 0; i < gameAction.lives; ++i)
            spriteBatch.draw(life_symb, (40 + i * 52f), 40, 50.0f, 50.0f, 0, 0, 512, 512, false, false);
            
        bitmapFont.getData().setScale(1);
        if (gameAction.lives > 0)
            bitmapFont.draw(spriteBatch, String.format("SCORE : %d", gameAction.score), 50.0f, 650.0f);
        else {
            if (overlay_alpha < 0.7f) overlay_alpha += (0.3f * delta);
            spriteBatch.enableBlending();
            
            black_sprite = new Sprite(black_ovl, Utilities.GAME_WIDTH - 100, Utilities.GAME_HEIGHT - 80);
            black_sprite.setColor(0, 0, 0, overlay_alpha);
            black_sprite.setPosition(50, 40);
            black_sprite.draw(spriteBatch);
            
            if (gameAction.canShowScore){
                bitmapTitleFont.getData().setScale(0.75f);
                bitmapTitleFont.draw(spriteBatch, "CURRENT SCORE", 400, 600);
                bitmapTitleFont.getData().setScale(1);
                bitmapTitleFont.draw(spriteBatch, "HIGHEST SCORE", 350, 400);
                bitmapTitleFont.draw(spriteBatch, String.format("%019d", SpaceShooterGame.highestScore),
                                                                150, 300);

                bitmapFont.draw(spriteBatch, String.format("%019d", gameAction.score), 170, 500);
            }
            
            if (gameAction.canProceed)
                bitmapFont.draw(spriteBatch, "<- SHOT TO CONTINUE ->", 200, 100);
        }
        
        spriteBatch.end();
        
        camera.update();
    }
}
