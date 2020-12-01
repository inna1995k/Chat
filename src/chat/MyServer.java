package chat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {
    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthService authService;
    private long startTime;
    private long timeout;
    private DataOutputStream out;
    private MyServer myServer;

    public AuthService getAuthService() {
        return authService;
    }

    public MyServer() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            authService = new AuthService();
            authService.start();
            clients = new ArrayList<>();
            while (true) {
                System.out.println("Server awaits clients");
                Socket socket = server.accept();


//                    if (myServer) {//myServer.timeout
//                    } else {
//                        socket.close();
//                    }

                System.out.println("Client connected");
                new ClientHandler(this, socket);
            }
        } catch (IOException ex) {
            System.out.println("Server error");
        } finally {
            if(authService!=null) {
                authService.stop();
            }
        }
    }


    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientsList();
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientsList();
    }

    public synchronized void broadcast(String s) {
        for(ClientHandler client: clients) {
            client.sendMsg(s);
        }
    }

    public synchronized void broadcastClientsList() {
        StringBuilder sb = new StringBuilder("/clients ");
        for (ClientHandler o : clients) {
            sb.append(o.getName() + " ");
        }
        broadcast(sb.toString());
    }

    public synchronized boolean isNickLogged(String nick) {
        for(ClientHandler client: clients) {
            if (client.getName().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public void sendDirect(String nick, String message) {
        for (ClientHandler client: clients) {
            if (client.getName().equals(nick)) {
                client.sendMsg(message);
                return;
            }
        }
        System.out.println("Unknown nick - message not sent");
    }

//    public boolean timeOut(){
//        Thread timeOut = new Thread(() -> {
//            startTime = System.currentTimeMillis();
//            while(System.currentTimeMillis() - startTime < timeout){
//                try{
//                    Thread.sleep(100);
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//            }
//        });
//        timeOut.setDaemon(true);
//        timeOut.start();
//        return false;
//    }
}
