import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        st.execute();

        System.out.println(st);

        return 0;
    }

    public int createGroup(String name,int limit,String descr) throws SQLException {
        CallableStatement st=_conn.prepareCall("call creategroup(?,?,?,?);");
        st.setInt(1,currentUserId);
        st.setString(2,name);
        st.setInt(3,limit);
        st.setString(4,descr);
        st.execute();

        System.out.println(st);

        return 0;
    }

    public int initiateAddingGroup(int gid,String msg) throws SQLException {
        CallableStatement st=_conn.prepareCall("call initiateaddinggroup(?,?,?);");
        st.setInt(1,currentUserId);
        st.setInt(2,gid);
        st.setString(3,msg);
        st.execute();

        System.out.println(st);

        return 0;
    }


    public List showFriendRequests() throws SQLException {
        PreparedStatement st=_conn.prepareStatement("select * from showfriendrequests(?);");
        st.setInt(1,currentUserId);
        ResultSet rs=st.executeQuery();
        String col1=rs.getMetaData().getColumnName(1);
        System.out.println("col1: "+col1);
        while(rs.next())
        {

        }
        return new ArrayList();
    }

    public List showGroupRequests() throws SQLException {
        PreparedStatement st=_conn.prepareStatement("select * from showGrouprequests(?);");
        st.setInt(1,currentUserId);
        ResultSet rs=st.executeQuery();
        String col1=rs.getMetaData().getColumnName(1);
        System.out.println("col1: "+col1);
        while(rs.next())
        {

        }
        return new ArrayList();
    }



}
