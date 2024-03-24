import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Demineur extends JFrame implements ActionListener {
    private JButton[][] grille;
    private boolean[][] mines;
    private boolean[][] decouvertes;

    public Demineur() {
        this.setTitle("Démineur");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setBounds(200, 200, 400, 400);
        this.setLayout(null);

        // Initialisation de la grille
        grille = new JButton[8][8];
        mines = new boolean[8][8];
        decouvertes = new boolean[8][8];

        // Initialisation des boutons
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                grille[i][j] = new JButton();
                grille[i][j].setBounds(50 + j * 40, 50 + i * 40, 40, 40);
                grille[i][j].setBackground(Color.LIGHT_GRAY);
                grille[i][j].setOpaque(true);
                grille[i][j].addActionListener(this);
                this.add(grille[i][j]);
            }
        }

        // Placement aléatoire des mines
        placerMines();

        this.setVisible(true);
    }

    private void placerMines() {
        int nombreMines = 10;
        int minesPlacees = 0;

        while (minesPlacees < nombreMines) {
            int ligne = (int) (Math.random() * 8);
            int colonne = (int) (Math.random() * 8);

            if (!mines[ligne][colonne]) {
                mines[ligne][colonne] = true;
                minesPlacees++;
            }
        }
    }

    private int compterMinesAdjacentes(int ligne, int colonne) {
        int compteur = 0;

        for (int i = ligne - 1; i <= ligne + 1; i++) {
            for (int j = colonne - 1; j <= colonne + 1; j++) {
                if (i >= 0 && i < 8 && j >= 0 && j < 8 && mines[i][j]) {
                    compteur++;
                }
            }
        }

        return compteur;
    }

    private void decouvrirCase(int ligne, int colonne) {
        if (ligne >= 0 && ligne < 8 && colonne >= 0 && colonne < 8 && !decouvertes[ligne][colonne]) {
            decouvertes[ligne][colonne] = true;
            grille[ligne][colonne].setBackground(Color.WHITE);

            if (mines[ligne][colonne]) {
                // Game Over
                JOptionPane.showMessageDialog(this, "Boom! Vous avez touché une mine. Game Over!");
                System.exit(0);
            } else {
                int minesAdjacentes = compterMinesAdjacentes(ligne, colonne);
                if (minesAdjacentes > 0) {
                    grille[ligne][colonne].setText(String.valueOf(minesAdjacentes));
                    grille[ligne][colonne].setFont(new Font("Arial", Font.BOLD, 14));
                } else {
                    for (int i = ligne - 1; i <= ligne + 1; i++) {
                        for (int j = colonne - 1; j <= colonne + 1; j++) {
                            decouvrirCase(i, j);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (source == grille[i][j]) {
                    decouvrirCase(i, j);
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        Demineur jeu = new Demineur();
    }
}
