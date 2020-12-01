package simplechat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private MyServer myServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;

    public String getName() {
        return name;
    }

    public ClientHandler(MyServer myServer, Socket socket) {
        this.myServer = myServer;
        this.socket = socket;
        this.name = "";//пустое имя
        try {
            this.in = new DataInputStream(socket.getInputStream());//потоки(чтобы различать)
            this.out = new DataOutputStream(socket.getOutputStream());//потоки
            new Thread(()-> {//поток клиентский
                try {
                    authenticate();//будет идентифицироваться
                    readMessages();//и читать сообщение
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException ex) {
            throw new RuntimeException("Client creation error");
        }
    }

    private void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();//клиентский сокет
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        myServer.unsubscribe(this);//отписаться от сервиса что б он перестал посылать сообщения
        myServer.broadcast("User " + name + "left");//выходим из чата
    }

    private void readMessages() throws IOException {
        while (true) {
            if (in.available()>0) {
                String message = in.readUTF();
                System.out.println("From " + name + ":" + message);
                if (message.equals("/end")) {
                    return;
                }
                myServer.broadcast(name + ": " + message);//транслируется остальным клиентам
            }
        }
    }

    private void authenticate() throws IOException {
        while(true) {
            if (in.available()>0){
                String str = in.readUTF();//пытаемся прочитать сообщение которое приходит с сервиса
                if (str.startsWith("/auth")) {//должны начинать с авторизационного сообщения
                    String[] parts = str.split("\\s");//разбить по бэкспейсам сообщения
                    String nick = myServer.getAuthService().getNickByLoginAndPwd(parts[1], parts[2]);
                    if (nick != null) {
                        if (!myServer.isNickLogged(nick)) {//проверка что пользователь в сиситеме не зарегистрирован
                            System.out.println(nick + " logged into chat");
                            name = nick;
                            sendMsg("/authOk " + nick);//успешно авторизовались
                            myServer.broadcast(nick + " is in chat");//оповестить всех остальных подписчиков(broadcast широковещательные передачи)
                            myServer.subscribe(this);//подписаться
                            return;
                        } else {
                            System.out.println("User " + nick + " tried to re-enter");
                            sendMsg("User already logged in");
                        }
                    } else {
                        System.out.println("Wrong login/password");
                        sendMsg("Incorrect login attempted");
                    }
                }
            }

        }
    }

    public void sendMsg(String s) {
        try {
            out.writeUTF(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
