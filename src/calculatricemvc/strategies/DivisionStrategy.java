package calculatricemvc.strategies;

/**
 * Stratégie pour l'opération de division
 */
public class DivisionStrategy implements OperationStrategy {
    
    @Override
    public double calculer(double a, double b) throws ArithmeticException {
        if (b == 0) {
            throw new ArithmeticException("Division par zéro impossible");
        }
        return a / b;
    }
    
    @Override
    public String getSymbol() {
        return "÷";
    }
    
    @Override
    public String getNom() {
        return "Division";
    }
    
    @Override
    public String getDescription() {
        return "Division de deux nombres";
    }
    
    @Override
    public boolean estApplicable(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division par zéro impossible");
        }
        return true;
    }
}