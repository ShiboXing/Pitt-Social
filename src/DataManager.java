import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DataManager {
    final private String schemaSource = "DataSource/schema.sql";
    final private String triggerSource = "DataSource/trigger.sql";
    final private String testSource = "DataSource/test.sql";
    final private String adminUser = "postgres";
    final private String adminPassword = "postgres";
    final private String databaesUrl = "jdbc:postgresql://localhost/postgres";
    private Connection _conn;

    public DataManager() throws ClassNotFoundException, SQLException {
        Class c = Class.forName("org.postgresql.Driver");
        Properties props = new Properties();
        props.setProperty("user", this.adminUser);
        props.setProperty("password", this.adminPassword);
        this._conn = DriverManager.getConnection(databaesUrl, props);
    }

    public void initDatabase(boolean isInitiate) throws SQLException, IOException {
        if (isInitiate) {
            executeSQLFile(this.schemaSource);
            executeSQLFile(this.triggerSource);
        } else {
            PreparedStatement st = _conn.prepareStatement("set search_path to pitt_social;");
            st.execute();
        }
    }

    public void initDatabaseWithTest(boolean isInitiate) throws SQLException, IOException {
        if (isInitiate) {
            executeSQLFile(this.schemaSource);
            executeSQLFile(this.testSource);
            executeSQLFile(this.triggerSource);
        } else {
            PreparedStatement st = _conn.prepareStatement("set search_path to pitt_social;");
            st.execute();
        }
    }



    public Connection getConnection()
    {
        return _conn;
    }

    private void executeSQLFile(String filePath) throws SQLException, IOException {
        Statement statement = _conn.createStatement();
        // initialize file reader
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder lines = new StringBuilder();
        String line = "";
        // read script line by line
        while ((line = reader.readLine()) != null) {
            lines.append(line+'\n');
        }
        //System.out.println("queries: " + lines.toString());
        statement.execute(lines.toString());
    }


}
