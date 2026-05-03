package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import service.FoodWasteService;
import service.InventoryService;
import service.OrderService;
import service.PaymentService;
import service.ReportService;
import storage.IFoodWaste;
import storage.IInventoryItem;
import storage.IMenuItem;
import storage.IOrder;
import storage.IPayment;
import storage.InMemoryFoodWaste;
import storage.InMemoryInventoryItem;
import storage.InMemoryMenuItem;
import storage.InMemoryOrder;
import storage.InMemoryPayment;

/**
 *
 * @author admin
 */
public class MainFrame extends JFrame implements ActionListener, ComponentListener, MouseListener, MouseMotionListener {

    public static final int SIDEBAR_WIDTH = 250;
    public static final int TITLEBAR_HEIGHT = 32;
    public static final int GAP = 12;

    private Point dragOffset;
    private Rectangle previousBounds, animationStartBounds, animationTargetBounds;
    private GraphicsEnvironment graphicsEnvironment;
    private Rectangle screenBounds;

    private Image iconImage;
    private JLabel titleBarLabel;
    private JButton sidebarButton, minimizeButton, maximizeButton, closeButton;
    private ImageIcon sidebarIcon, minimizeIcon, maximizeIcon, closeIcon;
    private JPanel titleBarPanel;

    private SidebarPanel sidebarPanel;
    private MultiplePanel mainPanel;

    private Timer animationTimer;
    private int animationSteps = 12, animationDelayMs = 10, animationStep = 0;
    private boolean isDragging = false, isMaximized = false, sidebarVisible = false, isAnimating = false;

    private String iconResource;

    private OrderService orderService;
    private PaymentService paymentService;
    private InventoryService inventoryService;
    private FoodWasteService foodWasteService;
    private ReportService reportService;
    private IMenuItem menuItemData;

