package calculatricemvc;

import calculatricemvc.strategies.OperationStrategy;
import calculatricemvc.strategies.AdditionStrategy;
import calculatricemvc.strategies.SoustractionStrategy;
import calculatricemvc.strategies.MultiplicationStrategy;
import calculatricemvc.strategies.DivisionStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controleur implements KeyListener, MouseListener {
    private Modele modele;
    private Vue vue;
    private GestionnaireErreurs gestionnaireErreurs;
    private double resultatCourant = 0;
    private OperationStrategy operateurStrategy = null;
    private boolean nouvelleSaisie = true;
    private boolean premierCalcul = true;
    private final Color couleurBoutonNormale = new Color(51, 51, 51);
    private final Color couleurAccent = new Color(0, 102, 102);
    private HistoriqueVue historiqueVue;

    public Controleur(Modele m, Vue v) {
        this.modele = m;
        this.vue = v;
        this.gestionnaireErreurs = new GestionnaireErreurs(vue);
        
        // === BOUTONS CHIFFRES ===
        for (int i = 0; i < 10; i++) {
            int chiffre = i;
            vue.boutonsChiffres[i].addActionListener(e -> ajouterChiffre(chiffre));
            vue.boutonsChiffres[i].addMouseListener(this);
        }
        
        // === OPÉRATEURS DE BASE AVEC STRATEGY ===
        vue.boutonPlus.addActionListener(e -> definirOperateur(new AdditionStrategy()));
        vue.boutonMoins.addActionListener(e -> definirOperateur(new SoustractionStrategy()));
        vue.boutonMult.addActionListener(e -> definirOperateur(new MultiplicationStrategy()));
        vue.boutonDiv.addActionListener(e -> definirOperateur(new DivisionStrategy()));
        
        vue.boutonEgal.addActionListener(e -> calculerResultatFinal());
        vue.boutonClear.addActionListener(e -> effacerAffichage());
        
        // === ÉCOUTEURS SOURIS POUR LES BOUTONS DE BASE ===
        vue.boutonPlus.addMouseListener(this);
        vue.boutonMoins.addMouseListener(this);
        vue.boutonMult.addMouseListener(this);
        vue.boutonDiv.addMouseListener(this);
        vue.boutonEgal.addMouseListener(this);
        vue.boutonClear.addMouseListener(this);
        
        // === BOUTONS SCIENTIFIQUES ===
        vue.boutonSin.addActionListener(e -> appliquerFonction("sin"));
        vue.boutonCos.addActionListener(e -> appliquerFonction("cos"));
        vue.boutonTan.addActionListener(e -> appliquerFonction("tan"));
        vue.boutonSqrt.addActionListener(e -> appliquerFonction("√"));
        vue.boutonCarre.addActionListener(e -> appliquerFonction("x²"));
        vue.boutonLog.addActionListener(e -> appliquerFonction("log"));
        vue.boutonExp.addActionListener(e -> appliquerFonction("exp"));
        vue.boutonFactorielle.addActionListener(e -> appliquerFonction("!"));
        
        vue.boutonSin.addMouseListener(this);
        vue.boutonCos.addMouseListener(this);
        vue.boutonTan.addMouseListener(this);
        vue.boutonSqrt.addMouseListener(this);
        vue.boutonCarre.addMouseListener(this);
        vue.boutonLog.addMouseListener(this);
        vue.boutonExp.addMouseListener(this);
        vue.boutonFactorielle.addMouseListener(this);
        
        // === GESTION CLAVIER ===
        vue.addKeyListener(this);
        vue.champAffichage.addKeyListener(this);
        vue.setFocusable(true);
        vue.requestFocusInWindow();
        
        // === MENU FICHIER ===
        vue.itemNouveau.addActionListener(e -> effacerAffichage());
        vue.itemQuitter.addActionListener(e -> System.exit(0));
        vue.itemAPropos.addActionListener(e -> JOptionPane.showMessageDialog(
            vue,
            """
            Calculatrice MVC avec Pattern Strategy
            Version Standard avec Mode Scientifique
            
            Réalisée par : Safa
            Encadrant : Dr. Fekair Mohamed El Amine
            L3 - IHM - TP03 / TP04 / TP05
            
            Fonctionnalités :
            • Opérations de base (+, -, ×, ÷)
            • Fonctions scientifiques avancées
            • Historique avec sauvegarde
            • Gestion d'erreurs complète
            • Pattern Strategy pour l'extensibilité
            """,
            "À propos",
            JOptionPane.INFORMATION_MESSAGE
        ));
        
        // === MENU AFFICHAGE ===
        vue.itemScientifique.addActionListener(e -> {
            vue.panneauScientifique.setVisible(true);
            vue.setStatut("Mode Scientifique activé");
        });
        vue.itemStandard.addActionListener(e -> {
            vue.panneauScientifique.setVisible(false);
            vue.setStatut("Mode Standard activé");
        });
        
        // === MENU HISTORIQUE ===
        vue.itemHistorique.addActionListener(e -> afficherHistorique());
        vue.itemSauvegarder.addActionListener(e -> sauvegarderHistorique());
        vue.itemCharger.addActionListener(e -> chargerHistorique());
        
        vue.setStatut("Calculatrice initialisée - Prêt à calculer");
    }

    // =====================================================
    // === MÉTHODES DE CALCUL
    // =====================================================
    private void ajouterChiffre(int chiffre) {
        if (gestionnaireErreurs.estEnErreur()) {
            gestionnaireErreurs.reinitialiserAffichage();
        }
        
        if (nouvelleSaisie || vue.champAffichage.getText().equals("0")) {
            vue.champAffichage.setText("");
            nouvelleSaisie = false;
        }
        
        String actuel = vue.champAffichage.getText();
        
        if (actuel.length() >= 15) {
            gestionnaireErreurs.gererErreur(
                GestionnaireErreurs.TypeErreur.DEPASSEMENT_CAPACITE, 
                "Limite de 15 caractères atteinte"
            );
            return;
        }
        
        vue.champAffichage.setText(actuel + chiffre);
        vue.setStatut("Saisie en cours...");
    }
    
    private void definirOperateur(OperationStrategy strategy) {
        String texte = vue.champAffichage.getText().trim();
        
        if (texte.isEmpty() || gestionnaireErreurs.estEnErreur()) {
            gestionnaireErreurs.gererErreur(
                GestionnaireErreurs.TypeErreur.CHAMP_VIDE,
                "Aucun nombre saisi avant opérateur"
            );
            return;
        }
        
        try {
            double valeur = Double.parseDouble(texte);
            
            if (premierCalcul) {
                resultatCourant = valeur;
                premierCalcul = false;
                vue.setStatut("Premier opérande: " + valeur + " - " + strategy.getDescription());
            } else if (operateurStrategy != null) {
                modele.calculerAvecStrategy(resultatCourant, valeur, operateurStrategy);
                resultatCourant = modele.getResultat();
                afficherNombre(resultatCourant);
                vue.setStatut("Calcul intermédiaire: " + operateurStrategy.getDescription());
            }
            
            operateurStrategy = strategy;
            nouvelleSaisie = true;
            
        } catch (Exception e) {
            gestionnaireErreurs.gererException(e);
        }
    }
    
    private void calculerResultatFinal() {
        String texte = vue.champAffichage.getText().trim();
        
        if (texte.isEmpty()) {
            gestionnaireErreurs.gererErreur(
                GestionnaireErreurs.TypeErreur.CHAMP_VIDE, 
                "Aucun second opérande saisi"
            );
            return;
        }
        
        if (operateurStrategy == null) {
            gestionnaireErreurs.gererErreur(
                GestionnaireErreurs.TypeErreur.OPERATION_INCOMPLETE, 
                "Opérateur non sélectionné"
            );
            return;
        }
        
        if (gestionnaireErreurs.estEnErreur()) {
            return;
        }
        
        try {
            double valeur = Double.parseDouble(texte);
            modele.calculerAvecStrategy(resultatCourant, valeur, operateurStrategy);
            resultatCourant = modele.getResultat();
            afficherNombre(resultatCourant);
            vue.setStatut("Calcul final réussi: " + operateurStrategy.getDescription());
            operateurStrategy = null;
            nouvelleSaisie = true;
            premierCalcul = true;
            
        } catch (Exception e) {
            gestionnaireErreurs.gererException(e);
        }
    }
    
    private void appliquerFonction(String fonction) {
        String texte = vue.champAffichage.getText().trim();
        
        if (texte.isEmpty() || gestionnaireErreurs.estEnErreur()) {
            gestionnaireErreurs.gererErreur(
                GestionnaireErreurs.TypeErreur.CHAMP_VIDE,
                "Aucun nombre saisi pour la fonction"
            );
            return;
        }
        
        try {
            double valeur = Double.parseDouble(texte);
            double resultat = modele.calculerScientifique(fonction, valeur);
            
            afficherNombre(resultat);
            vue.setStatut("Fonction " + fonction + " appliquée avec succès");
            nouvelleSaisie = true;
            
        } catch (Exception ex) {
            gestionnaireErreurs.gererErreurScientifique(fonction, ex);
        }
    }
    
    private void afficherNombre(double valeur) {
        if (Double.isInfinite(valeur) || Double.isNaN(valeur)) {
            gestionnaireErreurs.gererErreur(
                GestionnaireErreurs.TypeErreur.DEPASSEMENT_CAPACITE,
                "Résultat infini ou non numérique"
            );
            return;
        }
        
        String texte;
        if (valeur == (long) valeur)
            texte = String.valueOf((long) valeur);
        else
            texte = String.valueOf(valeur);
            
        vue.champAffichage.setText(texte);
        vue.setTitle("Calculatrice Avancée – Résultat : " + texte);
    }
    
    private void effacerAffichage() {
        vue.champAffichage.setText("0");
        resultatCourant = 0;
        operateurStrategy = null;
        nouvelleSaisie = true;
        premierCalcul = true;
        vue.setTitle("Calculatrice Avancée – Prêt");
        vue.setStatut("Affichage effacé - Prêt pour nouveaux calculs");
    }
    
    // =====================================================
    // === MÉTHODES HISTORIQUE
    // =====================================================
    private void afficherHistorique() {
        try {
            if (historiqueVue == null) {
                historiqueVue = new HistoriqueVue(modele.getHistorique());
                historiqueVue.getBoutonEffacer().addActionListener(e -> {
                    int confirmation = JOptionPane.showConfirmDialog(
                        historiqueVue,
                        "Êtes-vous sûr de vouloir effacer tout l'historique?\n" +
                        modele.getTailleHistorique() + " opérations seront perdues.",
                        "Confirmation Effacement Historique",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirmation == JOptionPane.YES_OPTION) {
                        modele.effacerHistorique();
                        historiqueVue.mettreAJourListe(modele.getHistorique());
                        vue.setStatut("🗑️ Historique effacé");
                    }
                });
                historiqueVue.getBoutonFermer().addActionListener(e -> historiqueVue.dispose());
            }
            historiqueVue.mettreAJourListe(modele.getHistorique());
            historiqueVue.setVisible(true);
            vue.setStatut("Historique affiché - " + modele.getTailleHistorique() + " opérations");
        } catch (Exception e) {
            gestionnaireErreurs.gererException(e);
        }
    }
    
    private void sauvegarderHistorique() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Sauvegarder l'historique");
            
            if (fileChooser.showSaveDialog(vue) == JFileChooser.APPROVE_OPTION) {
                java.io.File fichier = fileChooser.getSelectedFile();
                if (!fichier.getName().toLowerCase().endsWith(".hist")) {
                    fichier = new java.io.File(fichier.getAbsolutePath() + ".hist");
                }
                
                java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(
                    new java.io.FileOutputStream(fichier));
                oos.writeObject(new ArrayList<>(modele.getHistorique()));
                oos.close();
                
                JOptionPane.showMessageDialog(vue, 
                    "Historique sauvegardé avec succès!\n" +
                    "Fichier: " + fichier.getName() + "\n" +
                    modele.getTailleHistorique() + " opérations sauvegardées", 
                    "Sauvegarde réussie", 
                    JOptionPane.INFORMATION_MESSAGE);
                vue.setStatut("Historique sauvegardé: " + fichier.getName());
            }
        } catch (Exception ex) {
            gestionnaireErreurs.gererErreur(
                GestionnaireErreurs.TypeErreur.ERREUR_SAUVEGARDE,
                ex.getMessage()
            );
        }
    }
    
    private void chargerHistorique() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Charger l'historique");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Fichiers historique (*.hist)", "hist"));
            
            if (fileChooser.showOpenDialog(vue) == JFileChooser.APPROVE_OPTION) {
                java.io.ObjectInputStream ois = new java.io.ObjectInputStream(
                    new java.io.FileInputStream(fileChooser.getSelectedFile()));
                @SuppressWarnings("unchecked")
                List<Operation> historiqueCharge = (List<Operation>) ois.readObject();
                ois.close();
                
                modele.effacerHistorique();
                for (Operation op : historiqueCharge) {
                    modele.ajouterOperation(op);
                }
                
                JOptionPane.showMessageDialog(vue, 
                    "Historique chargé avec succès!\n" + 
                    historiqueCharge.size() + " opérations restaurées.", 
                    "Chargement réussi", 
                    JOptionPane.INFORMATION_MESSAGE);
                vue.setStatut("Historique chargé - " + historiqueCharge.size() + " opérations");
            }
        } catch (Exception ex) {
            gestionnaireErreurs.gererErreur(
                GestionnaireErreurs.TypeErreur.ERREUR_CHARGEMENT,
                ex.getMessage()
            );
        }
    }
    
    // =====================================================
    // === CLAVIER
    // =====================================================
    @Override
    public void keyTyped(KeyEvent e) {
        try {
            char c = e.getKeyChar();
            if (Character.isDigit(c)) {
                ajouterChiffre(Character.getNumericValue(c));
            } else if (c == '+') {
                definirOperateur(new AdditionStrategy());
            } else if (c == '-') {
                definirOperateur(new SoustractionStrategy());
            } else if (c == '*' || c == '×') {
                definirOperateur(new MultiplicationStrategy());
            } else if (c == '/' || c == '÷') {
                definirOperateur(new DivisionStrategy());
            } else if (c == '=' || c == '\n') {
                calculerResultatFinal();
            } else if (c == 'c' || c == 'C') {
                effacerAffichage();
            } else if (c == 'h' || c == 'H') {
                afficherHistorique();
            } else if (c == KeyEvent.VK_ESCAPE) {
                effacerAffichage();
            }
        } catch (Exception ex) {
            gestionnaireErreurs.gererException(ex);
        }
    }
    
    @Override 
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            effacerAffichage();
        }
    }
    
    @Override 
    public void keyReleased(KeyEvent e) {}
    
    // =====================================================
    // === SOURIS (HOVER)
    // =====================================================
    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() instanceof JButton b) {
            b.setBackground(Color.LIGHT_GRAY);
        }
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() instanceof JButton b) {
            String texte = b.getText();
            if (texte.matches("[0-9]")) 
                b.setBackground(couleurBoutonNormale);
            else 
                b.setBackground(couleurAccent);
        }
    }
    
    @Override 
    public void mouseClicked(MouseEvent e) {}
    
    @Override 
    public void mousePressed(MouseEvent e) {}
    
    @Override 
    public void mouseReleased(MouseEvent e) {}
    
    // =====================================================
    // === MAIN
    // =====================================================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> {
            Modele m = new Modele();
            Vue v = new Vue();
            new Controleur(m, v);
        });
    }
}