package br.com.professorisidro.aviao.model;

import br.com.professorisidro.aviao.core.GameObject;

public abstract class AbstractModel {
	public boolean collidable;
	public boolean moveable;
	
	public AbstractModel(boolean collidable, boolean moveable) {
		this.collidable = collidable;
		this.moveable   = moveable;
	}
	
	public boolean isCollidable() {
		return collidable;
	}

	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}
	
	public boolean collidesWith(AbstractModel other) {
		return this.getGameObject()
				   .getBoundingBox()
				   .intersects(other.getGameObject()
						            .getBoundingBox());
	}

	
	
	public abstract void update(float delta);
	public abstract GameObject getGameObject();
	

}
