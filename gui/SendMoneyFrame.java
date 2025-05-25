package gui;

import dao.TransactionDAO;
import dao.UserDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import model.Transaction;
import model.User;

public class SendMoneyFrame extends JFrame {
    private User currentUser;

    public SendMoneyFrame(User user) {
        this.currentUser = user;
        setTitle("Send Money to Another User");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Send Money", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        formPanel.add(new JLabel("Recipient ID:"));
        JTextField recipientField = new JTextField();
        formPanel.add(recipientField);

        formPanel.add(new JLabel("Amount (Rs.):"));
        JTextField amountField = new JTextField();
        formPanel.add(amountField);

        formPanel.add(new JLabel("Your Balance: Rs." + currentUser.getBalance()));
        JButton sendButton = new JButton("Send Money");
        sendButton.setBackground(new Color(70, 130, 180));
        sendButton.setForeground(Color.BLACK);
        formPanel.add(sendButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> {
            new MainFrame(currentUser).setVisible(true);
            dispose();
        });
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String recipientId = recipientField.getText();
                String amountStr = amountField.getText();

                if (recipientId.isEmpty() || amountStr.isEmpty()) {
                    JOptionPane.showMessageDialog(SendMoneyFrame.this, 
                        "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double amount = Double.parseDouble(amountStr);
                    
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(SendMoneyFrame.this, 
                            "Amount must be positive", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (amount > currentUser.getBalance()) {
                        JOptionPane.showMessageDialog(SendMoneyFrame.this, 
                            "Insufficient balance", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    List<User> allUsers = UserDAO.loadUsers();
                    User recipient = null;
                    for (User u : allUsers) {
                        if (u.getRecipientId().equalsIgnoreCase(recipientId)) {
                            recipient = u;
                            break;
                        }
                    }
                    
                    if (recipient == null) {
                        JOptionPane.showMessageDialog(SendMoneyFrame.this, 
                            "Recipient not found", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (recipient.getRecipientId().equals(currentUser.getRecipientId())) {
                        JOptionPane.showMessageDialog(SendMoneyFrame.this, 
                            "Cannot send money to yourself", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    currentUser.setBalance(currentUser.getBalance() - amount);
                    recipient.setBalance(recipient.getBalance() + amount);
                    
                    UserDAO.updateUser(currentUser);
                    UserDAO.updateUser(recipient);
                    
                    Transaction transaction = new Transaction(
                        currentUser.getRecipientId(), 
                        recipient.getRecipientId(), 
                        amount, 
                        "SEND_MONEY"
                    );
                    TransactionDAO.addTransaction(transaction);
                    
                    JOptionPane.showMessageDialog(SendMoneyFrame.this, 
                        "Rs." + amount + " sent successfully to " + recipientId + 
                        "\nNew Balance: Rs." + currentUser.getBalance(), 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    recipientField.setText("");
                    amountField.setText("");
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SendMoneyFrame.this, 
                        "Invalid amount format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(mainPanel);
    }
}