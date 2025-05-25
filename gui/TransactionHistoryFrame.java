package gui;

import dao.TransactionDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Transaction;
import model.User;

public class TransactionHistoryFrame extends JFrame {
    private User currentUser;

    public TransactionHistoryFrame(User user) {
        this.currentUser = user;
        setTitle("Transaction History");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Your Transaction History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        List<Transaction> transactions = TransactionDAO.getTransactionsByUser(currentUser.getRecipientId());
        
        String[] columnNames = {"Date", "Type", "Amount", "Sender", "Receiver"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        
        for (Transaction t : transactions) {
            String type = t.getType().equals("SEND_MONEY") ? "Debit" : "Credit";
            String amount = (t.getSenderId().equalsIgnoreCase(currentUser.getRecipientId()) ? "-" : "+") + 
                        "Rs." + String.format("%.2f", t.getAmount());
            
            model.addRow(new Object[]{
                t.getTimestamp(),
                type,
                amount,
                t.getSenderId(),
                t.getReceiverId()
            });
        }
        
        JTable transactionTable = new JTable(model);
        transactionTable.setRowHeight(25);
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 14));
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> {
            new MainFrame(currentUser).setVisible(true);
            dispose();
        });
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}