import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class MapManager {
    private final int TILE_SIZE;
    private HashMap<Integer, Image> tileImages;
    private Map currentMap;
    private HashMap<String, Map> maps;
    private boolean inHouse = false;

    public MapManager(int tileSize) {
        this.TILE_SIZE = tileSize;
        tileImages = new HashMap<>();
        maps = new HashMap<>();
        loadTileImages();
        loadMaps();
        currentMap = maps.get("world");
    }

    private void loadTileImages() {
        tileImages.put(0, new ImageIcon("src/Grass.png").getImage());
        tileImages.put(1, new ImageIcon("src/Chemin.png").getImage());
        tileImages.put(2, new ImageIcon("src/Water.png").getImage());
        tileImages.put(3, new ImageIcon("src/output-onlinepngtools.png").getImage());
        tileImages.put(4, new ImageIcon("src/Treee.png").getImage());
        tileImages.put(5, new ImageIcon("src/Herbe.png").getImage());
        tileImages.put(6, new ImageIcon("src/HouseGround.png").getImage());
        tileImages.put(7, new ImageIcon("src/Table.png").getImage());
        tileImages.put(8, new ImageIcon("src/Carpet.png").getImage());
        tileImages.put(9, new ImageIcon("src/Tele.png").getImage());
        tileImages.put(10, new ImageIcon("src/Cuisine.png").getImage());
        tileImages.put(11, new ImageIcon("src/Frigo.png").getImage());
        tileImages.put(12, new ImageIcon("src/Fenetre.png").getImage());
        tileImages.put(13, new ImageIcon("src/Meuble1.png").getImage());
        tileImages.put(14, new ImageIcon("src/mur.png").getImage());
        tileImages.put(15, new ImageIcon("src/Treee.png").getImage());
        // tileImages.put(16, new ImageIcon("src/pnj.png").getImage()); // Retiré car le PNJ est géré séparément
    }

    private void loadMaps() {
        maps.put("world", loadMap("map.txt", "map1.txt", "collision.txt"));
        maps.put("house", loadMap("housemapoverlay.txt", "housemap.txt", "house_collision.txt"));
    }

    private Map loadMap(String mapFile, String overlayFile, String collisionFile) {
        int[][] mapData = loadMapData(mapFile);
        int[][] overlayData = loadMapData(overlayFile);
        int[][] collisionData = loadMapData(collisionFile);
        return new Map(mapData, overlayData, collisionData, tileImages, TILE_SIZE);
    }

    private int[][] loadMapData(String filename) {
        ArrayList<int[]> mapRows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(" ");
                int[] row = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    row[i] = Integer.parseInt(tokens[i]);
                }
                mapRows.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapRows.toArray(new int[0][]);
    }

    public void draw(Graphics2D g2d, double cameraX, double cameraY, int viewWidth, int viewHeight) {
        currentMap.draw(g2d, cameraX, cameraY, viewWidth, viewHeight);
    }

    public void updatePlayerPosition(Player player) {
        int tileX = player.getTileX();
        int tileY = player.getTileY();

        if (!inHouse && tileX == 11 && tileY == 5) {
            enterHouse(player);
        } else if (inHouse && currentMap.getOverlayData()[tileY][tileX] == 8) {
            exitHouse(player);
        }
    }

    private void enterHouse(Player player) {
        currentMap = maps.get("house");
        player.setPosition(4, 5, 0, 33);
        inHouse = true;
    }

    private void exitHouse(Player player) {
        currentMap = maps.get("world");
        player.setPosition(11, 6, 26.577, 16.794);
        inHouse = false;
    }

    public int getMapWidth() {
        return currentMap.getWidth();
    }

    public int getMapHeight() {
        return currentMap.getHeight();
    }

    public int getTileSize() {
        return TILE_SIZE;
    }

    public boolean isInHouse() {
        return inHouse;
    }

    public Map getCurrentMap() {
        return currentMap;
    }
}
