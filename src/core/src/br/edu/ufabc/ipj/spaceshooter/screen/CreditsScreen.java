package br.edu.ufabc.ipj.spaceshooter.screen;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import br.edu.ufabc.ipj.spaceshooter.utils.Utilities;
import br.edu.ufabc.ipj.spaceshooter.utils.Utilities.ScreenAxis;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class CreditsScreen extends BaseScreen {
    
    private float countTimer;
    private float batchOffset;
    
    private boolean hadKeyCommand;
    
    private final String gameTitle;
    private final String gameCredits;
    
    private final Texture texture;
    private final Texture tBackBtn;
    private final Matrix4 viewMatrix;
    private final Matrix4 transMatrix;
    private final BitmapFont bitmapTitleFont;
    private final SpriteBatch spriteBatch;
    
    public CreditsScreen(String id){
        super(id);
        
        countTimer = 0;
        batchOffset = 0;
        
        hadKeyCommand = Commands.hasCommand();
        
        gameTitle = "   }\nShooter";
        
        gameCredits = "Game logic developer:\n"
                        + "    Mauro Mascarenhas de\n    Araújo" 
                        + "\n\nSpecial thanks to:\n"
                        + "    Francisco Isidro\n    Masseto"
                        + "\n\nHUD icons by:\n    Freepik (CC-BY)"
                        + "\n\nSoundtrack:\n"
                        + "    - 8-Bit Explosion 04\n"
                        + "       ~ Little Robot Sound\n"
                        + "       Factory (CC-BY)\n\n"
                        + "    - Retro Explosion 05\n"
                        + "       ~ MATTIX (CC-BY)\n\n"
                        + "    - Laser3 ~ NS Studios\n"
                        + "        (CC - BY)\n\n"
                        + "    - Spaceship Atmosphere\n"
                        + "      04 ~ Tristan Lohengrin\n"
                        + "        (CC - BY)\n\n"
                        + "    - Spaceship Atmosphere\n"
                        + "      05 ~ Tristan Lohengrin\n"
                        + "        (CC - BY)"
                        + "\n\n3D Models:\n"
                        + "    - 3D Scifi pedestal\n"
                        + "    turntable model ~ Bill\n"
                        + "      Nguyen (RFL)\n\n"
                        + "    - Rudymnv Cosair\n"
                        + "      ~ rUDYmNV (RFL)\n\n"
                        + "    - SF Fighter 3D model\n"
                        + "      ~ CGPitbull (RFL)\n\n"
                        + "    - Asteroid ~ Printable\n"
                        + "      Models (Personal use)\n\n"
                        + "    - Cargo Starship\n"
                        + "      ~ Herminio Nieves\n"
                        + "        (CC-BY)\n\n"
                        + "    - AMRAAM Missile 3D\n"
                        + "      model ~ CadNav\n"
                        + "        (Personal use)\n\n"
                        + "    - Intergalactic\n"
                        + "      Spaceship ~ Dennis\n"
                        + "        Haupt (Personal use)\n\n"
                        + "    - Shot\n"
                        + "      ~ Mauro Mascarenhas\n"
                        + "        (CC-BY-SA)"
                        + "\n\nFonts:\n"
                        + "    - Space Age\n"
                        + "      ~ Justin Callaghan\n"
                        + "        (Personal use)";
        
        texture = new Texture("static_images/space_background_dark_3.png");
        tBackBtn = new Texture("static_images/back_icon.png");
        
        viewMatrix = new Matrix4();
        transMatrix = new Matrix4();
        spriteBatch = new SpriteBatch();
        
        bitmapTitleFont = new BitmapFont(Gdx.files.internal("fonts/space_age/big_bold/space_age.fnt"));
    }
    
    @Override
    public void dispose() {
        texture.dispose();
        spriteBatch.dispose();
        bitmapTitleFont.dispose();
        
        super.dispose();
    }
    
    @Override
    public void update(float delta) {
        // Gives priority to keyboard commands
        if (!hadKeyCommand && (Commands.hasCommand(Commands.Command.SHOT) || 
                Commands.hasCommand(Commands.Command.ESCAPE))){
            SpaceShooterGame.playMenuSelectionBeep();
            this.setDone(true);
            return;
        }
        else hadKeyCommand = Commands.hasCommand();
        
        int xCoord = Utilities.toGameCoordinates(ScreenAxis.X, Gdx.input.getX()),
            yCoord = Utilities.toGameCoordinates(ScreenAxis.Y, Gdx.input.getY());
        
        if (batchOffset > 4120 ||
                (yCoord >= 630  && yCoord <= 730
                        && xCoord <= 150 && xCoord >= 50 
                        && Gdx.input.justTouched())){
            SpaceShooterGame.playMenuSelectionBeep();
            this.setDone(true);
        }
        
        // Increases timer and offset
        if (countTimer > 3.0f)
            batchOffset += (30 * delta);
        else countTimer += delta;
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
                            1920, 1080, false, false);
        
        bitmapTitleFont.getData().setScale(1.25f);
        bitmapTitleFont.draw(spriteBatch, gameTitle, 450, 620 + batchOffset);
        bitmapTitleFont.getData().setScale(1.00f);
        bitmapTitleFont.draw(spriteBatch, gameCredits, 100, 400 + batchOffset);
        
        spriteBatch.draw(tBackBtn, 50, 630, 100.0f, 100.0f, 0, 0, 512, 512, false, false);
        
        spriteBatch.end();
    }
}
