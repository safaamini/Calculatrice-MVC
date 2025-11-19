package calculatricemvc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Vue extends JFrame {
    // Composants d'affichage
    public JTextField champAffichage;
    
    // Boutons chiffres (0-9)
    public JButton[] boutonsChiffres = new JButton[10];
    
    // Boutons opérateurs de base
    public JButton boutonPlus, boutonMoins, boutonMult, boutonDiv;
    public JButton boutonEgal, boutonClear;
    
    // Boutons scientifiques
    public JButton boutonSin, boutonCos, boutonTan, boutonSqrt, boutonCarre, boutonLog;
    public JButton boutonExp, boutonFactorielle;
    
    // Menus
    public JMenuItem itemNouveau, itemQuitter, itemAPropos;
    public JMenuItem itemScientifique, itemStandard;
    public JMenuItem itemHistorique, itemSauvegarder, itemCharger;
    
    
    // Panneaux
    public JPanel panneauScientifique;
    private JLabel labelStatut;

    public Vue() {
        setTitle("Calculatrice Avancée – Prêt");
        setSize(420, 500); // Taille originale
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        Color fond = new Color(51, 51, 51);
        Color texte = Color.WHITE;
        Color accent = new Color(0, 102, 102);
        
        // Icône
        try {
            ImageIcon icone = new ImageIcon(getClass().getResource("/calculatricemvc/icon.png"));
            if (icone.getImage() != null) setIconImage(icone.getImage());
        } catch (Exception e) {
            // Icône non trouvée, continuer sans
        }
        
        // === CHAMP D'AFFICHAGE ===
        champAffichage = new JTextField("0");
        champAffichage.setEditable(false);
        champAffichage.setHorizontalAlignment(JTextField.RIGHT);
        champAffichage.setFont(new Font("Segoe UI", Font.BOLD, 32));
        champAffichage.setBackground(fond);
        champAffichage.setForeground(texte);
        champAffichage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        champAffichage.setCaretColor(texte);
        
        // === BOUTONS CHIFFRES ===
        for (int i = 0; i < 10; i++) {
            boutonsChiffres[i] = creerBouton(String.valueOf(i), fond, texte);
        }
        
        // === BOUTONS OPÉRATEURS DE BASE ===
        boutonPlus = creerBouton("+", accent, texte);
        boutonMoins = creerBouton("−", accent, texte);
        boutonMult = creerBouton("×", accent, texte);
        boutonDiv = creerBouton("÷", accent, texte);
        boutonEgal = creerBouton("=", accent, texte);
        boutonClear = creerBouton("C", accent, texte);
        
        // === BOUTONS SCIENTIFIQUES ===
        boutonSin = creerBouton("sin", accent, texte);
        boutonCos = creerBouton("cos", accent, texte);
        boutonTan = creerBouton("tan", accent, texte);
        boutonSqrt = creerBouton("√", accent, texte);
        boutonCarre = creerBouton("x²", accent, texte);
        boutonLog = creerBouton("log", accent, texte);
        boutonExp = creerBouton("exp", accent, texte);
        boutonFactorielle = creerBouton("!", accent, texte);
        
        // === BARRE DE MENUS ===
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Fichier
        JMenu menuFichier = new JMenu("Fichier");
        itemNouveau = new JMenuItem("Nouveau");
        itemQuitter = new JMenuItem("Quitter");
        
        itemNouveau.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        itemQuitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        
        menuFichier.add(itemNouveau);
        menuFichier.addSeparator();
        menuFichier.add(itemQuitter);
        
        // Menu Affichage
        JMenu menuAffichage = new JMenu("Affichage");
        itemStandard = new JMenuItem("Mode Standard");
        itemScientifique = new JMenuItem("Mode Scientifique");
        
        menuAffichage.add(itemStandard);
        menuAffichage.add(itemScientifique);
        
        // Menu Historique
        JMenu menuHistorique = new JMenu("Historique");
        itemHistorique = new JMenuItem("Afficher l'historique");
        itemSauvegarder = new JMenuItem("Sauvegarder l'historique");
        itemCharger = new JMenuItem("Charger l'historique");
        
        itemHistorique.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));
        itemSauvegarder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        itemCharger.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
        
        menuHistorique.add(itemHistorique);
        menuHistorique.addSeparator();
        menuHistorique.add(itemSauvegarder);
        menuHistorique.add(itemCharger);
        
        // Menu Aide
        JMenu menuAide = new JMenu("Aide");
        itemAPropos = new JMenuItem("À propos");
        menuAide.add(itemAPropos);
        
        menuBar.add(menuFichier);
        menuBar.add(menuAffichage);
        menuBar.add(menuHistorique);
        menuBar.add(menuAide);
        
        setJMenuBar(menuBar);
        
        // === PANEAU PRINCIPAL ===
        JPanel panneauPrincipal = new JPanel(new BorderLayout(10, 10));
        panneauPrincipal.setBackground(fond);
        panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panneauPrincipal.add(champAffichage, BorderLayout.NORTH);
        
        // === PANEAU BOUTONS PRINCIPAL (4x4 grid) ===
        JPanel panneauBoutons = new JPanel(new GridLayout(4, 4, 8, 8));
        panneauBoutons.setBackground(fond);
        
        // Ligne 1
        panneauBoutons.add(boutonsChiffres[7]);
        panneauBoutons.add(boutonsChiffres[8]);
        panneauBoutons.add(boutonsChiffres[9]);
        panneauBoutons.add(boutonClear);
        
        // Ligne 2
        panneauBoutons.add(boutonsChiffres[4]);
        panneauBoutons.add(boutonsChiffres[5]);
        panneauBoutons.add(boutonsChiffres[6]);
        panneauBoutons.add(boutonDiv);
        
        // Ligne 3
        panneauBoutons.add(boutonsChiffres[1]);
        panneauBoutons.add(boutonsChiffres[2]);
        panneauBoutons.add(boutonsChiffres[3]);
        panneauBoutons.add(boutonMult);
        
        // Ligne 4
        panneauBoutons.add(boutonsChiffres[0]);
        panneauBoutons.add(boutonEgal);
        panneauBoutons.add(boutonPlus);
        panneauBoutons.add(boutonMoins);
        
        // === PANEAU SCIENTIFIQUE (à gauche) ===
        panneauScientifique = new JPanel(new GridLayout(8, 1, 8, 8));
        panneauScientifique.setBackground(fond);
        panneauScientifique.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        
        panneauScientifique.add(boutonSin);
        panneauScientifique.add(boutonCos);
        panneauScientifique.add(boutonTan);
        panneauScientifique.add(boutonSqrt);
        panneauScientifique.add(boutonCarre);
        panneauScientifique.add(boutonLog);
        panneauScientifique.add(boutonExp);
        panneauScientifique.add(boutonFactorielle);
        
        panneauScientifique.setVisible(false); // Caché en mode standard
        
        // === LABEL DE STATUT ===
        labelStatut = new JLabel(" Prêt ");
        labelStatut.setForeground(Color.WHITE);
        labelStatut.setOpaque(true);
        labelStatut.setBackground(new Color(30, 30, 30));
        labelStatut.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelStatut.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // === ASSEMBLAGE FINAL ===
        panneauPrincipal.add(panneauScientifique, BorderLayout.WEST);
        panneauPrincipal.add(panneauBoutons, BorderLayout.CENTER);
        panneauPrincipal.add(labelStatut, BorderLayout.SOUTH);
        
        setContentPane(panneauPrincipal);
        setVisible(true);
    }
    
    private JButton creerBouton(String texte, Color fond, Color couleurTexte) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        bouton.setBackground(fond);
        bouton.setForeground(couleurTexte);
        bouton.setFocusPainted(false);
        bouton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        return bouton;
    }
    
    public void setStatut(String message) {
        labelStatut.setText(" " + message + " ");
        
        // Changement de couleur selon le type de message
        if (message.toLowerCase().contains("erreur") || message.contains("❌")) {
            labelStatut.setBackground(new Color(120, 0, 0));
        } else if (message.toLowerCase().contains("réussi") || message.toLowerCase().contains("succès") || message.contains("✅")) {
            labelStatut.setBackground(new Color(0, 100, 0));
        } else if (message.toLowerCase().contains("avertissement") || message.contains("⚠️")) {
            labelStatut.setBackground(new Color(200, 100, 0));
        } else {
            labelStatut.setBackground(new Color(30, 30, 30));
        }
    }
    
    
}