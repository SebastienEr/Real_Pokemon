import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Font;



public class GamePanel extends JPanel implements Runnable {
    private final int TILE_SIZE = 64;
    private final int VIEW_WIDTH = 14;
    private final int VIEW_HEIGHT = 12;

    private Player player;
    private NPC npc;
    private MapManager mapManager;
    private InputHandler inputHandler;

    private Thread gameThread;
    private boolean running;
    private boolean paused = false;
    private boolean showingDialog = false;

    private Game game;

    public GamePanel(Game game) {
        this.game = game;
        setFocusable(true);

        SpriteManager playerSpriteManager = new SpriteManager("src/Hero-M-Walk.png", "src/Hero-M-Run.png");
        // SpriteManager npcSpriteManager = new SpriteManager("src/pnj.png", null); // Sprite du PNJ avec animations

        player = new Player(17, 9, TILE_SIZE, playerSpriteManager);
        // npc = new NPC(5, 3, TILE_SIZE, npcSpriteManager);

        mapManager = new MapManager(TILE_SIZE);

        inputHandler = new InputHandler(player, mapManager);
        addKeyListener(inputHandler);

        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }



    public void resumeGame() {
        paused = false;
        player.resetMovement();
        inputHandler.resetKeyStates();
        this.requestFocusInWindow();
    }

    @Override
    public void run() {
        long lastUpdateTime = System.nanoTime();
        final double TARGET_FPS = 60.0;
        final double OPTIMAL_TIME = 1e9 / TARGET_FPS;

        while (running) {
            long now = System.nanoTime();
            double deltaTime = (now - lastUpdateTime) / 1e9;
            lastUpdateTime = now;

            if (!paused) {
                updateGame(deltaTime);
            }

            repaint();

            long sleepTime = (long) ((OPTIMAL_TIME - (System.nanoTime() - now)) / 1e6);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateGame(double deltaTime) {
        inputHandler.update(deltaTime);
        player.update(deltaTime);
        mapManager.updatePlayerPosition(player);
    
        if (mapManager.isInHouse()) {
            if (npc == null) {
                SpriteManager npcSpriteManager = new SpriteManager("src/pnj.png", null);
                npc = new NPC(5, 3, TILE_SIZE, npcSpriteManager);
            }
            npc.update(deltaTime);
        } else {
            npc = null;
        }
    
        checkForBattle();
        handleInteraction();
    }
    

    private void checkForBattle() {
        int tileX = player.getTileX();
        int tileY = player.getTileY();

        int tileKey = mapManager.getCurrentMap().getOverlayData()[tileY][tileX];

        if (tileKey == 5 && Math.random() < 0.005) {
            paused = true;
            player.stopMovement();
            System.out.println("Battle!");
            game.showBattlePanel();
            tileKey = 0;
            tileX = 0;
            tileY = 0;
            System.out.println(tileX);
            System.out.println(tileY);
            System.out.println(tileKey);


            resumeGame();
        }
    }

    private void handleInteraction() {
        if (inputHandler.isEPressed()) {
            if (npc != null && !showingDialog && isPlayerAdjacentToNPC()) {
                adjustNPCDirection();
                showingDialog = true;
            } else if (showingDialog) {
                showingDialog = false;
            }
            inputHandler.resetEPressed();
        }
    }
    

    private boolean isPlayerAdjacentToNPC() {
        if (npc == null) return false;
    
        int playerTileX = player.getTileX();
        int playerTileY = player.getTileY();
    
        int npcTileX = npc.getTileX();
        int npcTileY = npc.getTileY();
    
        return (playerTileX == npcTileX && Math.abs(playerTileY - npcTileY) == 1)
                || (playerTileY == npcTileY && Math.abs(playerTileX - npcTileX) == 1);
    }
    

    private void adjustNPCDirection() {
        int playerTileX = player.getTileX();
        int playerTileY = player.getTileY();
    
        int npcTileX = npc.getTileX();
        int npcTileY = npc.getTileY();
    
        int dx = playerTileX - npcTileX;
        int dy = playerTileY - npcTileY;
    
        if (Math.abs(dx) > Math.abs(dy)) {
            if (playerTileX == 5) {
                npc.setDirection(Direction.UP);
            } else {
                npc.setDirection(Direction.UP);
            }
        } else {
            if (dy > 0) {
                npc.setDirection(Direction.UP);
            } else {
                npc.setDirection(Direction.UP);
            }
        }
    }
    
    

    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (paused) return;

    Graphics2D g2d = (Graphics2D) g;

    g2d.setColor(Color.BLACK);
    g2d.fillRect(0, 0, getWidth(), getHeight());

    double cameraX = player.getX() - (VIEW_WIDTH * TILE_SIZE) / 2.0 + TILE_SIZE / 2.0;
    double cameraY = player.getY() - (VIEW_HEIGHT * TILE_SIZE) / 2.0 + TILE_SIZE / 2.0;

    cameraX = Math.max(0, Math.min(cameraX, mapManager.getMapWidth() * TILE_SIZE - VIEW_WIDTH * TILE_SIZE));
    cameraY = Math.max(0, Math.min(cameraY, mapManager.getMapHeight() * TILE_SIZE - VIEW_HEIGHT * TILE_SIZE));

    mapManager.draw(g2d, cameraX, cameraY, VIEW_WIDTH * TILE_SIZE, VIEW_HEIGHT * TILE_SIZE);

    if (npc != null) {
        npc.draw(g2d, cameraX, cameraY);
    }

    player.draw(g2d, cameraX, cameraY);

    if (showingDialog) {
        drawDialogBox(g2d);
    }
}


    private void drawDialogBox(Graphics2D g2d) {
        int boxWidth = getWidth() - 100;
        int boxHeight = 100;
        int boxX = 50;
        int boxY = getHeight() - boxHeight - 50;

        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Pokemon clasic", Font.PLAIN, 20));
        String dialogText = "Bonjour, Jeune dresseur !\nVa dans les hautes herbes verte pour xp ton pokemon,quand tu te sentiras pret,\nva affronter le pokemon legendaire dans l'herbe rouge en haut du village";
        int lineHeight = g2d.getFontMetrics().getHeight();
        int y = boxY + 22;
        for (String line : dialogText.split("\n")) {
            g2d.drawString(line, boxX + 20, y);
            y += lineHeight;
        }
    }
}
