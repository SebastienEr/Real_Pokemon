import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class Player extends Entity implements Drawable, Updatable {
    private SpriteManager spriteManager;
    private double elapsedTime = 0;
    private boolean inventoryVisible = false;
    private List<Item> inventory;

    public Player(int startX, int startY, int tileSize, SpriteManager spriteManager) {
        super(startX, startY, tileSize);
        this.spriteManager = spriteManager;
        this.inventory = new ArrayList<>();
        // Calcul de la vitesse de marche en pixels par seconde
        this.setMoveSpeed(200.0);
        this.setDirection(2); // Direction par défaut (bas)
        // Add some items to the inventory for demonstration
        inventory.add(new Item("Potion", new ImageIcon("src/potion.png").getImage(), 5));
        inventory.add(new Item("Pokeball", new ImageIcon("src/pokeball.png").getImage(), 10));
    }

    public void toggleInventory() {
        inventoryVisible = !inventoryVisible;
    }

    public List<Item> getInventory() {
        return inventory;
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

        // Draw the inventory
        if (inventoryVisible) {
            drawInventory(g);
        }
    }

    public void drawInventory(Graphics2D g) {
        int inventoryWidth = 370;
        int inventoryHeight = 270;
        int inventoryX = 20; // Fixed X coordinate
        int inventoryY = 20; // Fixed Y coordinate

        g.setColor(new java.awt.Color(0, 0, 0, 150)); // Semi-transparent black
        g.fillRect(inventoryX, inventoryY, inventoryWidth, inventoryHeight);

        g.setColor(java.awt.Color.WHITE);
        g.drawRect(inventoryX, inventoryY, inventoryWidth, inventoryHeight);

        // Draw slots
        int slotSize = 50;
        int padding = 10;
        int cols = (inventoryWidth - 2 * padding) / slotSize;
        int rows = (inventoryHeight - 2 * padding) / slotSize;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int slotX = inventoryX + padding + col * slotSize;
                int slotY = inventoryY + padding + row * slotSize;
                g.drawRect(slotX, slotY, slotSize, slotSize);

                // Draw item if present
                int itemIndex = row * cols + col;
                if (itemIndex < inventory.size()) {
                    Item item = inventory.get(itemIndex);
                    g.drawImage(item.getImage(), slotX, slotY, slotSize, slotSize, null);
                    g.drawString(String.valueOf(item.getQuantity()), slotX + slotSize - 15, slotY + slotSize - 5);
                }
            }
        }
    }

    // Inner class to represent an item
    class Item {
        private String name;
        private Image image;
        private int quantity;

        public Item(String name, Image image, int quantity) {
            this.name = name;
            this.image = image;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public Image getImage() {
            return image;
        }

        public int getQuantity() {
            return quantity;
        }

        public void decreaseQuantity() {
            if (quantity > 0) {
                quantity--;
            }
        }
    }
}
