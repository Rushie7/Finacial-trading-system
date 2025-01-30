import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class FinancialTradingSystem extends JFrame {
    // Sample market data
    Map<String, Double> marketPrices = new HashMap<>();

    // Sample user data
    Map<String, String> users = new HashMap<>(); // username -> password

    // UI Components
    JComboBox<String> symbolComboBox;
    JTextField priceTextField;
    JTextField quantityTextField;
    JButton buyButton;
    JButton sellButton;
    JTextField usernameField;
    JPasswordField passwordField;
    JTextArea portfolioTextArea;

    String currentUser; // Track the current user

    public FinancialTradingSystem() {
        setTitle("Financial Trading System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize market data
        marketPrices.put("AAPL", 150.25);
        marketPrices.put("GOOG", 2800.50);
        marketPrices.put("MSFT", 240.75);

        // Sample user data (you can replace it with a database)
        users.put("Phenom", "javaking");
        users.put("user2", "password2");

        // Initialize UI components
        JPanel topPanel = new JPanel();
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        JPanel bottomPanel = new JPanel();

        symbolComboBox = new JComboBox<>(marketPrices.keySet().toArray(new String[0]));
        priceTextField = new JTextField(10);
        priceTextField.setEditable(false);
        quantityTextField = new JTextField(10);
        buyButton = new JButton("Buy");
        sellButton = new JButton("Sell");
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        portfolioTextArea = new JTextArea(10, 30);
        portfolioTextArea.setEditable(false);

        // Add components to panels
        topPanel.add(new JLabel("Username:"));
        topPanel.add(usernameField);
        topPanel.add(new JLabel("Password:"));
        topPanel.add(passwordField);
        topPanel.add(new JLabel("Symbol:"));
        topPanel.add(symbolComboBox);
        topPanel.add(new JLabel("Price:"));
        topPanel.add(priceTextField);
        topPanel.add(new JLabel("Quantity:"));
        topPanel.add(quantityTextField);
        topPanel.add(buyButton);
        topPanel.add(sellButton);
        centerPanel.add(new JLabel("Portfolio Information:"));
        centerPanel.add(new JScrollPane(portfolioTextArea));

        // Add panels to the frame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Event listeners
        symbolComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updatePrice();
            }
        });

        buyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(FinancialTradingSystem.this, "Please log in first.");
                    return;
                }
                if (validateInputs()) {
                    String symbol = (String) symbolComboBox.getSelectedItem();
                    double price = marketPrices.get(symbol);
                    int quantity = Integer.parseInt(quantityTextField.getText());
                    // Perform buy order logic here
                    JOptionPane.showMessageDialog(FinancialTradingSystem.this,
                            "Buy Order: Symbol - " + symbol + ", Price - " + price + ", Quantity - " + quantity);
                    updatePortfolio(symbol, quantity);
                }
            }
        });

        sellButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(FinancialTradingSystem.this, "Please log in first.");
                    return;
                }
                if (validateInputs()) {
                    String symbol = (String) symbolComboBox.getSelectedItem();
                    double price = marketPrices.get(symbol);
                    int quantity = Integer.parseInt(quantityTextField.getText());
                    // Perform sell order logic here
                    JOptionPane.showMessageDialog(FinancialTradingSystem.this,
                            "Sell Order: Symbol - " + symbol + ", Price - " + price + ", Quantity - " + quantity);
                    updatePortfolio(symbol, -quantity);
                }
            }
        });

        // Login button action listener
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (authenticate(username, password)) {
                    currentUser = username;
                    JOptionPane.showMessageDialog(FinancialTradingSystem.this, "Login successful.");
                } else {
                    JOptionPane.showMessageDialog(FinancialTradingSystem.this, "Invalid username or password.");
                }
            }
        });
        topPanel.add(loginButton);

        // Logout button action listener
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentUser = null;
                JOptionPane.showMessageDialog(FinancialTradingSystem.this, "Logout successful.");
            }
        });
        topPanel.add(logoutButton);
    }

    // Update price when symbol selection changes
    private void updatePrice() {
        String symbol = (String) symbolComboBox.getSelectedItem();
        double price = marketPrices.get(symbol);
        priceTextField.setText(String.valueOf(price));
    }

    // Validate user inputs
    private boolean validateInputs() {
        String symbol = (String) symbolComboBox.getSelectedItem();
        String quantityText = quantityTextField.getText();

        if (symbol == null || symbol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a symbol.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Quantity field cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityText);

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Update portfolio information
    private void updatePortfolio(String symbol, int quantity) {
        String portfolioInfo = portfolioTextArea.getText();
        double price = marketPrices.get(symbol);
        double totalPrice = price * quantity;

        if (portfolioInfo.isEmpty()) {
            portfolioInfo = "Symbol\tQuantity\tTotal Value\n";
        }

        String newInfo = symbol + "\t" + quantity + "\t" + totalPrice + "\n";
        portfolioInfo += newInfo;
        portfolioTextArea.setText(portfolioInfo);
    }

    // Authenticate user
    private boolean authenticate(String username, String password) {
        String storedPassword = users.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                FinancialTradingSystem tradingSystem = new FinancialTradingSystem();
                tradingSystem.setVisible(true);
            }
        });
    }
}
