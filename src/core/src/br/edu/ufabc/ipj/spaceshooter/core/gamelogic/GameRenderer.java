package br.edu.ufabc.ipj.spaceshooter.core.gamelogic;

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
    private final GameAction gameAction;
    private final ModelBatch modelBatch;
    private final Environment environment;
    private final PerspectiveCamera camera;
    
    // Static content
    private final Texture texture;
    private final Texture life_symb;
    private final Texture shot_symb;
    private final Texture black_ovl;
    private final Matrix4 viewMatrix;
    private final Matrix4 transMatrix;
    private final BitmapFont bitmapFont;
    private final SpriteBatch spriteBatch;
    
    public GameRenderer(GameAction action){
        this.gameAction = action;
        
        //Loads static content
        texture = new Texture("static_images/space_background_dark_2.jpg");
        shot_symb = new Texture("static_images/target_icon.png");
        black_ovl = new Texture("static_images/black_overlay.jpg");
        life_symb = new Texture("static_images/spacecraft_icon.png");
        
        bitmapFont = new BitmapFont(Gdx.files.internal("fonts/space_age.fnt"));
        
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
                            2048, 1152, false, false);
        
        if (gameAction.canShot)
            spriteBatch.draw(shot_symb, 1250, 40, 60.0f, 60.0f, 0, 0, 512, 512, false, false);
        
        for (int i = 0; i < gameAction.lives; ++i)
            spriteBatch.draw(life_symb, (40 + i * 52f), 40, 50.0f, 50.0f, 0, 0, 512, 512, false, false);
            
        bitmapFont.getData().setScale(2.0f);
        bitmapFont.draw(spriteBatch, String.format("SCORE : %d", gameAction.score), 50.0f, 650.0f);
        spriteBatch.end();
        
        modelBatch.begin(camera);
        for (AbstractModel o : gameAction.shots)
            if (o.getGameObject().isVisible()) modelBatch.render(o.getGameObject(), environment);
        for (AbstractModel o : gameAction.objects)
            if (o.getGameObject().isVisible()) modelBatch.render(o.getGameObject(), environment);
        modelBatch.end();
        
        if (gameAction.lives < 1){
            spriteBatch.begin();
            spriteBatch.enableBlending();
            Sprite s = new Sprite(black_ovl, Utilities.GAME_WIDTH, Utilities.GAME_HEIGHT);
            s.setColor(0, 0, 0, 0.7f);
            s.draw(spriteBatch);
            /*spriteBatch.draw(black_ovl, 0, 0, Utilities.GAME_WIDTH, Utilities.GAME_HEIGHT, 0, 0,
                            800, 500, false, false);*/
            spriteBatch.end();
        }
        
        camera.update();
    }
}
