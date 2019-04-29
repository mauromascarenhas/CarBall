package br.edu.ufabc.ipj.spaceshooter.screen;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import br.edu.ufabc.ipj.spaceshooter.utils.DifficultySelector;
import br.edu.ufabc.ipj.spaceshooter.utils.MenuItem;
import br.edu.ufabc.ipj.spaceshooter.utils.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class MenuScreen extends BaseScreen {

    private enum Coordinate{
        X,
        Y
    };
    
    public MenuItem hoveredItem;
    public DifficultySelector selectedDifficulty;
    
    private int oldXCoord, oldYCoord;
    
    private boolean hadKeyCommand;
    
    private final String gameTitle;
    
    private final Texture texture;
    private final Matrix4 viewMatrix;
    private final Matrix4 transMatrix;
    private final BitmapFont bitmapFont;
    private final BitmapFont bitmapTitleFont;
    private final SpriteBatch spriteBatch;
    
    public MenuScreen(String id){
        this(id, DifficultySelector.EASY);
    }
    
    public MenuScreen(String id, DifficultySelector difficulty){
        super(id);
        
        hadKeyCommand = Commands.hasCommand();
        
        oldXCoord = Gdx.input.getX();
        oldYCoord = Gdx.input.getY();
        
        hoveredItem = MenuItem.PLAY;
        
        gameTitle = "   }\nShooter";
        selectedDifficulty = difficulty;
        
        texture = new Texture("static_images/space_background_dark_1.jpg");
        viewMatrix = new Matrix4();
        transMatrix = new Matrix4();
        spriteBatch = new SpriteBatch();
        
        bitmapFont = new BitmapFont(Gdx.files.internal("fonts/space_age/big/space_age.fnt"));
        bitmapTitleFont = new BitmapFont(Gdx.files.internal("fonts/space_age/big_bold/space_age.fnt"));
    }
    
    @Override
    public void dispose() {
        texture.dispose();
        spriteBatch.dispose();
        
        super.dispose();
    }
    
    @Override
    public void update(float delta) {
        // Gives priority to keyboard commands
        if (!hadKeyCommand && Commands.hasCommand()){
            if (Commands.hasCommand(Commands.Command.UP)){
                SpaceShooterGame.playMenuHoveringBeep();
                changeSelection(Commands.Command.UP);
            }
            else if (Commands.hasCommand(Commands.Command.DOWN)){
                SpaceShooterGame.playMenuHoveringBeep();
                changeSelection(Commands.Command.DOWN);
            }
            else if (Commands.hasCommand(Commands.Command.SHOT)){
                SpaceShooterGame.playMenuSelectionBeep();
                setDone(true);
            }
            else if (hoveredItem == MenuItem.PLAY){
                if (Commands.hasCommand(Commands.Command.LEFT)){
                    SpaceShooterGame.playMenuHoveringBeep();
                    changeDifficulty(Commands.Command.LEFT);
                }
                else if (Commands.hasCommand(Commands.Command.RIGHT)){
                    SpaceShooterGame.playMenuHoveringBeep();
                    changeDifficulty(Commands.Command.RIGHT);
                }
            }
            return;
        }
        else hadKeyCommand = Commands.hasCommand();
        
        int xCoord = toGameCoordinates(Coordinate.X, Gdx.input.getX()),
            yCoord = toGameCoordinates(Coordinate.Y, Gdx.input.getY());
        
        if (yCoord >= 220 && (oldXCoord != xCoord || oldYCoord != yCoord)
                || Gdx.input.justTouched()){
            oldXCoord = xCoord;
            oldYCoord = yCoord;
            
            if (yCoord < 315){
                hoveredItem = MenuItem.QUIT;
                if (Gdx.input.justTouched()){
                    SpaceShooterGame.playMenuSelectionBeep();
                    System.exit(0);
                }
                else SpaceShooterGame.playMenuHoveringBeep();
            }
            else if (yCoord < 415){
                hoveredItem = MenuItem.CREDITS;
                if (Gdx.input.justTouched()){
                    SpaceShooterGame.playMenuSelectionBeep();
                    this.setDone(true);
                }
                else SpaceShooterGame.playMenuHoveringBeep();
            }
            else if (yCoord < 500){
                hoveredItem = MenuItem.PLAY;
                if (Gdx.input.justTouched()){
                    if (xCoord < 390 || xCoord > 800){
                        SpaceShooterGame.playMenuSelectionBeep();
                        setDone(true);
                    }
                    else{
                        SpaceShooterGame.playMenuHoveringBeep();
                        changeDifficulty(xCoord < 600 ? Commands.Command.LEFT : Commands.Command.RIGHT);
                    }
                }
            }
        }
    }

    @Override
    public void draw(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glClearColor(0, 0, 0, 0);
        
        viewMatrix.setToOrtho2D(0, 0, Utilities.GAME_WIDTH, Utilities.GAME_HEIGHT);
        spriteBatch.setProjectionMatrix(viewMatrix);
        spriteBatch.setTransformMatrix(transMatrix);
        
        spriteBatch.begin();
        spriteBatch.draw(texture, 0, 0, Utilities.GAME_WIDTH, Utilities.GAME_HEIGHT, 0, 0,
                            2000, 1500, false, false);
        
        bitmapTitleFont.getData().setScale(1.25f);
        bitmapTitleFont.draw(spriteBatch, gameTitle, 450, 620);
        
        bitmapFont.getData().setScale(hoveredItem == MenuItem.PLAY ? 1.25f : 1.0f);
        bitmapFont.draw(spriteBatch, "PLAY < " + selectedDifficulty.toString() + " >", 100, 450);
        bitmapFont.getData().setScale(hoveredItem == MenuItem.CREDITS ? 1.25f : 1.0f);
        bitmapFont.draw(spriteBatch, "CREDITS", 100, 350);
        bitmapFont.getData().setScale(hoveredItem == MenuItem.QUIT ? 1.25f : 1.0f);
        bitmapFont.draw(spriteBatch, "QUIT", 100, 250);
        
        spriteBatch.end();
    }
    
    private void changeSelection(Commands.Command command){
        hadKeyCommand = true;
        if (!(command == Commands.Command.UP || command == Commands.Command.DOWN)) return;
        
        int i = hoveredItem.getValue();
        if (command == Commands.Command.DOWN) hoveredItem = MenuItem.fromInteger(++i > MenuItem.QUIT.getValue() ? 
                                                                                    MenuItem.PLAY.getValue() : i);
        else hoveredItem = MenuItem.fromInteger(--i == 0 ? MenuItem.QUIT.getValue() : i);
    }
    
    private void changeDifficulty(Commands.Command command){
        hadKeyCommand = true;
        if (!(command == Commands.Command.RIGHT || command == Commands.Command.LEFT)) return;
        
        int i = selectedDifficulty.getValue();
        if (command == Commands.Command.RIGHT) selectedDifficulty = DifficultySelector.fromInteger(++i > DifficultySelector.HARD.getValue() ? 
                                                                                                        DifficultySelector.EASY.getValue() : i);
        else selectedDifficulty = DifficultySelector.fromInteger(--i == 0 ? DifficultySelector.HARD.getValue() : i);
    }
    
    private int toGameCoordinates(Coordinate coordinate, int value){
        int newCoord;
        if (coordinate == Coordinate.X)
            newCoord = (int)(value / ((float)Gdx.graphics.getWidth() / Utilities.GAME_WIDTH));
        else 
            newCoord = (Utilities.GAME_HEIGHT) 
                    - (int)(value / ((float)Gdx.graphics.getHeight()/ Utilities.GAME_HEIGHT));
        return newCoord;
    }
}
