package br.edu.ufabc.ipj.spaceshooter.screen;

import com.badlogic.gdx.Screen;

public abstract class BaseScreen implements Screen{

    private String id;
    private boolean done;
    
    public BaseScreen(String id){
        this.id = id;
        this.done = false;
    }
    
    public void setDone(boolean done){
        this.done = done;
    }
    
    public boolean isDone(){
        return this.done;
    }
    
    public String getId(){
        return this.id;
    }
    
    // toda tela terá que implementar estes 2 métodos  
    public abstract void update(float delta);
    public abstract void draw(float delta);
    
    @Override
    public void show() {
        // Won't perform anything
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw(delta);
    }

    @Override
    public void resize(int i, int i1) {
        // Won't perform anything
    }

    @Override
    public void pause() {
        // Won't perform anything
    }

    @Override
    public void resume() {
        // Won't perform anything
    }

    @Override
    public void hide() {
        // Won't perform anything
    }

    @Override
    public void dispose() {
        // Won't perform anything
    }
    
}
