package calculatricemvc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class HistoriqueVue extends JFrame {
    private JList<String> listeHistorique;
    private DefaultListModel<String> modeleListe;
    private JButton boutonEffacer;
    private JButton boutonFermer;
    
    public HistoriqueVue(List<Operation> historique) {
        setTitle("Historique des Calculs");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Modèle de liste
        modeleListe = new DefaultListModel<>();
        mettreAJourListe(historique);
        
        // Composants
        listeHistorique = new JList<>(modeleListe);
        listeHistorique.setFont(new Font("Consolas", Font.PLAIN, 12));
        listeHistorique.setBackground(new Color(240, 240, 240));
        
        boutonEffacer = new JButton("Effacer l'historique");
        boutonFermer = new JButton("Fermer");
        
        // Layout
        setLayout(new BorderLayout(10, 10));
        
        JPanel panneauNord = new JPanel();
        panneauNord.setBorder(new EmptyBorder(10, 10, 10, 10));
        panneauNord.add(new JLabel("Historique des calculs:"));
        
        JPanel panneauSud = new JPanel(new FlowLayout());
        panneauSud.add(boutonEffacer);
        panneauSud.add(boutonFermer);
        
        add(panneauNord, BorderLayout.NORTH);
        add(new JScrollPane(listeHistorique), BorderLayout.CENTER);
        add(panneauSud, BorderLayout.SOUTH);
    }
    
    public void mettreAJourListe(List<Operation> historique) {
        modeleListe.clear();
        if (historique.isEmpty()) {
            modeleListe.addElement("Aucun calcul dans l'historique");
        } else {
            for (int i = historique.size() - 1; i >= 0; i--) {
                modeleListe.addElement(historique.get(i).toString());
            }
        }
    }
    
    public JButton getBoutonEffacer() { return boutonEffacer; }
    public JButton getBoutonFermer() { return boutonFermer; }
}