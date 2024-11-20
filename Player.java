import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Player extends Entity implements Drawable, Updatable {
    private SpriteManager spriteManager;
    private double elapsedTime = 0;

    public Player(int startX, int startY, int tileSize, SpriteManager spriteManager) {
        super(startX, startY, tileSize);
        this.spriteManager = spriteManager;

        // Calcul de la vitesse de marche en pixels par seconde
        this.setMoveSpeed(200.0);
        this.setDirection(2); // Direction par défaut (bas)
    }

    @Override
    public void update(double deltaTime) {
        // Accumuler le temps écoulé pour afficher les coordonnées toutes les 2 secondes
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

        BufferedImage sprite = spriteManager.getSprite(isRunning(), getDirection(), getAnimationFrame());

        g.drawImage(sprite, screenX, screenY, playerWidth, playerHeight, null);
    }
}
