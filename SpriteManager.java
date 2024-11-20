import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteManager {
    private BufferedImage[][][] sprites; // [mode][direction][frame]

    public SpriteManager(String walkSpritePath, String runSpritePath) {
        loadSprites(walkSpritePath, runSpritePath);
    }

    private void loadSprites(String walkSpritePath, String runSpritePath) {
        try {
            BufferedImage walkSpriteSheet = ImageIO.read(getClass().getResourceAsStream(walkSpritePath));
            BufferedImage runSpriteSheet = ImageIO.read(getClass().getResourceAsStream(runSpritePath));

            int spriteWidth = walkSpriteSheet.getWidth() / 4;
            int spriteHeight = walkSpriteSheet.getHeight() / 4;

            sprites = new BufferedImage[2][4][4]; // [0]: marche, [1]: course

            // Charger les sprites de marche
            for (int dir = 0; dir < 4; dir++) {
                for (int frame = 0; frame < 4; frame++) {
                    sprites[0][dir][frame] = walkSpriteSheet.getSubimage(
                        frame * spriteWidth,
                        dir * spriteHeight,
                        spriteWidth,
                        spriteHeight
                    );
                }
            }

            // Charger les sprites de course
            for (int dir = 0; dir < 4; dir++) {
                for (int frame = 0; frame < 4; frame++) {
                    sprites[1][dir][frame] = runSpriteSheet.getSubimage(
                        frame * spriteWidth,
                        dir * spriteHeight,
                        spriteWidth,
                        spriteHeight
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getSprite(boolean running, int direction, int frame) {
        int mode = running ? 1 : 0;
        return sprites[mode][direction][frame];
    }
}
