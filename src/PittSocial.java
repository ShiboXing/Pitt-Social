import java.util.Properties;
import java.sql.*;

public class PittSocial {
    public static void main(String args[]) throws ClassNotFoundException
    {
        Class c=Class.forName("org.postgresql.Driver");
        Properties props =new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","password");
    }
}
