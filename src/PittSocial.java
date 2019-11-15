import java.util.Properties;
import java.sql.*;

public class PittSocial {
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        Class c=Class.forName("org.postgresql.Driver");
        Properties props =new Properties();
        String url="jdbc:postgresql://localhost/postgres";
        props.setProperty("user","postgres");
        props.setProperty("password","team3");
        Connection conn =DriverManager.getConnection(url,props);
    }
}
