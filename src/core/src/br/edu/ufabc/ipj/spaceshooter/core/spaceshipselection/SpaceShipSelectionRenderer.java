package br.edu.ufabc.ipj.spaceshooter.core.spaceshipselection;

import br.edu.ufabc.ipj.spaceshooter.model.AbstractModel;
import br.edu.ufabc.ipj.spaceshooter.utils.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
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
    private final Texture bckTexture;
    private final Texture nxtTexture;
    private final Texture prvTexture;
    private final Matrix4 viewMatrix;
    private final Matrix4 transMatrix;
    private final SpriteBatch spriteBatch;
    
    public SpaceShipSelectionRenderer(SpaceShipSelectionAction action){     
        this.switchTimer = 0;
        
        this.gameAction = action;
        
        //Loads static content
        texture = new Texture("static_images/space_background_dark_1.jpg");
        bckTexture = new Texture("static_images/back_icon.png");
        prvTexture = new Texture("static_images/left_arrow_icon.png");
        nxtTexture = new Texture("static_images/right_arrow_icon.png");
        
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
        spriteBatch.draw(texture, 0, 0, Utilities.GAME_WIDTH, Utilities.GAME_HEIGHT, 0, 0,
                            2000, 1500, false, false);
        spriteBatch.draw(prvTexture, 0, 0, Utilities.GAME_WIDTH * 0.33f, Utilities.GAME_HEIGHT,
                0, 0, 512, 512, false, false);
        spriteBatch.draw(nxtTexture, Utilities.GAME_WIDTH * 0.66f, 0, Utilities.GAME_WIDTH * 0.33f, Utilities.GAME_HEIGHT,
                0, 0, 512, 512, false, false);
        spriteBatch.end();
        
        modelBatch.begin(camera);
        for (AbstractModel o : gameAction.objects)
            if (o.getGameObject().isVisible()) modelBatch.render(o.getGameObject(), environment);
        
        modelBatch.end();
        
        spriteBatch.begin();
        spriteBatch.draw(bckTexture, 50, 630, 100.0f, 100.0f, 0, 0, 512, 512, false, false);
        spriteBatch.end();
        
        camera.update();
    }
}
