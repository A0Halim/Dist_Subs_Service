import java.io.*;
import java.net.*;

public class Server3 {
    private static final int PORT = 5003; // Server3'un portu

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server1 is running on port " + PORT);

        new PingThread("localhost", 5001).start(); // server1'i pingler
        new PingThread("localhost", 5002).start(); // server2'yi pingler 


        // Gelen client baglantilarini surekli dinler 
        try {
            while (true) {
                // eger client gelirse yeni bir thread olusturur
                new ClientHandler(serverSocket.accept()).start();
            }
        } finally {
            serverSocket.close();
        }
    }

    // Her yeni client icin bir thread olusturan sinif 
    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        // socket parametresi sunucuya baglanan client ile iletisimi saglar
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            // buffer --> clientten gelen mesaj alinir
            BufferedReader in = null;
            String message = null;
            // PrintWritter ile istemciye yanit gonderilir 
            PrintWriter out = null;
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                message = in.readLine();

                // cliente geri mesaj dondurur
                out.println("55 TAMM");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Received message on Server3("+currentThread().getName() +") from client: " + message);

        }
    }

    // Bu sınıf, belirtilen sunucuyu belirli aralıklarla ping yapar ve sunucunun erişilebilir olup olmadığını kontrol eder
    private static class PingThread extends Thread {
        // Pinglenecek sunucunun bilgileri
        private String host;
        private int port;

        public PingThread(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public void run() {

            try {
                while (true) {
                    // Sonsuz bir döngüde Socket ile belirtilen host ve porta bağlanmayı dener.
                    try (Socket socket = new Socket(host, port)) {
                        System.out.println("Pinged " + host + " on port " + port);
                    } catch (IOException e) {
                        System.out.println("Ping to " + host + " on port " + port + " failed, retrying...");
                    }

                    try {
                        // her baglanmayi denedikten sonra 10 saniye bekler
                        Thread.sleep(10000);
                    } catch (InterruptedException ie) {
                        System.out.println("Ping thread interrupted: " + ie.getMessage());
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }


        }
    }
    public static class AdminHandler extends Thread {

    }

    public static class DistributedServerHandler extends Thread {

    }
}
