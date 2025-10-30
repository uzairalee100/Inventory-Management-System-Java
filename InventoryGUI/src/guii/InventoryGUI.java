/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package guii;




import java.awt.*;
import inventoryapp.InventoryApp;
import inventoryapp.InventoryApp.Product;
import inventoryapp.InventoryApp.Inventory;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;



/**
 *
 * @author hp
 */


public class InventoryGUI extends javax.swing.JFrame {
    
    private InventoryApp.Inventory inventory;

    

    public InventoryGUI() {
        initComponents();
        styleInventoryFrame() ;
        
        // Background Image
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/background.jpeg"));
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(jPanel1.getWidth(), jPanel1.getHeight(), Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaled);
        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
        jPanel1.add(background);
        
        
        inventory = new InventoryApp.Inventory();

        txtID.setEditable(false); // user cannot type ID
        txtID.setText(String.valueOf(inventory.getNextId())); // show next ID
        refreshTable(); // initialize empty table
        loadFromFile();
           

    }
    

    
    private void styleInventoryFrame() {
    // ===== TITLE LABEL =====
    lblID1.setOpaque(true);
    lblID1.setBackground(new Color(255, 255, 255, 200));
    lblID1.setForeground(new Color(0, 51, 153));
    lblID1.setFont(new Font("Segoe UI", Font.BOLD, 28));
    lblID1.setHorizontalAlignment(SwingConstants.CENTER);
    lblID1.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(0, 51, 102), 2),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));

    // ===== LABELS =====
    JLabel[] labels = {lblID, lblName, lblQuantity, lblPrice};
    for (JLabel lbl : labels) {
        lbl.setForeground(new Color(0, 51, 102));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(255, 255, 255, 180));
        lbl.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    // ===== BUTTONS =====
    Color buttonColor = new Color(0, 102, 204);
    Color hoverColor = new Color(0, 153, 255);
    JButton[] buttons = {btnAdd, btnUpdate, btnDelete, btnSearch, btnDisplay};
    for (JButton btn : buttons) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 102)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(buttonColor);
            }
        });
    }

    // ===== TABLE =====
    tblInventory.setBackground(new Color(255, 255, 255, 230));
    tblInventory.setForeground(Color.BLACK);
    tblInventory.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    tblInventory.setGridColor(new Color(204, 204, 255));
    tblInventory.setRowHeight(25);
    tblInventory.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 153), 2));

    // Alternate row coloring
    tblInventory.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (isSelected) {
                c.setBackground(new Color(173, 216, 230));
            } else {
                c.setBackground(row % 2 == 0 ? new Color(245, 250, 255) : Color.WHITE);
            }
            return c;
        }
    });

    // ===== TABLE HEADER =====
    JTableHeader header = tblInventory.getTableHeader();
    header.setBackground(new Color(255, 255, 255, 230));
    header.setForeground(new Color(0, 51, 153));
    header.setFont(new Font("Segoe UI", Font.BOLD, 16));
    header.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 153), 2));
}
    
    
   
private void refreshTable() {
    DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();
    model.setRowCount(0);

    for (InventoryApp.Product p : inventory.getAllProducts()) {
        model.addRow(new Object[]{p.id, p.name, p.quantity, p.price});
    }

    // always show the next auto ID
    txtID.setText(String.valueOf(inventory.getNextId()));
}

private void clearFields() {
    txtName.setText("");
    txtQuantity.setText("");
    txtPrice.setText("");
    txtID.setText(String.valueOf(inventory.getNextId()));
}

private void saveToFile() {
    try (PrintWriter writer = new PrintWriter(new FileWriter("inventory.txt", false))) {
        DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            int id = Integer.parseInt(model.getValueAt(i, 0).toString());
            String name = model.getValueAt(i, 1).toString();
            int qty = Integer.parseInt(model.getValueAt(i, 2).toString());
            double price = Double.parseDouble(model.getValueAt(i, 3).toString());
            double total = qty * price;
            writer.println(id + "," + name + "," + qty + "," + price + "," + total);
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
    }
}

