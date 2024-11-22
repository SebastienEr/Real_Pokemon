import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Dimension;
import java.util.HashMap;

public class Map {
    private int[][] mapData;
    private int[][] overlayData;
    private int[][] collisionData;
    private HashMap<Integer, Image> tileImages;
    private final int TILE_SIZE;
    private int width;
    private int height;

    public Map(int[][] mapData, int[][] overlayData, int[][] collisionData, HashMap<Integer, Image> tileImages, int tileSize) {
        this.mapData = mapData;
        this.overlayData = overlayData;
        this.collisionData = collisionData;
        this.tileImages = tileImages;
        this.TILE_SIZE = tileSize;
        this.width = mapData[0].length;
        this.height = mapData.length;
    }

    public void draw(Graphics2D g, double cameraX, double cameraY, int viewWidth, int viewHeight) {
        int firstTileX = (int) (cameraX / TILE_SIZE);
        int firstTileY = (int) (cameraY / TILE_SIZE);
        int lastTileX = firstTileX + viewWidth + 1;
        int lastTileY = firstTileY + viewHeight + 1;

        for (int y = firstTileY; y < lastTileY; y++) {
            for (int x = firstTileX; x < lastTileX; x++) {
                if (x >= 0 && x < width && y >= 0 && y < height) {
                    int screenX = (int) (x * TILE_SIZE - cameraX);
                    int screenY = (int) (y * TILE_SIZE - cameraY);

                    drawTile(g, mapData[y][x], screenX, screenY);

                    if (overlayData != null && y < overlayData.length && x < overlayData[0].length) {
                        int overlayTileID = overlayData[y][x];
                        if (overlayTileID != 16) { // Ne pas dessiner le PNJ en tant que tuile
                            drawOverlayTile(g, x, y, screenX, screenY);
                        }
                    }
                }
            }
        }
    }

    private void drawTile(Graphics2D g, int tileID, int screenX, int screenY) {
        Image tileImage = tileImages.get(tileID);
        if (tileImage != null) {
            g.drawImage(tileImage, screenX, screenY, TILE_SIZE, TILE_SIZE, null);
        }
    }

    private void drawOverlayTile(Graphics2D g, int x, int y, int screenX, int screenY) {
        int overlayTileID = overlayData[y][x];
        Image overlayTileImage = tileImages.get(overlayTileID);

        if (overlayTileID != -1 && overlayTileImage != null) {
            if (overlayTileID == 3 || overlayTileID == 4 || overlayTileID == 15) {
                if (isTopLeftOfLargeTile(x, y, overlayTileID)) {
                    Dimension size = getTileSize(overlayTileID);

                    int adjustedX = screenX - (TILE_SIZE * (size.width - 1));
                    int adjustedY = screenY - (TILE_SIZE * (size.height - 1));

                    g.drawImage(overlayTileImage, adjustedX, adjustedY, TILE_SIZE * size.width, TILE_SIZE * size.height, null);
                }
            } else {
                g.drawImage(overlayTileImage, screenX, screenY, TILE_SIZE, TILE_SIZE, null);
            }
        }
    }

    private Dimension getTileSize(int tileType) {
        switch (tileType) {
            case 3: // Maison
                return new Dimension(3, 3);
            case 4: // Arbre
                return new Dimension(2, 2);
            case 15: // Arbre
                return new Dimension(2, 2);
            default:
                return new Dimension(1, 1);
        }
    }

    private boolean isTopLeftOfLargeTile(int mapX, int mapY, int tileType) {
        if (mapX > 0 && overlayData[mapY][mapX - 1] == tileType) return false;
        if (mapY > 0 && overlayData[mapY - 1][mapX] == tileType) return false;
        return true;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getOverlayData() {
        return overlayData;
    }

    public int[][] getMapData() {
        return mapData;
    }

    public int[][] getCollisionData() {
        return collisionData;
    }
    
}
