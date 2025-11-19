package calculatricemvc;

import calculatricemvc.strategies.OperationStrategy;
import calculatricemvc.strategies.AdditionStrategy;
import calculatricemvc.strategies.SoustractionStrategy;
import calculatricemvc.strategies.MultiplicationStrategy;
import calculatricemvc.strategies.DivisionStrategy;
import calculatricemvc.strategies.PuissanceStrategy;
import calculatricemvc.strategies.ModuloStrategy;
import calculatricemvc.strategies.PourcentageStrategy;
import calculatricemvc.strategies.RacineNiemeStrategy;

import java.util.ArrayList;
import java.util.List;

public class Modele {
    private double resultat;
    private List<Operation> historique;
    private OperationStrategy strategyCourante;
    
    public Modele() {
        this.historique = new ArrayList<>();
    }
    
    // =====================================================
    // === MÉTHODES DE CALCUL AVEC STRATEGY
    // =====================================================
    
    /**
     * Méthode utilisant le pattern Strategy pour les opérations binaires
     */
    public void calculerAvecStrategy(double a, double b, OperationStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Stratégie de calcul non spécifiée");
        }
        
        this.strategyCourante = strategy;
        
        try {
            // Vérification de l'applicabilité
            if (!strategy.estApplicable(a, b)) {
                throw new ArithmeticException("Opération non applicable avec les valeurs fournies");
            }
            
            // Exécution du calcul
            resultat = strategy.calculer(a, b);
            
            // Vérification du résultat
            if (Double.isNaN(resultat) || Double.isInfinite(resultat)) {
                throw new ArithmeticException("Résultat non numérique ou infini");
            }
            
            // Création de l'expression pour l'historique
            String expression = formatNombre(a) + " " + strategy.getSymbol() + " " + formatNombre(b);
            historique.add(new Operation(expression, resultat));
            
        } catch (ArithmeticException e) {
            throw e; // Relance pour gestion spécifique
        } catch (Exception e) {
            throw new RuntimeException("Erreur avec la stratégie " + strategy.getNom() + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Méthode legacy maintenue pour compatibilité
     */
    public void calculer(double a, double b, String operateur) {
        OperationStrategy strategy = creerStrategyFromOperateur(operateur);
        calculerAvecStrategy(a, b, strategy);
    }
    
    /**
     * Convertit un opérateur string en stratégie correspondante
     */
    private OperationStrategy creerStrategyFromOperateur(String operateur) {
        if (operateur == null || operateur.trim().isEmpty()) {
            throw new IllegalArgumentException("Opérateur non spécifié");
        }
        
        switch (operateur) {
            case "+": 
                return new AdditionStrategy();
            case "-": 
                return new SoustractionStrategy();
            case "*": 
            case "×":
                return new MultiplicationStrategy();
            case "/": 
            case "÷":
                return new DivisionStrategy();
            case "^":
            case "x^y":
                return new PuissanceStrategy();
            case "mod":
                return new ModuloStrategy();
            case "%":
                return new PourcentageStrategy();
            case "y√x":
                return new RacineNiemeStrategy();
            default: 
                throw new IllegalArgumentException("Opérateur invalide: '" + operateur + "'");
        }
    }
    
    // =====================================================
    // === FONCTIONS SCIENTIFIQUES
    // =====================================================
    
    /**
     * Calcule une fonction scientifique sur un seul opérande
     */
    public double calculerScientifique(String fonction, double valeur) {
        if (fonction == null || fonction.trim().isEmpty()) {
            throw new IllegalArgumentException("Fonction non spécifiée");
        }
        
        String expression = "";
        try {
            switch (fonction) {
                case "sin":
                    resultat = Math.sin(Math.toRadians(valeur));
                    expression = "sin(" + formatNombre(valeur) + "°)";
                    break;
                    
                case "cos":
                    resultat = Math.cos(Math.toRadians(valeur));
                    expression = "cos(" + formatNombre(valeur) + "°)";
                    break;
                    
                case "tan":
                    double angleRadians = Math.toRadians(valeur);
                    if (Math.cos(angleRadians) == 0) {
                        throw new ArithmeticException("Tangente non définie pour cet angle");
                    }
                    resultat = Math.tan(angleRadians);
                    expression = "tan(" + formatNombre(valeur) + "°)";
                    break;
                    
                case "√":
                    if (valeur < 0) {
                        throw new ArithmeticException("Racine carrée d'un nombre négatif non définie");
                    }
                    resultat = Math.sqrt(valeur);
                    expression = "√(" + formatNombre(valeur) + ")";
                    break;
                    
                case "x²":
                    resultat = Math.pow(valeur, 2);
                    if (Double.isInfinite(resultat)) {
                        throw new ArithmeticException("Dépassement de capacité lors de l'élévation au carré");
                    }
                    expression = "(" + formatNombre(valeur) + ")²";
                    break;
                    
                case "log":
                    if (valeur <= 0) {
                        throw new ArithmeticException("Logarithme d'un nombre négatif ou nul impossible");
                    }
                    resultat = Math.log10(valeur);
                    expression = "log(" + formatNombre(valeur) + ")";
                    break;
                    
                case "exp":
                    resultat = Math.exp(valeur);
                    if (Double.isInfinite(resultat)) {
                        throw new ArithmeticException("Dépassement de capacité lors de l'exponentielle");
                    }
                    expression = "exp(" + formatNombre(valeur) + ")";
                    break;
                    
                case "!":
                    if (valeur < 0) {
                        throw new ArithmeticException("Factorielle d'un nombre négatif non définie");
                    }
                    if (valeur != Math.floor(valeur)) {
                        throw new ArithmeticException("Factorielle uniquement pour les nombres entiers");
                    }
                    if (valeur > 170) {
                        throw new ArithmeticException("Factorielle trop grande - dépassement de capacité");
                    }
                    
                    resultat = factorielle((int)valeur);
                    expression = "(" + (int)valeur + ")!";
                    break;
                    
                default:
                    throw new IllegalArgumentException("Fonction scientifique inconnue: '" + fonction + "'");
            }
            
            // Vérification finale du résultat
            if (Double.isNaN(resultat) || Double.isInfinite(resultat)) {
                throw new ArithmeticException("Résultat non numérique ou infini");
            }
            
            // Ajout à l'historique
            historique.add(new Operation(expression, resultat));
            return resultat;
            
        } catch (ArithmeticException e) {
            throw e;
        }
    }
    
    /**
     * Calcule la factorielle d'un nombre entier
     */
    private double factorielle(int n) {
        if (n < 0) {
            throw new ArithmeticException("Factorielle d'un nombre négatif non définie");
        }
        if (n == 0 || n == 1) {
            return 1;
        }
        
        double result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
            if (Double.isInfinite(result)) {
                throw new ArithmeticException("Dépassement de capacité lors du calcul de la factorielle");
            }
        }
        return result;
    }
    
    /**
     * Formate un nombre pour l'affichage (supprime les .0 inutiles)
     */
    private String formatNombre(double valeur) {
        if (valeur == (long) valeur) {
            return String.valueOf((long) valeur);
        } else {
            // Limite à 6 décimales maximum
            String formatted = String.format("%.6f", valeur);
            // Supprime les zéros inutiles à la fin
            return formatted.replaceAll("0*$", "").replaceAll("\\.$", "");
        }
    }
    
    // =====================================================
    // === GESTION DE L'HISTORIQUE
    // =====================================================
    
    public List<Operation> getHistorique() {
        return new ArrayList<>(historique);
    }
    
    public void effacerHistorique() {
        historique.clear();
    }
    
    public void ajouterOperation(Operation operation) {
        if (operation == null) {
            throw new IllegalArgumentException("Opération non spécifiée");
        }
        historique.add(operation);
    }
    
    public int getTailleHistorique() {
        return historique.size();
    }
    
    // =====================================================
    // === GETTERS ET MÉTHODES UTILITAIRES
    // =====================================================
    
    public double getResultat() {
        return resultat;
    }
    
    public String getNomStrategyCourante() {
        return strategyCourante != null ? strategyCourante.getClass().getSimpleName() : "Aucune stratégie active";
    }
    
    public String getSymboleStrategyCourante() {
        return strategyCourante != null ? strategyCourante.getSymbol() : "";
    }
    
    public String getDescriptionStrategyCourante() {
        return strategyCourante != null ? strategyCourante.getDescription() : "Aucune opération en cours";
    }
    
    public boolean estStrategyActive() {
        return strategyCourante != null;
    }
    
    public void reinitialiser() {
        this.resultat = 0;
        this.strategyCourante = null;
    }
    
    /**
     * Retourne la liste de toutes les stratégies disponibles
     */
    public List<OperationStrategy> getStrategiesDisponibles() {
        List<OperationStrategy> strategies = new ArrayList<>();
        strategies.add(new AdditionStrategy());
        strategies.add(new SoustractionStrategy());
        strategies.add(new MultiplicationStrategy());
        strategies.add(new DivisionStrategy());
        strategies.add(new PuissanceStrategy());
        strategies.add(new ModuloStrategy());
        strategies.add(new PourcentageStrategy());
        strategies.add(new RacineNiemeStrategy());
        return strategies;
    }
    
    /**
     * Retourne le nombre total d'opérations effectuées
     */
    public int getNombreTotalOperations() {
        return historique.size();
    }
}