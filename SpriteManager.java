import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.util.HashMap;

public class SpriteManager {
    private HashMap<Direction, BufferedImage[]> walkSprites; 
    private HashMap<Direction, BufferedImage[]> runSprites;  
    private double animationTimer;
    private int currentFrame;
    private final double FRAME_DURATION = 0.1; 

    public SpriteManager(String walkSpritePath, String runSpritePath) {
        walkSprites = new HashMap<>();
        runSprites = new HashMap<>();
        loadSprites(walkSpritePath, runSpritePath);
    }

    private void loadSprites(String walkSpritePath, String runSpritePath) {
        try {
            BufferedImage walkSpriteSheet = ImageIO.read(getClass().getResourceAsStream(walkSpritePath));
            BufferedImage runSpriteSheet = null;

            if (runSpritePath != null) {
                runSpriteSheet = ImageIO.read(getClass().getResourceAsStream(runSpritePath));
            }

            int spriteWidth = walkSpriteSheet.getWidth() / 4;
            int spriteHeight = walkSpriteSheet.getHeight() / 4;

            // Charger les sprites de marche
            for (Direction dir : Direction.values()) {
                BufferedImage[] frames = new BufferedImage[4];
                int dirIndex = dir.ordinal();
                for (int frame = 0; frame < 4; frame++) {
                    frames[frame] = walkSpriteSheet.getSubimage(
                        frame * spriteWidth,
                        dirIndex * spriteHeight,
                        spriteWidth,
                        spriteHeight
                    );
                }
                walkSprites.put(dir, frames);
            }

            // Charger les sprites de course si disponible
            if (runSpriteSheet != null) {
                for (Direction dir : Direction.values()) {
                    BufferedImage[] frames = new BufferedImage[4];
                    int dirIndex = dir.ordinal();
                    for (int frame = 0; frame < 4; frame++) {
                        frames[frame] = runSpriteSheet.getSubimage(
                            frame * spriteWidth,
                            dirIndex * spriteHeight,
                            spriteWidth,
                            spriteHeight
                        );
                    }
                    runSprites.put(dir, frames);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getSprite(boolean running, Direction direction, int frame) {
        if (running && runSprites.containsKey(direction)) {
            return runSprites.get(direction)[frame];
        } else {
            return walkSprites.get(direction)[frame];
        }
    }

    public void update(double deltaTime) {
        animationTimer += deltaTime;
        if (animationTimer >= FRAME_DURATION) {
            animationTimer = 0;
            currentFrame = (currentFrame + 1) % 4; // Supposons 4 frames d'animation
        }
    }

    public void drawCharacter(Graphics2D g2d, double x, double y, Direction direction, boolean running, int frame) {
        BufferedImage sprite = getSprite(running, direction, frame);
        g2d.drawImage(sprite, (int) x, (int) y, null);
    }
    
}
