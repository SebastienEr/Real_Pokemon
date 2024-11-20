import java.awt.Graphics2D;

public abstract class Entity {
    private double x;
    private double y;
    private final int TILE_SIZE;
    private double moveSpeed; 
    private int direction;
    private boolean running = false;

    private int animationFrame = 0;
    private double animationCounter = 0;

    public Entity(int startX, int startY, int tileSize) {
        this.x = startX * tileSize;
        this.y = startY * tileSize;
        this.TILE_SIZE = tileSize;
    }

    public abstract void update(double deltaTime);

    public abstract void draw(Graphics2D g, double cameraX, double cameraY);

    protected boolean canMoveTo(double nextX, double nextY, MapManager mapManager) {
        int tileX = (int)(nextX / TILE_SIZE);
        int tileY = (int)(nextY / TILE_SIZE);

        int[][] collisionData = mapManager.getCurrentMap().getCollisionData();
        if (tileY < 0 || tileY >= collisionData.length || tileX < 0 || tileX >= collisionData[0].length) {
            return false;
        }

        // Vérifie si la valeur est 20 pour déterminer la collision
        return collisionData[tileY][tileX] != 20;
    }

    // Changement de la visibilité à public
    public void moveUp(double deltaTime, MapManager mapManager) {
        double nextY = y - moveSpeed * deltaTime;
        if (canMoveTo(x, nextY, mapManager)) {
            y = nextY;
        }
        direction = 3; // Haut
        animate(deltaTime);
    }

    public void moveDown(double deltaTime, MapManager mapManager) {
        double nextY = y + moveSpeed * deltaTime;
        if (canMoveTo(x, nextY, mapManager)) {
            y = nextY;
        }
        direction = 0; // Bas
        animate(deltaTime);
    }

    public void moveLeft(double deltaTime, MapManager mapManager) {
        double nextX = x - moveSpeed * deltaTime;
        if (canMoveTo(nextX, y, mapManager)) {
            x = nextX;
        }
        direction = 1; // Gauche
        animate(deltaTime);
    }

    public void moveRight(double deltaTime, MapManager mapManager) {
        double nextX = x + moveSpeed * deltaTime;
        if (canMoveTo(nextX, y, mapManager)) {
            x = nextX;
        }
        direction = 2; // Droite
        animate(deltaTime);
    }
    public void setMoving(boolean moving) {
        if (!moving) {
            moveSpeed = 0; 
        }
    }

    public void stopMovement() {
        moveSpeed = 0; 
        direction = -1; // Reset the direction to indicate no movement
    }

    public void resetMovement() {
        moveSpeed = 0.1;
    }
    
    public void resetAll() {
        moveSpeed = 0;
        direction = -1;
        running = false;
        animationFrame = 0;
        animationCounter = 0;
        
    }
    


    protected void animate(double deltaTime) {
        double animationSpeed = running ? 0.09 : 0.1; // Vitesse en secondes

        animationCounter += deltaTime;
        if (animationCounter >= animationSpeed) {
            animationFrame = (animationFrame + 1) % 4;
            animationCounter = 0;
        }
    }

    public int getTileX() {
        return (int) (x / TILE_SIZE);
    }

    public int getTileY() {
        return (int) (y / TILE_SIZE);
    }

    public void setPosition(int tileX, int tileY, double offsetX, double offsetY) {
        this.x = tileX * TILE_SIZE + offsetX;
        this.y = tileY * TILE_SIZE + offsetY;
    }

    // Getters and Setters pour les attributs privés

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getAnimationFrame() {
        return animationFrame;
    }

    public void setAnimationFrame(int animationFrame) {
        this.animationFrame = animationFrame;
    }

    public double getAnimationCounter() {
        return animationCounter;
    }

    public void setAnimationCounter(double animationCounter) {
        this.animationCounter = animationCounter;
    }

    public int getTileSize() {
        return TILE_SIZE;
    }
}
