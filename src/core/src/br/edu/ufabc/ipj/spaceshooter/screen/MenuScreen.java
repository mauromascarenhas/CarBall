package br.edu.ufabc.ipj.spaceshooter.screen;

import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import br.edu.ufabc.ipj.spaceshooter.utils.DifficultySelector;
import br.edu.ufabc.ipj.spaceshooter.utils.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class MenuScreen extends BaseScreen {

    enum Coordinate{
        X,
        Y
    };
    
    public DifficultySelector selectedDifficulty;
    
    private boolean hadCommand;
    private boolean playHovered;
    private boolean quitHovered;
    private boolean creditsHovered;
    
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
        
        hadCommand = false;
        
        playHovered = false;
        quitHovered = false;
        creditsHovered = false;
        
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
        if (!hadCommand && Commands.set[Commands.Command.LEFT.getValue()])
            changeDifficulty(Commands.Command.LEFT);
        else if (!hadCommand && Commands.set[Commands.Command.RIGHT.getValue()])
            changeDifficulty(Commands.Command.RIGHT);
        else hadCommand = Commands.hasCommand();
        
        int xCoord = toGameCoordinates(Coordinate.X, Gdx.input.getX()),
            yCoord = toGameCoordinates(Coordinate.Y, Gdx.input.getY());
        
        playHovered = false;
        quitHovered = false;
        creditsHovered = false;
        
        if (xCoord >= 100 && yCoord >= 220){
            if (yCoord < 315){
                quitHovered = true;
                if (Gdx.input.justTouched()) System.exit(0);
            }
            else if (yCoord < 415){
                creditsHovered = true;
                if (Gdx.input.justTouched()) /*TODO : Implement Credits Screen*/;
            }
            else if (yCoord < 500){
                playHovered = true;
                if (Gdx.input.justTouched()){
                    if (xCoord < 390 || xCoord > 800) setDone(true);
                    else changeDifficulty(xCoord < 600 ? Commands.Command.LEFT : Commands.Command.RIGHT);
                }
            }
        }
        
        System.out.println("Position -> X : " + xCoord + " ; Y : " + yCoord);
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
        
        bitmapFont.getData().setScale(playHovered ? 1.25f : 1.0f);
        bitmapFont.draw(spriteBatch, "PLAY < " + selectedDifficulty.toString() + " >", 100, 450);
        bitmapFont.getData().setScale(creditsHovered ? 1.25f : 1.0f);
        bitmapFont.draw(spriteBatch, "CREDITS", 100, 350);
        bitmapFont.getData().setScale(quitHovered ? 1.25f : 1.0f);
        bitmapFont.draw(spriteBatch, "QUIT", 100, 250);
        
        spriteBatch.end();
    }
    
    private void changeDifficulty(Commands.Command command){
        hadCommand = true;
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
