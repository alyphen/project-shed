package com.echospiral.projectshed.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.echospiral.projectshed.ProjectShed;

import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static java.lang.Math.ceil;

public class WinScreen extends ScreenAdapter {

    private ProjectShed game;

    private String winner;
    private BitmapFont font;
    private Sound music;

    private float countdown;

    private Screen nextScreen;

    private OrthographicCamera camera;

    public WinScreen(ProjectShed game) {
        this.game = game;
        font = new BitmapFont();
        winner = "No one";
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        music = Gdx.audio.newSound(Gdx.files.internal("music/winscreen.ogg"));

    }

    public void playSound() {
        music.play();

    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Screen getNextScreen() {
        return nextScreen;
    }

    public void setNextScreen(Screen nextScreen) {
        this.nextScreen = nextScreen;
    }

    public void resetCountdown() {
        countdown = 3F;
    }

    public void dispose() {
        music.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        game.getSpriteBatch().begin();
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        font.setColor(0.0f, 1.0f, 0.0f, 1.0f);
        font.setScale(8);
        font.drawWrapped(game.getSpriteBatch(), getWinner() + " wins!\n(Next match starting in " + (int) ceil(countdown) + " seconds)", 20, 570, 728);
        game.getSpriteBatch().end();
        countdown -= delta;
        if (countdown <= 0) game.setScreen(getNextScreen());
    }
}
