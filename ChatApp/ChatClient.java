import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ChatClient extends Application {
    private PrintWriter out;
    private String userName;

    private Map<String, TextArea> privateChats = new HashMap<>();
    private TabPane tabPane;
    private TextArea generalArea;
    private ListView<String> userListView;
    private TextField inputField;

    @Override
    public void start(Stage primaryStage) {
        // Prompt for username
        TextInputDialog dialog = new TextInputDialog("User");
        dialog.setHeaderText("Enter your username:");
        Optional<String> result = dialog.showAndWait();
        userName = result.orElse("User");

        // UI Elements
        generalArea = new TextArea();
        generalArea.setEditable(false);

        VBox generalBox = new VBox(generalArea);
        Tab generalTab = new Tab("General", generalBox);

        tabPane = new TabPane(generalTab);
        userListView = new ListView<>();
        userListView.setPrefWidth(150);

        inputField = new TextField();
        Button sendButton = new Button("Send");

        // Layout
        SplitPane splitPane = new SplitPane(userListView, tabPane);
        splitPane.setDividerPositions(0.2);
        VBox root = new VBox(splitPane, new HBox(10, inputField, sendButton));

        // Input Handling
        sendButton.setOnAction(e -> sendMessage());
        inputField.setOnAction(e -> sendButton.fire());
        userListView.setOnMouseClicked(e -> {
            String selectedUser = userListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                inputField.setText("@" + selectedUser + ": ");
                inputField.requestFocus();
                inputField.positionCaret(inputField.getText().length());
            }
        });

        // Scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Chat - " + userName);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Connect to server
        new Thread(this::runClient).start();
    }

    private void runClient() {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(userName); // Send username

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                String finalMessage = serverMessage;
                Platform.runLater(() -> handleServerMessage(finalMessage));
            }
        } catch (IOException e) {
            Platform.runLater(() -> generalArea.appendText("Connection to server lost: " + e.getMessage() + "\n"));
        }
    }

    private void handleServerMessage(String message) {
        if (message.startsWith("USER_LIST:")) {
            String[] users = message.substring(10).split(",");
            userListView.getItems().setAll(Arrays.stream(users)
                    .filter(u -> !u.equals(userName) && !u.isBlank())
                    .toList());
        } else if (message.startsWith("[Private]")) {
            String sender = message.split(" ")[1].replace(":", "");
            openPrivateTab(sender);
            privateChats.get(sender).appendText(message + "\n");
        } else if (message.startsWith("[To ")) {
            String target = message.split(" ")[1].replace("]", "");
            openPrivateTab(target);
            privateChats.get(target).appendText(message + "\n✓ Delivered\n");
        } else {
            generalArea.appendText(message + "\n");
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            inputField.clear();
        }
    }

    private void openPrivateTab(String user) {
        if (!privateChats.containsKey(user)) {
            TextArea area = new TextArea();
            area.setEditable(false);
            privateChats.put(user, area);
            VBox box = new VBox(area);
            Tab tab = new Tab(user, box);
            tabPane.getTabs().add(tab);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
