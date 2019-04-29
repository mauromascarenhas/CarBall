package br.edu.ufabc.ipj.spaceshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import br.edu.ufabc.ipj.spaceshooter.screen.BaseScreen;
import br.edu.ufabc.ipj.spaceshooter.screen.GameScreen;
import br.edu.ufabc.ipj.spaceshooter.screen.LoadingScreen;
import br.edu.ufabc.ipj.spaceshooter.screen.MenuScreen;
import br.edu.ufabc.ipj.spaceshooter.screen.SpaceshipSelectionScreen;
import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import br.edu.ufabc.ipj.spaceshooter.utils.DifficultySelector;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;


public class SpaceShooterGame extends Game implements InputProcessor {
    
    public static long highestScore = 0;

    public static Music backgroundSong;
    public static Music menuHoveringBeep;
    public static Music menuSelectionBeep;
    
    public static AssetManager assetManager;
    public static ModelBuilder modelBuider;
    public static ParticleSystem particleSystem;
    public static BillboardParticleBatch particleBatch;
    
    private BaseScreen currentScreen;
    private DifficultySelector selectedDifficulty;

    @Override
    public void create() {
        selectedDifficulty = DifficultySelector.EASY;
        
        modelBuider = new ModelBuilder();
        assetManager = new AssetManager();
        particleSystem = new ParticleSystem();
        
        particleBatch = new BillboardParticleBatch();
        
        menuHoveringBeep = Gdx.audio.newMusic(Gdx.files.internal("songs/selection/menu_select_hover.wav"));
        menuSelectionBeep = Gdx.audio.newMusic(Gdx.files.internal("songs/selection/menu_select_enter.wav"));
        
        backgroundSong = Gdx.audio.newMusic(Math.random() < 0.7 ?
                                            Gdx.files.internal("songs/soundtrack/background_song_1.wav") : 
                                            Gdx.files.internal("songs/soundtrack/background_song_2.wav"));
        backgroundSong.setLooping(true);
        backgroundSong.play();
        
        assetManager.load("three_dimensional/pedestal/tech_pedestal.g3db", Model.class);
        
        assetManager.load("three_dimensional/asteroid/Asteroid.g3db", Model.class);
        
        assetManager.load("three_dimensional/shot/shot.g3db", Model.class);
        assetManager.load("three_dimensional/missile/missile.g3db", Model.class);
        
        assetManager.load("three_dimensional/scifi_cosair/cosair.g3db", Model.class);
        assetManager.load("three_dimensional/scifi_spacecraft/SciFi_Fighter.g3db", Model.class);
        assetManager.load("three_dimensional/scifi_intergalactic/SciFi_Intergalactic.g3db", Model.class);
        assetManager.load("three_dimensional/scifi_cargostarship/scifi_cargostarship.g3db", Model.class);
        
        
        ParticleEffectLoader.ParticleEffectLoadParameter loadParameter;
        loadParameter = new ParticleEffectLoader.ParticleEffectLoadParameter(particleSystem.getBatches());
        ParticleEffectLoader loader = new ParticleEffectLoader(new InternalFileHandleResolver());
        
        assetManager.setLoader(ParticleEffect.class, loader);
        assetManager.load("particles/asteroid_explosion", ParticleEffect.class, loadParameter);
        assetManager.load("particles/spaceship_explosion", ParticleEffect.class, loadParameter);
        
        particleSystem.add(particleBatch);
        
        //Enables mouse interaction
        Gdx.input.setInputProcessor(this);
        
        currentScreen = new LoadingScreen("loading");
        this.setScreen(currentScreen);
    }

    @Override
    public void render() {
        currentScreen.render(Gdx.graphics.getDeltaTime());
        
        String currentId = currentScreen.getId();
        if (currentScreen.isDone())
            if(currentId.equals("loading")) currentScreen = new MenuScreen("menu", selectedDifficulty);
            else if (currentId.equals("menu")){
                selectedDifficulty = ((MenuScreen)currentScreen).selectedDifficulty;
                currentScreen = new SpaceshipSelectionScreen("selection");
            }
            else if (currentId.equals("selection")) currentScreen = new GameScreen("game", ((SpaceshipSelectionScreen)currentScreen).getSelected(),
                                                                                    selectedDifficulty);
            else if (currentId.equals("game")) currentScreen = new MenuScreen("menu");
    }
    
    public static void playMenuHoveringBeep(){
        if (menuHoveringBeep.isPlaying()) menuHoveringBeep.stop();
        menuHoveringBeep.play();
    }
    
    public static void playMenuSelectionBeep(){
        if (menuSelectionBeep.isPlaying()) menuSelectionBeep.stop();
        menuSelectionBeep.play();
    }

    /*
     * Keyboard input abstraction
     */
    
    @Override
    public boolean keyDown(int i) {
        switch(i){
            case Input.Keys.UP:
                Commands.set[Commands.Command.UP.getValue()] = true;
                return true;
            case Input.Keys.DOWN:
                Commands.set[Commands.Command.DOWN.getValue()] = true;
                return true;
            case Input.Keys.LEFT:
                Commands.set[Commands.Command.LEFT.getValue()] = true;
                return true;
            case Input.Keys.RIGHT:
                Commands.set[Commands.Command.RIGHT.getValue()] = true;
                return true;
            case Input.Keys.SPACE:
                Commands.set[Commands.Command.SHOT.getValue()] = true;
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        switch(i){
            case Input.Keys.UP:
                Commands.set[Commands.Command.UP.getValue()] = false;
                return true;
            case Input.Keys.DOWN:
                Commands.set[Commands.Command.DOWN.getValue()] = false;
                return true;
            case Input.Keys.LEFT:
                Commands.set[Commands.Command.LEFT.getValue()] = false;
                return true;
            case Input.Keys.RIGHT:
                Commands.set[Commands.Command.RIGHT.getValue()] = false;
                return true;
            case Input.Keys.SPACE:
                Commands.set[Commands.Command.SHOT.getValue()] = false;
                return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        // Non implemented method
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        // Non implemented method
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        // Non implemented method
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        // Non implemented method
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        // Non implemented method
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        // Non implemented method
        return false;
    }
}