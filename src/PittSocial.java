import java.io.IOException;
import java.util.Properties;
import java.sql.*;

public class PittSocial {
    private Connection _conn;
    private String currentUser;

    public PittSocial(String username, String password, String url) throws ClassNotFoundException, SQLException {
        Class c = Class.forName("org.postgresql.Driver");
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        this._conn = DriverManager.getConnection(url, props);
    }

    public void createUser() throws SQLException {
        //PreparedStatement st=_conn.prepareStatement("call createuser('testest',\t'primis.in@placerateget.com',\t'5679',\n" +
        // "    '1997-09-10','2019-01-17 07:35:18.000000');");
    }

    public void login() throws {
//        TODO: login and change current user
//        this.currentUser
    }
}
