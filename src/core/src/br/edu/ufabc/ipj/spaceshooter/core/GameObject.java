package br.edu.ufabc.ipj.spaceshooter.core;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class GameObject extends ModelInstance{
    
    private float angle;
    
    private boolean visible;
    private boolean animated;
    private boolean animationFinished;
    
    private BoundingBox boundingBox;
    private AnimationController controller;
	
    private Vector3 minOriginal;
    private Vector3 maxOriginal;
    private Vector3 ctrOriginal;
    
    /* for debug */
    private Vector3 position;
    private ModelInstance boxInstance;
    
    
    public GameObject(Model model){
        super(model);
        
        visible = true;
        animationFinished = false;
        boundingBox = new BoundingBox();
        calculateBoundingBox(boundingBox);
        
        // for debug purposes
        maxOriginal = new Vector3();
	minOriginal = new Vector3();
        ctrOriginal = new Vector3();
        
	boundingBox.getMax(maxOriginal);
	boundingBox.getMin(minOriginal);
        boundingBox.getCenter(ctrOriginal);
	boxInstance = new ModelInstance(SpaceShooterGame.modelBuider.createBox(
			(Math.abs(maxOriginal.x)+Math.abs(minOriginal.x)), 
			(Math.abs(maxOriginal.y)+Math.abs(minOriginal.y)), 
			(Math.abs(maxOriginal.z)+Math.abs(minOriginal.z)), 
               new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)),
               Usage.Position | Usage.Normal));
	BlendingAttribute bl = new BlendingAttribute(GL20.GL_SRC_ALPHA 
	                                   | GL20.GL_ONE_MINUS_SRC_ALPHA);
	bl.opacity = 0.6f;
	boxInstance.materials.get(0).set(bl);
	position = new Vector3();
    }
    
    public GameObject(Model model, boolean visible) {
	this(model);
        this.visible = visible;
    }
    
    public GameObject(Model model, boolean visible, boolean animated,
                        boolean looped, float loopSpeed){
        this(model, visible);
        
        this.animated = animated;
        
        controller = new AnimationController(this);
        if (animated){
            controller.setAnimation(this.animations.first().id, (looped) ? -1 : 1, loopSpeed,
                    new AnimationController.AnimationListener() {
                
                @Override
                public void onEnd(AnimationController.AnimationDesc ad) {
                    animationFinished = true;
                }

                @Override
                public void onLoop(AnimationController.AnimationDesc ad) {
                    animationFinished = true;
                }
            });
        }
    }
    
    public void update(float delta) {
        if (animated) controller.update(delta);
        updateBoundingBox();
    }
    
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isAnimiationFinished() {
        return this.animationFinished;
    }
        
    public void resetAnimation() {
        animationFinished = false;		
    }
	
    public float getAngle(){
        return this.angle;
    }
    
    public void setAngle(float angle){
        this.angle += angle;
        this.angle %= 360;
    }
    
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
    
    public void updateBoundingBox(){
        this.transform.getTranslation(position);
        boundingBox.set(position.cpy().add(minOriginal),
                        position.cpy().add(maxOriginal));
        ctrOriginal.add(position.cpy());
        
        position.y = (boundingBox.getHeight() / 2);
        boxInstance.transform.setToTranslation(position);
    }
	
    public ModelInstance getBoxInstance() {
        return boxInstance;
    }
    
    public void updateBoxScale(float scale){
        minOriginal.scl(scale);
        maxOriginal.scl(scale);
    }
    
    public void updateBoxScale(float scaleX, float scaleY, float scaleZ){
        minOriginal.scl(scaleX, scaleY, scaleZ);
        maxOriginal.scl(scaleX, scaleY, scaleZ);
    }
}
