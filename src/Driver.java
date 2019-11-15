import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Driver {

    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException {
        PittSocial PS=new PittSocial("postgres","team3","jdbc:postgresql://localhost/postgres");


    }
}
