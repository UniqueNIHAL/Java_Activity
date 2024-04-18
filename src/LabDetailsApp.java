import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LabDetailsApp extends JFrame {
    private JTable labComputersTable;
    private JTextField searchField;

    public LabDetailsApp() {
        setTitle("University Lab Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Create header panel with gradient background
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = Color.decode("#2E3192");
                Color color2 = Color.decode("#1BFFFF");
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));

        // Add logo image to the header
        ImageIcon logoIcon = new ImageIcon("csit-logo.png"); // Adjust the path to your logo image
        JLabel logoLabel = new JLabel(logoIcon);
        headerPanel.add(logoLabel, BorderLayout.WEST);

        // Add title label to the header
        JLabel titleLabel = new JLabel("University Lab Information");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Add header panel to the frame
        add(headerPanel, BorderLayout.NORTH);

        // Create search panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add search field and button to the search panel
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(searchField, BorderLayout.CENTER);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText().trim();
                if (!keyword.isEmpty()) {
                    searchLabData(keyword);
                } else {
                    populateLabData();
                }
            }
        });
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Add search panel to the frame
        add(searchPanel, BorderLayout.SOUTH);

        // Create main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = Color.decode("#F0F5F9");
                Color color2 = Color.decode("#C4D4ED");
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        // Create lab computers panel with rounded corners
        JPanel labComputersPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth();
                int height = getHeight();
                int arc = 20; // Adjust the arc size for rounded corners
                g.setColor(getBackground());
                g.fillRoundRect(0, 0, width, height, arc, arc);
            }
        };
        labComputersPanel.setOpaque(false);
        labComputersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create lab computers table
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Lab Number", "Lab Name", "Operating System", "Processor", "RAM", "Storage", "Systems", "Working State"});
        labComputersTable = new JTable(model);
        labComputersTable.setFillsViewportHeight(true);
        labComputersPanel.add(new JScrollPane(labComputersTable), BorderLayout.CENTER);

        // Add lab computers panel to the main panel
        mainPanel.add(labComputersPanel, BorderLayout.CENTER);

        // Add main panel to the frame
        add(mainPanel, BorderLayout.CENTER);

        // Populate lab data from JDBC
        populateLabData();

        // Create footer panel with gradient background
        JPanel footerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = Color.decode("#2E3192");
                Color color2 = Color.decode("#1BFFFF");
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        footerPanel.setPreferredSize(new Dimension(getWidth(), 40));

        // Add copyright label to the footer
        JLabel copyrightLabel = new JLabel("\u00a9 2024 University Lab Information");
        copyrightLabel.setForeground(Color.WHITE);
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(copyrightLabel, BorderLayout.CENTER);

        // Add footer panel to the frame
        add(footerPanel, BorderLayout.SOUTH);

        // Set visibility
        setVisible(true);
    }

    private void populateLabData() {
        // JDBC URL, username, and password of the database
        String JDBC_URL = "jdbc:mysql://localhost:3306/lab";
        String USERNAME = "root";
        String PASSWORD = "12345";

        // SQL query to retrieve all lab details
        String query = "SELECT * FROM lab_data";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Clear existing rows from the table model
            DefaultTableModel model = (DefaultTableModel) labComputersTable.getModel();
            model.setRowCount(0);

            // Add rows to the table model
            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getInt("lab_no"),
                        resultSet.getString("lab_name"),
                        resultSet.getString("os"),
                        resultSet.getString("processor"),
                        resultSet.getInt("ram"),
                        resultSet.getInt("storage"),
                        resultSet.getInt("systems"),
                        resultSet.getString("working_state")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchLabData(String keyword) {
        // JDBC URL, username, and password of the database
        String JDBC_URL = "jdbc:mysql://localhost:3306/lab";
        String USERNAME = "root";
        String PASSWORD = "12345";

        // SQL query to search lab details based on keyword
        String query = "SELECT * FROM lab_data " +
                "WHERE lab_no LIKE '%" + keyword + "%' OR " +
                "lab_name LIKE '%" + keyword + "%' OR " +
                "os LIKE '%" + keyword + "%'";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Clear existing rows from the table model
            DefaultTableModel model = (DefaultTableModel) labComputersTable.getModel();
            model.setRowCount(0);

            // Add rows to the table model
            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getInt("lab_no"),
                        resultSet.getString("lab_name"),
                        resultSet.getString("os"),
                        resultSet.getString("processor"),
                        resultSet.getInt("ram"),
                        resultSet.getInt("storage"),
                        resultSet.getInt("systems"),
                        resultSet.getString("working_state")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Run the Swing application on the event dispatch thread
        SwingUtilities.invokeLater(LabDetailsApp::new);
    }
}
