import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class Demo {
    public static void main(String[] args) throws SQLException {
        try (Connection conn = getConnection()) {
            String sql = "CREATE TABLE IF NOT EXISTS courses("
                    + "offeringName VARCHAR(15) NOT NULL PRIMARY KEY,"
                    + "title VARCHAR(50) NOT NULL"
                    + ");";
            Statement st = conn.createStatement();
            st.execute(sql);

            sql = "INSERT INTO courses (offeringName, title)"
                    + "VALUES ('EN.601.226', 'Data Structures');";
            st.execute(sql);

        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null) {
            throw new URISyntaxException(databaseUrl, "DATABASE_URL is not set");
        }

        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        return DriverManager.getConnection(dbUrl, username, password);
    }
}