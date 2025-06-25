package criminalfacedetection;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.control.TextArea;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import java.io.ByteArrayInputStream;
import java.util.List;

public class FaceDetector {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static boolean running = true;

    public static void startCamera(ImageView view, TextArea infoArea) {
        new Thread(() -> {
            VideoCapture capture = new VideoCapture(0);
            CascadeClassifier faceCascade = new CascadeClassifier("resources/haarcascade_frontalface_alt.xml");
            Mat frame = new Mat();

            while (running && capture.read(frame)) {
                MatOfRect faces = new MatOfRect();
                faceCascade.detectMultiScale(frame, faces);
                for (Rect face : faces.toArray()) {
                    Imgproc.rectangle(frame, face, new Scalar(0, 255, 0));
                    String match = FaceRecognizer.matchFace(frame.submat(face));
                    Platform.runLater(() -> infoArea.setText("Match: " + match));
                }

                Image imageToShow = Utils.mat2Image(frame);
                Platform.runLater(() -> view.setImage(imageToShow));
            }
            capture.release();
        }).start();
    }
}