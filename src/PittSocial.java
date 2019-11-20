import java.io.IOException;
import java.sql.Date;
import java.util.*;
import java.sql.*;
import java.util.concurrent.Callable;

public class PittSocial {
    private Connection _conn;
    private int currentUserId;
    private Timestamp loginTime;

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
          int resID=rs.getInt(1);

          System.out.println(st);

          if (resID==0) //match not found
              return -1;
          else {
              this.currentUserId = resID;
              this.loginTime=rs.getTimestamp(2);
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
        System.out.println(st);
        List<Triplet<Integer,String,Object>> res= new ArrayList<>();
        while(rs.next())
            res.add(new Triplet<>(rs.getInt(1),rs.getString(2),null));

        return res;
    }

    public List showGroupRequests() throws SQLException {
        PreparedStatement st=_conn.prepareStatement("select * from showGrouprequests(?);");
        st.setInt(1,currentUserId);
        ResultSet rs=st.executeQuery();
        List<Triplet<Integer,Integer,String>> res= new ArrayList<>();
        while(rs.next())
            res.add(new Triplet<>(rs.getInt(1),rs.getInt(2),rs.getString(3)));

        return res;
    }

    public int resolveFriendRequest(int fromID,boolean confirm) throws SQLException {
        CallableStatement st=_conn.prepareCall("call resolveFriendRequest(?,?,?);");
        st.setInt(1,currentUserId);
        st.setInt(2,fromID);
        st.setBoolean(3,confirm);
        st.execute();
        System.out.println(st);
        return 0;
    }

    public int resolveGroupRequest(int gid, int fromID,boolean confirm) throws SQLException {
        CallableStatement st=_conn.prepareCall("call resolveGroupMemberRequest(?,?,?,?);");
        st.setInt(1,currentUserId);
        st.setInt(2,gid);
        st.setInt(3,fromID);
        st.setBoolean(4,confirm);
        st.execute();
        System.out.println(st);
        return 0;
    }

    public boolean sendMessageToUser(int toID, String msg) throws SQLException {

        PreparedStatement st=_conn.prepareStatement("select * from createMessage(?,?,?,?);");
        st.setInt(1,currentUserId);
        st.setInt(2,toID);
        st.setString(3,msg);
        st.setBoolean(4,false);
        ResultSet rs=st.executeQuery();
        System.out.println(st);
        rs.next();
        return rs.getBoolean(1);

    }

    public boolean sendMessageToGroup(int toGID,String msg) throws SQLException {
        PreparedStatement st=_conn.prepareStatement("select * from createMessage(?,?,?,?);");
        st.setInt(1,currentUserId);
        st.setInt(2,toGID);
        st.setString(3,msg);
        st.setBoolean(4,true);
        ResultSet rs=st.executeQuery();
        System.out.println(st);
        rs.next();
        return rs.getBoolean(1);
    }

    public String displayMessages() throws SQLException {
        PreparedStatement st=_conn.prepareStatement("select * from displayMessages(?)");
        st.setInt(1,currentUserId);
        ResultSet rs=st.executeQuery();
        System.out.println(st);
        String res="";
        int firstWidth=10;
        int secondWidth=50;
        int thirdWidth=30;

        String format="%1$"+firstWidth+"s | %2$"+secondWidth+"s | %3$"+thirdWidth+"s";
        String head=String.format(format,"Sender","Content","Time Sent")+'\n';
        String head2="";
        for (int i=0;i<firstWidth+secondWidth+thirdWidth+6;i++) head2+='-';
        head2=head2+'\n';

        res+=head;
        res+=head2;
        while(rs.next())
            res+=String.format(format,rs.getString(1), rs.getString(2),rs.getTimestamp(3))+'\n';

        return res;
    }

    public String displayNewMessages() throws SQLException {
        PreparedStatement st=_conn.prepareStatement("select * from displayNewMessages(?)");
        st.setInt(1,currentUserId);
        ResultSet rs=st.executeQuery();
        System.out.println(st);
        String res="";
        int firstWidth=10;
        int secondWidth=50;
        int thirdWidth=30;

        String format="%1$"+firstWidth+"s | %2$"+secondWidth+"s | %3$"+thirdWidth+"s";
        String head=String.format(format,"Sender","Content","Time Sent")+'\n';
        String head2="";
        for (int i=0;i<firstWidth+secondWidth+thirdWidth+6;i++) head2+='-';
        head2+='\n';

        res+=head;
        res+=head2;
        while(rs.next())
            res+=String.format(format,rs.getString(1), rs.getString(2),rs.getTimestamp(3))+'\n';
        return res;
    }

    public String displayFriends() throws SQLException {
        String res="";
        PreparedStatement st=_conn.prepareStatement("select * from returnFriendsList(?);");
        st.setInt(1,currentUserId);
        ResultSet rs=st.executeQuery();
        System.out.println(st);
        int firstWidth=20;
        int secondWidth=10;
        String format="%1$"+firstWidth+"s | %2$"+secondWidth+"s";
        String head=String.format(format,"Name","User ID")+'\n';
        String head2="";
        for (int i=0;i<firstWidth+secondWidth+6;i++) head2+='-';
        head2+='\n';
        res+=head;
        res+=head2;
        while(rs.next())
            res+=String.format(format,rs.getString(1),rs.getInt(2))+'\n';
        return res;

    }

    public String displayProfile(int userID) throws SQLException {
        String res="";
        PreparedStatement st=_conn.prepareStatement("select * from showProfile(?);");
        st.setInt(1,userID);
        ResultSet rs=st.executeQuery();

        System.out.println(st);

        int secondWidth=44; //must be divisible by 4
        int firstWidth=secondWidth/2; //must be even
        String format="| %1$"+firstWidth+"s | %2$"+secondWidth+"s |";
        String TitleFormat="| %1$"+(firstWidth+secondWidth)+"s |";

        String headTop="|";
        String headMid="|";
        String TitlePadding="";
        String Title="FRIEND PROFILE";
        for (int i=0;i<firstWidth+secondWidth+5;i++) {
            if (i==firstWidth+2) {
                headMid += '|';
                headTop+='-';
            }
            else{
                headMid+= '-';
                headTop+='-';
            }
            if(i<(firstWidth+secondWidth+5-Title.length())/2-1)
                TitlePadding+=' ';
        }
        headMid+="|\n";
        headTop+="|\n";

        rs.next();
        res+=headTop;
        res+=String.format(TitleFormat.replace((firstWidth+secondWidth)+"",(firstWidth+secondWidth+3)+""),"")+'\n';
        res+=String.format(TitleFormat,TitlePadding+Title+TitlePadding+' ')+'\n';
        res+=String.format(TitleFormat.replace((firstWidth+secondWidth)+"",(firstWidth+secondWidth+3)+""),"")+'\n';
        res+=headMid;
        res+=String.format(format,"Name",rs.getString(2))+'\n';
        res+=headMid;
        res+=String.format(format,"User ID",rs.getInt(1))+'\n';
        res+=headMid;
        res+=String.format(format,"Email",rs.getString(3))+'\n';
        res+=headMid;
        res+=String.format(format,"Date of Birth",rs.getString(4))+'\n';
        res+=headMid;

        return res;
    }

    public int logout() throws SQLException {
        CallableStatement st=_conn.prepareCall("call logout(?,?)");
        st.setInt(1,currentUserId);
        st.setTimestamp(2,loginTime);
        st.execute();
        System.out.println(st);

        int resID=currentUserId;
        currentUserId=-1;
        return resID;
    }
}
