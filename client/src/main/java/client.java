import java.io.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class client {

    public static void main(String[] args) {

        final String hostname = "localhost";
        final int port = 5555;

        try (final Socket socket = new Socket(hostname, port))
        {
            System.err.println("connected to server; type a line to see it echoed, type quit to exit");
            talkToServer(socket);
            System.err.println("server disconnected");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static String formatCommand(Gson gson, String msg){
        OperationData operations = new OperationData();
        operations.setOperationKind(OperationData.CommandType.OP_TYPING);
        operations.setMoveX(0);
        operations.setMoveY(0);
        operations.setInputStr(msg);
        return gson.toJson(operations, OperationData.class);
    }

    private static void talkToServer(final Socket socket) {
        final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        try (
              // get output and input streams for the server connection.
              final OutputStream outToServer = socket.getOutputStream();
//              final InputStream inFromServer = socket.getInputStream();

              // wrap the output stream in a PrintWriter so we can use print()/println() operations
              final PrintWriter writeToServer = new PrintWriter(new OutputStreamWriter(outToServer, StandardCharsets.UTF_8), true);

              // wrap the input stream in a BufferedReader for the readLine() operation
//              final BufferedReader readFromServer = new BufferedReader(new InputStreamReader(inFromServer, StandardCharsets.UTF_8));

              // wrap the standard input (the user's keyboard) in a BufferedReader
              // for the readLine() operation.
              final BufferedReader readFromUser = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

              ) {

            while (true) {
                final String message = readFromUser.readLine();

                if (message == null)
                    break;

                String msg = formatCommand(GSON, message);
                writeToServer.print(msg);
                writeToServer.flush();

                if (message.equals("stop")){
                    break;
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}