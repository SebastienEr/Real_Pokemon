import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
    private boolean upPressed, downPressed, leftPressed, rightPressed, sprintPressed, ePressed;
    private Player player;
    private MapManager mapManager;

    public InputHandler(Player player, MapManager mapManager) {
        this.player = player;
        this.mapManager = mapManager;
    }

    public void update(double deltaTime) {
        boolean moving = false;

        if (upPressed) {
            player.moveUp(deltaTime, mapManager);
            moving = true;
        }
        if (downPressed) {
            player.moveDown(deltaTime, mapManager);
            moving = true;
        }
        if (leftPressed) {
            player.moveLeft(deltaTime, mapManager);
            moving = true;
        }
        if (rightPressed) {
            player.moveRight(deltaTime, mapManager);
            moving = true;
        }

        if (moving) {
            if (sprintPressed) {
                player.setRunning(true);
                player.setMoveSpeed(300.0);
            } else {
                player.setRunning(false);
                player.setMoveSpeed(200.0);
            }
        } else {
            player.setRunning(false);
        }
    }

    public boolean isEPressed() {
        return ePressed;
    }

    public void resetEPressed() {
        ePressed = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = true;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;
            case KeyEvent.VK_SHIFT:
                sprintPressed = true;
                break;
            case KeyEvent.VK_E:
                ePressed = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
            case KeyEvent.VK_SHIFT:
                sprintPressed = false;
                break;
            case KeyEvent.VK_E:
                ePressed = false;
                break;
            default:
                break;
        }
    }

    public void resetKeyStates() {
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        sprintPressed = false;
        ePressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Non utilisé
    }
}
