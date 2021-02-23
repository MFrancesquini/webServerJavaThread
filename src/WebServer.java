import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class WebServer {

    public static void main(String[] args) throws IOException {

        /* cria um socket "servidor" associado a porta 8000 e fica aguardando conexões
        */


        ServerSocket servidor = new ServerSocket(8080);
        //aceita a primeira conexao que vier
        //while (servidor.isBound()) {
            Socket socket = servidor.accept();

        /*verifica se esta conectado TESTE OK
        if (socket.isConnected()) {
            //imprime na tela o IP do cliente
            System.out.println("The computer "+ socket.getInetAddress() + " is connected to the server.");
        }*/


            //cria um BufferedReader a partir do InputStream do cliente, no caso a requisicao que o Alex explicou que ele manda
            BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Fiz um teste daqui pra baixo, lendo cada linha do buffer, a fim de ver o que me retornava... vieran is dados da requisicao, o REQUEST HEADER


        /* Lê a primeira line
             contem as informaçoes da requisição
             */
            String line = buffer.readLine();
            //quebra a string pelo espaço em branco

            String[] reqData = line.split(" ");

            //pega o metodo
            String method = reqData[0];
            //System.out.println(method); GET

            //paga o caminho do archive
            String filePath = reqData[1];
            // System.out.println(filePath); /

            //pega o protocolo
            String protocol = reqData[2];
            //System.out.println(protocol); http/1.1

            //Enquanto a line não for vazia

            while (!line.isEmpty()) {

                //imprime a line
                System.out.println(line);

                //lê a proxima line
                line = buffer.readLine();
            }

            //se o caminho foi igual a / entao deve pegar o /index.html

            if (filePath.equals("/")) {
                filePath = "index.html";
            }

            //abre o arquivo pelo caminho
            File archive = new File(filePath);
            byte[] content;

            //status de sucesso - HTTP/1.1 200 OK
            String status = protocol + " 200 OK\r\n";

            //se o archive não existe então abrimos o archive de erro, e mudamos o status para 404
            if (!archive.exists()) {
                status = protocol + " 404 Not Found\r\n";
                archive = new File("404.html");
            }

            content = Files.readAllBytes(archive.toPath());

            //cria um formato para o GMT espeficicado pelo HTTP

            String header = status = "HTTP/1.0 200 Document Follows\r\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n" +
                    "Content-Length: <file_byte_size> \r\n" +
                    "\r\n";

            //cria o canal de resposta utilizando o outputStream
            OutputStream response = socket.getOutputStream();
            //escreve o headers em bytes
            response.write(header.getBytes());
            //escreve o conteudo em bytes
            response.write(content);
            //encerra a resposta
            response.flush();
        }

  //  }
}