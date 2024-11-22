import java.awt.Graphics2D;

public class NPC {
    private double x, y;
    private int tileX, tileY;
    private int tileSize;
    private SpriteManager spriteManager;
    private Direction direction;
    private boolean animating;
    private double animationTimer;
    private int currentFrame;
    private final double FRAME_DURATION = 0.1; // Durée de chaque frame en secondes

    public NPC(int tileX, int tileY, int tileSize, SpriteManager spriteManager) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.tileSize = tileSize;
        this.spriteManager = spriteManager;
        this.x = tileX * tileSize;
        this.y = tileY * tileSize;
        this.direction = Direction.DOWN;
        this.animating = false; // Le PNJ est immobile par défaut
        this.animationTimer = 0;
        this.currentFrame = 0;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void update(double deltaTime) {
        if (animating) {
            animationTimer += deltaTime;
            if (animationTimer >= FRAME_DURATION) {
                animationTimer = 0;
                currentFrame = (currentFrame + 1) % 4; 
            }
        } else {
            currentFrame = 0; 
        }
    }

    public void draw(Graphics2D g2d, double cameraX, double cameraY) {
        spriteManager.drawCharacter(g2d, x - cameraX, y - cameraY, direction, false, currentFrame);
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setAnimating(boolean animating) {
        this.animating = animating;
    }
}
