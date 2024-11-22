import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;
    private BattlePanel battlePanel;

    public Game() {
        gamePanel = new GamePanel(this);

        setTitle("Jeu Pokémon");
        setSize(896, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        gamePanel = new GamePanel(this);
        battlePanel = new BattlePanel(this);

        mainPanel.add(gamePanel, "GamePanel");
        mainPanel.add(battlePanel, "BattlePanel");

        add(mainPanel);

        setVisible(true);
    }

    public void showBattlePanel() {
        cardLayout.show(mainPanel, "BattlePanel");
        battlePanel.startBattle();
    }

    public void showGamePanel() {
        cardLayout.show(mainPanel, "GamePanel");
        gamePanel.resumeGame();
    }
    public void resumeGame() {
        gamePanel.resumeGame();
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }
}
