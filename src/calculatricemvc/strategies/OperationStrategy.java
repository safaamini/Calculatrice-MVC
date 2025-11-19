package calculatricemvc.strategies;

/**
 * Interface générique représentant une stratégie de calcul
 * Pattern Strategy pour les opérations mathématiques
 */
public interface OperationStrategy {
    
    /**
     * Exécute le calcul entre deux opérandes
     * @param a Premier opérande
     * @param b Second opérande
     * @return Résultat du calcul
     * @throws ArithmeticException en cas d'erreur mathématique
     */
    double calculer(double a, double b) throws ArithmeticException;
    
    /**
     * Retourne le symbole mathématique de l'opération
     * @return Symbole (+, -, *, /, etc.)
     */
    String getSymbol();
    
    /**
     * Retourne le nom affichable de l'opération
     * @return Nom de l'opération
     */
    String getNom();
    
    /**
     * Vérifie si l'opération est applicable avec les opérandes donnés
     * @param a Premier opérande
     * @param b Second opérande
     * @return true si l'opération peut être exécutée
     */
    default boolean estApplicable(double a, double b) {
        return true;
    }
    
    /**
     * Retourne une description de l'opération
     * @return Description
     */
    default String getDescription() {
        return getNom() + " (" + getSymbol() + ")";
    }
}