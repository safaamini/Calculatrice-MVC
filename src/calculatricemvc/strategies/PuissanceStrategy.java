package calculatricemvc.strategies;

/**
 * Stratégie pour l'opération de puissance (exponentiation)
 */
public class PuissanceStrategy implements OperationStrategy {
    
    @Override
    public double calculer(double a, double b) {
        double resultat = Math.pow(a, b);
        
        // Vérification des cas particuliers
        if (Double.isNaN(resultat)) {
            throw new ArithmeticException("Puissance non définie pour ces valeurs");
        }
        if (Double.isInfinite(resultat)) {
            throw new ArithmeticException("Dépassement de capacité lors du calcul de la puissance");
        }
        
        return resultat;
    }
    
    @Override
    public String getSymbol() {
        return "^";
    }
    
    @Override
    public String getNom() {
        return "Puissance";
    }
    
    @Override
    public String getDescription() {
        return "Élévation d'un nombre à une puissance";
    }
    
    @Override
    public boolean estApplicable(double a, double b) {
        // Vérifications supplémentaires pour la puissance
        if (a < 0 && b != Math.floor(b)) {
            throw new ArithmeticException("Puissance non entière d'un nombre négatif impossible");
        }
        if (a == 0 && b < 0) {
            throw new ArithmeticException("Puissance négative de zéro impossible");
        }
        return true;
    }
}