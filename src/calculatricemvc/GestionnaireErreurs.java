package calculatricemvc;

import javax.swing.*;

public class GestionnaireErreurs {
    private Vue vue;
    
    public GestionnaireErreurs(Vue vue) {
        this.vue = vue;
    }
    
    // =====================================================
    // === TYPES D'ERREURS IDENTIFIÉES
    // =====================================================
    
    public enum TypeErreur {
        DIVISION_ZERO,
        RACINE_NEGATIVE,
        LOG_NEGATIF,
        FACTORIELLE_NEGATIVE,
        FACTORIELLE_NON_ENTIERE,
        NOMBRE_INVALIDE,
        CHAMP_VIDE,
        OPERATION_INCOMPLETE,
        FONCTION_INCONNUE,
        DEPASSEMENT_CAPACITE,
        FORMAT_INVALIDE,
        ERREUR_SAUVEGARDE,
        ERREUR_CHARGEMENT
    }
    
    // =====================================================
    // === MÉTHODES DE GESTION DES ERREURS
    // =====================================================
    
    /**
     * Gère une erreur avec un type spécifique
     */
    public void gererErreur(TypeErreur type, String details) {
        String message = genererMessageErreur(type, details);
        String titre = genererTitreErreur(type);
        
        // Affichage dans l'interface
        vue.champAffichage.setText(titre);
        vue.setStatut(message);
        vue.setTitle("Calculatrice – " + titre);
        
        // Journalisation (pour débogage)
        journaliserErreur(type, details, message);
        
        // Message à l'utilisateur si nécessaire
        if (necessiteAlerteUtilisateur(type)) {
            afficherAlerteUtilisateur(type, message);
        }
    }
    
    /**
     * Gère une exception avec traduction en type d'erreur
     */
    public void gererException(Exception exception) {
        TypeErreur type = determinerTypeErreur(exception);
        String details = exception.getMessage() != null ? exception.getMessage() : exception.getClass().getSimpleName();
        
        gererErreur(type, details);
    }
    
    /**
     * Gère les erreurs de calcul scientifique
     */
    public void gererErreurScientifique(String fonction, Exception exception) {
        TypeErreur type = determinerTypeErreurScientifique(fonction, exception);
        String details = "Fonction: " + fonction + " - " + exception.getMessage();
        
        gererErreur(type, details);
    }
    
    /**
     * Gère les erreurs de saisie utilisateur
     */
    public void gererErreurSaisie(String saisie) {
        if (saisie == null || saisie.trim().isEmpty()) {
            gererErreur(TypeErreur.CHAMP_VIDE, "Aucune valeur saisie");
        } else if (!estNombreValide(saisie)) {
            gererErreur(TypeErreur.FORMAT_INVALIDE, "Saisie: '" + saisie + "'");
        } else if (depassementCapacite(saisie)) {
            gererErreur(TypeErreur.DEPASSEMENT_CAPACITE, "Valeur trop grande: " + saisie);
        }
    }
    
    // =====================================================
    // === MÉTHODES AUXILIAIRES
    // =====================================================
    
    /**
     * Génère un message d'erreur convivial selon le type
     */
    private String genererMessageErreur(TypeErreur type, String details) {
        switch (type) {
            case DIVISION_ZERO:
                return "Division par zéro impossible";
                
            case RACINE_NEGATIVE:
                return "Racine carrée d'un nombre négatif non définie";
                
            case LOG_NEGATIF:
                return "Logarithme d'un nombre négatif ou nul impossible";
                
            case FACTORIELLE_NEGATIVE:
                return "Factorielle d'un nombre négatif non définie";
                
            case FACTORIELLE_NON_ENTIERE:
                return "Factorielle uniquement pour les nombres entiers";
                
            case NOMBRE_INVALIDE:
                return "Nombre invalide pour l'opération";
                
            case CHAMP_VIDE:
                return "Veuillez saisir un nombre";
                
            case OPERATION_INCOMPLETE:
                return "Opération incomplète - opérateur manquant";
                
            case FONCTION_INCONNUE:
                return "Fonction scientifique non reconnue";
                
            case DEPASSEMENT_CAPACITE:
                return "Dépassement de capacité - valeur trop grande";
                
            case FORMAT_INVALIDE:
                return "Format de nombre invalide";
                
            case ERREUR_SAUVEGARDE:
                return "Erreur lors de la sauvegarde";
                
            case ERREUR_CHARGEMENT:
                return "Erreur lors du chargement";
             
            default:
                return "Erreur inattendue";
        }
    }
    
