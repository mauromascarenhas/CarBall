package br.com.professorisidro.aviao.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

import br.com.professorisidro.aviao.AirplaneGame;
import br.com.professorisidro.aviao.core.GameObject;

public class Aviao extends AbstractModel{
	private GameObject modelAviao;
	private int mode;
	private float MAX_ANGLE=45;
	private float STEP=500;
	public static final int MODE_PLANE = 0;
	public static final int MODE_PTL   = -1;
	public static final int MODE_LTP   = -2;
	public static final int MODE_PTR   =  1;
	public static final int MODE_RTP   =  2;
    private Vector3 curPosition;
    private Vector3 newPosition;
	

	public Aviao() {
		super(true, true);
		// TODO Auto-generated constructor stub
		modelAviao = new GameObject(AirplaneGame.loader.loadModel(Gdx.files.internal("f15/F15.g3db")));
		BlendingAttribute blending = new BlendingAttribute(GL20.GL_SRC_ALPHA 
				 | GL20.GL_ONE_MINUS_SRC_ALPHA);
		blending.opacity = 1;
		for (Material m: modelAviao.materials) {
			m.remove(ColorAttribute.Emissive);
			m.set(blending);
		}
		
		modelAviao.transform.scale(0.0085f, 0.0085f, 0.0085f);
		curPosition = new Vector3();
		newPosition = new Vector3();  

		
		
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		if (mode == MODE_PTR) {
			if (modelAviao.getAngle() >= -MAX_ANGLE) {
				modelAviao.setAngle(-MAX_ANGLE*delta);
			    modelAviao.transform.rotate(Vector3.Z, -MAX_ANGLE*delta);
			}
//			modelAviao.transform.getTranslation(curPosition);
//			newPosition.x = curPosition.x + -STEP*delta*(float)Math.sin(Math.toRadians(modelAviao.getAngle()));
//			newPosition.y = curPosition.y + STEP*delta*(float)Math.cos(Math.toRadians(modelAviao.getAngle()));
//			newPosition.z = curPosition.z;
//			modelAviao.transform.translate(newPosition);
			modelAviao.transform.translate(STEP*delta*(float)Math.sin(Math.toRadians(-modelAviao.getAngle())), 
                    STEP*delta*(float)Math.cos(Math.toRadians(-modelAviao.getAngle())),
                    0);
					
			
			
		}
		else if (mode == MODE_RTP) {
			if (modelAviao.getAngle() <= 0) {
				modelAviao.setAngle(MAX_ANGLE*delta*2);
			    modelAviao.transform.rotate(Vector3.Z, MAX_ANGLE*delta*2);
			}
			else {
				mode = MODE_PLANE;
			}
			modelAviao.transform.translate(0, 
                    -STEP*delta*(float)Math.cos(Math.toRadians(-modelAviao.getAngle())),
                    0);
		}
		else if (mode == MODE_PTL) {
			if (modelAviao.getAngle() <= MAX_ANGLE) {
				modelAviao.setAngle(MAX_ANGLE*delta);
			    modelAviao.transform.rotate(Vector3.Z, MAX_ANGLE*delta);
			}
			
			modelAviao.transform.translate(-STEP*delta*(float)Math.sin(Math.toRadians(modelAviao.getAngle())), 
					                       STEP*delta*(float)Math.cos(Math.toRadians(modelAviao.getAngle())),
					                       0);
		    

		}
		else if (mode == MODE_LTP) {
			if (modelAviao.getAngle() >= 0) {
				modelAviao.setAngle(-MAX_ANGLE*delta);
			    modelAviao.transform.rotate(Vector3.Z, -MAX_ANGLE*delta);
			}
			else {
				mode = MODE_PLANE;
			}
			modelAviao.transform.translate(0, 
		 		       -STEP*delta*(float)Math.cos(Math.toRadians(modelAviao.getAngle())),0);
		}

		
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public int getMode() {
		return this.mode;
	}

	
	@Override
	public GameObject getGameObject() {
		// TODO Auto-generated method stub
		return modelAviao;
	}
	
	

}
