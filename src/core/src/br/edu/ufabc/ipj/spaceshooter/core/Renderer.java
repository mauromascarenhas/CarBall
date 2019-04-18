package br.edu.ufabc.ipj.spaceshooter.core;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.model.AbstractModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

public class Renderer {
    private GameAction gameAction;
    private ModelBatch modelBatch;
    private Environment environment;
    private PerspectiveCamera camera;
    private CameraInputController input;
    
    public Renderer(GameAction action){
        this.gameAction = action;
        
        modelBatch = new ModelBatch();
        environment = new Environment();
        
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,  0.6f, 0.6f, 0.6f,0));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f,-0.2f, -0.8f, 1));
        
        camera = new PerspectiveCamera(67.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.far = 300.0f;
        camera.position.set(0, 5, 10);
        camera.lookAt(0, 0, 0);
        camera.update();
        
        input = new CameraInputController(camera);
        Gdx.input.setInputProcessor(input);
    }
    
    public void draw(float delta){
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 0);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);		
        
        modelBatch.begin(camera);
        for (AbstractModel o : gameAction.objects)
            if (o.getGameObject().isVisible()) modelBatch.render(o.getGameObject(), environment);
        
        if (SpaceShooterGame.DEBUG)
            for (AbstractModel o : gameAction.objects)
                modelBatch.render(o.getGameObject().getBoxInstance(), environment);
        modelBatch.end();
        
        camera.update();
    }
}
