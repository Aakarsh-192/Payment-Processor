package gui;

import java.awt.*;
import javax.swing.*;
import model.User;

public class MainFrame extends JFrame {
    private User currentUser;

    public MainFrame(User user) {
        this.currentUser = user;
        setTitle("Payment Processor - Welcome " + user.getUsername());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JLabel balanceLabel = new JLabel("Balance: Rs." + String.format("%.2f", user.getBalance()));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setForeground(new Color(0, 100, 0));
        headerPanel.add(balanceLabel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton checkBalanceBtn = createStyledButton("Check Balance");
        JButton addMoneyBtn = createStyledButton("Add Money");
        JButton sendMoneyBtn = createStyledButton("Send Money");
        JButton transactionHistoryBtn = createStyledButton("Transaction History");
        JButton exitBtn = createStyledButton("Exit");

        buttonPanel.add(checkBalanceBtn);
        buttonPanel.add(addMoneyBtn);
        buttonPanel.add(sendMoneyBtn);
        buttonPanel.add(transactionHistoryBtn);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(exitBtn, BorderLayout.SOUTH);

        add(mainPanel);

        checkBalanceBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Your current balance is: Rs." + String.format("%.2f", currentUser.getBalance()), 
                "Account Balance", JOptionPane.INFORMATION_MESSAGE);
        });

        addMoneyBtn.addActionListener(e -> {
            new AddMoneyFrame(currentUser).setVisible(true);
            dispose();
        });

        sendMoneyBtn.addActionListener(e -> {
            new SendMoneyFrame(currentUser).setVisible(true);
            dispose();
        });

        transactionHistoryBtn.addActionListener(e -> {
            new TransactionHistoryFrame(currentUser).setVisible(true);
            dispose();
        });

        exitBtn.addActionListener(e -> {
            dispose();
            new AuthFrame().setVisible(true);
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }
}