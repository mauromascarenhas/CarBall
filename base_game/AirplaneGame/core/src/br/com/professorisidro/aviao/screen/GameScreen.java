package br.com.professorisidro.aviao.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;

import br.com.professorisidro.aviao.AirplaneGame;
import br.com.professorisidro.aviao.core.Commands;
import br.com.professorisidro.aviao.model.Aviao;



public class GameScreen extends AbstractScreen {
	
	private Environment environment;
	private PerspectiveCamera camera;
	private ModelBatch modelBatch;
	private Matrix4 viewMatrix;
	private Matrix4 tranMatrix;
	private SpriteBatch spriteBatch;
	private BitmapFont font;
	private Texture    fundo;
	private Aviao      aviao;
	private ModelInstance linha;
	
	

	public GameScreen(String id) {
		super(id);
//		linha = new ModelInstance(
				
				//AirplaneGame.modelBuilder.createRect(-30, 0,-30, 30,0,30, 30,0, -30, -30, 0 ,100 ,0,1,0, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Position|Usage.Normal));
//		fundo = new Texture(Gdx.files.internal("pista/fundo.jpg"));
        aviao = new Aviao();		
		viewMatrix = new Matrix4();
		tranMatrix = new Matrix4();
		spriteBatch = new SpriteBatch();
//		font = new BitmapFont(Gdx.files.internal("fonts/digital_final.fnt"));
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1));
		environment.add(new DirectionalLight().set(Color.WHITE, -1f, -0.8f, -0.2f));
		camera = new PerspectiveCamera(67.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.near = 0.01f;
		camera.far = 500f;
		camera.position.set(0,8,20);
		camera.lookAt(0,3,0);
		camera.update();
	
		
		
		
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float delta) {
		if (Commands.set[Commands.ROTATE_L]) {
			aviao.setMode(Aviao.MODE_PTL);
		}
		if (aviao.getMode() == Aviao.MODE_PTL) {
			if (!Commands.set[Commands.ROTATE_L]) {
				aviao.setMode(Aviao.MODE_LTP);
			}
		}
		
		if ( Commands.set[Commands.ROTATE_R]) {
			aviao.setMode(Aviao.MODE_PTR);
		}
		if (aviao.getMode() == Aviao.MODE_PTR) {
			if (!Commands.set[Commands.ROTATE_R]) {
				aviao.setMode(Aviao.MODE_RTP);
			}
		}
		aviao.update(delta);
		camera.update();
		
		// só pra saber o instante de tempo
		
	}

	@Override
	public void draw(float delta) {
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_COLOR_BUFFER_BIT);
		//Gdx.gl.glClearColor(102f/255,204f/255,1,1);
		Gdx.gl.glClearColor(0,0,0, 1);
		viewMatrix.setToOrtho2D(0, 0, 1024, 68);
		spriteBatch.setProjectionMatrix(viewMatrix);
		spriteBatch.setTransformMatrix(tranMatrix);
		spriteBatch.begin();
//		spriteBatch.draw(fundo, 0,0);
		spriteBatch.end();

		modelBatch.begin(camera);
//		modelBatch.render(linha, environment);
		modelBatch.render(aviao.getGameObject(), environment);
		modelBatch.end();
		camera.update();

//		spriteBatch.begin();
//		
//		spriteBatch.end();

	}

}
