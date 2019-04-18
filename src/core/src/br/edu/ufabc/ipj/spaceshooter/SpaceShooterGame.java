package br.edu.ufabc.ipj.spaceshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import br.edu.ufabc.ipj.spaceshooter.screen.BaseScreen;
import br.edu.ufabc.ipj.spaceshooter.screen.LoadingScreen;
import br.edu.ufabc.ipj.spaceshooter.screen.SpaceshipSelectionScreen;
import com.badlogic.gdx.graphics.g3d.Model;


public class SpaceShooterGame extends Game implements InputProcessor {
    
    private BaseScreen currentScreen;
    public static AssetManager assetManager;
    public static ModelBuilder modelBuider;
    public static boolean DEBUG = false;

    @Override
    public void create() {
        modelBuider = new ModelBuilder();
        assetManager = new AssetManager();
        
        assetManager.load("three_dimensional/pedestal/tech_pedestal.g3db", Model.class);
        
        assetManager.load("three_dimensional/scifi_cosair/cosair.g3db", Model.class);
        assetManager.load("three_dimensional/scifi_spacecraft/SciFi_Fighter.g3db", Model.class);
        
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
            if(currentId.equals("loading")) currentScreen = new SpaceshipSelectionScreen("selection");
    }

    @Override
    public boolean keyDown(int i) {
        // Non implemented method
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        // Non implemented method
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