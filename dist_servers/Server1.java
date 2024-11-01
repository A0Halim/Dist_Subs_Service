import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1 {
    private static final int PORT = 5001; // Server1 icin port numarasi
    private static final String HOST = "localhost";
    
    
    private static final int PORT2 = 5002; // Server2'nin portu
    private static final int PORT3 = 5003; // Server3'un portu

    public static void main(String[] args) {
        sunucuBaslat();
        digerSunucularaBaglan();
    }

    // Sunucu dinleme islemini thread ile yapiyoruz
    private static void sunucuBaslat() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server1 dinliyor! Port: " + PORT);
                
                // Surekli baglanti gelene kadar dinle
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Bir baglanti kuruldu: " + clientSocket.getRemoteSocketAddress());
                    // istemci icin idareci olarak thread olusturuyoruz
                    new Thread(() -> handleClient(clientSocket)).start();
                }
            } 
            catch (IOException e) {
                System.err.println("Server1, dinlerken hata olustu: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    // Diger sunuculara baglanma islemi
    private static void digerSunucularaBaglan() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                sunucuyaBaglan(HOST, PORT2); // server1 --> server2
                sunucuyaBaglan(HOST, PORT3); // server1 --> server3
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.err.println("Thread durduruldu: " + ie.getMessage());
            } catch (IOException e) {
                System.err.println("Baglanilamadi! : " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    // Belirli bir sunucuya baglanma islemi
    private static void sunucuyaBaglan(String host, int port) throws IOException {
        boolean connected = false;
        int attempts = 0;
        
        int deneme_sayisi = 100;
        // diger sunuculara baglanilabiliyor mu? (aktifler mi) degillerse 10 kere baglanmayi dene
        while (!connected && attempts < deneme_sayisi) {
            attempts++;
            try {
                System.out.println("Baglanmaya calisiliyor: " + host + " Port: " + port);
                try (Socket connection = new Socket(host, port)) {
                    System.out.println("Server1 ve Server" + (port - 5001 + 1) + " baglantisi kuruldu.");
                    connected = true;
                    // baglandiktan sonra yapilacaklar buraya eklenecek !
                    // (port - 5001 + 1) dinamik olarak server bilgisi vericek
                    // ornek Server3 e baglansın 5003 - 5001 + 1 = 3
                }
            } catch (IOException e) {
                System.err.println("Baglanilamadi! Port: " + port + ". Yeniden deniyor... (" + attempts + ")");
                try {
                    Thread.sleep(1000); // 1 saniye bekle
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Baglanti denemesi kesildi.", ie);
                }
            }
        }
        
        if (!connected) {
            throw new IOException("Server baska bir servere baglanamadi.");
        }
    }

    // clientin islerini yapan yer
    private static void handleClient(Socket clientSocket) {
        try {
            // Burada istemciden gelen abone bilgilerini okuyup yazma islerini yapicaz 
            // Örnegin, BufferedReader (veri okuma) ve PrintWriter (veri yazma)
            System.out.println("istemci ile islem yapiliyor: " + clientSocket.getRemoteSocketAddress());
        } 
        catch (Exception e) {
            System.err.println("istemci ile islem yapilirken hata olustu: " + e.getMessage());
        } 
        finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Socket kapatilamadi: " + e.getMessage());
            }
        }
    }
}
