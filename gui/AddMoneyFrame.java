package gui;

import dao.TransactionDAO;
import dao.UserDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.Transaction;
import model.User;

public class AddMoneyFrame extends JFrame {
    private User currentUser;

    public AddMoneyFrame(User user) {
        this.currentUser = user;
        setTitle("Add Money to Wallet");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Add Money to Your Wallet", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel qrPanel = new JPanel(new BorderLayout());
        qrPanel.setBorder(BorderFactory.createTitledBorder("Scan QR Code"));
        
        ImageIcon qrIcon = new ImageIcon("qr.png");
        JLabel qrLabel = new JLabel(qrIcon);
        qrPanel.add(qrLabel, BorderLayout.CENTER);

        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        idPanel.add(new JLabel("Your Recipient ID: "));
        JTextField recipientIdField = new JTextField(currentUser.getRecipientId(), 20);
        recipientIdField.setEditable(false);
        idPanel.add(recipientIdField);

        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(e -> {
            recipientIdField.selectAll();
            recipientIdField.copy();
            JOptionPane.showMessageDialog(this, "Recipient ID copied to clipboard!");
        });
        idPanel.add(copyButton);

        qrPanel.add(idPanel, BorderLayout.SOUTH);

        mainPanel.add(qrPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> {
            new MainFrame(currentUser).setVisible(true);
            dispose();
        });
        buttonPanel.add(backButton);

        JButton simulateButton = new JButton("Simulate Adding Rs.500");
        simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double amount = 500.00;
                currentUser.setBalance(currentUser.getBalance() + amount);
                UserDAO.updateUser(currentUser);
                
                Transaction transaction = new Transaction("SYSTEM", currentUser.getRecipientId(), amount, "ADD_MONEY");
                TransactionDAO.addTransaction(transaction);
                
                JOptionPane.showMessageDialog(AddMoneyFrame.this, 
                    "Rs." + amount + " added successfully!\nNew Balance: Rs." + currentUser.getBalance(), 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPanel.add(simulateButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}