import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class WebServerNoComments {

    public static void main(String[] args) throws IOException {

        ServerSocket servidor = new ServerSocket(8080);

      //  WebServerNoComments webServerNoComments = new WebServerNoComments();

       while (true) {
            Socket socket = servidor.accept();
            Thread thread = new Thread(new ThreadServer(socket));
            System.out.println(thread.getName());
            thread.start();
       }
    }
}