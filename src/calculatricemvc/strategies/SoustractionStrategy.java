package calculatricemvc.strategies;

/**
 * Stratégie pour l'opération de soustraction
 */
public class SoustractionStrategy implements OperationStrategy {
    
    @Override
    public double calculer(double a, double b) {
        return a - b;
    }
    
    @Override
    public String getSymbol() {
        return "-";
    }
    
    @Override
    public String getNom() {
        return "Soustraction";
    }
    
    @Override
    public String getDescription() {
        return "Soustraction de deux nombres";
    }
}