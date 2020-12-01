package chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;

public class AuthenticationDialog extends Application {
    public Socket socket = null;
    FXMLLoader loader;


    @Override
    public void start(Stage primaryStage) throws Exception{
        loader = new FXMLLoader(getClass().getResource("AuthDialog.fxml"));
        Parent root = loader.load();
//        AuthDialog controller = (AuthDialog) loader.getController();
//        try {
//            socket = new Socket("localhost", 8189);
//            controller.socket = this.socket;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene (root));
        primaryStage.show();
//        root = load(getClass().getResource("ChatClient.fxml"));
//        primaryStage.setTitle("Chat Client");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();
    }

//    public static void main(String[] args) throws Exception {
//        Application.launch(AuthenticationDialog.class);
////        Application.launch(Chat.class);
//    }
}
