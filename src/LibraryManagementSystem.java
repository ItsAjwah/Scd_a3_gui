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
import java.util.ArrayList;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;



public class LibraryManagementSystem {
    private JFrame frame;
    private DefaultTableModel model;
    private String filename = "item.txt";
ArrayList<String> Tittle=new ArrayList<>();
ArrayList<Integer> popcount=new ArrayList<>();
    public LibraryManagementSystem() {
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        String[] col_names = {"Title", "Author", "Publication Year", "Read Item"};
        model = new DefaultTableModel(col_names, 0) {
          //override kia so koi column edit na ho ske siwaye read wale ke
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        JTable table = new JTable(model);

        JScrollPane scrol_panel = new JScrollPane(table);
        frame.add(scrol_panel);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    table.addRowSelectionInterval(row, row);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                table.clearSelection();
            }

        });

        //yhan se dobara review kerna hy
        // Create a custom renderer for the "Read Item" column
        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
//        for (int i = 0; i < model.getRowCount(); i++) {
//            String bookName = model.getValueAt(i, 0).toString(); // Assuming the book name is in the first column
//            table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), bookName));
//        }

        for (int i = 0; i < model.getRowCount(); i++) {
            String bookTitle = model.getValueAt(i, 0).toString(); // Assuming the book title is in the first column
            table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), bookTitle));
        }




        JPanel button_panel = new JPanel();
        JButton add_button = new JButton("Add Item");
        JButton delete_button = new JButton("Delete Item");
        JButton edit_button = new JButton("Edit Item");
        JButton pop_butt = new JButton("View Popularity");

        button_panel.add(pop_butt);

        // Function to open the popularity screen
    
    button_panel = new JPanel();
        button_panel.add(add_button);
        button_panel.add(delete_button);
        button_panel.add(edit_button);
        button_panel.add(pop_butt);

        frame.add(button_panel, BorderLayout.SOUTH);

        add_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                open_screen_to_add_book();
            }
        });

        delete_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                open_screen_to_delete_book();
            }
        });

        edit_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                open_screen_to_edit_book();
            }
        });
        pop_butt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JFrame pop_frame = new JFrame("Library Item Popularity");
                pop_frame.setVisible(true);
                pop_frame.setSize(600, 400);
                JPanel bar=new JPanel(){
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);

                        int bar_width = 40;
                        int gap = 20;
                        int x = 60; // Adjust the initial x position for the first bar
                        int max_data = popcount.stream().max(Integer::compare).orElse(0);

                        // Draw Y-axis labels and values
                        g.setColor(Color.BLACK);
                        g.drawLine(50, 30, 50, getHeight() - 30);
                        for (int i = 0; i <= 10; i++) {
                            int y = getHeight() - 30 - (i*(getHeight() - 60) / 10);
                            g.drawString(Integer.toString(i*(max_data / 10)), 10, y);
                            g.drawLine(45, y, 50, y);
                        }

                        // Set the origin of the bars to the bottom of the graph
                        int y_axis_orgin = getHeight() - 30;

                        for (int i = 0; i < popcount.size(); i++) {
                            int bar_height = (int) (((double) popcount.get(i) / max_data)*(getHeight() - 60));
                            g.setColor(Color.ORANGE);
                            g.fillRect(x, y_axis_orgin - bar_height, bar_width, bar_height);
                            g.setColor(Color.BLACK);
                            g.drawRect(x, y_axis_orgin - bar_height, bar_width, bar_height);

                            // Rotate the X-axis labels by 90 degrees
                            Graphics2D g2d = (Graphics2D) g.create();
                            g2d.translate(x + bar_width / 2, y_axis_orgin - 10);
                            g2d.rotate(Math.toRadians(90));
                            g2d.drawString(Tittle.get(i), 0, 0);
                            g2d.dispose();

                            x += bar_width + gap;  //spacing add kari width mein

                        }
                    }
                };

                pop_frame.add(bar);
            }

            });

        loadDataFromFile(filename);

        frame.setVisible(true);
    }

    class ButtonRenderer extends DefaultTableCellRenderer {
        private JButton button;

        public ButtonRenderer() {
            button = new JButton("Read");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return button;
        }
    }
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String bookTitle;

        public ButtonEditor(JCheckBox checkBox, String bookTitle) {
            super(checkBox);
            this.bookTitle = bookTitle;
            button = new JButton("Read");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openBookContent(bookTitle);
                }
            });
        }

        private void openBookContent(String bookTitle)

        {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(bookTitle + ".txt"));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();

                JFrame contentFrame = new

                        JFrame("Book Content: " + bookTitle);
                JTextArea textArea = new JTextArea(content.toString());
                textArea.setEditable(false);
                contentFrame.add(new JScrollPane(textArea));
                contentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                contentFrame.setSize(600, 400);
                contentFrame.setVisible(true);

                // Dispose of the contentFrame when the user clicks the 'read' button.
                contentFrame.dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error reading the book content.");
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }
    }

    private void open_screen_to_add_book() {
        JFrame addbook_frame = new JFrame("Add Book");
        addbook_frame.setSize(300, 200);

        JTextField title_field = new JTextField(20);
        JTextField auhtor_field = new JTextField(20);
        JTextField year_field = new JTextField(10);

        JButton addbook_butt = new JButton("Add");

        addbook_frame.setLayout(new BorderLayout());
        addbook_butt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = title_field.getText();
                String author = auhtor_field.getText();
                String year = year_field.getText();
                popcount.add(0);

                if (!title.isEmpty() && !author.isEmpty() && !year.isEmpty()) {
                    model.addRow(new Object[]{title, author, year, "Not Read"});
                    saveDataToFile(filename);
                }

                addbook_frame.dispose();
            }
        });

        JPanel addbookpanel = new JPanel();
        addbookpanel.setLayout(new GridLayout(3,2));
        addbookpanel.add(new JLabel("Title:"));
        addbookpanel.add(title_field);
        addbookpanel.add(new JLabel("Author:"));
        addbookpanel.add(auhtor_field);
        addbookpanel.add(new JLabel("Publication Year:"));
        addbookpanel.add(year_field);


        addbook_frame.add(addbookpanel,BorderLayout.NORTH);
        addbook_frame.add(addbook_butt,BorderLayout.SOUTH);
        addbook_frame.setVisible(true);
    }

    private void open_screen_to_delete_book() {
        JFrame deletebook_frame = new JFrame("Delete Book");
        deletebook_frame.setSize(300, 100);

        JTextField titleField = new JTextField(20);
        JButton deleteButton = new JButton("Delete");

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                int row_count = model.getRowCount();
                boolean check=false;
                for (int i = row_count - 1; i >= 0; i--) {
                    if (model.getValueAt(i, 0).equals(title)) {
                        model.removeRow(i);
                        popcount.remove(i);
                        deletebook_frame.dispose();
                        check=true;
                    }
                }
                if(!check)
                {
                    JOptionPane.showMessageDialog(deletebook_frame, "Item not found in the file",
                            "Not Found", JOptionPane.ERROR_MESSAGE);
                }
                deletebook_frame.dispose();
                // Save the updated data back to the file
                saveDataToFile("item.txt");


            }
        });


        JPanel deletebookpanel = new JPanel();
        deletebookpanel.add(new JLabel("Title to Delete:"));
        deletebookpanel.add(titleField);
        deletebookpanel.add(deleteButton);

        deletebook_frame.add(deletebookpanel);
        deletebook_frame.setVisible(true);
    }

    private void open_screen_to_edit_book() {
        JFrame editbook_frame = new JFrame("Edit Book");
        editbook_frame.setSize(300, 200);

        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField yearField = new JTextField(10);
        JButton editButton = new JButton("Edit");

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                String year = yearField.getText();
                boolean check1=false;
                if (!title.isEmpty()) {
                    int numRows = model.getRowCount();
                    for (int i = 0; i < numRows; i++) {
                        if (model.getValueAt(i, 0).equals(title)) {
                            model.setValueAt(author, i, 1);
                            model.setValueAt(year, i, 2);
                            saveDataToFile(filename);
                            check1=true;

                        }
                    }
                    if(!check1)
                    {
                        JOptionPane.showMessageDialog(editbook_frame, "Item not found in the file to Edit",
                                "Not Found", JOptionPane.ERROR_MESSAGE);
                    }
                }

                editbook_frame.dispose();
            }
        });

        JPanel editbookpanel = new JPanel();
        editbookpanel.add(new JLabel("Title to Edit:"));
        editbookpanel.add(titleField);
        editbookpanel.add(new JLabel("New Author:"));
        editbookpanel.add(authorField);
        editbookpanel.add(new JLabel("New Publication Year:"));
        editbookpanel.add(yearField);
        editbookpanel.add(editButton);

        editbook_frame.add(editbookpanel);
        editbook_frame.setVisible(true);

    }

    private void loadDataFromFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("item.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Tittle.add(data[0]);
                popcount.add(Integer.valueOf(data[3]));
//                if (data.length == 3) {
                String []storedata=new String[3];
                storedata[0]=data[0];
                storedata[1]=data[1];
                storedata[2]=data[2];
                    model.addRow(storedata);

//                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDataToFile(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            int row_count = model.getRowCount();
            for (int i = 0; i < row_count; i++) {
                String title = model.getValueAt(i, 0).toString();
                String author = model.getValueAt(i, 1).toString();
                String year = model.getValueAt(i, 2).toString();

                writer.write(title + "," + author + "," + year + ","+popcount.get(i) + "\n");
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

