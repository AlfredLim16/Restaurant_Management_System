package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 *
 * @author admin
 */
public class SidebarPanel extends JPanel implements ActionListener, MouseListener {

    public static final int SIDEBAR_WIDTH = 250;

    private static final Color ACTIVE_FOREGROUND = new Color(0, 143, 74);
    private static final Color ACTIVE_BACKGROUND = new Color(222, 222, 222);
    private static final Color INACTIVE_FOREGROUND = new Color(85, 85, 85);
    private static final Color HOVER_BACKGROUND = new Color(222, 222, 222);

    public static final ImageIcon ICON_DASHBOARD = loadIcon("dashboard.png", 18);
    public static final ImageIcon ICON_ORDER = loadIcon("order.png", 18);
    public static final ImageIcon ICON_INVENTORY = loadIcon("inventory.png", 18);
    public static final ImageIcon ICON_PAYMENT = loadIcon("payment.png", 18);
    public static final ImageIcon ICON_REPORT = loadIcon("report.png", 18);
    public static final ImageIcon ICON_FOODWASTE = loadIcon("foodWaste.png", 18);
    public static final ImageIcon ICON_SETTINGS = loadIcon("settings.png", 18);
    public static final ImageIcon ICON_ABOUT = loadIcon("about.png", 18);

    private JButton activeButton;
    private final MainFrame mainFrame;
    private final JLabel lblMain, lblSystem;
    private final MultiplePanel multiplePanel;
    private final JSeparator horizontalSeparatorBottom;
    private final JButton btnHome, btnOrders, btnPayments, btnReports, btnInventory, btnWaste, btnSettings, btnAbout;
    private JButton sidebarBtn;

