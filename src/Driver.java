import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Driver {
<<<<<<< HEAD

    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException {
        PittSocial PS=new PittSocial("postgres","team3","jdbc:postgresql://localhost/postgres");
=======
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        PittSocial PS = new PittSocial("postgres", "team3", "jdbc:postgresql://localhost/postgres");
>>>>>>> ba600061d16439bf782f64c7f8b5bb7360a3f3a4


    }
}
