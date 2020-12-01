package chat.multiscene;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

public class FxmlLoad {
    public Scene load(FxmlInfo info) {
        if(info.hasScene()) {
            return info.getScene();
        }
        URL url = getClass().getResource(info.getResource());
        if (url==null) {
            Platform.exit();
            return null;
        }
        FXMLLoader loader = new FXMLLoader(url);
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Platform.exit();
            return null;
        }
        info.setScene(scene);
        ChatSceneApp.updateScene(info.getSceneFlow(), info);
        Stageable controller = loader.getController();
        if (controller != null) {
            controller.setStage(info.getStage());
        }
        return scene;
    }


}
