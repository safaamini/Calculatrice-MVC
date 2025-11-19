package calculatricemvc.strategies;

/**
 * Stratégie pour le calcul de pourcentage
 */
public class PourcentageStrategy implements OperationStrategy {
    
    @Override
    public double calculer(double a, double b) {
        // a % de b = (a * b) / 100
        return (a * b) / 100.0;
    }
    
    @Override
    public String getSymbol() {
        return "%";
    }
    
    @Override
    public String getNom() {
        return "Pourcentage";
    }
    
    @Override
    public String getDescription() {
        return "Calcul de pourcentage (a % de b)";
    }
}
