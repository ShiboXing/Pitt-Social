import java.io.IOException;
import java.util.Properties;
import java.sql.*;

public class PittSocial {
    private Connection _conn;
    private int currentUserId;

    public PittSocial(Connection new_conn) throws ClassNotFoundException, SQLException {
        _conn=new_conn;
    }

    public void createUser(String username,String email, String password,String birthDate) throws SQLException {
        CallableStatement st=_conn.prepareCall("call createuser(?,?,?,?,null);");
        st.setString(1,username);
        st.setString(2,email);
        st.setString(3,password);
        st.setDate(4, Date.valueOf(birthDate));
        st.execute();

        System.out.println(st);
    }

    public int login(String email,String password) throws IOException, SQLException {

          PreparedStatement st=_conn.prepareStatement("select * from login(?,?);");
          st.setString(1,email);
          st.setString(2,password);
          ResultSet rs=st.executeQuery();
          rs.next();
          String col1=rs.getMetaData().getColumnName(1);
          int resID=rs.getInt(col1);
          assert !rs.next():"rs contains more than one row!";

          System.out.println(st);

          if (resID==0) //match not found
              return -1;
          else {
              this.currentUserId = resID;
              return 0;
          }
    }

    public int initiateFriendship(int toid, String msg) throws SQLException {
        CallableStatement st=_conn.prepareCall("call initiatefriendship(?,?,?);");
        st.setInt(1,currentUserId);
        st.setInt(2,toid);
        st.setString(3,msg);
        assert !st.execute():"initiatefriendship returns result!";

        System.out.println(st);
        return 0;
    }




}
