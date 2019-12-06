//import javafx.util.Pair;

import javafx.util.Pair;

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
        if (rs.next()) {
            int resID = rs.getInt(1);
            if (resID == 0) //match not found
                return -1;
            else {
                this.currentUserId = resID;
                this.loginTime = rs.getTimestamp(2);
                return 0;
            }
        }
        System.out.println(st);
        return -1;
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


    public String showFriendRequests() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from showfriendrequests(?);");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();
        System.out.println(st);

        return createDisplayFriendRequestBody(rs, 10, 50);
    }

    public ArrayList<Integer> getFriendRequests() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from showfriendrequests(?);");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();
        System.out.println(st);
        ArrayList<Integer> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rs.getInt(1));
        }
        return result;
    }

    public String showGroupRequests() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from showGrouprequests(?);");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();

        return createDisplayGroupRequestBody(rs, 10, 10, 50);
    }

    public ArrayList<Pair<Integer, Integer>> getGroupRequests() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from showGrouprequests(?);");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();
        System.out.println(st);
        ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();
        while (rs.next()) {
            result.add(new Pair<>(rs.getInt(1), rs.getInt(2)));
        }
        return result;
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

    public boolean resolveGroupRequest(int gid, int fromID, boolean confirm) throws SQLException {
        CallableStatement st = _conn.prepareCall("select * from resolveGroupMemberRequest(?,?,?,?);");
        st.setInt(1, currentUserId);
        st.setInt(2, gid);
        st.setInt(3, fromID);
        st.setBoolean(4, confirm);
        ResultSet rs = st.executeQuery();
        System.out.println(st);
        rs.next();
        return rs.getBoolean(1);
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
        PreparedStatement st = _conn.prepareStatement("select * from returnFriendsList(?);");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();
        System.out.println(st);
        return createDisplayFriendsBody(rs, 20, 10);

    }

    public String displayProfile(int userID) throws SQLException {
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

    public String searchForUser(String keyword) throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from searchForUser(?)");
        st.setString(1, "%" + keyword + "%");
        ResultSet rs = st.executeQuery();
        System.out.println(st);
        return createDisplaySearchUserBody(rs, 20, 20, 20);
    }

    public String threeDegrees(int targetUserId) throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from ThreeDegree(?, ?)");
        st.setInt(1, this.currentUserId);
        st.setInt(2, targetUserId);
        ResultSet rs = st.executeQuery();
        System.out.println(st);
        return createDisplayThreeDegreesBody(rs, 50);
    }

    public String displayTopKMessages(int k) throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from topMessages(?, ?, ?)");
        st.setInt(1, currentUserId);
        st.setInt(2, k);
        Date currentDate = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.MONTH, -6);
        st.setTimestamp(3, new Timestamp(c.getTimeInMillis()));
        ResultSet rs = st.executeQuery();
        System.out.println(st);
        return createDisplayTopMessageBody(rs, 20, 20);
    }

    public int dropUser() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("call dropuser(?)");
        st.setInt(1, this.currentUserId);
        this.logout();
        st.execute();
        return 0;
    }

    public boolean isLoggedIn() {
        return this.currentUserId != -1;
    }

    public String getUserNameFromId(int userId) throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from returnUserName(?)");
        st.setInt(1, userId);
        ResultSet rs = st.executeQuery();
        System.out.println(st);
        rs.next();
        return rs.getString(1);
    }

    private String createDisplayMessageBody(ResultSet rs, int firstWidth, int secondWidth, int thirdWidth) throws SQLException {
        StringBuilder res = new StringBuilder();

        res.append(InfoPrinter.createTitle("Display Message", firstWidth + secondWidth + thirdWidth + 7));

        String format = "|%1$" + firstWidth + "s | %2$" + secondWidth + "s | %3$" + thirdWidth + "s" + " |";
        String head = String.format(format, "Sender", "Content", "Time Sent") + '\n';

        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + thirdWidth + 7) +
                "|\n";
        res.append(head2);
        while (rs.next()) {
            res.append(String.format(format, rs.getString(1), rs.getString(2), rs.getTimestamp(3))).append('\n');
        }
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + thirdWidth + 7)).append("|\n");

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

    private String createDisplayFriendRequestBody(ResultSet rs, int firstWidth, int secondWidth) throws SQLException {
        StringBuilder res = new StringBuilder();
        res.append(InfoPrinter.createTitle("Friends Requests", firstWidth + secondWidth + 4));
        String format = "|%1$" + firstWidth + "s | %2$" + secondWidth + "s" + " |";
        String head = String.format(format, "User Id", "Message") + '\n';

        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + 4) +
                "|\n";
        res.append(head2);
        while (rs.next())
            res.append(String.format(format, rs.getInt(1), rs.getString(2))).append('\n');
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + 4)).append("|\n");

        return res.toString();
    }

    private String createDisplayGroupRequestBody(ResultSet rs, int firstWidth, int secondWidth, int thirdWidth) throws SQLException {
        StringBuilder res = new StringBuilder();
        res.append(InfoPrinter.createTitle("Group Requests", firstWidth + secondWidth + thirdWidth + 7));
        String format = "|%1$" + firstWidth + "s | %2$" + secondWidth + "s | %3$" + thirdWidth + "s" + " |";
        String head = String.format(format, "Group Id", "User Id", "Message") + '\n';

        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + thirdWidth + 7) +
                "|\n";
        res.append(head2);
        while (rs.next()) {
            res.append(String.format(format, rs.getInt(1), rs.getInt(2), rs.getString(3))).append('\n');
        }
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + thirdWidth + 7)).append("|\n");

        return res.toString();
    }

    private String createDisplaySearchUserBody(ResultSet rs, int firstWidth, int secondWidth, int thirdWidth) throws SQLException {
        StringBuilder res = new StringBuilder();
        res.append(InfoPrinter.createTitle("Search for User", firstWidth + secondWidth + thirdWidth + 7));
        String format = "|%1$" + firstWidth + "s | %2$" + secondWidth + "s | %3$" + thirdWidth + "s" + " |";
        String head = String.format(format, "User Id", "Username", "Email") + '\n';

        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + thirdWidth + 7) +
                "|\n";
        res.append(head2);
        while (rs.next()) {
            res.append(String.format(format, rs.getInt(1), rs.getString(2), rs.getString(3))).append('\n');
        }
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + thirdWidth + 7)).append("|\n");

        return res.toString();
    }

    private String createDisplayThreeDegreesBody(ResultSet rs, int firstWidth) throws SQLException {
        StringBuilder res = new StringBuilder();
        res.append(InfoPrinter.createTitle("Three Degrees", firstWidth + 1));
        String format = "|%1$" + firstWidth + "s |";
        String head = String.format(format, "Path") + '\n';

        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + 1) +
                "|\n";
        res.append(head2);
        while (rs.next()) {
            String result = rs.getString(1);
            if (result.equals("-1")) {
                result = "No Such Path";
            }
            res.append(String.format(format, result)).append('\n');
        }
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + 1)).append("|\n");

        return res.toString();
    }

    private String createDisplayTopMessageBody(ResultSet rs, int firstWidth, int secondWidth) throws SQLException {
        StringBuilder res = new StringBuilder();
        res.append(InfoPrinter.createTitle("Top Message Users", firstWidth + secondWidth + 4));
        String format = "|%1$" + firstWidth + "s |";
        String head = String.format(format, "User Id", "User Name") + '\n';

        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + 4) +
                "|\n";
        res.append(head2);
        while (rs.next()) {
            int _userId = rs.getInt(1);
            res.append(String.format(format, _userId, this.getUserNameFromId(_userId))).append('\n');
        }
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + 4)).append("|\n");

        return res.toString();
    }
}
