import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Client1 {
    private static final String HOST = "localhost";
    private static final int PORT1 = 5001; // server1'in portu

    public static void main(String[] args) {
        connectionOfServer(HOST, PORT1);
    }

    public static void connectionOfServer(String host, int port) {
        try (Socket socket = new Socket(host, port);
             OutputStream outputStream = socket.getOutputStream()) {

            System.out.println("Sunucu1'e Basariyla Baglanildi...");

            // Subscriber oluştur
            Subscriber subscriber = Subscriber.newBuilder()
                    .setNameSurname("Hikmet Bal")
                    .setStartDate(System.currentTimeMillis())
                    .setLastAccessed(System.currentTimeMillis())
                    .addAllInterests(Arrays.asList("Coding", "Science", "Art"))
                    .setIsOnline(true)
                    .setDemand(DemandType.SUBS)
                    .build();

            // Protobuf nesnesini serialize et ve sunucuya gönder
            subscriber.writeTo(outputStream);
            outputStream.flush();

            System.out.println("Abone bilgisi sunucuya gönderildi: " + subscriber);
        } catch (IOException e) {
            System.err.println("Sunucu1'e Baglanilamadi: " + e.getMessage());
        }
    }
}

