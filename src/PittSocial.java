import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;
import java.util.concurrent.Callable;

public class PittSocial {
    private Connection _conn;
    private int currentUserId;
    private Timestamp loginTime;

    public PittSocial(Connection new_conn) throws ClassNotFoundException, SQLException {
        _conn = new_conn;
        this.currentUserId = -1;
    }

    public void createUser(String username, String email, String password, String birthDate) throws SQLException {
        CallableStatement st = _conn.prepareCall("call createuser(?,?,?,?,null);");
        st.setString(1, username);
        st.setString(2, email);
        st.setString(3, password);
        st.setDate(4, Date.valueOf(birthDate));
        st.execute();

        System.out.println(st);
    }

    public int login(String email, String password) throws IOException, SQLException {

        PreparedStatement st = _conn.prepareStatement("select * from login(?,?);");
        st.setString(1, email);
        st.setString(2, password);
        ResultSet rs = st.executeQuery();
        rs.next();
        int resID = rs.getInt(1);

        System.out.println(st);

        if (resID == 0) //match not found
            return -1;
        else {
            this.currentUserId = resID;
            this.loginTime = rs.getTimestamp(2);
            return 0;
        }
    }

    public int initiateFriendship(int toid, String msg) throws SQLException {
        CallableStatement st = _conn.prepareCall("call initiatefriendship(?,?,?);");
        st.setInt(1, currentUserId);
        st.setInt(2, toid);
        st.setString(3, msg);
        st.execute();

        System.out.println(st);

        return 0;
    }

    public int createGroup(String name, int limit, String descr) throws SQLException {
        CallableStatement st = _conn.prepareCall("call creategroup(?,?,?,?);");
        st.setInt(1, currentUserId);
        st.setString(2, name);
        st.setInt(3, limit);
        st.setString(4, descr);
        st.execute();

        System.out.println(st);
        return 0;
    }

    public int initiateAddingGroup(int gid, String msg) throws SQLException {
        CallableStatement st = _conn.prepareCall("call initiateaddinggroup(?,?,?);");
        st.setInt(1, currentUserId);
        st.setInt(2, gid);
        st.setString(3, msg);
        st.execute();

        System.out.println(st);

        return 0;
    }


    public List showFriendRequests() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from showfriendrequests(?);");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();
        System.out.println(st);
        List<Triplet<Integer, String, Object>> res = new ArrayList<>();
        while (rs.next())
            res.add(new Triplet<>(rs.getInt(1), rs.getString(2), null));

