package criminalfacedetection;

public class Main {
    public static void main(String[] args) {
        FaceDetectionApp.launch(FaceDetectionApp.class);
    }
}

// === File: FaceDetectionApp.java ===
package criminalfacedetection;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FaceDetectionApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        GUI gui = new GUI();
        Scene scene = new Scene(gui.createUI(), 800, 600);
        primaryStage.setTitle("Criminal Face Detection System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}