    public SidebarPanel(MultiplePanel mp, MainFrame mainFrame) {
        this.multiplePanel = mp;
        this.mainFrame = mainFrame;

        setLayout(null);
        setBackground(Color.WHITE);
        setOpaque(false);

        lblMain = new JLabel("MAIN");
        lblMain.setFont(new Font("Segoe UI Historic", Font.PLAIN, 14));
        lblMain.setBounds(18, 16, 200, 20);
        add(lblMain);

        btnHome = sidebarButton("Dashboard", ICON_DASHBOARD);
        btnHome.setBounds(10, 48, SIDEBAR_WIDTH - 20, 32);
        add(btnHome);

        btnOrders = sidebarButton("Orders", ICON_ORDER);
        btnOrders.setBounds(10, 84, SIDEBAR_WIDTH - 20, 32);
        add(btnOrders);

        btnPayments = sidebarButton("Payments", ICON_PAYMENT);
        btnPayments.setBounds(10, 120, SIDEBAR_WIDTH - 20, 32);
        add(btnPayments);

        btnInventory = sidebarButton("Inventory", ICON_INVENTORY);
        btnInventory.setBounds(10, 156, SIDEBAR_WIDTH - 20, 32);
        add(btnInventory);

        btnWaste = sidebarButton("Food Waste", ICON_FOODWASTE);
        btnWaste.setBounds(10, 192, SIDEBAR_WIDTH - 20, 32);
        add(btnWaste);

        btnReports = sidebarButton("Reports", ICON_REPORT);
        btnReports.setBounds(10, 228, SIDEBAR_WIDTH - 20, 32);
        add(btnReports);

        btnSettings = sidebarButton("Settings", ICON_SETTINGS);
        btnSettings.setBounds(10, 314, SIDEBAR_WIDTH - 20, 32);
        add(btnSettings);

        btnAbout = sidebarButton("About", ICON_ABOUT);
        btnAbout.setBounds(10, 350, SIDEBAR_WIDTH - 20, 32);
        add(btnAbout);

        horizontalSeparatorBottom = horizontalSeparator(10, 272, SIDEBAR_WIDTH - 20, new Color(240, 240, 240));
        add(horizontalSeparatorBottom);

        lblSystem = new JLabel("SYSTEM");
        lblSystem.setFont(new Font("Segoe UI Historic", Font.PLAIN, 14));
        lblSystem.setBounds(18, 286, 200, 20);
        add(lblSystem);

        btnHome.addActionListener(this);
        btnOrders.addActionListener(this);
        btnPayments.addActionListener(this);
        btnInventory.addActionListener(this);
        btnWaste.addActionListener(this);
        btnReports.addActionListener(this);
        btnSettings.addActionListener(this);
        btnAbout.addActionListener(this);

        setActiveButton(btnHome);
        multiplePanel.showPanel(new DashboardPanel(mainFrame));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        resetButtonColors();
        if(e.getSource() == btnHome){
            setActiveButton(btnHome);
            multiplePanel.showPanel(new DashboardPanel(mainFrame));
        }else if(e.getSource() == btnOrders){
            setActiveButton(btnOrders);
            // Order Feature
        }else if(e.getSource() == btnPayments){
            setActiveButton(btnPayments);
            // Payment Feature
        }else if(e.getSource() == btnInventory){
            setActiveButton(btnInventory);
            multiplePanel.showPanel(new InventoryPanel());
        }else if(e.getSource() == btnWaste){
            setActiveButton(btnWaste);
            multiplePanel.showPanel(new WastePanel(mainFrame));
        }else if(e.getSource() == btnReports){
            setActiveButton(btnReports);
            // Report Feature
        }else if(e.getSource() == btnSettings){
            setActiveButton(btnSettings);
            multiplePanel.showPanel(new SettingsPanel());
        }else if(e.getSource() == btnAbout){
            setActiveButton(btnAbout);
            multiplePanel.showPanel(new AboutPanel());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(activeButton != sidebarBtn){
            sidebarBtn.setBackground(HOVER_BACKGROUND);
            sidebarBtn.setForeground(INACTIVE_FOREGROUND);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(activeButton != sidebarBtn){
            sidebarBtn.setBackground(Color.WHITE);
            sidebarBtn.setForeground(INACTIVE_FOREGROUND);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graph = (Graphics2D) g.create();
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = 16;
        int width = getWidth();
        int height = getHeight();

        graph.setColor(new Color(0, 0, 0, 20));
        graph.fillRoundRect(2, 2, width, height, arc, arc);
        graph.setColor(new Color(0, 0, 0, 10));
        graph.fillRoundRect(1, 1, width, height, arc, arc);

        graph.setColor(Color.WHITE);
        graph.fillRoundRect(0, 0, width, height, arc, arc);

        graph.setColor(new Color(0, 0, 0, 15));
        graph.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

        graph.dispose();
        super.paintComponent(g);
    }

    private static ImageIcon loadIcon(String fileName, int size) {
        try{
            URL iconsPath = SidebarPanel.class.getResource("/icons/" + fileName);
            if(iconsPath == null){
                System.err.println("Icon not found: " + fileName);
                return new ImageIcon();
            }
            Image img = new ImageIcon(iconsPath).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }catch(Exception e){
            System.err.println("Error loading icon: " + fileName);
            return new ImageIcon();
        }
    }

    private void resetButtonColors() {
        resetButtonColor(btnHome);
        resetButtonColor(btnOrders);
        resetButtonColor(btnPayments);
        resetButtonColor(btnInventory);
        resetButtonColor(btnWaste);
        resetButtonColor(btnReports);
        resetButtonColor(btnSettings);
        resetButtonColor(btnAbout);
    }

    private void resetButtonColor(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(INACTIVE_FOREGROUND);
    }

    public JButton sidebarButton(String text, ImageIcon icon) {
        sidebarBtn = new JButton(text, icon);
        sidebarBtn.setHorizontalAlignment(SwingConstants.LEFT);
        sidebarBtn.setFont(new Font("Segoe UI Historic", Font.PLAIN, 14));
        sidebarBtn.setBackground(Color.WHITE);
        sidebarBtn.setForeground(INACTIVE_FOREGROUND);
        sidebarBtn.setMargin(new Insets(0, 8, 0, 0));
        sidebarBtn.setIconTextGap(12);
        sidebarBtn.setBorderPainted(false);
        sidebarBtn.setFocusPainted(false);
        sidebarBtn.setContentAreaFilled(false);
        sidebarBtn.setOpaque(true);
        return sidebarBtn;
    }

    public void setActiveButton(JButton button) {
        activeButton = button;
        button.setBackground(ACTIVE_BACKGROUND);
        button.setForeground(ACTIVE_FOREGROUND);
    }

    public JSeparator horizontalSeparator(int x, int y, int width, Color color) {
        JSeparator sep = new JSeparator();
        sep.setBounds(x, y, width, 3);
        sep.setForeground(color);
        return sep;
    }
}
