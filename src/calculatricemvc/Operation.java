package calculatricemvc;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Operation implements Serializable {
    private String expression;
    private double resultat;
    private LocalDateTime dateHeure;
    
    public Operation(String expression, double resultat) {
        this.expression = expression;
        this.resultat = resultat;
        this.dateHeure = LocalDateTime.now();
    }
    
    // Getters
    public String getExpression() { return expression; }
    public double getResultat() { return resultat; }
    public LocalDateTime getDateHeure() { return dateHeure; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("[%s] %s = %.2f", 
            dateHeure.format(formatter), expression, resultat);
    }
}