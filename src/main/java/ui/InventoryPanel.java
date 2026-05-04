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

public class InventoryPanel extends JPanel implements ActionListener {

    private JLabel header, subHeader;

    private JLabel[] itemLabels;
    private JButton[] btnAdd, btnMinus;
    private JLabel[] qtyLabels;

    private JTable table;
    private DefaultTableModel model;

    private JButton btnSave, btnReset, btnDelete;

    private JComboBox<String> categoryBox;

    private int[] quantities;

    private String[] items = {
        "Chicken", "Nuggets", "Fish Fillet",
        "McCafe", "CokeFloat", "Sundae", "McFlurry"
    };

    public InventoryPanel() {
        setLayout(null);
        setBackground(Color.WHITE);

        quantities = new int[items.length];

        initHeader();
        initItemControls();
        initTable();
        initButtons();
    }

    private void initHeader() {
        header = new JLabel("Inventory");
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setBounds(30, 20, 300, 30);
        add(header);

        subHeader = new JLabel("Manage your stock items");
        subHeader.setBounds(30, 50, 300, 20);
        add(subHeader);
    }

    private void initItemControls() {
        itemLabels = new JLabel[items.length];
        btnAdd = new JButton[items.length];
        btnMinus = new JButton[items.length];
        qtyLabels = new JLabel[items.length];

        int y = 100;

        for (int i = 0; i < items.length; i++) {
            itemLabels[i] = new JLabel(items[i]);
            itemLabels[i].setBounds(30, y, 120, 25);
            add(itemLabels[i]);

            btnAdd[i] = new JButton("+");
            btnAdd[i].setBounds(150, y, 50, 25);
            btnAdd[i].addActionListener(this);
            add(btnAdd[i]);

            qtyLabels[i] = new JLabel("0");
            qtyLabels[i].setBounds(210, y, 30, 25);
            add(qtyLabels[i]);

            btnMinus[i] = new JButton("-");
            btnMinus[i].setBounds(250, y, 50, 25);
            btnMinus[i].addActionListener(this);
            add(btnMinus[i]);

            y += 40;
        }
    }

    private void initTable() {
        String[] columns = {"ID", "Item", "Quantity", "Category"};

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(350, 100, 500, 300);
        add(pane);
    }

    private void initButtons() {
        categoryBox = new JComboBox<>(new String[]{
            "Meat", "Beverage", "Dessert"
        });
        categoryBox.setBounds(350, 420, 150, 30);
        add(categoryBox);

        btnSave = new JButton("Add Item");
        btnSave.setBounds(350, 470, 120, 30);
        btnSave.setBackground(Color.GREEN);
        btnSave.addActionListener(this);
        add(btnSave);

        btnReset = new JButton("Reset");
        btnReset.setBounds(480, 470, 100, 30);
        btnReset.addActionListener(this);
        add(btnReset);

        btnDelete = new JButton("Delete Row");
        btnDelete.setBounds(590, 470, 130, 30);
        btnDelete.setBackground(Color.RED);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(this);
        add(btnDelete);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Handle + and - buttons
        for (int i = 0; i < items.length; i++) {
            if (e.getSource() == btnAdd[i]) {
                quantities[i]++;
                qtyLabels[i].setText(String.valueOf(quantities[i]));
            }

            if (e.getSource() == btnMinus[i]) {
                if (quantities[i] > 0) {
                    quantities[i]--;
                    qtyLabels[i].setText(String.valueOf(quantities[i]));
                }
            }
        }

        // Add item to table
        if (e.getSource() == btnSave) {
            String category = categoryBox.getSelectedItem().toString();

            for (int i = 0; i < items.length; i++) {
                if (quantities[i] > 0) {
                    model.addRow(new Object[]{
                        model.getRowCount() + 1,
                        items[i],
                        quantities[i],
                        category
                    });
                }
            }
        }

        // Reset
        if (e.getSource() == btnReset) {
            for (int i = 0; i < quantities.length; i++) {
                quantities[i] = 0;
                qtyLabels[i].setText("0");
            }
        }

        // Delete row
        if (e.getSource() == btnDelete) {
            int row = table.getSelectedRow();
            if (row != -1) {
                model.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Select a row first!");
            }
        }
    }
}