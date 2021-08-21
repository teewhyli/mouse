import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class server {

    KeyEventMapping s = new KeyEventMapping();
    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void start(int port) {
        ExecutorService es = Executors.newCachedThreadPool();
        try (final ServerSocket serverSocket = new ServerSocket(port)) {
            while(true) {
                // perhaps return a Future here to check if server should shutdown?
                es.submit(new Controller(serverSocket.accept(), new ControlUtility()));
//                OnConnectionChangeListener result = f.get();
//                result.disconnected(es);
            }
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }
    }

    public interface OnConnectionChangeListener
    {
        void disconnected(ExecutorService es);
    }

    public static class TEST implements OnConnectionChangeListener {

        @Override
        public void disconnected(ExecutorService es) {
            System.out.println("disconnected");
            es.shutdown();
        }
    }

    private static class Controller implements Callable<OnConnectionChangeListener> {
        private final Socket clientSocket;
        private final Instructions instructions;

        public Controller(Socket socket, Instructions instructions) {
            this.clientSocket = socket;
            this.instructions = instructions;
        }

        @Override
        public OnConnectionChangeListener call() {
            System.out.println("connected!");
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                char[] buf = new char[1024 * 2]; // 2MB
                int len;
                while((len = in.read(buf, 0, buf.length)) != -1 ) {
                    sb.append(new String(buf, 0, len));
                    System.out.println(sb);
                    instructionController.processInstructions(processCommand(sb));
                    sb.setLength(0);
                }
            } catch (IOException e) {
                Logger.getLogger(e.getMessage());
            } catch (Exception e) { // need to change to a less generic exception like parsing...
                e.printStackTrace();
            }
            return new TEST();
        }
    }

    public static void main(String[] args) {
        server server = new server();
        server.start(5555);
    }

    public Instructions processCommand(StringBuilder inputLine) throws Exception {
        String input = inputLine.toString();
        Instructions result = GSON.fromJson(input, Instructions.class);
        if (result.getInputStr() == null || result.getInputStr().equals("")) {
            throw new Exception("");
        }
        return result;
    }
}