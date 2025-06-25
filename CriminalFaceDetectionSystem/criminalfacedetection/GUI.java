package criminalfacedetection;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class GUI {
    private ImageView cameraView;
    private TextArea infoArea;

    public BorderPane createUI() {
        cameraView = new ImageView();
        infoArea = new TextArea();
        infoArea.setEditable(false);

        Button startButton = new Button("Start Detection");
        startButton.setOnAction(e -> FaceDetector.startCamera(cameraView, infoArea));

        VBox controls = new VBox(10, startButton);
        BorderPane root = new BorderPane();
        root.setCenter(cameraView);
        root.setRight(infoArea);
        root.setBottom(controls);
        return root;
    }
}