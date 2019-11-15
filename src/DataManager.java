import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataManager {
    final private String schemaSource = "DataSource/schema.sql";
    final private String insertSource = "DataSource/trigger.sql";
    final private String adminUser = "postgres";
    final private String adminPassword = "team3";
    final private String databaesUrl = "jdbc:postgresql://localhost/postgres";
    private Connection _conn;

    public DataManager() throws ClassNotFoundException, SQLException {
        Class c = Class.forName("org.postgresql.Driver");
        Properties props = new Properties();
        props.setProperty("user", this.adminUser);
        props.setProperty("password", this.adminPassword);
        this._conn = DriverManager.getConnection(databaesUrl, props);
    }

    public void initDatabase() throws SQLException, IOException {
        executeSQLFile(this.schemaSource);
        executeSQLFile(this.insertSource);
    }

    private void executeSQLFile(String filePath) throws SQLException, IOException {
        Statement statement = _conn.createStatement();
        // initialize file reader
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder lines = new StringBuilder();
        String line = "";
        // read script line by line
        while ((line = reader.readLine()) != null) {
            lines.append(line);
        }
        System.out.println("queries: " + lines.toString());
        statement.execute(lines.toString());
    }
}
