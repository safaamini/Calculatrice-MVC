package calculatricemvc.strategies;

/**
 * Stratégie pour le calcul de racine n-ième
 */
public class RacineNiemeStrategy implements OperationStrategy {
    
    @Override
    public double calculer(double a, double b) throws ArithmeticException {
        // a = radicand, b = indice (nième)
        if (b == 0) {
            throw new ArithmeticException("Racine d'indice zéro non définie");
        }
        if (a < 0 && b % 2 == 0) {
            throw new ArithmeticException("Racine paire d'un nombre négatif non définie");
        }
        
        double resultat = Math.pow(a, 1.0 / b);
        
        if (Double.isNaN(resultat)) {
            throw new ArithmeticException("Racine non définie pour ces valeurs");
        }
        
        return resultat;
    }
    
    @Override
    public String getSymbol() {
        return "√n";
    }
    
    @Override
    public String getNom() {
        return "Racine n-ième";
    }
    
    @Override
    public String getDescription() {
        return "Racine n-ième d'un nombre";
    }
    
    @Override
    public boolean estApplicable(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Racine d'indice zéro non définie");
        }
        if (a < 0 && b % 2 == 0) {
            throw new ArithmeticException("Racine paire d'un nombre négatif non définie");
        }
        return true;
    }
}