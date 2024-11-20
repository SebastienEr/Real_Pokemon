import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class BattlePanel extends JPanel {
    private Game game;
    private Pokemon playerPokemon;
    private Pokemon currentOpponent;
    private JProgressBar playerHpBar;
    private JProgressBar opponentHpBar;
    private JLabel playerImageLabel;
    private JLabel opponentImageLabel;
    private JLabel turnLabel;
    private boolean isPlayerTurn;
    private JLabel playerXpLabel;
    private JLabel opponentLevelLabel;

    public BattlePanel(Game game) {
        this.game = game;
        setLayout(new BorderLayout());
        initializeComponents();
        
    }

    public void startBattle() {
        playMusic("src/battle_pokemon.wav");
        playerPokemon = PokemonList.getPlayerPokemon();
        currentOpponent = PokemonList.getRandomPokemon();
        isPlayerTurn = true;
        initializeHpBars();
        removeAll();
        add(createMainPanel(), BorderLayout.CENTER);
        revalidate();
        repaint();
        updateTurn();
    }

    private void initializeComponents() {
        turnLabel = new JLabel("", JLabel.CENTER);
    }

    private void initializeHpBars() {
        playerHpBar = new JProgressBar(0, playerPokemon.getMaxHp());
        opponentHpBar = new JProgressBar(0, currentOpponent.getMaxHp());

        playerHpBar.setValue(playerPokemon.getHp());
        playerHpBar.setString(playerPokemon.getHp() + "/" + playerPokemon.getMaxHp());
        playerHpBar.setStringPainted(true);
        opponentHpBar.setValue(currentOpponent.getHp());
        opponentHpBar.setString(currentOpponent.getHp() + "/" + currentOpponent.getMaxHp());
        opponentHpBar.setStringPainted(true);
    }

    private JButton createAttackButton(String attackName, int attackIndex) {
        JButton attackButton = new JButton(attackName);
        attackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isPlayerTurn && currentOpponent.isAlive()) {
                    String[] attacks = playerPokemon.getAttacks();
                    String selectedAttack = attacks[attackIndex];
                    int damage = Integer.parseInt(selectedAttack.split(":")[1]);
                    currentOpponent.takeDamage(damage, playerPokemon.getClass().getSimpleName());
                    opponentHpBar.setValue(currentOpponent.getHp());
                    opponentHpBar.setString(currentOpponent.getHp() + "/" + currentOpponent.getMaxHp());
                    checkGameOver();
                    isPlayerTurn = false;
                    updateTurn();
                }
            }
        });
        return attackButton;
    }

    private static Clip clip;

    private static void playMusic(String filepath) {
        try {
            File musicPath = new File(filepath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.addLineListener(new LineListener() {
                    @Override
                    public void update(LineEvent event) {
                        if (event.getType() == LineEvent.Type.STOP) {
                            clip.setFramePosition(0); // Restart the music
                            clip.start();
                        }
                    }
                });
                clip.start();
            } else {
                System.out.println("Music file not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                ImageIcon background1 = new ImageIcon(getClass().getResource("/src/Terrain.png"));
                int terrainX = 0;
                int terrainY = 0;
                int terrainWidth = getWidth();
                int terrainHeight = getHeight() / 2;
                g.drawImage(background1.getImage(), terrainX, terrainY, terrainWidth, terrainHeight, this);

                ImageIcon background2 = new ImageIcon(getClass().getResource("/src/Attaques.png"));
                int attaquesX = 0;
                int attaquesY = terrainHeight;
                int attaquesWidth = getWidth();
                int attaquesHeight = getHeight() / 2;
                g.drawImage(background2.getImage(), attaquesX, attaquesY, attaquesWidth, attaquesHeight, this);

                ImageIcon dialogue = new ImageIcon(getClass().getResource("/src/Dialogue.png"));
                int dialogueWidth = 500;
                int dialogueHeight = 100;
                int dialogueX = (getWidth() - dialogueWidth) / 2;
                int dialogueY = attaquesY + 50;
                g.drawImage(dialogue.getImage(), dialogueX, dialogueY, dialogueWidth, dialogueHeight, this);

                g.setFont(new Font("Pokemon Classic", Font.BOLD, 14));
                g.setColor(Color.BLACK);
                String turnText = isPlayerTurn
                        ? "Que doit faire\n" + playerPokemon.getName() + " ?"
                        : currentOpponent.getName() + " utilise " + currentOpponent.getAttacks()[0].split(":")[0] + " !";
                drawString(g, turnText, dialogueX + 20, dialogueY + 30);

                int opponentImageX = getWidth() - opponentImageLabel.getIcon().getIconWidth() - 100;
                int opponentImageY = terrainY + 50;
                g.drawImage(((ImageIcon) opponentImageLabel.getIcon()).getImage(), opponentImageX, opponentImageY, this);

                int playerImageX = terrainX + 100;
                int playerImageY = terrainY + terrainHeight - playerImageLabel.getIcon().getIconHeight() - 50;
                g.drawImage(((ImageIcon) playerImageLabel.getIcon()).getImage(), playerImageX, playerImageY, this);
            }

            private void drawString(Graphics g, String text, int x, int y) {
                for (String line : text.split("\n")) {
                    g.drawString(line, x, y);
                    y += g.getFontMetrics().getHeight();
                }
            }
        };

        playerImageLabel = new JLabel(new ImageIcon(getClass().getResource("/src/" + playerPokemon.getName() + "Back.png")));
        opponentImageLabel = new JLabel(new ImageIcon(getClass().getResource("/src/" + currentOpponent.getName() + ".png")));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        gbc.gridy = 1;
        turnLabel.setFont(new Font("Pokemon Classic", Font.PLAIN, 18));
        panel.add(turnLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(playerHpBar, gbc);

        gbc.gridx = 5;
        gbc.gridy = 0;
        panel.add(opponentHpBar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        String[] attacks = playerPokemon.getAttacks();
        JButton attackButton1 = createAttackButton(attacks[0].split(":")[0], 0);
        panel.add(attackButton1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        JButton attackButton2 = createAttackButton(attacks[1].split(":")[0], 1);
        panel.add(attackButton2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        JButton usePotionButton = createPotionButton();
        panel.add(usePotionButton, gbc);

        updateXpLabels();

        gbc.gridy = 7;
        panel.add(playerXpLabel, gbc);
        gbc.gridx = 1;
        panel.add(opponentLevelLabel, gbc);

        return panel;
    }

    private JButton createPotionButton() {
        JButton usePotionButton = new JButton("Use Potion");
        usePotionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isPlayerTurn && playerPokemon.isAlive()) {
                    playerPokemon.heal(20);
                    playerHpBar.setValue(playerPokemon.getHp());
                    playerHpBar.setString(playerPokemon.getHp() + "/" + playerPokemon.getMaxHp());
                    isPlayerTurn = false;
                    updateTurn();
                }
            }
        });
        return usePotionButton;
    }

    private void updateXpLabels() {
        stopMusic();
            clip.stop();    
        playerXpLabel = new JLabel("XP: " + playerPokemon.getXp() + " | Level: " + playerPokemon.getLevel());
        opponentLevelLabel = new JLabel("Level: " + currentOpponent.getLevel());
    }

    private void updateTurn() {
        if (!playerPokemon.isAlive()) {
            game.showGamePanel();
            stopMusic();
            clip.stop();

        } else if (!currentOpponent.isAlive()) {
            playerPokemon.gainXp(2500);
            game.showGamePanel();
            stopMusic();
            clip.stop();    

            
        } else {
            stopMusic();
            clip.stop();    
            turnLabel.setFont(new Font("Pokemon Classic", Font.BOLD, 14));
            displayTurnText("", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!isPlayerTurn && currentOpponent.isAlive()) {
                        Timer timer = new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                playerPokemon.takeDamage(currentOpponent.getAttack());
                                playerHpBar.setValue(playerPokemon.getHp());
                                playerHpBar.setString(playerPokemon.getHp() + "/" + playerPokemon.getMaxHp());
                                checkGameOver();
                                isPlayerTurn = true;
                                updateTurn();
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
            });
        }
    }

    private void displayTurnText(String text, ActionListener onComplete) {
        turnLabel.setText("");
        Timer timer = new Timer(50, new ActionListener() {
            int index = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < text.length()) {
                    turnLabel.setText(turnLabel.getText() + text.charAt(index));
                    index++;
                } else {
                    ((Timer) e.getSource()).stop();
                    onComplete.actionPerformed(e);
                }
            }
        });
        timer.start();
    }

    private void checkGameOver() {
        if (!playerPokemon.isAlive()) {
            playerHpBar.setString("K.O");
        } else if (!currentOpponent.isAlive()) {
            opponentHpBar.setString("K.O");
            playerPokemon.gainXp(2500);
        }
        updateXpLabels();
    }
}
