package br.edu.ufabc.ipj.spaceshooter.core.spaceshipselection;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.model.AbstractModel;
import br.edu.ufabc.ipj.spaceshooter.utils.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;

public class SpaceShipSelectionRenderer {
    private float switchTimer;
    
    private final ModelBatch modelBatch;
    private final Environment environment;
    private final PerspectiveCamera camera;
    private final SpaceShipSelectionAction gameAction;
    
    // Static content
    private final Texture texture;
    private final Texture lckTexture;
    private final Texture bckTexture;
    private final Texture nxtTexture;
    private final Texture prvTexture;
    private final Matrix4 viewMatrix;
    private final Matrix4 transMatrix;
    private final BitmapFont bitmapFont;
    private final SpriteBatch spriteBatch;
    
    public SpaceShipSelectionRenderer(SpaceShipSelectionAction action){     
        this.switchTimer = 0;
        
        this.gameAction = action;
        
        //Loads static content
        texture = new Texture("static_images/space_background_dark_3.png");
        bckTexture = new Texture("static_images/back_icon.png");
        lckTexture = new Texture("static_images/padlock_icon.png");
        prvTexture = new Texture("static_images/left_arrow_icon.png");
        nxtTexture = new Texture("static_images/right_arrow_icon.png");
        
        bitmapFont = new BitmapFont(Gdx.files.internal("fonts/space_age/big/space_age.fnt"));
        
        viewMatrix = new Matrix4();
        transMatrix = new Matrix4();
        spriteBatch = new SpriteBatch();
        
        modelBatch = new ModelBatch();
        environment = new Environment();
        
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,  0.6f, 0.6f, 0.6f,0));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f,-0.2f, -0.8f, 1));
        
        camera = new PerspectiveCamera(67.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.far = 300.0f;
        camera.position.set(0, 18, 25);
        camera.lookAt(0, 3, 0);
        camera.update();
    }
    
    public void draw(float delta){
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 0);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);	
        
        switchTimer += delta;
        
        viewMatrix.setToOrtho2D(0, 0, Utilities.GAME_WIDTH, Utilities.GAME_HEIGHT);
        spriteBatch.setProjectionMatrix(viewMatrix);
        spriteBatch.setTransformMatrix(transMatrix);
        
        spriteBatch.begin();
        spriteBatch.draw(texture, 0, 0, Utilities.GAME_WIDTH, Utilities.GAME_HEIGHT, 0, 0,
                            1920, 1080, false, false);
        if (switchTimer <= 0.5){
            spriteBatch.draw(prvTexture, 10, Utilities.GAME_HEIGHT * 0.1f,
                    Utilities.GAME_WIDTH * 0.30f, Utilities.GAME_HEIGHT * 0.8f,
                    0, 0, 512, 512, false, false);
            spriteBatch.draw(nxtTexture, Utilities.GAME_WIDTH * 0.7f, Utilities.GAME_HEIGHT * 0.1f,
                    Utilities.GAME_WIDTH * 0.30f, Utilities.GAME_HEIGHT * 0.8f,
                    0, 0, 512, 512, false, false);
        }
        else {
            spriteBatch.draw(prvTexture, 0, Utilities.GAME_HEIGHT * 0.1f,
                    Utilities.GAME_WIDTH * 0.30f, Utilities.GAME_HEIGHT * 0.8f,
                    0, 0, 512, 512, false, false);
            spriteBatch.draw(nxtTexture, 10 + Utilities.GAME_WIDTH * 0.7f, Utilities.GAME_HEIGHT * 0.1f,
                    Utilities.GAME_WIDTH * 0.30f, Utilities.GAME_HEIGHT * 0.8f,
                    0, 0, 512, 512, false, false);
            if (switchTimer > 1) switchTimer = 0;
        }
        spriteBatch.end();
        
        modelBatch.begin(camera);
        for (AbstractModel o : gameAction.objects)
            if (o.getGameObject().isVisible()) modelBatch.render(o.getGameObject(), environment);
        
        modelBatch.end();
        
        spriteBatch.begin();
        spriteBatch.draw(bckTexture, 50, 630, 100.0f, 100.0f, 0, 0, 512, 512, false, false);
        if ((gameAction.currentSelection.getValue() - 1) * Utilities.SPACESHIP_MULT > SpaceShooterGame.highestScore){
            spriteBatch.setColor(1.0f, 1.0f, 1.0f, 0.3f);
            spriteBatch.draw(lckTexture,
                        (Utilities.GAME_WIDTH - 512) * 0.5f, (Utilities.GAME_HEIGHT - 512) * 0.5f,
                        512, 512, 0, 0, 512, 512, false, false);
            spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            
            bitmapFont.getData().setScale(0.8f);
            bitmapFont.draw(spriteBatch, String.format("COST : %1$d <PTS>",
                                (gameAction.currentSelection.getValue() - 1) * Utilities.SPACESHIP_MULT),
                                380, 710);
        }
        else {
            bitmapFont.getData().setScale(0.5f);
            bitmapFont.draw(spriteBatch, String.format("SPEED : %1$04.1f / %2$02.1f <u/s>",
                                Math.abs(gameAction.currentSpeed), gameAction.maxSpeed),
                                420, 740);
            bitmapFont.draw(spriteBatch, String.format("COOLDOWN : %1$s / %2$02.1f <s>",
                                gameAction.currentReload, gameAction.maxReload),
                                420, 710);
            bitmapFont.draw(spriteBatch, String.format("WEAPON : %1$s", gameAction.isMissile ? "MISSILE" : "SCIFI SPHERE"),
                                420, 680);
        }
        spriteBatch.end();
        camera.update();
    }
}
