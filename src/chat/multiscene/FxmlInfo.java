package chat.multiscene;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;

public class FxmlInfo {
    private Socket socket;
    private String resource;
    private SceneFlow sceneFlow;
    private Stage stage;
    private Scene scene;
    private String nick;

    public FxmlInfo(String resource, SceneFlow sceneFlow, Stage stage, Socket socket) {
        this.resource = resource;
        this.sceneFlow = sceneFlow;
        this.stage = stage;
        this.socket = socket;
    }

    public Scene getScene() {
        if(scene == null) {
            scene =new FxmlLoad().load(this);
        }
        return scene;
    }


    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public String getResource() {
        return resource;
    }

    public SceneFlow getSceneFlow() {
        return sceneFlow;
    }

    public Stage getStage() {
        return stage;
    }

    public boolean hasScene() {
        return scene != null;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}