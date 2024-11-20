// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

// public class SimpleWindow {
//     private static JFrame frame;
//     private static Pokemon playerPokemon = PokemonList.getPlayerPokemon();
//     private static Pokemon currentOpponent = PokemonList.getRandomPokemon();
//     private static JProgressBar playerHpBar;
//     private static JProgressBar opponentHpBar;
//     private static JLabel playerImageLabel = new JLabel(new ImageIcon(SimpleWindow.class.getResource("/src/" + playerPokemon.getName() + "Back.png")));
//     private static JLabel opponentImageLabel;
//     private static JLabel turnLabel = new JLabel("", JLabel.CENTER);
//     private static boolean isPlayerTurn = true;
//     private static JLabel playerXpLabel = new JLabel();
//     private static JLabel opponentLevelLabel = new JLabel();

//     public static void createAndShowGUI() {
//         frame = new JFrame("Pokemon Battle");
//         frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//         frame.setSize(900, 820);

//         initializeHpBars();
//         initializeMenuBar();
//         JPanel panel = createMainPanel();

//         frame.add(panel);
//         frame.setVisible(true);

//         updateTurn();
//     }

//     private static void initializeHpBars() {
//         playerHpBar = new JProgressBar(0, playerPokemon.getMaxHp());
//         opponentHpBar = new JProgressBar(0, currentOpponent.getMaxHp());

//         playerHpBar.setValue(playerPokemon.getHp());
//         playerHpBar.setString(playerPokemon.getHp() + "/" + playerPokemon.getMaxHp());
//         playerHpBar.setStringPainted(true);
//         opponentHpBar.setValue(currentOpponent.getHp());
//         opponentHpBar.setString(currentOpponent.getHp() + "/" + currentOpponent.getMaxHp());
//         opponentHpBar.setStringPainted(true);
//     }

//     private static void initializeMenuBar() {
//         JMenuBar menuBar = new JMenuBar();
//         frame.setJMenuBar(menuBar);
//     }

//     private static JButton createAttackButton(String attackName, int attackIndex) {
//         JButton attackButton = new JButton(attackName);
//         attackButton.addActionListener(new ActionListener() {
//             public void actionPerformed(ActionEvent e) {
//                 if (isPlayerTurn && currentOpponent.isAlive()) {
//                     String[] attacks = playerPokemon.getAttacks();
//                     String selectedAttack = attacks[attackIndex];
//                     int damage = Integer.parseInt(selectedAttack.split(":")[1]);
//                     currentOpponent.takeDamage(damage, playerPokemon.getClass().getSimpleName());
//                     opponentHpBar.setValue(currentOpponent.getHp());
//                     opponentHpBar.setString(currentOpponent.getHp() + "/" + currentOpponent.getMaxHp());
//                     checkGameOver();
//                     isPlayerTurn = false;
//                     updateTurn();
//                 }
//             }
//         });
//         return attackButton;
//     }

//     private static JPanel createMainPanel() {
//         JPanel panel = new JPanel(new GridBagLayout()) {
//             @Override
//             protected void paintComponent(Graphics g) {
//                 super.paintComponent(g);

//                 ImageIcon background1 = new ImageIcon(getClass().getResource("/src/Terrain.png"));
//                 int terrainX = 0;
//                 int terrainY = 0;
//                 int terrainWidth = getWidth();
//                 int terrainHeight = getHeight() / 2;
//                 g.drawImage(background1.getImage(), terrainX, terrainY, terrainWidth, terrainHeight, this);

//                 ImageIcon background2 = new ImageIcon(getClass().getResource("/src/Attaques.png"));
//                 int attaquesX = 0;
//                 int attaquesY = terrainHeight;
//                 int attaquesWidth = getWidth();
//                 int attaquesHeight = getHeight() / 2;
//                 g.drawImage(background2.getImage(), attaquesX, attaquesY, attaquesWidth, attaquesHeight, this);

//                 ImageIcon dialogue = new ImageIcon(getClass().getResource("/src/Dialogue.png"));
//                 int dialogueWidth = 500;
//                 int dialogueHeight = 100;
//                 int dialogueX = (getWidth() - dialogueWidth) / 2;
//                 int dialogueY = attaquesY + 50;
//                 g.drawImage(dialogue.getImage(), dialogueX, dialogueY, dialogueWidth, dialogueHeight, this);

//                 g.setFont(new Font("Pokemon Classic", Font.BOLD, 14));
//                 g.setColor(Color.BLACK);
//                 String turnText = isPlayerTurn
//                         ? "Que doit faire\n" + playerPokemon.getName() + " ?"
//                         : currentOpponent.getName() + " utilise " + currentOpponent.getAttacks()[0].split(":")[0] + " !";
//                 drawString(g, turnText, dialogueX + 20, dialogueY + 30);

//                 int opponentImageX = getWidth() - opponentImageLabel.getIcon().getIconWidth() - 100;
//                 int opponentImageY = terrainY + 50;
//                 g.drawImage(((ImageIcon) opponentImageLabel.getIcon()).getImage(), opponentImageX, opponentImageY, this);

