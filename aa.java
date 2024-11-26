import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class aa {

    public static boolean validPalindrome(String o) {
        String normalized = o.toLowerCase();
        StringBuffer buffer = new StringBuffer(normalized);
        buffer.reverse();
        return normalized.equals(buffer.toString());
    }

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "123456";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Database connection succeeded!");
            } else {
                System.out.println("Database connection failed!");
                return;
            }
        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        String createTableSQL = "CREATE TABLE IF NOT EXISTS palindrome ("
                + "id SERIAL PRIMARY KEY, "
                + "text VARCHAR(100) NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createTableSQL);
            System.out.println("Table created successfully");

        } catch (SQLException e) {
            System.out.println("An error occurred while creating the table: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("---- Palindrome Checking Program ----");
            System.out.println("1) Check palindromes");
            System.out.println("2) Exit program");
            System.out.print("Select an option: ");
            int menu = sc.nextInt();
            sc.nextLine();

            switch (menu) {
                case 1:
                    System.out.print("Enter a word to check: ");
                    String text = sc.nextLine();

                    boolean Palindrome = validPalindrome(text);
                    System.out.println("Is \"" + text + "\" a palindrome? " + Palindrome);

                    if (Palindrome) {
                        String insertSQL = "INSERT INTO palindrome (text) VALUES (?)";
                        try (Connection conn = DriverManager.getConnection(url, user, password);
                             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

                            pstmt.setString(1, text);
                            pstmt.executeUpdate();
                            System.out.println("The word \"" + text + "\" has been saved to the database.");
                        } catch (SQLException e) {
                            System.out.println("An error occurred while saving the data: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("The word \"" + text + "\" is not a palindrome. No data was saved.");
                    }
                    break;

                case 2:
                    System.out.println("Exit program");
                    sc.close();
                    return;

                default:
                    System.out.println("Please specify correctly ( 1,2 )");
                    break;
            }
        }
    }
}
