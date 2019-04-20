package br.edu.ufabc.ipj.spaceshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import br.edu.ufabc.ipj.spaceshooter.screen.BaseScreen;
import br.edu.ufabc.ipj.spaceshooter.screen.GameScreen;
import br.edu.ufabc.ipj.spaceshooter.screen.LoadingScreen;
import br.edu.ufabc.ipj.spaceshooter.screen.SpaceshipSelectionScreen;
import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import com.badlogic.gdx.Input;
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
        
        assetManager.load("three_dimensional/asteroid/Asteroid.g3db", Model.class);
        
        assetManager.load("three_dimensional/scifi_cosair/cosair.g3db", Model.class);
        assetManager.load("three_dimensional/scifi_spacecraft/SciFi_Fighter.g3db", Model.class);
        assetManager.load("three_dimensional/scifi_cargostarship/scifi_cargostarship.g3db", Model.class);
        
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
            else if (currentId.equals("selection")) currentScreen = new GameScreen("game", ((SpaceshipSelectionScreen)currentScreen).getSelected());
    }

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
            case Input.Keys.D:
                Commands.set[Commands.Command.DEBUG.getValue()] = true;
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
            case Input.Keys.D:
                Commands.set[Commands.Command.DEBUG.getValue()] = false;
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