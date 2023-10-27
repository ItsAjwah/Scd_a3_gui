import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class LibraryGUI {
    private JFrame frame;
    private DefaultTableModel model;
    private String filename = "item.txt";

    public LibraryGUI() {
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        String[] columnNames = {"Title", "Author", "Publication Year", "Read Item"};
        model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        JButton addButton = new JButton("Add Item");
        JButton deleteButton = new JButton("Delete Item");
        JButton editButton = new JButton("Edit Item");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAddBookScreen();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openDeleteBookScreen();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openEditBookScreen();
            }
        });

        loadDataFromFile(filename);

        frame.setVisible(true);
    }

    private void openAddBookScreen() {
        JFrame addBookFrame = new JFrame("Add Book");
        addBookFrame.setSize(300, 200);

        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField yearField = new JTextField(10);

        JButton addBookButton = new JButton("Add");

        addBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                String year = yearField.getText();

                if (!title.isEmpty() && !author.isEmpty() && !year.isEmpty()) {
                    model.addRow(new Object[]{title, author, year, "Not Read"});
                    saveDataToFile(filename);
                }

                addBookFrame.dispose();
            }
        });

        JPanel addBookPanel = new JPanel();
        addBookPanel.add(new JLabel("Title:"));
        addBookPanel.add(titleField);
        addBookPanel.add(new JLabel("Author:"));
        addBookPanel.add(authorField);
        addBookPanel.add(new JLabel("Publication Year:"));
        addBookPanel.add(yearField);
        addBookPanel.add(addBookButton);

        addBookFrame.add(addBookPanel);
        addBookFrame.setVisible(true);
    }

    private void openDeleteBookScreen() {
        JFrame deleteBookFrame = new JFrame("Delete Book");
        deleteBookFrame.setSize(300, 100);

        JTextField titleField = new JTextField(20);
        JButton deleteButton = new JButton("Delete");

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                int numRows = model.getRowCount();
                for (int i = numRows - 1; i >= 0; i--) {
                    if (model.getValueAt(i, 0).equals(title)) {
                        model.removeRow(i);
                    }
                }

                // Save the updated data back to the file
                saveDataToFile("item.txt");

                deleteBookFrame.dispose();
            }
        });


        JPanel deleteBookPanel = new JPanel();
        deleteBookPanel.add(new JLabel("Title to Delete:"));
        deleteBookPanel.add(titleField);
        deleteBookPanel.add(deleteButton);

        deleteBookFrame.add(deleteBookPanel);
        deleteBookFrame.setVisible(true);
    }

    private void openEditBookScreen() {
        JFrame editBookFrame = new JFrame("Edit Book");
        editBookFrame.setSize(300, 200);

        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField yearField = new JTextField(10);
        JButton editButton = new JButton("Edit");

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                String year = yearField.getText();

                if (!title.isEmpty()) {
                    int numRows = model.getRowCount();
                    for (int i = 0; i < numRows; i++) {
                        if (model.getValueAt(i, 0).equals(title)) {
                            model.setValueAt(author, i, 1);
                            model.setValueAt(year, i, 2);
                            saveDataToNewFile(filename);
                        }
                    }
                }

                editBookFrame.dispose();
            }
        });

        JPanel editBookPanel = new JPanel();
        editBookPanel.add(new JLabel("Title to Edit:"));
        editBookPanel.add(titleField);
        editBookPanel.add(new JLabel("New Author:"));
        editBookPanel.add(authorField);
        editBookPanel.add(new JLabel("New Publication Year:"));
        editBookPanel.add(yearField);
        editBookPanel.add(editButton);

        editBookFrame.add(editBookPanel);
        editBookFrame.setVisible(true);
    }

    private void loadDataFromFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("item.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    model.addRow(data);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDataToFile(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            int numRows = model.getRowCount();
            for (int i = 0; i < numRows; i++) {
                String title = model.getValueAt(i, 0).toString();
                String author = model.getValueAt(i, 1).toString();
                String year = model.getValueAt(i, 2).toString();
                String readStatus = model.getValueAt(i, 3).toString();
                writer.write(title + "," + author + "," + year + "," + readStatus + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDataToNewFile(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            int numRows = model.getRowCount();
            for (int i = 0; i < numRows; i++) {
                String title = model.getValueAt(i, 0).toString();
                String author = model.getValueAt(i, 1).toString();
                String year = model.getValueAt(i, 2).toString();
                String readStatus = model.getValueAt(i, 3).toString();
                writer.write(title + "," + author + "," + year + "," + readStatus + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LibraryManagementSystem();
            }
        });
    }
}
