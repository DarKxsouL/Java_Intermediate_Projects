import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int PORT = 12345;
    private static ConcurrentHashMap<String, PrintWriter> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Chat server started on port " + PORT);
        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket).start();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                out = new PrintWriter(socket.getOutputStream(), true);

                // Receive username
                username = in.readLine();
                if (username == null || username.isBlank()) {
                    return;
                }

                if (clients.containsKey(username)) {
                    out.println("Username already taken.");
                    return;
                }

                clients.put(username, out);
                broadcastUserList();
                broadcastMessage("Server", username + " joined the chat.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("@")) {
                        int colon = message.indexOf(":");
                        if (colon > 1) {
                            String target = message.substring(1, colon).trim();
                            String msg = message.substring(colon + 1).trim();
                            PrintWriter recipient = clients.get(target);
                            if (recipient != null) {
                                recipient.println("[Private] " + username + ": " + msg);
                                out.println("[To " + target + "] " + msg);
                            } else {
                                out.println("User '" + target + "' not found.");
                            }
                        }
                    } else {
                        broadcastMessage(username, message);
                    }
                }
            } catch (IOException e) {
                System.out.println(username + " disconnected.");
            } finally {
                if (username != null) {
                    clients.remove(username);
                    broadcastMessage("Server", username + " left the chat.");
                    broadcastUserList();
                }
                if (out != null) {
                    out.close();
                }
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
    }

    private static void broadcastUserList() {
        StringBuilder userList = new StringBuilder("USER_LIST:");
        for (String user : clients.keySet()) {
            userList.append(user).append(",");
        }
        String list = userList.toString();
        for (PrintWriter writer : clients.values()) {
            writer.println(list);
        }
    }

    private static void broadcastMessage(String sender, String message) {
        for (String user : clients.keySet()) {
            clients.get(user).println(sender + ": " + message);
        }
    }
}
