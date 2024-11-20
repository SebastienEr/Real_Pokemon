import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    private final int TILE_SIZE = 64;
    private final int VIEW_WIDTH = 14;
    private final int VIEW_HEIGHT = 12;

    private Player player;
    private MapManager mapManager;
    private InputHandler inputHandler;

    private Thread gameThread;
    private boolean running;
    private boolean paused = false;

    private Game game;

    public GamePanel(Game game) {
        this.game = game;
        setFocusable(true);

        SpriteManager spriteManager = new SpriteManager("src/Hero-M-Walk.png", "src/Hero-M-Run.png");

        player = new Player(17, 9, TILE_SIZE, spriteManager);

        mapManager = new MapManager(TILE_SIZE);

        inputHandler = new InputHandler(player, mapManager);
        addKeyListener(inputHandler);

        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void resumeGame() {
        paused = false;
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
        checkForBattle();
    }

    private void checkForBattle() {
        int tileX = player.getTileX();
        int tileY = player.getTileY();

        int tileKey = mapManager.getCurrentMap().getOverlayData()[tileY][tileX];

        if (tileKey == 5 && Math.random() < 0.01) {
            paused = true;
            player.stopMovement();; // Ensure the player stops moving
            game.showBattlePanel();
            
        
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
        player.draw(g2d, cameraX, cameraY);
    }
}
