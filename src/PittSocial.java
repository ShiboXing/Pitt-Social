import java.io.IOException;
import java.util.Properties;
import java.sql.*;

public class PittSocial {
    private Connection _conn;
    private String currentUser;

    public PittSocial(Connection new_conn) throws ClassNotFoundException, SQLException {
        _conn=new_conn;
    }

    public void createUser(String username,String email, String password,String birthDate) throws SQLException {
        CallableStatement st=_conn.prepareCall("call createuser(?,?,?,?,null)");
        st.setString(1,username);
        st.setString(2,email);
        st.setString(3,password);
        st.setDate(4, Date.valueOf(birthDate));
        System.out.println(st);
        ResultSet rs=st.executeQuery();
        //ResultSet rs=_conn.createStatement().executeQuery(st.toString());
        System.out.println(rs);
    }

    public void login() throws IOException {
//        TODO: login and change current user
//        this.currentUser
    }
}
