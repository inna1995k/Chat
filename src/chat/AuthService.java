package chat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    Connection conn = null;

    private class User {
        private String login;
        private String passwd;
        private String nick;

        public User(String login, String passwd, String nick) {
            this.login = login;
            this.passwd = passwd;
            this.nick = nick;
        }
    }

    private List<User> userList;

    public AuthService() {
        userList = new ArrayList<>();
        userList.add(new User("login1", "pass1", "nick1"));
        userList.add(new User("login2", "pass2", "nick2"));
        userList.add(new User("login3", "pass3", "nick3"));

//        try {
//            conn = DriverManager.
//                    getConnection("jdbc:h2:~/test", "sa", "");
//            Statement statement = conn.createStatement();
//            ResultSet rs = statement.executeQuery("SELECT * FROM USERS");
//            while (rs.next()) {
//                String login = rs.getString("LOGIN");
//                String passwd = rs.getString("PASSWORD");
//                String nick = rs.getString("NICK");
//                userList.add(new User(login, passwd, nick));
//            }
//            rs.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

//        );


    }

    public void start() {
        System.out.println("Authentication service started");
    }

    public void stop() {
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Authentication service stopped");
    }

    public String getNickByLoginAndPwd(String login, String passwd) {
        for(User user: userList) {
            if (user.login.equals(login) && user.passwd.equals(passwd)) {
                return user.nick;
            }
        }
        return null;
    }
}
