import Players.COLOR_ENUM;
import Game.Game;
import Players.HumanPlayer;
import Grid.Grid;
import Players.MinimaxIAPlayer;
import Players.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Puissance4 {
    private JFrame fenetre;
    private JPanel plateauPanel;
    private JPanel controlsPanel;
    private JComboBox<String> player1ComboBox;
    private JComboBox<String> player2ComboBox;
    private MouseListener mouseListener;

    private Boolean mouseListenerEnabled;
    private Boolean hasPlayed;

    private Player player1;
    private Player player2;

    private Player actual_player;
    private Grid grid;

    public Puissance4() {
        grid = new Grid();
        player1 = new HumanPlayer(COLOR_ENUM.YELLOW);
        player2 = new MinimaxIAPlayer(COLOR_ENUM.RED);
        actual_player = player1;
        hasPlayed = false;
        fenetre = new JFrame("Puissance 4");
        plateauPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < grid.getLength(); i++) {
                    for (int j = 0; j < grid.getWidth(); j++) {
                        g.setColor(getColor(grid.getCell(i,j)));
                        g.fillOval(j * 50 + 25, i * 50 + 25, 25, 25);
                        g.setColor(Color.BLACK);
                        g.drawOval(j * 50 + 25, i * 50 + 25, 25, 25);
                    }
                }
            }

            private Color getColor(COLOR_ENUM c) {
                if (c == null) {
                    return Color.WHITE;
                } else {
                    switch (c) {
                        case YELLOW:
                            return Color.YELLOW;
                        case RED:
                            return Color.RED;
                        default:
                            return Color.WHITE;
                    }
                }
            }
        };
        plateauPanel.setPreferredSize(new Dimension(380, 350));

        controlsPanel = new JPanel(new GridLayout(2, 3)); // Utilisation d'un GridLayout pour les étiquettes et les listes déroulantes
        JLabel label1 = new JLabel("Player 1:");
        JLabel label2 = new JLabel("Player 2:");
        String[] players = {"Humain", "IA minimax", "IA alphabeta", "IA MCTS"}; // Exemple de noms de joueurs
        player1ComboBox = new JComboBox<>(players);
        player2ComboBox = new JComboBox<>(players);
        // Ajout des étiquettes et des listes déroulantes au panneau de contrôles
        controlsPanel.add(label1);
        controlsPanel.add(label2);
        controlsPanel.add(player1ComboBox);
        controlsPanel.add(player2ComboBox);

        // Ajout des panneaux à la fenêtre
        fenetre.add(controlsPanel, BorderLayout.NORTH);
        fenetre.add(plateauPanel, BorderLayout.CENTER);

        mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!getMouseListenerEnabled()) {
                    return;
                }
                int colonne = e.getX() / 50;
                if (colonne >= 0 && colonne < grid.getWidth()) {
                    if (!grid.canAddToken(colonne)) {
                        JOptionPane.showMessageDialog(fenetre, "Cette colonne est déjà pleine!");
                        return;
                    } else {
                        COLOR_ENUM color = actual_player.getColor();
                        grid.addToken(colonne, color);
                        setHasPlayed(true);
                        setMouseListenerEnabled(false);
                    }
                }
            }
        };
        plateauPanel.addMouseListener(mouseListener);

        fenetre.pack();
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setVisible(true);

        while(!grid.gridFull() && !grid.checkWin()){
            setHasPlayed(false);
            while (!getHasPlayed()){
                if(actual_player instanceof HumanPlayer){
                    setMouseListenerEnabled(true);
                } else {
                    grid.addToken(actual_player.do_turn(grid), actual_player.getColor());
                    setHasPlayed(true);
                }
            }
            plateauPanel.repaint();
            if (grid.checkWin()){
                JOptionPane.showMessageDialog(fenetre, "Le joueur " + actual_player.getColor() + " a gagné!");
            }
            switchPlayer();
        }

        if(grid.gridFull()){
            JOptionPane.showMessageDialog(fenetre, "La grille est pleine!");
        }

    }

    public void switchPlayer(){
        if (actual_player == player1){
            actual_player = player2;
        } else {
            actual_player = player1;
        }
    }

    public Boolean getHasPlayed() {
        return hasPlayed;
    }

    public void setHasPlayed(Boolean hasPlayed) {
        this.hasPlayed = hasPlayed;
    }

    public Boolean getMouseListenerEnabled() {
        return mouseListenerEnabled;
    }

    public void setMouseListenerEnabled(Boolean mouseListenerEnabled) {
        this.mouseListenerEnabled = mouseListenerEnabled;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public static void main(String[] args) {
        new Puissance4();

    }
}
