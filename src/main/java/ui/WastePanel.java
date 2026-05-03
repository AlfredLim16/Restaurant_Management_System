package ui;

import exception.ValidationException;
import model.FoodWaste;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class WastePanel extends JPanel implements ActionListener, ComponentListener {

    private final MainFrame mainFrame;

    private JTable wasteTable;
    private int[] wasteCounts;
    private JLabel[] countLabels;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;
    private JButton saveButton, resetButton, deleteButton, addButton, minusButton;
    private JSeparator horizontalSeparator, verticalSeparator;
    private JComboBox<String> reasonComboBox, categoryComboBox;
    private JLabel header, subHeader, wasteInputLabel, summaryLabel, mostWastedLabel, dailyCostLabel, totalWasteLabel, reasonLabel, categoryLabel;
    private JLabel itemLabel, countLabel;
    private String actionCommand;
    private Object wasteIdObject;

    private static final int MARGIN_LEFT = 35;
    private static final int MARGIN_RIGHT = 35;
    private static final int MARGIN_BOTTOM = 35;
    private static final int RIGHT_PANEL_X = 350;

    private static final String[] FOOD_ITEMS = {
        "Chicken",
        "Nuggets",
        "Fish Fillet",
        "McCafe",
        "CokeFloat",
        "Sundae",
        "McFlurry"
    };

    private static final String[] WASTE_REASONS = {
        "Overcooked",
        "Expired",
        "Customer Return",
        "Spoiled",
        "Over-prepared"
    };

    private static final String[] FOOD_CATEGORIES = {
        "Meat",
        "Snack",
        "Beverage",
        "Dessert",
        "Other"
    };

    private static final String[] TABLE_COLUMNS = {
        "Food Item",
        "Quantity",
        "Reason",
        "Category",
        "Cost",
        "Date"
    };

    public WastePanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        setLayout(null);
        setBackground(Color.WHITE);

        wasteCounts = new int[FOOD_ITEMS.length];
        countLabels = new JLabel[FOOD_ITEMS.length];

        header = UIComponents.header("Food Waste");
        header.setBounds(MARGIN_LEFT, 25, 500, 32);
        add(header);

        subHeader = UIComponents.subHeader("Monitor and reduce food waste");
        subHeader.setBounds(MARGIN_LEFT, 55, 500, 32);
        add(subHeader);

        horizontalSeparator = UIComponents.separator();
        horizontalSeparator.setBounds(MARGIN_LEFT, 92, 200, 1);
        add(horizontalSeparator);

        wasteInputLabel = UIComponents.label("Food Waste Input");
        wasteInputLabel.setBounds(MARGIN_LEFT, 110, 200, 32);
        add(wasteInputLabel);

        int itemPositionY = 150;
        for(int itemIndex = 0;itemIndex < FOOD_ITEMS.length;itemIndex++, itemPositionY += 50){
            final int loopIndex = itemIndex;

            itemLabel = UIComponents.label(FOOD_ITEMS[itemIndex]);
            itemLabel.setBounds(MARGIN_LEFT, itemPositionY, 110, 30);
            add(itemLabel);

            addButton = UIComponents.operatorButton("+");
            addButton.setBounds(150, itemPositionY, 45, 32);
            addButton.setActionCommand("add:" + loopIndex);
            addButton.addActionListener(this);
            add(addButton);

            countLabel = new JLabel("0", SwingConstants.CENTER);
            countLabel.setFont(new Font(UIComponents.FONT, Font.PLAIN, 14));
            countLabel.setBounds(200, itemPositionY, 50, 30);
            add(countLabel);
            countLabels[itemIndex] = countLabel;

            minusButton = UIComponents.operatorButton("-");
            minusButton.setBounds(250, itemPositionY, 45, 32);
            minusButton.setActionCommand("minus:" + loopIndex);
            minusButton.addActionListener(this);
            add(minusButton);
        }

        totalWasteLabel = UIComponents.label("Total Waste: 0");
        totalWasteLabel.setBounds(MARGIN_LEFT, itemPositionY + 5, 260, 25);
        add(totalWasteLabel);

        verticalSeparator = new JSeparator(SwingConstants.VERTICAL);
        verticalSeparator.setBounds(330, 110, 1, 400);
        verticalSeparator.setForeground(UIComponents.COLOR_SEPARATOR);
        add(verticalSeparator);

        summaryLabel = UIComponents.label("Food Waste Summary");
        summaryLabel.setBounds(RIGHT_PANEL_X, 110, 180, 32);
        add(summaryLabel);

        mostWastedLabel = UIComponents.label("Most Wasted: -");
        mostWastedLabel.setBounds(RIGHT_PANEL_X + 190, 110, 200, 32);
        add(mostWastedLabel);

        dailyCostLabel = UIComponents.label("Daily Cost: 0.00");
        dailyCostLabel.setBounds(RIGHT_PANEL_X + 400, 110, 200, 32);
        add(dailyCostLabel);

        tableModel = UIComponents.tableModel(TABLE_COLUMNS);
        wasteTable = UIComponents.table(tableModel);
        tableScrollPane = UIComponents.scrollPane(wasteTable);
        tableScrollPane.setBounds(RIGHT_PANEL_X, 150, 520, 280);
        add(tableScrollPane);

        reasonLabel = UIComponents.label("Reason:");
        reasonLabel.setBounds(RIGHT_PANEL_X, 445, 80, 25);
        add(reasonLabel);

        reasonComboBox = new JComboBox<>(WASTE_REASONS);
        reasonComboBox.setBounds(RIGHT_PANEL_X + 85, 445, 160, 25);
        add(reasonComboBox);

        categoryLabel = UIComponents.label("Category:");
        categoryLabel.setBounds(RIGHT_PANEL_X + 260, 445, 80, 25);
        add(categoryLabel);

        categoryComboBox = new JComboBox<>(FOOD_CATEGORIES);
        categoryComboBox.setBounds(RIGHT_PANEL_X + 345, 445, 140, 25);
        add(categoryComboBox);

        saveButton = UIComponents.saveRecordButton("Save Record");
        saveButton.setBounds(RIGHT_PANEL_X, 485, 120, 32);
        saveButton.addActionListener(this);
        add(saveButton);

        resetButton = UIComponents.resetButton("Reset");
        resetButton.setBounds(RIGHT_PANEL_X + 130, 485, 100, 32);
        resetButton.addActionListener(this);
        add(resetButton);

        deleteButton = UIComponents.deleteButton("Delete Row");
        deleteButton.setBounds(RIGHT_PANEL_X + 240, 485, 120, 32);
        deleteButton.addActionListener(this);
        add(deleteButton);

        addComponentListener(this);
        loadExistingRecords();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == saveButton){
            saveRecords();
            return;
        }
        if(event.getSource() == resetButton){
            resetCounts();
            return;
        }
        if(event.getSource() == deleteButton){
            int selectedRow = wasteTable.getSelectedRow();
            if(selectedRow == -1){
                JOptionPane.showMessageDialog(null, "Select a row first.");
                return;
            }

            wasteIdObject = tableModel.getValueAt(selectedRow, 0);
            try{
                mainFrame.getFoodWasteService().deleteWaste(Integer.parseInt(wasteIdObject.toString()));
                tableModel.removeRow(selectedRow);
                refreshWasteSummary();
            }catch(ValidationException exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        if(event.getSource() instanceof JButton clickedButton){
            actionCommand = clickedButton.getActionCommand();
            if(actionCommand == null){
                return;
            }
            if(actionCommand.startsWith("add:")){
                increment(Integer.parseInt(actionCommand.substring(4)));
            }else if(actionCommand.startsWith("minus:")){
                decrement(Integer.parseInt(actionCommand.substring(6)));
            }
        }
    }

    @Override
    public void componentResized(ComponentEvent event) {
        updateLayout();
    }

    @Override
    public void componentMoved(ComponentEvent event) {
    
    }

    @Override
    public void componentShown(ComponentEvent event) {
    
    }

    @Override
    public void componentHidden(ComponentEvent event) {
    
    }

    private void loadExistingRecords(){
        tableModel.setRowCount(0);
        ArrayList<FoodWaste> wasteRecords = mainFrame.getFoodWasteService().getAllWasteRecords();
        for(FoodWaste wasteRecord : wasteRecords){
            tableModel.addRow(new Object[]{
                wasteRecord.getWasteId(),
                wasteRecord.getItemName(),
                wasteRecord.getQuantity(),
                wasteRecord.getReason(),
                wasteRecord.getCategory(),
                String.format("%.2f", wasteRecord.getEstimatedCost()),
                wasteRecord.getWasteDate().toLocalDate().toString()
            });
        }
        String[] visibleColumns = {"ID", "Food Item", "Qty", "Reason", "Category", "Cost", "Date"};
        for(int itemIndex = 0;itemIndex < visibleColumns.length && itemIndex < wasteTable.getColumnCount();itemIndex++){
            wasteTable.getColumnModel().getColumn(itemIndex).setHeaderValue(visibleColumns[itemIndex]);
        }
        wasteTable.getTableHeader().repaint();
        refreshWasteSummary();
    }

    private void refreshWasteSummary(){
        double dailyCost = mainFrame.getFoodWasteService().calculateDailyWasteCost();
        dailyCostLabel.setText(String.format("Daily Cost: %.2f", dailyCost));

        HashMap<String, Double> quantityByItem = mainFrame.getFoodWasteService().getQuantityByItem();
        String mostWastedItem = "-";
        double maximumQuantity = 0;
        for(Map.Entry<String, Double> quantityEntry : quantityByItem.entrySet()){
            if(quantityEntry.getValue() > maximumQuantity){
                maximumQuantity = quantityEntry.getValue();
                mostWastedItem = quantityEntry.getKey();
            }
        }
        mostWastedLabel.setText("Most Wasted: " + mostWastedItem);
    }

    private void saveRecords(){
        String selectedReason = (String) reasonComboBox.getSelectedItem();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        String costInput = JOptionPane.showInputDialog(null, "Estimated cost per unit wasted:");
        if(costInput == null){
            return;
        }

        double costPerUnit;
        try{
            costPerUnit = Double.parseDouble(costInput);
        }catch(NumberFormatException exception){
            JOptionPane.showMessageDialog(null, "Please enter a valid cost.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isSaved = false;
        for(int itemIndex = 0;itemIndex < FOOD_ITEMS.length;itemIndex++){
            if(wasteCounts[itemIndex] <= 0){
                continue;
            }
            try{
                double totalCost = wasteCounts[itemIndex] * costPerUnit;
                mainFrame.getFoodWasteService().recordWaste(FOOD_ITEMS[itemIndex], wasteCounts[itemIndex], "units", selectedReason, totalCost, "staff", selectedCategory);
                isSaved = true;
            }catch(ValidationException exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if(!isSaved){
            JOptionPane.showMessageDialog(null, "No waste quantities entered.");
            return;
        }
        loadExistingRecords();
        resetCounts();
        JOptionPane.showMessageDialog(null, "Waste records saved successfully.");
    }

    private void increment(int itemIndex){
        wasteCounts[itemIndex]++;
        countLabels[itemIndex].setText(String.valueOf(wasteCounts[itemIndex]));
        updateTotalLabel();
    }

    private void decrement(int itemIndex){
        if(wasteCounts[itemIndex] > 0){
            wasteCounts[itemIndex]--;
            countLabels[itemIndex].setText(String.valueOf(wasteCounts[itemIndex]));
            updateTotalLabel();
        }
    }

    private void resetCounts(){
        for(int itemIndex = 0;itemIndex < wasteCounts.length;itemIndex++){
            wasteCounts[itemIndex] = 0;
            countLabels[itemIndex].setText("0");
        }
        updateTotalLabel();
    }

    private void updateTotalLabel(){
        int totalWaste = 0;
        for(int wasteCount : wasteCounts){
            totalWaste += wasteCount;
        }
        totalWasteLabel.setText("Total Waste: " + totalWaste);
    }

    private void updateLayout(){
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        horizontalSeparator.setBounds(MARGIN_LEFT, 92, Math.max(50, panelWidth - MARGIN_LEFT - MARGIN_RIGHT), 1);
        verticalSeparator.setBounds(330, 110, 1, Math.max(50, panelHeight - 110 - MARGIN_BOTTOM));

        int buttonPositionY = panelHeight - MARGIN_BOTTOM - 32;
        int reasonPositionY = buttonPositionY - 35;

        summaryLabel.setBounds(RIGHT_PANEL_X, 110, 180, 32);
        mostWastedLabel.setBounds(RIGHT_PANEL_X + 190, 110, 200, 32);
        dailyCostLabel.setBounds(RIGHT_PANEL_X + 400, 110, 200, 32);

        reasonComboBox.setBounds(RIGHT_PANEL_X + 85, reasonPositionY, 160, 25);
        categoryComboBox.setBounds(RIGHT_PANEL_X + 345, reasonPositionY, 140, 25);

        saveButton.setBounds(RIGHT_PANEL_X, buttonPositionY, 120, 32);
        resetButton.setBounds(RIGHT_PANEL_X + 130, buttonPositionY, 100, 32);
        deleteButton.setBounds(RIGHT_PANEL_X + 240, buttonPositionY, 120, 32);

        int tableWidth = Math.max(100, panelWidth - RIGHT_PANEL_X - MARGIN_RIGHT);
        int tableHeight = Math.max(50, reasonPositionY - 150 - 8);
        tableScrollPane.setBounds(RIGHT_PANEL_X, 150, tableWidth, tableHeight);
        tableScrollPane.revalidate();
        wasteTable.revalidate();
        wasteTable.doLayout();
    }
}
