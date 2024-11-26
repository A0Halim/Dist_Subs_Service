package Clients;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import com.protos.DemandType;
import com.protos.Subscriber;

public class Client2 {
    private static final String HOST = "localhost";
    private static final int PORT2 = 5002; // server2'nin portu

    public static void main(String[] args) {
        connectionOfServer(HOST, PORT2);
    }

    public static void connectionOfServer(String host, int port) {
        try (Socket socket = new Socket(host, port);
             OutputStream outputStream = socket.getOutputStream()) {

            System.out.println("Sunucu2'ye Basariyla Baglanildi...");

            // Subscriber oluştur
            Subscriber subscriber = Subscriber.newBuilder()
                    .setNameSurname("Oğuz Dertli")
                    .setStartDate(System.currentTimeMillis())
                    .setLastAccessed(System.currentTimeMillis())
                    .addAllInterests(Arrays.asList("Technology", "Hiking", "Outdoor"))
                    .setIsOnline(true)
                    .setDemand(DemandType.SUBS)
                    .setID(2)
                    .build();

            // Protobuf nesnesini serialize et ve sunucuya gönder
            subscriber.writeTo(outputStream);
            outputStream.flush();

            System.out.println("Abone bilgisi sunucuya gönderildi: " + subscriber);
        } catch (IOException e) {
            System.err.println("Sunucu2'ye Baglanilamadi: " + e.getMessage());
        }
    }
}
