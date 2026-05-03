package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public final class UIComponents {

    public static final Color COLOR_PRIMARY = new Color(0, 143, 74);
    public static final Color COLOR_DANGER = new Color(200, 50, 50);
    public static final Color COLOR_TEXT_DARK = Color.BLACK;
    public static final Color COLOR_TEXT_MID = new Color(85, 85, 85);
    public static final Color COLOR_TEXT_LIGHT = new Color(60, 60, 60);
    public static final Color COLOR_BORDER = new Color(200, 200, 200);
    public static final Color COLOR_TABLE_BORDER = new Color(218, 218, 218);
    public static final Color COLOR_HEADER_BG = new Color(240, 240, 240);
    public static final Color COLOR_GRID = new Color(230, 230, 230);
    public static final Color COLOR_SELECTION = new Color(220, 240, 228);
    public static final Color COLOR_SEPARATOR = new Color(210, 210, 210);

    public static final String FONT = "Segoe UI Historic";

    private UIComponents(){

    }

    public static JLabel header(String text){
        JLabel headerLabel = new JLabel(text, SwingConstants.LEFT);
        headerLabel.setFont(new Font(FONT, Font.PLAIN, 20));
        headerLabel.setForeground(COLOR_TEXT_DARK);
        return headerLabel;
    }

    public static JLabel subHeader(String text){
        JLabel subHeaderlabel = new JLabel(text, SwingConstants.LEFT);
        subHeaderlabel.setFont(new Font(FONT, Font.PLAIN, 16));
        subHeaderlabel.setForeground(COLOR_TEXT_MID);
        return subHeaderlabel;
    }

    public static JLabel label(String text){
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT, Font.PLAIN, 14));
        label.setForeground(COLOR_TEXT_LIGHT);
        return label;
    }

    public static JSeparator separator(){
        JSeparator separator = new JSeparator();
        separator.setForeground(COLOR_SEPARATOR);
        return separator;
    }

    public static JButton primaryButton(JButton button){
        button.setFont(new Font(FONT, Font.PLAIN, 14));
        button.setBackground(COLOR_PRIMARY);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton secondaryButton(JButton button){
        button.setFont(new Font(FONT, Font.PLAIN, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(COLOR_TEXT_LIGHT);
        button.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton tertiaryButton(JButton button){
        button.setFont(new Font(FONT, Font.PLAIN, 14));
        button.setBackground(COLOR_DANGER);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    public static JButton operatorButton(String symbol){
        JButton operator = new JButton(symbol);
        operator.setFocusPainted(false);
        operator.setBackground(Color.WHITE);
        operator.setFont(new Font(UIComponents.FONT, Font.PLAIN, 16));
        operator.setBorder(BorderFactory.createLineBorder(UIComponents.COLOR_BORDER));
        operator.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return operator;
    }
    
    public static JButton addItemButton(String text) {
        JButton btn = new JButton(text);
        primaryButton(btn);
        return btn;
    }
    
    public static JButton saveRecordButton(String text) {
        JButton btn = new JButton(text);
        primaryButton(btn);
        return btn;
    }
    
    public static JButton processPaymentButton(String text) {
        JButton btn = new JButton(text);
        primaryButton(btn);
        return btn;
    }
    
    public static JButton runReportButton(String text) {
        JButton btn = new JButton(text);
        primaryButton(btn);
        return btn;
    }
    
    public static JButton updateQuantityButton(String text) {
        JButton btn = new JButton(text);
        secondaryButton(btn);
        return btn;
    }
    
    public static JButton restockButton(String text) {
        JButton btn = new JButton(text);
        secondaryButton(btn);
        return btn;
    }
    
    public static JButton resetButton(String text) {
        JButton btn = new JButton(text);
        secondaryButton(btn);
        return btn;
    }
    
    public static JButton tabButton(String text) {
        JButton btn = new JButton(text);
        secondaryButton(btn);
        return btn;
    }
    
    public static JButton deleteButton(String text) {
        JButton btn = new JButton(text);
        tertiaryButton(btn);
        return btn;
    }
    
    public static JButton refundButton(String text) {
        JButton btn = new JButton(text);
        tertiaryButton(btn);
        return btn;
    }

    public static DefaultTableModel tableModel(String[] columns){
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
    }

    public static JTable table(DefaultTableModel model){
        JTable table = new JTable(model);
        table.setFont(new Font(FONT, Font.PLAIN, 14));
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font(FONT, Font.PLAIN, 16));
        table.getTableHeader().setBackground(COLOR_HEADER_BG);
        table.getTableHeader().setForeground(COLOR_TEXT_LIGHT);
        table.setGridColor(COLOR_GRID);
        table.setSelectionBackground(COLOR_SELECTION);
        table.setSelectionForeground(Color.BLACK);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setPreferredSize(new Dimension(table.getPreferredSize().width, 32));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for(int i = 0;i < table.getColumnCount();i++){
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
        return table;
    }

    public static JScrollPane scrollPane(JTable table){
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(COLOR_TABLE_BORDER));
        return sp;
    }

    public static final class StatCard extends JPanel {

        private final JLabel valueLabel;
        private final JLabel titleLbl;

        public StatCard(String title, String initialValue, Color valueColor){
            setLayout(null);
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

            titleLbl = new JLabel(title, SwingConstants.CENTER);
            titleLbl.setFont(new Font(FONT, Font.PLAIN, 12));
            titleLbl.setForeground(COLOR_TEXT_MID);
            add(titleLbl);

            valueLabel = new JLabel(initialValue, SwingConstants.CENTER);
            valueLabel.setFont(new Font(FONT, Font.PLAIN, 26));
            valueLabel.setForeground(valueColor != null ? valueColor : COLOR_TEXT_DARK);
            add(valueLabel);
        }

        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
            if (width > 0 && height > 0) {
                int margin = 8;
                int labelWidth = width - 2 * margin;
                titleLbl.setBounds(margin, 12, labelWidth, 18);
                valueLabel.setBounds(margin, 36, labelWidth, 36);
            }
        }

        public void setValue(String value){
            valueLabel.setText(value);
            repaint();
        }
    }

}
