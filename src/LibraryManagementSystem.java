import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class LibraryManagementSystem {
    private JFrame frame;
    private DefaultTableModel model;
    private String filename = "item.txt";

    public LibraryManagementSystem() {
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        String[] columnNames = {"Title", "Author", "Publication Year", "Read Item"};
        model = new DefaultTableModel(columnNames, 0) {
            // Override isCellEditable to make all cells non-editable except the "Read" button
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
//ye kia h?
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    table.addRowSelectionInterval(row, row);
                }
            }
        });

        // Create a custom button renderer for the "Read" column
        // table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        // Add an action listener to handle the "Read" button click
        //table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JTextField(),table));

        // Create a custom button renderer for the "Read" column
        // table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
// Add an action listener to handle the "Read" button click
        //table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JTextField(), table));

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Item");
        JButton deleteButton = new JButton("Delete Item");
        JButton editButton = new JButton("Edit Item");
        JButton pop_butt = new JButton("View Popularity");

        buttonPanel.add(pop_butt);

        // Function to open the popularity screen
    
    buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(pop_butt);

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
        pop_butt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openPopularityScreen();
            }

            private void openPopularityScreen() {

                JFrame popularityFrame = new JFrame("Library Item Popularity");
                popularityFrame.setSize(600, 400);
//
//                HashMap<String, Integer> popularityData = new HashMap<>();
//                popularityData.put("Book 1", 10);
//                popularityData.put("Book 2", 5);
//                popularityData.put("Book 3", 8);
//                popularityData.put("Book 4", 3);
//
//                JPanel chartPanel = new JPanel() {
//                    @Override
//                    protected void paintComponent(Graphics g) {
//                        super.paintComponent(g);
//                        drawBarChart(g, popularityData);
//                    }
//                };
//
//                popularityFrame.add(chartPanel, BorderLayout.CENTER);
//                popularityFrame.setVisible(true);
//            }
//
//            private void drawBarChart(Graphics g, HashMap<String, Integer> data) {
//                int barWidth = 30;
//                int spacing = 20;
//                int x = 50;
//                int maxY = 300;
//                g.setColor(Color.pink);
//
//                for (String item : data.keySet()) {
//                    int value = data.get(item);
//                    int barHeight = value * 10;
//
//                    g.fillRect(x, maxY - barHeight, barWidth, barHeight);
//                    g.drawString(item, x, maxY + 20);
//
//                    x += barWidth + spacing;
//                }
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

        addBookFrame.setLayout(new BorderLayout());
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
        addBookPanel.setLayout(new GridLayout(3,2));
        addBookPanel.add(new JLabel("Title:"));
        addBookPanel.add(titleField);
        addBookPanel.add(new JLabel("Author:"));
        addBookPanel.add(authorField);
        addBookPanel.add(new JLabel("Publication Year:"));
        addBookPanel.add(yearField);


        addBookFrame.add(addBookPanel,BorderLayout.NORTH);
        addBookFrame.add(addBookButton,BorderLayout.SOUTH);
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
                        deleteBookFrame.dispose();
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




















//class ButtonRenderer extends DefaultCellEditor {
//    private JButton button;
//    private String label;
//    private boolean isPushed;
//    private JTable table; // Add a table variable
//
//    public ButtonRenderer(JTable table) {
//        super(textField);
//        this.table = table;
//        button = new JButton();
//        button.setOpaque(true);
//        button.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                fireEditingStopped();
//            }
//        });
//    }
//
//    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//        if (isSelected) {
//            button.setForeground(table.getSelectionForeground());
//            button.setBackground(table.getSelectionBackground());
//        } else {
//            button.setForeground(table.getForeground());
//            button.setBackground(UIManager.getColor("Button.background"));
//        }
//
//        label = (value == null) ? "" : value.toString();
//        button.setText(label);
//        isPushed = true;
//        return button;
//    }
//
//    public Object getCellEditorValue() {
//        if (isPushed) {
//            // Handle the "Read" button click
//            int selectedRow = table.getSelectedRow();
//            String title = table.getModel().getValueAt(selectedRow, 0).toString();
//            loadBookDataForReading(title);
//        }
//        isPushed = false;
//        return label;
//    }
//
//    public boolean stopCellEditing() {
//        isPushed = false;
//        return super.stopCellEditing();
//    }
//
//    protected void fireEditingStopped() {
//        super.fireEditingStopped();
//    }
//    class ButtonEditor extends DefaultCellEditor {
//        private String label;
//        private JTable table;
//
//        public ButtonEditor(JTextField textField, JTable table) {
//            super(textField);
//            this.table = table;
//        }
//
//        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//            this.label = (value == null) ? "" : value.toString();
//            return super.getTableCellEditorComponent(table, label, isSelected, row, column);
//        }
//    }
//    private void loadBookDataForReading(String title) {
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
//            String line;
//            StringBuilder bookContent = new StringBuilder();
//            boolean found = false;
//            while ((line = reader.readLine()) != null) {
//                String[] data = line.split(",");
//                if (data.length == 4 && data[0].equals(title)) {
//                    found = true;
//                    // Assuming that the last field indicates the read status
//                    String readStatus = data[3];
//                    if ("Not Read".equals(readStatus)) {
//                        bookContent.append(data[0]).append("\n");
//                        bookContent.append(data[1]).append("\n");
//                        bookContent.append(data[2]).append("\n");
//                        bookContent.append("Book content goes here..."); // Replace with actual book content
//                    } else {
//                        JOptionPane.showMessageDialog(null, "The book '" + title + "' has already been read.");
//                    }
//                    break; // No need to continue searching
//                }
//            }
//            reader.close();
//            if (found) {
//                showBookReadDialog(title, bookContent.toString());
//            } else {
//                JOptionPane.showMessageDialog(null, "Book not found: " + title);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void showBookReadDialog(String title, String bookContent) {
//        JTextArea textArea = new JTextArea(20, 40);
//        textArea.setText(bookContent);
//        textArea.setWrapStyleWord(true);
//        textArea.setLineWrap(true);
//        textArea.setCaretPosition(0);
//        textArea.setEditable(false);
//        JScrollPane scrollPane = new JScrollPane(textArea);
//
//        JPanel panel = new JPanel();
//        panel.add(scrollPane);
//
//        JFrame bookReadFrame = new JFrame("Read Book: " + title);
//        bookReadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        bookReadFrame.add(panel);
//        bookReadFrame.pack();
//        bookReadFrame.setVisible(true);
//    }
//}
//class ButtonEditor extends DefaultCellEditor {
//    private JButton button;
//    private String label;
//    private boolean isPushed;
//
//    public ButtonEditor(JTextField textField, JTable table) {
//        super(textField);
//        button = new JButton();
//        button.setOpaque(true);
//        button.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                fireEditingStopped();
//            }
//        });
//    }
//
//    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//        if (isSelected) {
//            button.setForeground(table.getSelectionForeground());
//            button.setBackground(table.getSelectionBackground());
//        } else {
//            button.setForeground(table.getForeground());
//            button.setBackground(UIManager.getColor("Button.background"));
//        }
//
//        label = (value == null) ? "" : value.toString();
//        button.setText(label);
//        isPushed = true;
//        return button;
//    }
//
//    public Object getCellEditorValue() {
//        if (isPushed) {
//            // Handle the "Read" button click
//            int selectedRow = table.getSelectedRow();
//            String title = table.getModel().getValueAt(selectedRow, 0).toString();
//            loadBookDataForReading(title);
//        }
//        isPushed = false;
//        return label;
//    }
//
//    public boolean stopCellEditing() {
//        isPushed = false;
//        return super.stopCellEditing();
//    }
//
//    protected void fireEditingStopped() {
//        super.fireEditingStopped();
//    }
//}
