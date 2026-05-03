package ui;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 *
 * @author admin
 */
public class SettingsPanel extends JPanel implements ComponentListener {

    private JLabel header, subHeader;
    private JSeparator separator;
    private static final int MARGIN_LEFT = 35;
    private static final int MARGIN_RIGHT = 35;
    private static final int MARGIN_BOTTOM = 35;

    public SettingsPanel(){
        setLayout(null);
        setBackground(Color.WHITE);

        header = UIComponents.header("Settings");
        header.setBounds(MARGIN_LEFT, 25, 500, 32);
        add(header);

        subHeader = UIComponents.subHeader("Manage restaurant configurations");
        subHeader.setBounds(MARGIN_LEFT, 55, 500, 32);
        add(subHeader);

        separator = UIComponents.separator();
        separator.setBounds(MARGIN_LEFT, 92, 200, 1);
        add(separator);

        addComponentListener(this);
    }
    
    @Override
    public void componentResized(ComponentEvent e){
        updateLayout();
    }

    @Override
    public void componentMoved(ComponentEvent e){

    }

    @Override
    public void componentShown(ComponentEvent e){

    }

    @Override
    public void componentHidden(ComponentEvent e){

    }

    private void updateLayout(){
        int width = getWidth();
        int height = getHeight();

        int sepWidth = Math.max(50, width - MARGIN_LEFT - MARGIN_RIGHT);
        separator.setBounds(MARGIN_LEFT, 92, sepWidth, 1);
    }
}
