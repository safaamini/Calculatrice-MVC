package calculatricemvc.strategies;

/**
 * Stratégie pour l'opération de modulo (reste de division)
 */
public class ModuloStrategy implements OperationStrategy {
    
    @Override
    public double calculer(double a, double b) throws ArithmeticException {
        if (b == 0) {
            throw new ArithmeticException("Modulo par zéro impossible");
        }
        
        // Le modulo pour les nombres réels
        return a % b;
    }
    
    @Override
    public String getSymbol() {
        return "%";
    }
    
    @Override
    public String getNom() {
        return "Modulo";
    }
    
    @Override
    public String getDescription() {
        return "Reste de la division entière";
    }
    
    @Override
    public boolean estApplicable(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Modulo par zéro impossible");
        }
        return true;
    }
}
