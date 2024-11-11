package view;

import controller.ProductController;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductGUI extends JFrame {
    private JTextField searchField;
    private JButton addButton;
    private JTable table;
    private DefaultTableModel model;
    private ProductController controller;

    public ProductGUI() {
        controller = new ProductController();
        setTitle("Product Management");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        searchField = new JTextField(20);
        addButton = new JButton("Add Product");

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Name", "Description", "Price", "Quantity", "Status", "Actions"});
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only allow editing in the "Actions" column
            }
        };

        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(addButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        addButton.addActionListener(this::showAddProductDialog);
        searchField.addActionListener(this::performSearch);

        loadProducts();
    }

    private void showAddProductDialog(ActionEvent e) {
        // Dialog for adding a new product
        JTextField nameField = new JTextField(20);
        JTextArea descriptionArea = new JTextArea(5, 20);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        JTextField priceField = new JTextField(10);
        JTextField quantityField = new JTextField(10);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionScrollPane);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add New Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String description = descriptionArea.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            controller.addProduct(name, description, price, quantity);
            loadProducts();
        }
    }

    private void performSearch(ActionEvent e) {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadProducts(controller.getProducts());  // Load all if search is empty
        } else {
            List<Product> filteredProducts = controller.searchProducts(query);
            loadProducts(filteredProducts);
        }
    }

    private void loadProducts() {
        loadProducts(controller.getProducts());
    }

    private void loadProducts(List<Product> products) {
        model.setRowCount(0);  // Clear the table

        for (Product product : products) {
            model.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getQuantity(),
                    product.getStatus() ? "Available" : "Unavailable",
                    "Toggle Availability"  // Text for the button
            });
        }
    }

    private void toggleProductStatus(int id) {
        // Toggle the status in the controller
        controller.toggleProductStatus(id);
        loadProducts();  // Reload to reflect the updated status
    }

    // Custom renderer for displaying the button in the Actions column
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Toggle Availability" : value.toString());
            return this;
        }
    }

    // Custom editor for handling button clicks in the Actions column
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int selectedId;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    toggleProductStatus(selectedId); // Toggle status based on selected ID
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "Toggle Availability" : value.toString();
            button.setText(label);
            isPushed = true;
            selectedId = (int) table.getValueAt(row, 0);  // Get ID of the selected product
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductGUI().setVisible(true));
    }
}
