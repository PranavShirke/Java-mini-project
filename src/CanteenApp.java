import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        backgroundImage = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}

class OrderHistoryItem {
    String orderNumber;
    String items;
    double totalPrice;
    String paymentStatus;

    public OrderHistoryItem(String orderNumber, String items, double totalPrice, String paymentStatus) {
        this.orderNumber = orderNumber;
        this.items = items;
        this.totalPrice = totalPrice;
        this.paymentStatus = paymentStatus;
    }
}

public class CanteenApp extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel homePanel, menuPanel, reviewPanel, orderPanel, loginPanel, orderConfirmationPanel, paymentPanel, orderHistoryPanel;
    private JTable menuTable, reviewTable, orderHistoryTable;
    private DefaultTableModel reviewTableModel; 
    private JTextField searchField, rollNumberField;
    private JPasswordField passwordField;
    private JButton addToOrderButton, loginButton, proceedToPaymentButton, viewOrderHistoryButton;
    private JLabel welcomeLabel, menuLabel, orderLabel, totalLabel, loginLabel;
    private JLabel orderConfirmationLabel, orderNumberLabel, orderedItemsLabel, paymentStatusLabel, totalAmountLabel;
    private JComboBox<String> paymentModeComboBox;
    private JComboBox<String> deliveryTimeComboBox;
    private ArrayList<OrderHistoryItem> orderHistory = new ArrayList<>();
    private double totalPrice = 0.0; 
    private int orderNumber;
    private final String[] rollNumbers = {"10361", "10362", "10363"};
    private final String[] passwords = {"123", "234", "345"};

    public CanteenApp() {
        setTitle("Canteen App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        createTabbedPane();
        createLoginPanel();
        createHomePanel();
        createMenuPanel();
        createReviewPanel();
        createOrderPanel();
        createOrderConfirmationPanel();
        createPaymentPanel();
        createOrderHistoryPanel(); 

        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("Home", homePanel);
        tabbedPane.addTab("Menu", menuPanel);
        tabbedPane.addTab("Review Order", reviewPanel);
        tabbedPane.addTab("Payment", paymentPanel);
        tabbedPane.addTab("Order Confirmation", orderConfirmationPanel);
        tabbedPane.addTab("Order History", orderHistoryPanel); // Add Order History tab

        tabbedPane.setSelectedIndex(0); 
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    private void createTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 16));
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginLabel = new JLabel("Roll Number:");
        rollNumberField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        loginPanel.add(loginLabel);
        loginPanel.add(rollNumberField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JPanel()); 
        loginPanel.add(loginButton);
    }

    private void createHomePanel() {
        homePanel = new BackgroundPanel("C://Users//prana//Documents//vs code//WhatsApp Image 2024-11-13 at 11.51.00_e6333433.jpg");
        homePanel.setLayout(new BorderLayout());
        welcomeLabel = new JLabel("Welcome to the Canteen App!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 50));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        homePanel.add(welcomeLabel, BorderLayout.CENTER);
    }

    private void createMenuPanel() {
        menuPanel = new BackgroundPanel("C://Users//prana//Documents//vs code//pexels-photo-4723038.jpeg.jpg/");
        menuPanel.setLayout(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        menuPanel.add(searchPanel, BorderLayout.NORTH);

        String[] columnNames = {"Item", "Price"};
        menuTable = new JTable(new Object[][] {
            {"Sandwich", 30}, {"Vada Pav", 15}, {"Samosa", 15}, {"Nescafe Coffee", 45}, {"Frankie", 50}, {"Coffee", 15}, {"tea",10},{"Dahi",25}
        }, columnNames);
        menuTable.setRowHeight(30);
        menuTable.setFont(new Font("Arial", Font.PLAIN, 14));
        menuTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        JScrollPane menuScrollPane = new JScrollPane(menuTable);
        menuPanel.add(menuScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addToOrderButton = new JButton("Add to Order");
        addToOrderButton.setFont(new Font("Arial", Font.BOLD, 14));
        addToOrderButton.setPreferredSize(new Dimension(150, 40));
        addToOrderButton.addActionListener(e -> addToOrder());
        buttonPanel.add(addToOrderButton);
        menuPanel.add(buttonPanel, BorderLayout.SOUTH);

        menuLabel = new JLabel("Menu");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 20));
        menuLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        menuPanel.add(menuLabel, BorderLayout.NORTH);
    }

    private void createReviewPanel() {
        reviewPanel = new JPanel(new BorderLayout());
        reviewTableModel = new DefaultTableModel(new String[]{"Item", "Price", "Quantity"}, 0);
        reviewTable = new JTable(reviewTableModel);
        reviewTable.setFont(new Font("Arial", Font.PLAIN, 14));
        reviewTable.setRowHeight(30);
        JScrollPane reviewScrollPane = new JScrollPane(reviewTable);
        reviewPanel.add(reviewScrollPane, BorderLayout.CENTER);

        totalLabel = new JLabel("Total Price: ₹0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        reviewPanel.add(totalLabel, BorderLayout.NORTH);

        deliveryTimeComboBox = new JComboBox<>(new String[]{"Now", "10 Minutes", "20 Minutes", "30 Minutes"});
        JPanel deliveryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deliveryPanel.add(new JLabel("Delivery Time:"));
        deliveryPanel.add(deliveryTimeComboBox);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(deliveryPanel, BorderLayout.WEST);

        JButton removeItemButton = new JButton("Remove Selected Item");
        removeItemButton.addActionListener(e -> removeItemFromOrder());
        bottomPanel.add(removeItemButton, BorderLayout.CENTER);

        proceedToPaymentButton = new JButton("Proceed to Payment");
        proceedToPaymentButton.addActionListener(e -> proceedToPayment());
        bottomPanel.add(proceedToPaymentButton, BorderLayout.EAST);

        reviewPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void createOrderPanel() {
        orderPanel = new JPanel(new BorderLayout());
        orderLabel = new JLabel("Place Your Order");
        orderLabel.setFont(new Font("Arial", Font.BOLD, 20));
        orderPanel.add(orderLabel, BorderLayout.NORTH);
    }

    private void createPaymentPanel() {
        paymentPanel = new JPanel(new GridLayout(3, 1));
        JLabel paymentLabel = new JLabel("Select Payment Mode:");
        paymentLabel.setFont(new Font("Arial", Font.BOLD, 20));
        paymentModeComboBox = new JComboBox<>(new String[]{"Cash", "Credit Card", "Debit Card", "UPI"});
        paymentPanel.add(paymentLabel);
        paymentPanel.add(paymentModeComboBox);

        JPanel buttonPanel = new JPanel();
        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(e -> placeOrder());
        buttonPanel.add(placeOrderButton);

        paymentPanel.add(buttonPanel);
    }

    private void createOrderConfirmationPanel() {
        orderConfirmationPanel = new JPanel(new BorderLayout());
        orderConfirmationLabel = new JLabel("Order Confirmation");
        orderConfirmationLabel.setFont(new Font("Arial", Font.BOLD, 20));
        orderConfirmationPanel.add(orderConfirmationLabel, BorderLayout.NORTH);

        orderNumberLabel = new JLabel("Order Number: ");
        orderedItemsLabel = new JLabel("Ordered Items: ");
        paymentStatusLabel = new JLabel("Payment Status: ");
        totalAmountLabel = new JLabel("Total Amount: ");

        JPanel confirmationDetailsPanel = new JPanel(new GridLayout(4, 1));
        confirmationDetailsPanel.add(orderNumberLabel);
        confirmationDetailsPanel.add(orderedItemsLabel);
        confirmationDetailsPanel.add(paymentStatusLabel);
        confirmationDetailsPanel.add(totalAmountLabel);

        orderConfirmationPanel.add(confirmationDetailsPanel, BorderLayout.CENTER);
    }

    private void createOrderHistoryPanel() {
        orderHistoryPanel = new JPanel(new BorderLayout());

        String[] columnNames = {"Order Number", "Items", "Total Price", "Payment Status"};
        orderHistoryTable = new JTable(new DefaultTableModel(columnNames, 0));
        orderHistoryTable.setRowHeight(30);
        orderHistoryTable.setFont(new Font("Arial", Font.PLAIN, 14));
        orderHistoryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        JScrollPane orderHistoryScrollPane = new JScrollPane(orderHistoryTable);
        orderHistoryPanel.add(orderHistoryScrollPane, BorderLayout.CENTER);

        viewOrderHistoryButton = new JButton("View Order History");
        viewOrderHistoryButton.addActionListener(e -> viewOrderHistory());
        orderHistoryPanel.add(viewOrderHistoryButton, BorderLayout.SOUTH);
    }

    private void addToOrder() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow != -1) {
            String item = menuTable.getValueAt(selectedRow, 0).toString();
            double price = Double.parseDouble(menuTable.getValueAt(selectedRow, 1).toString());
            String quantityInput = JOptionPane.showInputDialog(this, "Enter quantity for " + item + ":");
            if (quantityInput != null && !quantityInput.isEmpty()) {
                int quantity = Integer.parseInt(quantityInput);
                reviewTableModel.addRow(new Object[]{item, price, quantity});
                totalPrice += price * quantity; // Update total price
                totalLabel.setText("Total Price: ₹" + totalPrice);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to add to the order.", "No Item Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removeItemFromOrder() {
        int selectedRow = reviewTable.getSelectedRow();
        if (selectedRow != -1) {
            double price = (Double) reviewTable.getValueAt(selectedRow, 1);
            int quantity = (Integer) reviewTable.getValueAt(selectedRow, 2);
            totalPrice -= (price * quantity);
            reviewTableModel.removeRow(selectedRow);
            totalLabel.setText("Total Price: ₹" + totalPrice);
        }
    }

    private void proceedToPayment() {
        tabbedPane.setSelectedIndex(4); 
    }

    private void placeOrder() {
        Random rand = new Random();
        orderNumber = rand.nextInt(10000); 
        StringBuilder items = new StringBuilder();
        for (int i = 0; i < reviewTableModel.getRowCount(); i++) {
            String item = (String) reviewTableModel.getValueAt(i, 0);
            int quantity = (Integer) reviewTableModel.getValueAt(i, 2);
            items.append(item).append(" (x").append(quantity).append("), ");
        }

        String paymentStatus = "Pending"; 

        OrderHistoryItem orderHistoryItem = new OrderHistoryItem(
                String.valueOf(orderNumber), items.toString(), totalPrice, paymentStatus);
        orderHistory.add(orderHistoryItem);

        // Display the order confirmation
        orderNumberLabel.setText("Order Number: " + orderNumber);
        orderedItemsLabel.setText("Ordered Items: " + items.toString());
        paymentStatusLabel.setText("Payment Status: " + paymentStatus);
        totalAmountLabel.setText("Total Amount: ₹" + totalPrice);

        tabbedPane.setSelectedIndex(5); 
        // Write the current order to history (in a text file)
    saveOrderToHistory();

    // Clear the review table
    reviewTableModel.setRowCount(0);  // Remove all rows from the review table

    // Reset total price
    totalPrice = 0.0;
    totalLabel.setText("Total Price: ₹" + totalPrice);

    // Optionally, you can display a message confirming the order was placed
    JOptionPane.showMessageDialog(this, "Your order has been placed successfully!");

    }

private void saveOrderToHistory() {
    try {
        FileWriter fw = new FileWriter("order_history.txt", true);  // Open the file in append mode
        BufferedWriter bw = new BufferedWriter(fw);
        
        // Add timestamp to differentiate orders
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        bw.write("Order placed on: " + timestamp);
        bw.newLine();
        
        // Iterate through the review table to write items, quantities, and prices
        for (int i = 0; i < reviewTableModel.getRowCount(); i++) {
            String item = (String) reviewTableModel.getValueAt(i, 0);
            double price = (Double) reviewTableModel.getValueAt(i, 1);
            int quantity = (Integer) reviewTableModel.getValueAt(i, 2);
            double itemTotal = price * quantity;
            bw.write(item + " | " + "₹" + price + " | Quantity: " + quantity + " | Total: ₹" + itemTotal);
            bw.newLine();
        }
        
        // Write the total price of the order
        bw.write("Total Price: ₹" + totalPrice);
        bw.newLine();
        bw.write("=====================================");
        bw.newLine();
        
        bw.close();
        fw.close();
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error saving order history.");
    }
}


    private void viewOrderHistory() {
        DefaultTableModel historyModel = (DefaultTableModel) orderHistoryTable.getModel();
        historyModel.setRowCount(0); 

        for (OrderHistoryItem order : orderHistory) {
            historyModel.addRow(new Object[]{
                    order.orderNumber,
                    order.items,
                    "₹" + order.totalPrice,
                    order.paymentStatus
            });
        }
    }

    private void login() {
        String rollNumber = rollNumberField.getText();
        String password = new String(passwordField.getPassword());

        for (int i = 0; i < rollNumbers.length; i++) {
            if (rollNumbers[i].equals(rollNumber) && passwords[i].equals(password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                tabbedPane.setSelectedIndex(1); 
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CanteenApp app = new CanteenApp();
            app.setVisible(true);
        });
    }
}
