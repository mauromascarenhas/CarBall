package br.edu.ufabc.ipj.spaceshooter.core.gamelogic;

import br.edu.ufabc.ipj.spaceshooter.core.GameObject;
import br.edu.ufabc.ipj.spaceshooter.model.AbstractModel;
import br.edu.ufabc.ipj.spaceshooter.model.Asteroid;
import br.edu.ufabc.ipj.spaceshooter.model.Missile;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiCargoSarship;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiCosair;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiFighter;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiIntergalactic;
import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import br.edu.ufabc.ipj.spaceshooter.utils.ModelSelector;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class GameAction {
    
    // Set done on destroy
    public boolean isDone;
    
    // Max shots
    private final int maxShots = 1;
    
    // Objects scale
    private final float shotScale;
    private final float asteroidScale;
    private final float spacecraftScale;
    
    // Objects speed
    private final float shotSpeed;
    private final float shotReload = 2.5f;
    private final float asteroidSpeed;
    private final float spacecraftSpeed;
    
    // Game logic variables
    public int lives;
    private int invulnerabilityCount;
    
    public long score;
    
    private float shotTimer;
    private float invulnerabilityTimer;
    
    private boolean isInvulnerable;
    
    protected Array<Missile> missiles;            
    protected Array<AbstractModel> objects;
    
    public GameAction(ModelSelector selected){
        this.isDone = false;
        
        this.score = 0;
        this.lives = 3;
        
        this.shotTimer = 0;
        this.invulnerabilityCount = 0;
        this.invulnerabilityTimer = 0;
        
        this.isInvulnerable = false;
        
        objects = new Array<AbstractModel>();
        missiles = new Array<Missile>();
        
        //Load models in here
        switch(selected){
            case SCIFI_COSAIR:
                objects.add(new SciFiCosair());
                objects.first().getGameObject().transform.rotate(Vector3.Y, 180);
                spacecraftSpeed = SciFiCosair.getDefaultSpeed();
                spacecraftScale = SciFiCosair.DEFAULT_SCALE;
                break;
            case SCIFI_FIGHTER:
                objects.add(new SciFiFighter());
                objects.first().getGameObject().transform.rotate(Vector3.Y, 180);
                spacecraftSpeed = SciFiFighter.getDefaultSpeed();
                spacecraftScale = SciFiFighter.DEFAULT_SCALE;
                break;
            case SCIFI_STARSHIP:
                objects.add(new SciFiCargoSarship());
                objects.first().getGameObject().transform.rotate(Vector3.Y, 180);
                spacecraftSpeed = SciFiCargoSarship.getDefaultSpeed();
                spacecraftScale = SciFiCargoSarship.DEFAULT_SCALE;
                break;
            case SCIFI_INTERGALACTIC:
                objects.add(new SciFiIntergalactic());
                objects.first().getGameObject().transform.rotate(Vector3.Y, 180);
                spacecraftSpeed = SciFiIntergalactic.getDefaultSpeed();
                spacecraftScale = SciFiIntergalactic.DEFAULT_SCALE;
                break;
            default:
                spacecraftSpeed = 8;
                spacecraftScale = 1;
        }
        
        shotTimer = 10.1f;
        shotScale = Missile.DEFAULT_SCALE;
        shotSpeed = Missile.getDefaultSpeed();
        
        asteroidScale = Asteroid.DEFAULT_SCALE;
        asteroidSpeed = Asteroid.getDefaultSpeed();
        
        objects.add(new Asteroid());
        
        for (AbstractModel obj: objects)
            for (Material mat : obj.getGameObject().materials)
                mat.remove(ColorAttribute.Emissive);
        
        objects.get(1).getGameObject().transform.translate(0, 0, -300.0f / asteroidScale);
    }
    
    public void update(float delta){
        for (AbstractModel o : objects)
            o.update(delta);
        
        shotTimer += delta;
        if (lives > 0) score += delta * 100;
        
        if (this.isInvulnerable){
            this.invulnerabilityTimer += delta;
            if (this.invulnerabilityTimer >= 0.5f){
                this.invulnerabilityTimer = 0;
                
                GameObject spaceCraft = objects.first().getGameObject();
                spaceCraft.setVisible(!spaceCraft.isVisible());
                
                this.invulnerabilityCount++;
            }
            
            this.isInvulnerable = this.invulnerabilityCount != 5;
            if (!this.isInvulnerable) this.invulnerabilityCount = 0;
        }
        
        Vector3 cPos = new Vector3();
        for (int i = objects.size - 1; i > 0; --i){
            GameObject current = objects.get(i).getGameObject();
            current.transform.translate(0, 0, asteroidSpeed / asteroidScale);
            current.transform.getTranslation(cPos);
            
            if (cPos.z > 26.0){
                objects.removeIndex(i);
                
                Asteroid newAsteroid = new Asteroid();
                for (Material mat : newAsteroid.getGameObject().materials)
                    mat.remove(ColorAttribute.Emissive);
                newAsteroid.getGameObject().transform.translate((float)(Math.random() * 61.0 - 30.0) / asteroidScale,
                                                                   0, -300.0f / asteroidScale);
                objects.add(newAsteroid);
            }
            
            if (!this.isInvulnerable && this.lives > 0){
                if (objects.get(i).collidesWith(objects.get(0)))
                    if (this.lives == 1){
                        objects.first().getGameObject().setVisible(false);
                        this.lives--;
                    }
                    else {
                        objects.first().getGameObject().setVisible(false);
                        this.invulnerabilityTimer = 0;
                        this.isInvulnerable = true;
                        this.lives--;
                    }
            }
            
            for (int k = missiles.size - 1; k > -1; --k)
                if (missiles.get(k).collidesWith(objects.get(i))){
                    missiles.removeIndex(k);
                    objects.removeIndex(i);
                    break;
                }
        }
        
        for (int i = missiles.size - 1; i > -1; --i){
            GameObject current = missiles.get(i).getGameObject();
            current.transform.translate(- shotSpeed / shotScale, 0, 0);
            current.transform.getTranslation(cPos);
            
            if (cPos.z < -300) missiles.removeIndex(i);
        }

        objects.first().getGameObject().transform.getTranslation(cPos);
        if (Commands.hasCommand(Commands.Command.LEFT) && cPos.x > -25) 
            objects.first().getGameObject().transform.translate(spacecraftSpeed * delta / spacecraftScale, 0, 0);
        else if (Commands.hasCommand(Commands.Command.RIGHT) && cPos.x < 25) 
            objects.first().getGameObject().transform.translate(-spacecraftSpeed * delta / spacecraftScale, 0, 0);
        
        if (Commands.hasCommand(Commands.Command.SHOT) && shotTimer > shotReload){
            Vector3 spaceCraftPos = new Vector3();
            objects.first().getGameObject().transform.getTranslation(spaceCraftPos);
            
            Missile newMissile = new Missile();
            for (Material mat : newMissile.getGameObject().materials)
                    mat.remove(ColorAttribute.Emissive);
            newMissile.getGameObject().transform.translate(0, 0, - spaceCraftPos.x / shotScale);
            missiles.add(newMissile);
            shotTimer = 0;
        }
        
        if (this.lives == 0 && Commands.hasCommand(Commands.Command.SHOT)) this.isDone = true;
    }
}
