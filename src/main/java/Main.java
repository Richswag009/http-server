import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {

        try{
            ServerSocket serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);
            Socket socket = serverSocket.accept(); // Wait for connection from client.
            System.out.println("accepted new connection");
            testServer(socket);
        } catch (IOException e) {
            System.out.println("Server exception" + e.getMessage());
        }
    }

     public static void testServer(Socket socket) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        while(true){
            String line = bufferedReader.readLine();
            System.out.println("received line: " + line);
            if(line.equals("exit")){
                break;
            }
            printWriter.println("Echo from server " + line);
        }
         socket.getOutputStream().flush();

    }
}
