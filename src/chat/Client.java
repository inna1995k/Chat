package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void setAuthorized(boolean authorized) {
        Client.authorized = authorized;
    }


    static boolean authorized;

    public static void main(String[] args) throws IOException {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 8189);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("/auth login1 pass1");
            setAuthorized(false);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            if(in.available()>0) {
                                String strFromServer = in.readUTF();
                                if (strFromServer.startsWith("/authOk")) {
                                    setAuthorized(true);
                                    System.out.println("Authorized on server");
                                    Client.runOutputThread(out);
                                    break;
                                }
                                System.out.println(strFromServer + "\n");
                            }
                        }
                        while (true) {
                            if (in.available()>0) {
                                String strFromServer = in.readUTF();
                                if (strFromServer.equalsIgnoreCase("/end")) {
                                    break;
                                }
                                System.out.println(strFromServer);
                                System.out.println("\n");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            t.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private static Thread runOutputThread(DataOutputStream out) {
        Thread thread = new Thread(()-> {
            while (!Thread.currentThread().isInterrupted()) {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String message = scanner.nextLine();
                    try {
                        out.writeUTF(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (message.equals("/end")) {
                        break;
                    }
                }
            }
        });
        thread.start();
        return thread;
    }
}
