
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

/*
 * This is a nice bottom border, even tho the functionality (Indeterminite JProgressBar) is gone
 */
/**
 *
 * @author BuckYoung
 */
public class StatusPanel extends JPanel {
    
    private static JPanel statusPanel = new JPanel();
    
    public static JPanel newStatusPanel() {
        //Init Panel
        Dimension d = new Dimension(350,3);
        statusPanel.setBackground(new Color(160, 200, 240));
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setPreferredSize(d);
        //return
        return statusPanel;
    }
    
}
