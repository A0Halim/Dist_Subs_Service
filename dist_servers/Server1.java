package dist_servers;

import java.io.IOException;

public class Server1 {

    public static void main(String[] args) {
        GenerateServer server1 = new GenerateServer(5001);
        try {
            server1.startServer();
        } catch (IOException e) {
            System.out.println("sistem başlatilamadi");
            e.printStackTrace();
        }
    }
}