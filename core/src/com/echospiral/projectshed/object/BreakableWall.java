package com.echospiral.projectshed.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.echospiral.projectshed.world.World;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL;
import static com.echospiral.projectshed.world.World.COLUMN_WIDTH;
import static com.echospiral.projectshed.world.World.ROW_HEIGHT;

public class BreakableWall extends Block {

    private Texture wood;
    private Animation animation;
    private float stateTime;
    private boolean building, destroying;
    private Sound buildingSound;
    private Sound destroyingSound;

    public BreakableWall(World world, int x, int y) {
        this(world, x, y, 7);
    }

    public BreakableWall(World world, int x, int y, int stage) {
        super(world, x, y);
        stateTime = stage;
        this.wood = new Texture(Gdx.files.internal("wood_build.png"));

        this.animation = new Animation(1f, new Array<TextureRegion>() {{
            add(new TextureRegion(wood, 0, 0, 64, 64));
            add(new TextureRegion(wood, 64, 0, 64, 64));
            add(new TextureRegion(wood, 128, 0, 64, 64));
            add(new TextureRegion(wood, 192, 0, 64, 64));
            add(new TextureRegion(wood, 256, 0, 64, 64));
            add(new TextureRegion(wood, 320, 0, 64, 64));
            add(new TextureRegion(wood, 384, 0, 64, 64));
            add(new TextureRegion(wood, 448, 0, 64, 64));
        }} );
        animation.setPlayMode(NORMAL);
        buildingSound = Gdx.audio.newSound(Gdx.files.internal("music/build.ogg"));
        destroyingSound = Gdx.audio.newSound(Gdx.files.internal("music/destruct.ogg"));
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
        if (isBuilding() && stateTime < 7F) stateTime += Math.min(delta, 7F - stateTime);
        if (isDestroying()) {
            if (stateTime > 0F) {
                stateTime -= Math.min(delta*3, stateTime);
            } else {
                getWorld().removeObject(this);
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        spriteBatch.draw(getAnimation().getKeyFrame(stateTime), getX(), getY(), COLUMN_WIDTH, ROW_HEIGHT);
    }

    private Animation getAnimation() {
        return animation;
    }

    @Override
    public Rectangle getRelativeBounds(int dx, int dy) {
        TextureRegion frame = getAnimation().getKeyFrame(stateTime);
        return new Rectangle(getX() + dx, getY() + dy, COLUMN_WIDTH, ROW_HEIGHT);
    }

    public boolean isBuilding() {
        return building;
    }

    public void setBuilding(boolean building) {
        this.building = building;
        if (building) {
            playBuildingSound();
        }
    }

    public void playBuildingSound() {
        buildingSound.play();
    }

    public void playDestroyingSound() {
        destroyingSound.play();
    }


    public boolean isDestroying() {
        return destroying;
    }

    public void setDestroying(boolean destroying) {
        this.destroying = destroying;
        if (destroying) {
            playDestroyingSound();
        }
    }

}
