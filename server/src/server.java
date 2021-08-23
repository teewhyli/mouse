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
    MouseControl p = new MouseControl();

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

    private class Controller implements Callable<OnConnectionChangeListener> {
        private final Socket clientSocket;
        private final ControlUtility instructionController;
        StringBuilder sb = new StringBuilder();

        public Controller(
                Socket socket,
                ControlUtility instructionController) {
            this.clientSocket = socket;
            this.instructionController = instructionController;
        }

        @Override
        public OnConnectionChangeListener call() {
            System.out.println("connected!");
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String line;
                while((line = in.readLine()) != null ) {
                    sb.append(line);
//                    System.out.println(sb);
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

    public Instructions processCommand(StringBuilder inputLine) {
        String input = inputLine.toString();
        return GSON.fromJson(input, Instructions.class);
    }
}