// === Load records from file ===
private void loadFromFile() {
    try (BufferedReader reader = new BufferedReader(new FileReader("inventory.txt"))) {
        DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();
        model.setRowCount(0); // clear existing table
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            model.addRow(data);
        }
    } catch (IOException e) {
        System.out.println("No existing file found. Starting fresh.");
    }
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblID = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblQuantity = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        lblPrice = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        btnDisplay = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInventory = new javax.swing.JTable();
        lblID1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inventory Management System");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 204, 102));

        lblID.setText("Product ID");

        txtID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDActionPerformed(evt);
            }
        });

        lblName.setText("Product Name");

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        lblQuantity.setText("Quantity");

        txtQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantityActionPerformed(evt);
            }
        });

        lblPrice.setText("Price");

        txtPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPriceActionPerformed(evt);
            }
        });

        btnAdd.setText("Add Product");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update Product");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete Product");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnSearch.setText("Search Product");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnDisplay.setText("Display All");
        btnDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayActionPerformed(evt);
            }
        });

        tblInventory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Quantity", "Price", "Total"
            }
        ));
        tblInventory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInventoryMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblInventory);

        lblID1.setText("Inventory Management System");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblID1, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(104, 104, 104))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lblName, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                                            .addComponent(lblID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(41, 41, 41)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(btnDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(42, 42, 42))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblID1)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblID)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblQuantity)
                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPrice)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnSearch)
                    .addComponent(btnDisplay))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void txtQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantityActionPerformed

    private void txtPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        try {
        String name = txtName.getText().trim();
        int qty = Integer.parseInt(txtQuantity.getText().trim());
        double price = Double.parseDouble(txtPrice.getText().trim());

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter product name.");
            return;
        }

        // Add product to inventory
        InventoryApp.Product p = inventory.addProduct(name, qty, price);
        JOptionPane.showMessageDialog(this, "Product added! ID: " + p.id);

        // ✅ Calculate total and add to table directly
        double total = qty * price;
        DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();
        model.addRow(new Object[]{p.id, p.name, qty, price, total});
        saveToFile();
        clearFields(); // reset input fields
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Invalid input. Try again.");
    }                // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed

    clearFields();
    }//GEN-LAST:event_btnDisplayActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
      String nameInput = JOptionPane.showInputDialog(this, "Enter Product Name to search:");
    if (nameInput == null || nameInput.trim().isEmpty()) return;

    nameInput = nameInput.trim().toLowerCase();
    DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();
    boolean found = false;

    for (int i = 0; i < model.getRowCount(); i++) {
        String tableName = model.getValueAt(i, 1).toString().toLowerCase();
        if (tableName.equals(nameInput)) {
            tblInventory.setRowSelectionInterval(i, i);
            tblInventory.scrollRectToVisible(tblInventory.getCellRect(i, 0, true));

            txtID.setText(model.getValueAt(i, 0).toString());
            txtName.setText(model.getValueAt(i, 1).toString());
            txtQuantity.setText(model.getValueAt(i, 2).toString());
            txtPrice.setText(model.getValueAt(i, 3).toString());

            found = true;
            JOptionPane.showMessageDialog(this, "Product found and selected in table.");
            break;
        }
    }

    if (!found) {
        JOptionPane.showMessageDialog(this, "Product not found.");
    }     // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
          try {
        int id = Integer.parseInt(txtID.getText().trim());
        String newName = txtName.getText().trim();
        int qty = Integer.parseInt(txtQuantity.getText().trim());
        double price = Double.parseDouble(txtPrice.getText().trim());
        double total = qty * price; // ✅ Calculate updated total

        boolean updated = inventory.updateProduct(id, newName, qty, price);

        if (updated) {
            JOptionPane.showMessageDialog(this, "Product updated!");

            // ✅ Update the selected row in the JTable
            DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();
            int selectedRow = tblInventory.getSelectedRow();

            if (selectedRow >= 0) {
                model.setValueAt(newName, selectedRow, 1); // update name
                model.setValueAt(qty, selectedRow, 2);     // update quantity
                model.setValueAt(price, selectedRow, 3);   // update price
                model.setValueAt(total, selectedRow, 4);   // update total
            }
            saveToFile();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Product not found.");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Invalid input. Try again.");
    }       // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        try {
        int selectedRow = tblInventory.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?", 
                                                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Remove row from table
        DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();
        model.removeRow(selectedRow);

        // Save updated table to file
        saveToFile();

        JOptionPane.showMessageDialog(this, "Record deleted successfully!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage());
    }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblInventoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInventoryMouseClicked
        int row = tblInventory.getSelectedRow();
    if (row >= 0) {
        txtID.setText(tblInventory.getValueAt(row, 0).toString());
        txtName.setText(tblInventory.getValueAt(row, 1).toString());
        txtQuantity.setText(tblInventory.getValueAt(row, 2).toString());
        txtPrice.setText(tblInventory.getValueAt(row, 3).toString());
    }            // TODO add your handling code here:
    }//GEN-LAST:event_tblInventoryMouseClicked

    private void txtIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InventoryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InventoryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InventoryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InventoryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InventoryGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDisplay;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblID1;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblQuantity;
    private javax.swing.JTable tblInventory;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtQuantity;
    // End of variables declaration//GEN-END:variables
}
