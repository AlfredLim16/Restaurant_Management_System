package ui;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 *
 * @author admin
 */
public class DashboardPanel extends JPanel implements ComponentListener {

    private final MainFrame mainFrame;

    private JSeparator separator;

    private UIComponents.StatCard cardRevenue;
    private UIComponents.StatCard cardActiveOrders;
    private UIComponents.StatCard cardCompletedOrders;
    private UIComponents.StatCard cardLowStock;

    private static final int MARGIN_LEFT = 35;
    private static final int MARGIN_RIGHT = 35;
    private static final int CARD_W = 200;
    private static final int CARD_H = 90;
    private static final int CARD_GAP = 20;
    private static final int CARD_Y = 120;
    private static final int NUM_CARDS = 4;

    public DashboardPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel header = UIComponents.header("Dashboard");
        header.setBounds(MARGIN_LEFT, 25, 500, 32);
        add(header);

        JLabel subHeader = UIComponents.subHeader("Overview of the Restaurant");
        subHeader.setBounds(MARGIN_LEFT, 55, 500, 32);
        add(subHeader);

        separator = UIComponents.separator();
        separator.setBounds(MARGIN_LEFT, 92, 200, 1);
        add(separator);

        cardRevenue = new UIComponents.StatCard("Total Revenue", "0.00", UIComponents.COLOR_PRIMARY);
        cardRevenue.setBounds(MARGIN_LEFT, CARD_Y, CARD_W, CARD_H);
        add(cardRevenue);

        cardActiveOrders = new UIComponents.StatCard("Active Orders", "0", new Color(30, 120, 200));
        cardActiveOrders.setBounds(MARGIN_LEFT + (CARD_W + CARD_GAP), CARD_Y, CARD_W, CARD_H);
        add(cardActiveOrders);

        cardCompletedOrders = new UIComponents.StatCard("Completed Orders", "0", UIComponents.COLOR_TEXT_MID);
        cardCompletedOrders.setBounds(MARGIN_LEFT + 2 * (CARD_W + CARD_GAP), CARD_Y, CARD_W, CARD_H);
        add(cardCompletedOrders);

        cardLowStock = new UIComponents.StatCard("Low Stock Alerts", "0", UIComponents.COLOR_DANGER);
        cardLowStock.setBounds(MARGIN_LEFT + 3 * (CARD_W + CARD_GAP), CARD_Y, CARD_W, CARD_H);
        add(cardLowStock);

        addComponentListener(this);
        refreshStatus();
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

    public void refreshStatus(){
        HashMap<String, Object> summary = mainFrame.getReportService().dashboardSummary();

        double revenue = (double) summary.getOrDefault("totalRevenue", 0.0);
        long active = (long) summary.getOrDefault("activeOrders", 0L);
        long total = (long) summary.getOrDefault("totalOrders", 0L);
        int low = (int) summary.getOrDefault("lowStockAlerts", 0);

        cardRevenue.setValue(String.format("%,.2f", revenue));
        cardActiveOrders.setValue(String.valueOf(active));
        cardCompletedOrders.setValue(String.valueOf(total));
        cardLowStock.setValue(String.valueOf(low));
    }

    private void updateLayout(){
        int panelWidth = getWidth();

        int usableWidth = Math.max(50, panelWidth - MARGIN_LEFT - MARGIN_RIGHT);
        separator.setBounds(MARGIN_LEFT, 92, usableWidth, 1);

        int totalGaps = CARD_GAP * (NUM_CARDS - 1);
        int cardWidth = Math.max(80, (usableWidth - totalGaps) / NUM_CARDS);

        UIComponents.StatCard[] statCards = {cardRevenue, cardActiveOrders, cardCompletedOrders, cardLowStock};
        for(int index = 0; index < statCards.length; index++){
            int positionX = MARGIN_LEFT + index * (cardWidth + CARD_GAP);
            statCards[index].setBounds(positionX, CARD_Y, cardWidth, CARD_H);
        }
    }
}
