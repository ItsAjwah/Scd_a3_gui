import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class LibraryApp {

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField yearField;

    public LibraryApp() {
        JFrame frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Create the table
        String[] columns = {"Title", "Author", "Year"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        // Create text fields and buttons
        titleField = new JTextField(20);
        authorField = new JTextField(20);
        yearField = new JTextField(10);
        JButton addButton = new JButton("Add Book");

        // Add components to the frame
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Title: "));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author: "));
        inputPanel.add(authorField);
        inputPanel.add(new JLabel("Year: "));
        inputPanel.add(yearField);
        inputPanel.add(addButton);

        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                int year = Integer.parseInt(yearField.getText());

                // Add the book to the table
                tableModel.addRow(new Object[]{title, author, year});

                // Clear input fields
                titleField.setText("");
                authorField.setText("");
                yearField.setText("");
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LibraryApp();
            }
        });
    }
}
