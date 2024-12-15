package dist_servers;

import java.io.IOException;

public class Server3 {

    public static void main(String[] args) {
        GenerateServer server1 = new GenerateServer(5003);
        try {
            server1.startServer();
        } catch (IOException e) {
            System.out.println("sistem başlatilamadi");
            e.printStackTrace();
        }
    }
}