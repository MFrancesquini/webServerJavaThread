import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class ThreadServer implements Runnable{
    private Socket socket;

    public ThreadServer(Socket socket){
        this.socket = socket;
    }


    @Override
    public void run() {

        if (socket.isConnected()) {
            System.out.println("The computer "+ socket.getInetAddress() + " is connected to the server.");
        }
        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = null;
        try {
            line = buffer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] reqData = line.split(" ");
            String method = reqData[0];
            String filePath = reqData[1];
            String protocol = reqData[2];

            while (!line.isEmpty()) {

                System.out.println(line);
                try {
                    line = buffer.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (filePath.equals("/")) {
                filePath = "index.html";
            }

            File archive = new File(filePath);
            byte[] content = null;

            String status = protocol + " 200 OK\r\n";

            if (!archive.exists()) {
                status = protocol + " 404 Not Found\r\n";
                archive = new File("404.html");
            }

        try {
            content = Files.readAllBytes(archive.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String header = status = "HTTP/1.0 200 Document Follows\r\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n" +
                    "Content-Length: <file_byte_size> \r\n" +
                    "\r\n";

        OutputStream response = null;
        try {
            response = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            response.write(header.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            response.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            response.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