        return res;
    }

    public List showGroupRequests() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from showGrouprequests(?);");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();
        List<Triplet<Integer, Integer, String>> res = new ArrayList<>();
        while (rs.next())
            res.add(new Triplet<>(rs.getInt(1), rs.getInt(2), rs.getString(3)));

        return res;
    }

    public int resolveFriendRequest(int fromID, boolean confirm) throws SQLException {
        CallableStatement st = _conn.prepareCall("call resolveFriendRequest(?,?,?);");
        st.setInt(1, currentUserId);
        st.setInt(2, fromID);
        st.setBoolean(3, confirm);
        st.execute();
        System.out.println(st);
        return 0;
    }

    public int resolveGroupRequest(int gid, int fromID, boolean confirm) throws SQLException {
        CallableStatement st = _conn.prepareCall("call resolveGroupMemberRequest(?,?,?,?);");
        st.setInt(1, currentUserId);
        st.setInt(2, gid);
        st.setInt(3, fromID);
        st.setBoolean(4, confirm);
        st.execute();
        System.out.println(st);
        return 0;
    }

    public boolean sendMessageToUser(int toID, String msg) throws SQLException {

        PreparedStatement st = _conn.prepareStatement("select * from createMessage(?,?,?,?);");
        st.setInt(1, currentUserId);
        st.setInt(2, toID);
        st.setString(3, msg);
        st.setBoolean(4, false);
        ResultSet rs = st.executeQuery();
        System.out.println(st);
        rs.next();
        return rs.getBoolean(1);

    }

    public boolean sendMessageToGroup(int toGID, String msg) throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from createMessage(?,?,?,?);");
        st.setInt(1, currentUserId);
        st.setInt(2, toGID);
        st.setString(3, msg);
        st.setBoolean(4, true);
        ResultSet rs = st.executeQuery();
        System.out.println(st);
        rs.next();
        return rs.getBoolean(1);
    }

    public String displayMessages() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from displayMessages(?)");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();
        System.out.println(st);
        return createDisplayMessageBody(rs, 10, 50, 30);
    }

    public String displayNewMessages() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from displayNewMessages(?)");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();
        return createDisplayMessageBody(rs, 10, 50, 30);
    }

    public String displayFriends() throws SQLException {
        String res = "";
        PreparedStatement st = _conn.prepareStatement("select * from returnFriendsList(?);");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();
        System.out.println(st);
        return createDisplayFriendsBody(rs, 20, 10);

    }

    public String displayProfile(int userID) throws SQLException {
        String res = "";
        PreparedStatement st = _conn.prepareStatement("select * from showProfile(?);");
        st.setInt(1, userID);
        ResultSet rs = st.executeQuery();

        System.out.println(st);
        return createDisplayProfileBody(rs, 44, 44 / 2);
    }

    public int logout() throws SQLException {
        CallableStatement st = _conn.prepareCall("call logout(?,?)");
        st.setInt(1, currentUserId);
        st.setTimestamp(2, loginTime);
        st.execute();
        System.out.println(st);

        int resID = currentUserId;
        currentUserId = -1;
        return resID;
    }

    public String searchForUser() {
        return null;
    }

    public String threeDegrees() {
        return null;

    }

    public String topMessages() {
        return null;

    }

    public String dropUser() {
        return null;

    }

    public boolean isLoggedIn() {
        return this.currentUserId != -1;
    }

    private String createDisplayMessageBody(ResultSet rs, int firstWidth, int secondWidth, int thirdWidth) throws SQLException {
        StringBuilder res = new StringBuilder();

        res.append(InfoPrinter.createTitle("Display Message", firstWidth + secondWidth + thirdWidth + 6));

        String format = "|%1$" + firstWidth + "s | %2$" + secondWidth + "s | %3$" + thirdWidth + "s" + " |";
        String head = String.format(format, "Sender", "Content", "Time Sent") + '\n';

        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + thirdWidth + 6) +
                "|\n";
        res.append(head2);
        while (rs.next()) {
            res.append(String.format(format, rs.getString(1), rs.getString(2), rs.getTimestamp(3))).append('\n');
        }
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + thirdWidth + 6)).append("|\n");

        return res.toString();
    }

    private String createDisplayFriendsBody(ResultSet rs, int firstWidth, int secondWidth) throws SQLException {
        StringBuilder res = new StringBuilder();
        res.append(InfoPrinter.createTitle("Display Friends", firstWidth + secondWidth + 4));
        String format = "|%1$" + firstWidth + "s | %2$" + secondWidth + "s" + " |";
        String head = String.format(format, "Name", "User ID") + '\n';
        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + 4) +
                "|\n";
        res.append(head2);
        while (rs.next())
            res.append(String.format(format, rs.getString(1), rs.getInt(2))).append('\n');
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + 4)).append("|\n");
        return res.toString();
    }

    private String createDisplayProfileBody(ResultSet rs, int firstWidth, int secondWidth) throws SQLException {
        StringBuilder res = new StringBuilder();
        String format = "| %1$" + firstWidth + "s | %2$" + secondWidth + "s |";
        String TitleFormat = "| %1$" + (firstWidth + secondWidth) + "s |";

        StringBuilder headTop = new StringBuilder("|");
        StringBuilder headMid = new StringBuilder("|");
        StringBuilder TitlePadding = new StringBuilder();
        String Title = "FRIEND PROFILE";
        for (int i = 0; i < firstWidth + secondWidth + 5; i++) {
            if (i == firstWidth + 2) {
                headMid.append('|');
                headTop.append('-');
            } else {
                headMid.append('-');
                headTop.append('-');
            }
            if (i < (firstWidth + secondWidth + 5 - Title.length()) / 2 - 1)
                TitlePadding.append(' ');
        }
        headMid.append("|\n");
        headTop.append("|\n");

        rs.next();
        res.append(headTop);
        res.append(String.format(TitleFormat.replace((firstWidth + secondWidth) + "", (firstWidth + secondWidth + 3) + ""), "")).append('\n');
        res.append(String.format(TitleFormat, TitlePadding + Title + TitlePadding + ' ')).append('\n');
        res.append(String.format(TitleFormat.replace((firstWidth + secondWidth) + "", (firstWidth + secondWidth + 3) + ""), "")).append('\n');
        res.append(headMid);
        res.append(String.format(format, "Name", rs.getString(2))).append('\n');
        res.append(headMid);
        res.append(String.format(format, "User ID", rs.getInt(1))).append('\n');
        res.append(headMid);
        res.append(String.format(format, "Email", rs.getString(3))).append('\n');
        res.append(headMid);
        res.append(String.format(format, "Date of Birth", rs.getString(4))).append('\n');
        res.append(headMid);
        return res.toString();

    }
}
