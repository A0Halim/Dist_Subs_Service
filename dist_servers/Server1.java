package dist_servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.protos.DemandType;
import com.protos.Subscriber;

public class Server1 {
    private static final int PORT = 5001; // Server1 icin port numarasi
    private static final String HOST = "localhost";
    
    private static final int PORT2 = 5002; // Server2'nin portu
    private static final int PORT3 = 5003; // Server3'un portu

    private static Map<Long, Subscriber> cilentsData = new HashMap<Long, Subscriber>();

    public static void main(String[] args) {
        sunucuBaslat();
    }

    // Sunucu dinleme islemini thread ile yapiyoruz
    private static void sunucuBaslat() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server1 baglaniyor! Port: " + PORT);
            // Baglanti başariliysa diger sunuculara baglanir
            digerSunucularaBaglan();
            // İstek gelene kadar dinler
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Bir baglanti kuruldu: " + clientSocket.getRemoteSocketAddress());
                // istemci icin idareci olarak thread olusturuyoruz
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } 
        catch (IOException e) {
            System.err.println("Server1, baglanirken hata olustu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Diger sunuculara baglanma islemi
    private static void digerSunucularaBaglan() {
        sunucuyaBaglan(HOST, PORT2); // server1 --> server2
        sunucuyaBaglan(HOST, PORT3); // server1 --> server3
    }

    /// Belirli bir sunucuya baglanma islemi
    private static void sunucuyaBaglan(String host, int port){
        new Thread(() -> {
            try {
                Socket connection = new Socket(host, port);
                System.out.println("Server1 ve Server" + (port - 5000) + " baglantisi kuruldu.");
                //baglanti sonrsi işlemler
            } catch (UnknownHostException e) {
                System.out.println("Bilinmeyen host." + port + " portu icin baglanma denemesi sonlandi");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println(port + " portu ile baglanti kurulanmadi");
                try {
                    Thread.sleep(3000);
                    sunucuyaBaglan(host, port);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                    System.out.println(port + " portu icin baglanti denemesi kesildi.");
                }
            }
        }).start();
    }

    // clientin islerini yapan yer
    private static void handleClient(Socket clientSocket) {
        try {
            System.out.println("istemci ile islem yapiliyor: " + clientSocket.getRemoteSocketAddress());
            Subscriber sub = Subscriber.parseFrom(clientSocket.getInputStream());
            if(cilentsData.containsKey(sub.getID()))
            {
                if(sub.getDemand() == DemandType.DEL)
                {
                    Long subId = sub.getID();
                    cilentsData.remove(subId);
                    System.out.println("Abone basariyla silindi. Abone ID : " + subId);
                }
            }else if(sub.getDemand() == DemandType.SUBS)
            {
                cilentsData.put(sub.getID(), sub);
                System.out.println("Basariyla Abone olundu. Abone ID : "+ sub.getID());
            }
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