//                 int playerImageX = terrainX + 100;
//                 int playerImageY = terrainY + terrainHeight - playerImageLabel.getIcon().getIconHeight() - 50;
//                 g.drawImage(((ImageIcon) playerImageLabel.getIcon()).getImage(), playerImageX, playerImageY, this);
//             }

//             private void drawString(Graphics g, String text, int x, int y) {
//                 for (String line : text.split("\n")) {
//                     g.drawString(line, x, y);
//                     y += g.getFontMetrics().getHeight();
//                 }
//             }
//         };

//         GridBagConstraints gbc = new GridBagConstraints();
//         gbc.insets = new Insets(10, 10, 10, 10);
//         gbc.gridx = 0;
//         gbc.gridy = 0;
//         gbc.gridwidth = 2;

//         gbc.gridy = 1;
//         turnLabel.setFont(new Font("Pokemon Classic", Font.PLAIN, 18));
//         panel.add(turnLabel, gbc);

//         gbc.gridwidth = 1;
//         gbc.gridy = 2;

//         opponentImageLabel = new JLabel(new ImageIcon(SimpleWindow.class.getResource("/src/" + currentOpponent.getName() + ".png")));

//         gbc.gridx = 0;
//         gbc.gridy = 0;
//         panel.add(playerHpBar, gbc);

//         gbc.gridx = 5;
//         gbc.gridy = 0;
//         panel.add(opponentHpBar, gbc);

//         gbc.gridx = 1;
//         gbc.gridy = 7;
//         String[] attacks = playerPokemon.getAttacks();
//         JButton attackButton1 = createAttackButton(attacks[0].split(":")[0], 0);
//         panel.add(attackButton1, gbc);

//         gbc.gridx = 1;
//         gbc.gridy = 8;
//         JButton attackButton2 = createAttackButton(attacks[1].split(":")[0], 1);
//         panel.add(attackButton2, gbc);

//         gbc.gridx = 1;
//         gbc.gridy = 9;
//         JButton usePotionButton = createPotionButton();
//         panel.add(usePotionButton, gbc);

//         updateXpLabels();

//         gbc.gridy = 7;
//         panel.add(playerXpLabel, gbc);
//         gbc.gridx = 1;
//         panel.add(opponentLevelLabel, gbc);

//         return panel;
//     }

//     private static JButton createPotionButton() {
//         JButton usePotionButton = new JButton("Use Potion");
//         usePotionButton.addActionListener(new ActionListener() {
//             public void actionPerformed(ActionEvent e) {
//                 if (isPlayerTurn && playerPokemon.isAlive()) {
//                     playerPokemon.heal(20);
//                     playerHpBar.setValue(playerPokemon.getHp());
//                     playerHpBar.setString(playerPokemon.getHp() + "/" + playerPokemon.getMaxHp());
//                     isPlayerTurn = false;
//                     updateTurn();
//                 }
//             }
//         });
//         return usePotionButton;
//     }

//     private static void updateXpLabels() {
//         playerXpLabel.setText("XP: " + playerPokemon.getXp() + " | Level: " + playerPokemon.getLevel());
//         opponentLevelLabel.setText("Level: " + currentOpponent.getLevel());
//     }

//     private static void updateTurn() {
//         if (!playerPokemon.isAlive()) {
//         } else if (!currentOpponent.isAlive()) {
//         } else {
//             turnLabel.setFont(new Font("Pokemon Classic", Font.BOLD, 14));
//             displayTurnText("", new ActionListener() {
//                 @Override
//                 public void actionPerformed(ActionEvent e) {
//                     if (!isPlayerTurn && currentOpponent.isAlive()) {
//                         Timer timer = new Timer(1000, new ActionListener() {
//                             @Override
//                             public void actionPerformed(ActionEvent e) {
//                                 playerPokemon.takeDamage(currentOpponent.getAttack());
//                                 playerHpBar.setValue(playerPokemon.getHp());
//                                 playerHpBar.setString(playerPokemon.getHp() + "/" + playerPokemon.getMaxHp());
//                                 checkGameOver();
//                                 isPlayerTurn = true;
//                                 updateTurn();
//                             }
//                         });
//                         timer.setRepeats(false);
//                         timer.start();
//                     }
//                 }
//             });
//         }
//     }

//     private static void displayTurnText(String text, ActionListener onComplete) {
//         turnLabel.setText("");
//         Timer timer = new Timer(50, new ActionListener() {
//             int index = 0;

//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 if (index < text.length()) {
//                     turnLabel.setText(turnLabel.getText() + text.charAt(index));
//                     index++;
//                 } else {
//                     ((Timer) e.getSource()).stop();
//                     onComplete.actionPerformed(e);
//                 }
//             }
//         });
//         timer.start();
//     }

//     private static void checkGameOver() {
//         if (!playerPokemon.isAlive()) {
//             playerHpBar.setString("K.O");
//             currentOpponent.gainXp(5000);
//         } else if (!currentOpponent.isAlive()) {
//             opponentHpBar.setString("K.O");
//             playerPokemon.gainXp(2500);
//         }
//         updateXpLabels();
//     }
// }
