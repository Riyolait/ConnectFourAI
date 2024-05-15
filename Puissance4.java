import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Puissance4 {
    private char[][] plateau;
    private char joueurActuel;
    private JFrame fenetre;
    private JPanel plateauPanel;
    private MouseListener mouseListener;

    public Puissance4() {
        plateau = new char[6][7];
        joueurActuel = 'J';
        fenetre = new JFrame("Puissance 4");
        plateauPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < plateau.length; i++) {
                    for (int j = 0; j < plateau[i].length; j++) {
                        g.setColor(getColor(plateau[i][j]));
                        g.fillOval(j * 50 + 25, i * 50 + 25, 25, 25);
                        g.setColor(Color.BLACK);
                        g.drawOval(j * 50 + 25, i * 50 + 25, 25, 25);
                    }
                }
            }

            private Color getColor(char c) {
                switch (c) {
                    case 'J':
                        return Color.YELLOW;
                    case 'O':
                        return Color.RED;
                    default:
                        return Color.WHITE;
                }
            }
        };
        plateauPanel.setPreferredSize(new Dimension(350, 350));
        fenetre.add(plateauPanel, BorderLayout.CENTER);

        mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int colonne = e.getX() / 50;
                if (colonne >= 0 && colonne < plateau[0].length) {
                    ajouterJeton(colonne);
                    plateauPanel.repaint();
                    if (estGagne()) {
                        JOptionPane.showMessageDialog(fenetre, "Le joueur " + joueurActuel + " a gagné !");
                        System.exit(0);
                    }
                    joueurActuel = (joueurActuel == 'J') ? 'O' : 'J';
                }
            }
        };
        plateauPanel.addMouseListener(mouseListener);

        fenetre.pack();
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setVisible(true);
    }

    public void ajouterJeton(int colonne) {
        for (int i = plateau.length - 1; i >= 0; i--) {
            if (plateau[i][colonne] == 0) {
                plateau[i][colonne] = joueurActuel;
                return;
            }
        }
        // Ne pas afficher de message d'erreur si la colonne est pleine
    }

    public boolean estGagne() {
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length - 3; j++) {
                if (plateau[i][j] != 0 && plateau[i][j] == plateau[i][j + 1] && plateau[i][j] == plateau[i][j + 2] && plateau[i][j] == plateau[i][j + 3]) {
                    return true;
                }
            }
        }
        for (int i = 0; i < plateau.length - 3; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                if (plateau[i][j] != 0 && plateau[i][j] == plateau[i + 1][j] && plateau[i][j] == plateau[i + 2][j] && plateau[i][j] == plateau[i + 3][j]) {
                    return true;
                }
            }
        }
        for (int i = 0; i < plateau.length - 3; i++) {
            for (int j = 0; j < plateau[i].length - 3; j++) {
                if (plateau[i][j] != 0 && plateau[i][j] == plateau[i + 1][j + 1] && plateau[i][j] == plateau[i + 2][j + 2] && plateau[i][j] == plateau[i + 3][j + 3]) {
                    return true;
                }
            }
        }
        for (int i = 0; i < plateau.length - 3; i++) {
            for (int j = 3; j < plateau[i].length; j++) {
                if (plateau[i][j] != 0 && plateau[i][j] == plateau[i + 1][j - 1] && plateau[i][j] == plateau[i + 2][j - 2] && plateau[i][j] == plateau[i + 3][j - 3]) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        new Puissance4();
    }
}
