package br.edu.ufabc.ipj.spaceshooter.screen;

import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import br.edu.ufabc.ipj.spaceshooter.utils.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class CreditsScreen extends BaseScreen {

    private enum Coordinate{
        X,
        Y
    };
    
    private boolean hadKeyCommand;
    
    private final String gameTitle;
    
    private final Texture texture;
    private final Matrix4 viewMatrix;
    private final Matrix4 transMatrix;
    private final BitmapFont bitmapFont;
    private final BitmapFont bitmapTitleFont;
    private final SpriteBatch spriteBatch;
    
    public CreditsScreen(String id){
        super(id);
        
        hadKeyCommand = Commands.hasCommand();
        
        gameTitle = "   }\nShooter";
        
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
        if (!hadKeyCommand && Commands.hasCommand(Commands.Command.SHOT)){
            this.setDone(true);
            return;
        }
        else hadKeyCommand = Commands.hasCommand();
        
        int xCoord = toGameCoordinates(Coordinate.X, Gdx.input.getX()),
            yCoord = toGameCoordinates(Coordinate.Y, Gdx.input.getY());
        
        if (yCoord >= 650 && xCoord <= 150 && Gdx.input.justTouched())
            this.setDone(true);
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
        
        spriteBatch.end();
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
