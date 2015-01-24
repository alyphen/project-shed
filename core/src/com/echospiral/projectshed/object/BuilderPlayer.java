package com.echospiral.projectshed.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.echospiral.projectshed.screen.GameScreen;
import com.echospiral.projectshed.world.World;

import static com.echospiral.projectshed.world.World.COLUMN_WIDTH;
import static com.echospiral.projectshed.world.World.ROW_HEIGHT;

/**
 * A Player that disables most collisions and can build walls.
 */
public class BuilderPlayer extends Player {
    GameScreen gameScreen;

    public BuilderPlayer(World world, GameScreen gameScreen, int x, int y, Animation moveUpAnimation, Animation moveLeftAnimation,
                  Animation moveRightAnimation, Animation moveDownAnimation) {
        super(world, x, y, moveUpAnimation, moveLeftAnimation, moveRightAnimation, moveDownAnimation);
        this.gameScreen = gameScreen;
    }

    @Override
    protected void doCollisions() {
        // Collide with the camera bounds

        Camera c = gameScreen.getCamera();
        Rectangle screen_rect = new Rectangle(0, 0, c.viewportWidth, c.viewportHeight);
        Player mainPlayer = world.getPlayer();
        screen_rect.setCenter(mainPlayer.getX(), mainPlayer.getY());

        Rectangle left = new Rectangle(screen_rect.x - 100, screen_rect.y - 100, 100, screen_rect.height + 200);
        Rectangle right = new Rectangle(screen_rect.x + screen_rect.width, screen_rect.y - 100, 100, screen_rect.height + 200);
        Rectangle top = new Rectangle(screen_rect.x - 100, screen_rect.y + screen_rect.height, screen_rect.width + 200, 100);
        Rectangle bottom = new Rectangle(screen_rect.x - 100, screen_rect.y - 100, screen_rect.width + 200, 100);

        Rectangle bounds = getRelativeBounds(0, 0);

        if(getRelativeBounds(getDx(), 0).overlaps(left)) {
            freeX = false;
            setX((int) (screen_rect.x));
        }

        if(getRelativeBounds(getDx(), 0).overlaps(right)) {
            freeX = false;
            setX((int)(screen_rect.x + screen_rect.width - bounds.width));
        }

        if(getRelativeBounds(0, getDy()).overlaps(top)) {
            freeY = false;
            setY((int) (screen_rect.y + screen_rect.height - bounds.height));
            Gdx.app.log("collide", "top");
        }

        if(getRelativeBounds(0, getDy()).overlaps(bottom)) {
            freeY = false;
            setY((int) (screen_rect.y));
            Gdx.app.log("collide", "bottom");
        }
    }

    @Override
    public void handleInput() {
        super.handleInput();
        if (null != getController() && getController().getActionButton()) {
            boolean posFree = true;
            for (WorldObject object : getWorld().getObjects()) {
                if (object == this) continue;
                if (object instanceof BreakableWall) {
                    BreakableWall wall = (BreakableWall) object;
                    if (getX() > object.getX()
                            && getY() > object.getY()
                            && getX() < object.getX() + COLUMN_WIDTH
                            && getY() < object.getY() + ROW_HEIGHT) {
                        wall.setBuilding(true);
                    } else {
                        wall.setBuilding(false);
                    }
                } else if (getX() + (COLUMN_WIDTH / 2) > object.getX()
                        && getY() + (ROW_HEIGHT / 2) > object.getY()
                        && getX() < object.getX() + COLUMN_WIDTH
                        && getY() < object.getY() + ROW_HEIGHT) {
                    posFree = false;
                }
            }
            if (posFree) {
                BreakableWall wall = new BreakableWall(getWorld(), (getX() / ROW_HEIGHT) * ROW_HEIGHT, (getY() / COLUMN_WIDTH) * COLUMN_WIDTH, 0);
                wall.setBuilding(true);
                getWorld().addObject(wall);
            }
        }
    }
}
