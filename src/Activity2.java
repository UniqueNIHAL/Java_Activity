import java.sql.*;

public class Activity2 {
    public static void main(String[] args) {
        // JDBC URL, username, and password of the database
        String jdbcUrl = "jdbc:mysql://localhost:3306/lab";
        String username = "root";
        String password = "12345";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {

            // Define SQL query to retrieve lab details
            String query = "SELECT * FROM lab_data";

            // Execute the query
            ResultSet resultSet = statement.executeQuery(query);

            // Print column headers
            System.out.printf("%-10s %-20s %-10s %-10s %-20s %-10s %-20s%n",
                    "Lab No", "Lab Name", "RAM", "Storage", "OS", "Systems", "Processor");
            System.out.println("--------------------------------------------------------------------------------------------");

            // Iterate over the result set and print lab details
            while (resultSet.next()) {
                System.out.printf("%-10s %-20s %-10s %-10s %-20s %-10s %-20s%n",
                        resultSet.getInt("lab_no"),
                        resultSet.getString("lab_name"),
                        resultSet.getInt("ram"),
                        resultSet.getInt("storage"),
                        resultSet.getString("os"),
                        resultSet.getInt("systems"),
                        resultSet.getString("processor"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
