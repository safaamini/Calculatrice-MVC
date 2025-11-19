package calculatricemvc.strategies;

/**
 * Stratégie pour l'opération de multiplication
 */
public class MultiplicationStrategy implements OperationStrategy {
    
    @Override
    public double calculer(double a, double b) {
        return a * b;
    }
    
    @Override
    public String getSymbol() {
        return "×";
    }
    
    @Override
    public String getNom() {
        return "Multiplication";
    }
    
    @Override
    public String getDescription() {
        return "Multiplication de deux nombres";
    }
    
    @Override
    public boolean estApplicable(double a, double b) {
        // Vérification du dépassement de capacité
        if (Double.isInfinite(a * b)) {
            throw new ArithmeticException("Dépassement de capacité lors de la multiplication");
        }
        return true;
    }
}