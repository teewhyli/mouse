import java.net.*;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.io.*;

public class server {

    public void start(int port) {
        ExecutorService es = Executors.newCachedThreadPool();
        try (final ServerSocket serverSocket = new ServerSocket(port)) {
            while(true) {
                // perhaps return a Future here to check if server should shutdown?
                Future<OnConnectionChangeListener> f = es.submit(new Controller(serverSocket.accept(), new Instructions()));
                OnConnectionChangeListener result = f.get();
                result.disconnected(es);
                break;
            }
        } catch (IOException | ExecutionException | InterruptedException e) {
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
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                while(true) {
                    char[] buf = new char[1024 * 2]; // 2MB
                    int len = in.read(buf, 0, buf.length);
                    sb.append(new String(buf, 0, len));
                    String instruct = this.instructions.processCommand(sb);
                    if (instruct != null && instruct.equals("stop")){
                        break;
                    }
                    System.out.println(instruct);
                    sb.setLength(0);
                }
            } catch (IOException e) {
                Logger.getLogger(e.getMessage());
            }
            return new TEST();
        }
    }

    public static void main(String[] args) {
        server server = new server();
        server.start(5555);
    }

}