package br.edu.ufabc.ipj.spaceshooter.core.gamelogic;

import br.edu.ufabc.ipj.spaceshooter.core.GameObject;
import br.edu.ufabc.ipj.spaceshooter.model.AbstractModel;
import br.edu.ufabc.ipj.spaceshooter.model.Asteroid;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiCargoSarship;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiCosair;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiFighter;
import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import br.edu.ufabc.ipj.spaceshooter.utils.ModelSelector;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class GameAction {
    
    // Set done on destroy
    public boolean isDone;
    
    // Objects scale
    private final float shotScale = 0;
    private final float asteroidScale;
    private final float spacecraftScale;
    
    // Objects speed
    private final float shotSpeed = 0;
    private final float asteroidSpeed;
    private final float spacecraftSpeed;
    
    // Game logic variables
    public int lives;
    private int invulnerabilityCount;
    
    public long score;
    
    private float shotTimer;
    private float invulnerabilityTimer;
    
    private boolean isInvulnerable;
            
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
            default:
                spacecraftSpeed = 8;
                spacecraftScale = 1;
        }
        
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
        }

        objects.first().getGameObject().transform.getTranslation(cPos);
        if (Commands.hasCommand(Commands.Command.LEFT) && cPos.x > -25) 
            objects.first().getGameObject().transform.translate(spacecraftSpeed * delta / spacecraftScale, 0, 0);
        else if (Commands.hasCommand(Commands.Command.RIGHT) && cPos.x < 25) 
            objects.first().getGameObject().transform.translate(-spacecraftSpeed * delta / spacecraftScale, 0, 0);
        
        if (!this.isInvulnerable && this.lives > 0){
            if (objects.get(1).collidesWith(objects.get(0)))
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
        else if (this.lives == 0 && Commands.hasCommand(Commands.Command.SHOT)) this.isDone = true;
    }
}
