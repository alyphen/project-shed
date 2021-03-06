package com.echospiral.projectshed.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.echospiral.projectshed.world.World;

import static com.badlogic.gdx.graphics.Color.WHITE;

public class Block extends WorldObject {

    public Block(World world, int x, int y) {
        super(world, x, y);
    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(WHITE);
        shapeRenderer.rect(getX(), getY(), world.COLUMN_WIDTH, world.ROW_HEIGHT);
    }

    @Override
    public Rectangle getRelativeBounds(int dx, int dy) {
        return new Rectangle(getX() + dx, getY() + dy, world.COLUMN_WIDTH, world.ROW_HEIGHT);
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
