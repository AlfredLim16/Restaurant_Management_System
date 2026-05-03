package ui;

import java.awt.Color;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class MultiplePanel extends JPanel {

    private SidebarPanel sidebar;
    private final MainFrame mainFrame;

    MultiplePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);
        setBackground(Color.WHITE);
    }

    public void setSidebar(SidebarPanel sidebar) {
        this.sidebar = sidebar;
    }

    public void showPanel(JPanel panel) {
        removeAll();
        add(panel);
        revalidate();
        repaint();
        updateSize();
    }

    public void toggleSidebar() {
        if(sidebar == null){
            return;
        }
        mainFrame.toggleSidebar();
    }

    public void updateSize() {
        revalidate();
        repaint();
        if(getComponentCount() > 0 && getComponent(0) instanceof JPanel panel){
            panel.setBounds(0, 0, getWidth(), getHeight());
        }
    }
}
