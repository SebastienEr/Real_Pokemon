import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Player extends Entity implements Drawable, Updatable {
    private SpriteManager spriteManager;
    private double elapsedTime = 0;

    public Player(int startX, int startY, int tileSize, SpriteManager spriteManager) {
        super(startX, startY, tileSize);
        this.spriteManager = spriteManager;

        this.setMoveSpeed(200.0);
        this.setDirection(2); 
    }

    @Override
    public void update(double deltaTime) {
        elapsedTime += deltaTime;
        if (elapsedTime >= 2.0) {
            System.out.println("Player Coordinates: X = " + getTileX() + ", Y = " + getTileY());
            elapsedTime = 0;
            double offsetX = getX() - getTileX() * getTileSize();
            double offsetY = getY() - getTileY() * getTileSize();
            System.out.println("Offset: X = " + offsetX + ", Y = " + offsetY);
        }
    }
    


    @Override
    public void draw(Graphics2D g, double cameraX, double cameraY) {
        int screenX = (int) (getX() - cameraX);
        int screenY = (int) (getY() - cameraY);

        int playerWidth = getTileSize();
        int playerHeight = getTileSize();

        Direction dir = Direction.fromInt(getDirection());
        BufferedImage sprite = spriteManager.getSprite(isRunning(), dir, getAnimationFrame());
        
        g.drawImage(sprite, screenX, screenY, playerWidth, playerHeight, null);
    }
}