    public MainFrame() {
        IOrder orderData = new InMemoryOrder();
        IPayment paymentData = new InMemoryPayment();
        IInventoryItem inventoryData = new InMemoryInventoryItem();
        IFoodWaste foodWasteData = new InMemoryFoodWaste();
        menuItemData = new InMemoryMenuItem();

        this.orderService = new OrderService(orderData, menuItemData);
        this.paymentService = new PaymentService(paymentData, orderData);
        this.inventoryService = new InventoryService(inventoryData);
        this.foodWasteService = new FoodWasteService(foodWasteData);
        this.reportService = new ReportService(orderData, paymentData, inventoryData, foodWasteData);

        setLayout(null);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.WHITE);
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));
        getContentPane().setBackground(Color.WHITE);

        setBounds(0, 0, 1200, 800);
        setLocationRelativeTo(null);

        titleBarPanel = new JPanel(null);
        titleBarPanel.setBackground(Color.WHITE);
        titleBarPanel.setBounds(0, 0, getWidth(), TITLEBAR_HEIGHT);

        sidebarButton = new JButton();
        sidebarButton.setFocusPainted(false);
        sidebarButton.setBorderPainted(false);
        sidebarButton.setContentAreaFilled(false);
        sidebarButton.setBounds(5, 2, 32, 32);
        updateSidebarIcon();
        sidebarButton.addActionListener(this);
        titleBarPanel.add(sidebarButton);

        maximizeButton = new JButton();
        maximizeButton.setFocusPainted(false);
        maximizeButton.setBorderPainted(false);
        maximizeButton.setContentAreaFilled(false);
        maximizeButton.setBounds(titleBarPanel.getWidth() - 80, 2, 32, 32);
        maximizeIcon = new ImageIcon(getClass().getResource("/icons/maximize.png"));
        iconImage = maximizeIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        maximizeButton.setIcon(new ImageIcon(iconImage));
        maximizeButton.addActionListener(this);
        titleBarPanel.add(maximizeButton);

        minimizeButton = new JButton();
        minimizeButton.setFocusPainted(false);
        minimizeButton.setBorderPainted(false);
        minimizeButton.setContentAreaFilled(false);
        minimizeButton.setBounds(titleBarPanel.getWidth() - 120, 2, 32, 32);
        minimizeIcon = new ImageIcon(getClass().getResource("/icons/minimize.png"));
        iconImage = minimizeIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        minimizeButton.setIcon(new ImageIcon(iconImage));
        minimizeButton.addActionListener(this);
        titleBarPanel.add(minimizeButton);

        closeButton = new JButton();
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setBounds(titleBarPanel.getWidth() - 40, 2, 32, 32);
        closeIcon = new ImageIcon(getClass().getResource("/icons/close.png"));
        iconImage = closeIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        closeButton.setIcon(new ImageIcon(iconImage));
        closeButton.addActionListener(this);
        titleBarPanel.add(closeButton);

        titleBarLabel = new JLabel("Restaurant Management System", SwingConstants.CENTER);
        titleBarLabel.setFont(new Font("Segoe UI Historic", Font.PLAIN, 14));
        titleBarLabel.setBounds(0, 0, titleBarPanel.getWidth(), TITLEBAR_HEIGHT);
        titleBarPanel.add(titleBarLabel);
        add(titleBarPanel);

        mainPanel = new MultiplePanel(this);
        add(mainPanel);

        sidebarPanel = new SidebarPanel(mainPanel, this);
        add(sidebarPanel);

        mainPanel.setSidebar(sidebarPanel);
        updateLayout();

        addComponentListener(this);
        titleBarPanel.addMouseListener(this);
        titleBarPanel.addMouseMotionListener(this);
        titleBarLabel.addMouseListener(this);
        titleBarLabel.addMouseMotionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == closeButton){
            dispose();
        }else if(e.getSource() == minimizeButton){
            setState(JFrame.ICONIFIED);
        }else if(e.getSource() == maximizeButton){
            graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            screenBounds = graphicsEnvironment.getMaximumWindowBounds();
            if(!isMaximized){
                previousBounds = getBounds();
                animationStartBounds = previousBounds;
                animationTargetBounds = screenBounds;
                resizeAnimation();
                isMaximized = true;
            }else{
                animationStartBounds = getBounds();
                animationTargetBounds = previousBounds;
                resizeAnimation();
                isMaximized = false;
            }
        }else if(e.getSource() == sidebarButton){
            toggleSidebar();
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if(!isAnimating){
            updateLayout();
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(isClickOnButton(e)){
            return;
        }
        if(isMaximized){
            restoreWindowUnderCursor(e);
        }
        dragOffset = new Point(e.getXOnScreen() - getX(), e.getYOnScreen() - getY());
        isDragging = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isDragging && !isMaximized){
            previousBounds = getBounds();
        }
        isDragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(!isDragging || isMaximized || dragOffset == null){
            return;
        }
        setLocation(e.getXOnScreen() - dragOffset.x, e.getYOnScreen() - dragOffset.y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private void updateLayout() {
        int width = getWidth();
        int height = getHeight();

        titleBarPanel.setBounds(0, 0, width, TITLEBAR_HEIGHT);
        titleBarLabel.setBounds(0, 0, width, TITLEBAR_HEIGHT);
        closeButton.setBounds(width - 40, 2, 32, 32);
        maximizeButton.setBounds(width - 80, 2, 32, 32);
        minimizeButton.setBounds(width - 120, 2, 32, 32);

        if(sidebarVisible){
            int sidebarH = height - TITLEBAR_HEIGHT - 2 * GAP;
            sidebarPanel.setBounds(GAP, TITLEBAR_HEIGHT + GAP, SIDEBAR_WIDTH, sidebarH);
            sidebarPanel.setVisible(true);

            int mainX = SIDEBAR_WIDTH + 2 * GAP;
            mainPanel.setBounds(mainX, TITLEBAR_HEIGHT, width - mainX - GAP, height - TITLEBAR_HEIGHT);
        }else{
            sidebarPanel.setVisible(false);
            mainPanel.setBounds(0, TITLEBAR_HEIGHT, width, height - TITLEBAR_HEIGHT);
        }
        mainPanel.updateSize();
    }

    public void toggleSidebar() {
        sidebarVisible = !sidebarVisible;
        updateSidebarIcon();
        updateLayout();
    }

    private void updateSidebarIcon() {
        iconResource = sidebarVisible ? "/icons/hide-sidebar.png" : "/icons/expand-sidebar.png";
        sidebarIcon = new ImageIcon(getClass().getResource(iconResource));
        iconImage = sidebarIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        sidebarButton.setIcon(new ImageIcon(iconImage));
    }

    private void resizeAnimation() {
        animationStep = 0;
        isAnimating = true;

        if(animationTimer != null && animationTimer.isRunning()){
            animationTimer.stop();
        }
        animationTimer = new Timer(animationDelayMs, null);
        animationTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                animationStep++;
                if(animationStep >= animationSteps){
                    isAnimating = false;
                    setBounds(animationTargetBounds);
                    animationTimer.stop();
                }else{
                    float t = (float) animationStep / animationSteps;
                    int x = Math.round(animationStartBounds.x + (animationTargetBounds.x - animationStartBounds.x) * t);
                    int y = Math.round(animationStartBounds.y + (animationTargetBounds.y - animationStartBounds.y) * t);
                    int w = Math.round(animationStartBounds.width + (animationTargetBounds.width - animationStartBounds.width) * t);
                    int h = Math.round(animationStartBounds.height + (animationTargetBounds.height - animationStartBounds.height) * t);
                    MainFrame.super.setBounds(x, y, w, h);
                }
            }
        });
        animationTimer.start();
    }

    private boolean isClickOnButton(MouseEvent e) {
        Point ptInTitleBar = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), titleBarPanel);
        Component deepest = titleBarPanel.getComponentAt(ptInTitleBar);
        return deepest instanceof JButton;
    }

    private void restoreWindowUnderCursor(MouseEvent e) {
        Point ptInTitleBar = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), titleBarPanel);
        double relativeX = Math.min(1.0, Math.max(0.0, (double) ptInTitleBar.x / Math.max(1, titleBarPanel.getWidth())));

        isMaximized = false;
        setSize(previousBounds.width, previousBounds.height);

        int newX = e.getXOnScreen() - (int) (getWidth() * relativeX);
        int newY = e.getYOnScreen() - ptInTitleBar.y;
        setLocation(newX, newY);
        previousBounds = getBounds();
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public FoodWasteService getFoodWasteService() {
        return foodWasteService;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public IMenuItem getMenuItemData() {
        return menuItemData;
    }
}
