package br.edu.ufabc.meuprimeirojogo.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

import br.edu.ufabc.meuprimeirojogo.MeuJogo;
import br.edu.ufabc.meuprimeirojogo.core.GameObject;

public class ShotOrc extends AbstractModel{

	private GameObject shot;
	private float      speed = 10f;
	public ShotOrc(Vector3 position){
		super(true,true);
		shot = new GameObject(MeuJogo.modelBuider.createSphere(0.1f, 0.1f, 0.1f, 10, 10, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Position | Usage.Normal));
		position.y = 1f;
		shot.transform.translate(position);		
	}
	
	public void rotateToAngle(float angle) {
		shot.transform.rotate(Vector3.Y, angle);
	}
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		shot.transform.translate(0, 0, speed*delta);
	}

	@Override
	public GameObject getGameObject() {
		// TODO Auto-generated method stub
		return shot;
	}

}
