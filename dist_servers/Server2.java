package dist_servers;

import java.io.IOException;

public class Server2 {

    public static void main(String[] args) {
        GenerateServer server1 = new GenerateServer(2);
        try {
            server1.startServer();
        } catch (IOException e) {
            System.out.println("sistem başlatilamadi");
            e.printStackTrace();
        }
    }
}