    /**
     * Génère un titre court pour l'affichage principal
     */
    private String genererTitreErreur(TypeErreur type) {
        switch (type) {
            case DIVISION_ZERO:
                return "Div/0";
                
            case RACINE_NEGATIVE:
            case LOG_NEGATIF:
            case FACTORIELLE_NEGATIVE:
            case FACTORIELLE_NON_ENTIERE:
                return "Math Error";
                
            case NOMBRE_INVALIDE:
            case FORMAT_INVALIDE:
                return "Invalid Input";
                
            case CHAMP_VIDE:
                return "No Input";
                
            case OPERATION_INCOMPLETE:
                return "Incomplete";
                
            case DEPASSEMENT_CAPACITE:
                return "Overflow";
                
            case FONCTION_INCONNUE:
                return "Unknown Function";
                
            default:
                return "Error";
        }
    }
    
    /**
     * Détermine le type d'erreur à partir d'une exception
     */
    private TypeErreur determinerTypeErreur(Exception exception) {
        if (exception instanceof ArithmeticException) {
            String message = exception.getMessage();
            if (message != null) {
                if (message.contains("division") && message.contains("zéro")) {
                    return TypeErreur.DIVISION_ZERO;
                } else if (message.contains("Racine négative")) {
                    return TypeErreur.RACINE_NEGATIVE;
                } else if (message.contains("Log négatif")) {
                    return TypeErreur.LOG_NEGATIF;
                } else if (message.contains("Factorielle") && message.contains("négatif")) {
                    return TypeErreur.FACTORIELLE_NEGATIVE;
                } else if (message.contains("Factorielle") && message.contains("entier")) {
                    return TypeErreur.FACTORIELLE_NON_ENTIERE;
                }
            }
            return TypeErreur.NOMBRE_INVALIDE;
            
        } else if (exception instanceof NumberFormatException) {
            return TypeErreur.FORMAT_INVALIDE;
            
        } else if (exception instanceof IllegalArgumentException) {
            return TypeErreur.FONCTION_INCONNUE;
            
        } else {
            return TypeErreur.NOMBRE_INVALIDE;
        }
    }
    
    /**
     * Détermine le type d'erreur pour les fonctions scientifiques
     */
    private TypeErreur determinerTypeErreurScientifique(String fonction, Exception exception) {
        if (exception instanceof ArithmeticException) {
            String message = exception.getMessage();
            if (fonction.equals("√") && message.contains("Racine négative")) {
                return TypeErreur.RACINE_NEGATIVE;
            } else if (fonction.equals("log") && message.contains("Log négatif")) {
                return TypeErreur.LOG_NEGATIF;
            } else if (fonction.equals("!") && message.contains("Factorielle")) {
                if (message.contains("négatif")) {
                    return TypeErreur.FACTORIELLE_NEGATIVE;
                } else if (message.contains("entier")) {
                    return TypeErreur.FACTORIELLE_NON_ENTIERE;
                }
            }
        }
        return TypeErreur.NOMBRE_INVALIDE;
    }
    
    /**
     * Vérifie si la saisie est un nombre valide
     */
    private boolean estNombreValide(String saisie) {
        if (saisie == null || saisie.trim().isEmpty()) {
            return false;
        }
        
        try {
            Double.parseDouble(saisie.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Vérifie le dépassement de capacité
     */
    private boolean depassementCapacite(String saisie) {
        try {
            double valeur = Double.parseDouble(saisie);
            return Double.isInfinite(valeur) || Math.abs(valeur) > 1e15;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Détermine si une alerte utilisateur est nécessaire
     */
    private boolean necessiteAlerteUtilisateur(TypeErreur type) {
        // Alertes seulement pour les erreurs critiques
        return type == TypeErreur.DIVISION_ZERO || 
               type == TypeErreur.DEPASSEMENT_CAPACITE ||
               type == TypeErreur.ERREUR_SAUVEGARDE ||
               type == TypeErreur.ERREUR_CHARGEMENT;
    }
    
    /**
     * Affiche une alerte à l'utilisateur
     */
    private void afficherAlerteUtilisateur(TypeErreur type, String message) {
        int typeMessage = JOptionPane.ERROR_MESSAGE;
        String titre = "Erreur - Calculatrice";
        
        JOptionPane.showMessageDialog(vue, message, titre, typeMessage);
    }
    
    /**
     * Journalise les erreurs (pour débogage)
     */
    private void journaliserErreur(TypeErreur type, String details, String message) {
        System.err.println("[" + java.time.LocalDateTime.now() + "] " +
                          "Type: " + type + 
                          " | Détails: " + details + 
                          " | Message: " + message);
    }
    
    /**
     * Réinitialise l'affichage d'erreur
     */
    public void reinitialiserAffichage() {
        vue.champAffichage.setText("0");
        vue.setStatut("Prêt");
        vue.setTitle("Calculatrice – Prêt");
    }
    
    /**
     * Vérifie si le champ affiche actuellement une erreur
     */
    public boolean estEnErreur() {
        String texte = vue.champAffichage.getText();
        return texte.equals("Div/0") || 
               texte.equals("Math Error") ||
               texte.equals("Invalid Input") ||
               texte.equals("No Input") ||
               texte.equals("Incomplete") ||
               texte.equals("Overflow") ||
               texte.equals("Unknown Function") ||
               texte.equals("Error");
    }
}
