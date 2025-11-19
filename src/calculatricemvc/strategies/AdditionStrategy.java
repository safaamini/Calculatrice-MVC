package calculatricemvc.strategies;

/**
 * Stratégie pour l'opération d'addition
 */
public class AdditionStrategy implements OperationStrategy {
    
    @Override
    public double calculer(double a, double b) {
        return a + b;
    }
    
    @Override
    public String getSymbol() {
        return "+";
    }
    
    @Override
    public String getNom() {
        return "Addition";
    }
    
    @Override
    public String getDescription() {
        return "Addition de deux nombres";
    }
}