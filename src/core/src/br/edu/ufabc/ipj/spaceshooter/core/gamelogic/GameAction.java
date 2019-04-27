package br.edu.ufabc.ipj.spaceshooter.core.gamelogic;

import br.edu.ufabc.ipj.spaceshooter.core.GameObject;
import br.edu.ufabc.ipj.spaceshooter.model.AbstractModel;
import br.edu.ufabc.ipj.spaceshooter.model.Asteroid;
import br.edu.ufabc.ipj.spaceshooter.model.Missile;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiCargoSarship;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiCosair;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiFighter;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiIntergalactic;
import br.edu.ufabc.ipj.spaceshooter.model.Shot;
import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import br.edu.ufabc.ipj.spaceshooter.utils.DifficultySelector;
import br.edu.ufabc.ipj.spaceshooter.utils.ModelSelector;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class GameAction {
    
    // Set done on destroy
    public boolean isDone;
    public boolean canProceed;
    
    public boolean canShot;
    
    // Sets if the game is paused
    public boolean isPaused;
    
    // Sets game difficulty
    private final DifficultySelector DIFFICULTY;
    
    // Objects scale
    private final float SHOT_SCALE;
    private final float ASTEROID_SCALE;
    private final float ASTEROID_TSPAWN;
    private final float SPACECRAFT_SCALE;
    
    // Objects speed
    private final float SHOT_SPEED;
    private final float SHOT_RELOAD;
    private final float ASTEROID_SPEED;
    private final float SPACECRAFT_SPEED;
    
    // Game logic variables
    public int lives;
    private int invulnerabilityCount;
    
    public long score;
    
    private float lostTimer;
    private float shotTimer;
    private float asteroidTimer;
    private float invulnerabilityTimer;
    
    // Sets it spacecraft uses missiles instead
    private boolean isInvulnerable;
    private final boolean IS_MISSILE;
    
    protected Array<AbstractModel> shots;            
    protected Array<AbstractModel> objects;
    
    public GameAction(ModelSelector selected,
                        DifficultySelector difficulty){
        this.isDone = false;
        
        this.score = 0;
        this.lives = 3;
        
        this.lostTimer = 0;
        this.shotTimer = 0;
        this.asteroidTimer = 0;
        this.invulnerabilityCount = 0;
        this.invulnerabilityTimer = 0;      
        
        this.canShot = true;
        this.isPaused = false;
        this.isInvulnerable = false;
        
        shots = new Array<AbstractModel>();
        objects = new Array<AbstractModel>();
        
        float shotReloadTmp, spacecraftSpeedTmp,
                asteroidSpeedTmp = Asteroid.getDefaultSpeed();
        
        //Load models in here
        switch(selected){
            case SCIFI_COSAIR:
                objects.add(new SciFiCosair());
                objects.first().getGameObject().transform.rotate(Vector3.Y, 180);
                spacecraftSpeedTmp = SciFiCosair.getDefaultSpeed();
                SPACECRAFT_SCALE = SciFiCosair.DEFAULT_SCALE;
                shotReloadTmp = SciFiCosair.SHOT_RELOAD_TIME;
                IS_MISSILE = SciFiCosair.USES_MISSILE;
                break;
            case SCIFI_FIGHTER:
                objects.add(new SciFiFighter());
                objects.first().getGameObject().transform.rotate(Vector3.Y, 180);
                spacecraftSpeedTmp = SciFiFighter.getDefaultSpeed();
                SPACECRAFT_SCALE = SciFiFighter.DEFAULT_SCALE;
                shotReloadTmp = SciFiFighter.SHOT_RELOAD_TIME;
                IS_MISSILE = SciFiFighter.USES_MISSILE;
                break;
            case SCIFI_STARSHIP:
                objects.add(new SciFiCargoSarship());
                objects.first().getGameObject().transform.rotate(Vector3.Y, 180);
                spacecraftSpeedTmp = SciFiCargoSarship.getDefaultSpeed();
                SPACECRAFT_SCALE = SciFiCargoSarship.DEFAULT_SCALE;
                shotReloadTmp = SciFiCargoSarship.SHOT_RELOAD_TIME;
                IS_MISSILE = SciFiCargoSarship.USES_MISSILE;
                break;
            case SCIFI_INTERGALACTIC:
                objects.add(new SciFiIntergalactic());
                objects.first().getGameObject().transform.rotate(Vector3.Y, 180);
                spacecraftSpeedTmp = SciFiIntergalactic.getDefaultSpeed();
                SPACECRAFT_SCALE = SciFiIntergalactic.DEFAULT_SCALE;
                shotReloadTmp = SciFiIntergalactic.SHOT_RELOAD_TIME;
                IS_MISSILE = SciFiIntergalactic.USES_MISSILE;
                break;
            default:
                shotReloadTmp = 1.5F;
                spacecraftSpeedTmp = 8;
                SPACECRAFT_SCALE = 1;
                IS_MISSILE = false;
        }
        
        switch(difficulty){
            case HARD:
                ASTEROID_TSPAWN = 0.75f;
                spacecraftSpeedTmp *= 5.0f;
                asteroidSpeedTmp *= 4.0f;
                shotReloadTmp *= 3.0f;
                break;
            case MEDIUM:
                ASTEROID_TSPAWN = 2.0f;
                spacecraftSpeedTmp *= 3.0f;
                asteroidSpeedTmp *= 2.0f;
                shotReloadTmp *= 1.5f;
                break;
            case EASY:
            default:
                ASTEROID_TSPAWN = 4.0f;
        }
        
        DIFFICULTY = difficulty;
        
        SHOT_RELOAD = shotReloadTmp;
        SPACECRAFT_SPEED = spacecraftSpeedTmp;
        
        shotTimer = 10.1f;
        SHOT_SPEED = Missile.getDefaultSpeed();
        SHOT_SCALE = IS_MISSILE ? Missile.DEFAULT_SCALE : Shot.DEFAULT_SCALE;
        
        ASTEROID_SPEED = asteroidSpeedTmp;
        ASTEROID_SCALE = Asteroid.DEFAULT_SCALE;
        
        objects.add(new Asteroid());
        
        for (AbstractModel obj: objects)
            for (Material mat : obj.getGameObject().materials)
                mat.remove(ColorAttribute.Emissive);
        
        objects.get(1).getGameObject().transform.translate(0, 0, -300.0f / ASTEROID_SCALE);
    }
    
    public void update(float delta){
        for (AbstractModel o : objects)
            o.update(delta);
        for (AbstractModel o : shots)
            o.update(delta);
        
        shotTimer += delta;
        asteroidTimer += delta;
        if (lives > 0) score += (delta * 100 * DIFFICULTY.getValue());
        else lostTimer += delta;
        
        if (lostTimer >= 4) this.canProceed = true;
        
        this.canShot = shotTimer > SHOT_RELOAD;
        
        if (this.isInvulnerable){
            this.invulnerabilityTimer += delta;
            if (this.invulnerabilityTimer >= 0.25f){
                this.invulnerabilityTimer = 0;
                
                GameObject spaceCraft = objects.first().getGameObject();
                spaceCraft.setVisible(!spaceCraft.isVisible());
                
                this.invulnerabilityCount++;
            }
            
            this.isInvulnerable = this.invulnerabilityCount != 11;
            if (!this.isInvulnerable) this.invulnerabilityCount = 0;
        }
        
        Vector3 cPos = new Vector3();
        for (int i = objects.size - 1; i > 0; --i){
            GameObject current = objects.get(i).getGameObject();
            current.transform.translate(0, 0, ASTEROID_SPEED / ASTEROID_SCALE);
            current.transform.getTranslation(cPos);
            
            if (cPos.z > 26.0){
                objects.removeIndex(i);
                continue;
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
            
            for (int k = shots.size - 1; k > -1; --k)
                if (shots.get(k).collidesWith(objects.get(i))){
                    objects.removeIndex(i);
                    shots.removeIndex(k);
                    score += (100 * DIFFICULTY.getValue());
                    break;
                }
        }
        
        if (asteroidTimer >= ASTEROID_TSPAWN){
            float sortedPos = (float)(Math.random() * 61.0 - 30.0) / ASTEROID_SCALE;
            
            for (int i = 0; i < DIFFICULTY.getValue(); ++i){
                if (Math.random() < 0.5 && i != 0) continue;
                
                Asteroid newAsteroid = new Asteroid();
                for (Material mat : newAsteroid.getGameObject().materials)
                    mat.remove(ColorAttribute.Emissive);
                newAsteroid.getGameObject().transform.translate(sortedPos += (15 / ASTEROID_SCALE), 0,
                                                                -300.0f / ASTEROID_SCALE);
                objects.add(newAsteroid);
            }
            
            asteroidTimer = 0;
        }
        
        for (int i = shots.size - 1; i > -1; --i){
            GameObject current = shots.get(i).getGameObject();
            current.transform.translate(0, 0, - SHOT_SPEED / SHOT_SCALE);
            current.transform.getTranslation(cPos);
            
            if (cPos.z < -300) shots.removeIndex(i);
        }

        objects.first().getGameObject().transform.getTranslation(cPos);
        if (Commands.hasCommand(Commands.Command.LEFT) && cPos.x > -25) 
            objects.first().getGameObject().transform.translate(SPACECRAFT_SPEED * delta / SPACECRAFT_SCALE, 0, 0);
        else if (Commands.hasCommand(Commands.Command.RIGHT) && cPos.x < 25) 
            objects.first().getGameObject().transform.translate(-SPACECRAFT_SPEED * delta / SPACECRAFT_SCALE, 0, 0);
        
        if (Commands.hasCommand(Commands.Command.SHOT) && canShot){
            Vector3 spaceCraftPos = new Vector3();
            objects.first().getGameObject().transform.getTranslation(spaceCraftPos);
            
            AbstractModel newShot = IS_MISSILE ? new Missile() : new Shot();
            for (Material mat : newShot.getGameObject().materials)
                    mat.remove(ColorAttribute.Emissive);
            newShot.getGameObject().transform.translate(spaceCraftPos.x / SHOT_SCALE, 0, 0);
            shots.add(newShot);
            shotTimer = 0;
        }
        
        if (this.canProceed && Commands.hasCommand(Commands.Command.SHOT)) this.isDone = true;
    }
}
