package chat;

import chat.multiscene.ChatSceneApp;
import chat.multiscene.SceneFlow;
import chat.multiscene.Stageable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatController implements Stageable {
    private Stage stage;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;


    @FXML
    TextArea messageArea;

    @FXML
    TextField newMessage;

    @FXML
    Button sendButton;

    @FXML
    ListView nickList;


    public void initialize() throws IOException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (in.available()>0) {
                            String strFromServer = in.readUTF();
                            System.out.println("From server: " + strFromServer);
                            if (strFromServer.equalsIgnoreCase("/end")) {
                                break;
                            }
                            Platform.runLater(()->{ messageArea.appendText(strFromServer + System.lineSeparator());});

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        ObservableList<String> nickListItems = FXCollections.observableArrayList();
        nickListItems.add("All");
        socket = ChatSceneApp.getScenes().get(SceneFlow.CHAT).getSocket();
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        String myNick = ChatSceneApp.getScenes().get(SceneFlow.CHAT).getNick();
        while (true) {
            if(in.available()>0) {
                String strFromServer = in.readUTF();
                if (strFromServer.startsWith("/clients")) {
                    String[] parts = strFromServer.split("\\s");
                    for(int i=1; i<parts.length; i++) {
                        if (!parts[i].equals(myNick)) nickListItems.add(parts[i]);
                    }
                    System.out.println("Authorized on server");
                    break;
                }
            }
        }
        nickList.setItems(nickListItems);
        nickList.getSelectionModel().select(0);
    }

    public void sendMessageTypeAction(ActionEvent actionEvent) {
        int selectedIndex = (Integer) nickList.getSelectionModel().getSelectedIndices().get(0);
        String messageText = newMessage.getText().trim();
        if(!messageText.isEmpty()) {
            messageArea.appendText(messageText + System.lineSeparator());
            if(selectedIndex!=0) {
                messageText = "/w " + nickList.getSelectionModel().getSelectedItems().get(0) + " " +messageText;
                System.out.println("message sent: " + messageText);
            }
            try {
                out.writeUTF(messageText);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            messageArea.appendText(messageText+System.lineSeparator());
            newMessage.clear();
        }
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}