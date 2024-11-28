package Clients;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

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
             OutputStream outputStream = socket.getOutputStream();
             Scanner oku = new Scanner(System.in);) {

            System.out.println("Sunucu2'ye Basariyla Baglanildi...");

            // Subscriber oluştur ve talimati kullanicidan al
            System.out.println("Lutfen talimati giriniz (Sub/Del)");
            String talimat = oku.nextLine();
            DemandType demand;
            
            if ("Sub".equals(talimat)) {
                demand = DemandType.SUBS;
            }
            else if ("Del".equals(talimat)) {
                demand = DemandType.DEL;
            }
            else {
                throw new IllegalArgumentException("Hatali bir talimat girildi, istemciden cikiliyor....");
            }
            Subscriber subscriber = Subscriber.newBuilder()
                    .setNameSurname("Oguz Dertli")
                    .setStartDate(System.currentTimeMillis())
                    .setLastAccessed(System.currentTimeMillis())
                    .addAllInterests(Arrays.asList("Technology", "Hiking", "Outdoor"))
                    .setIsOnline(true)
                    .setDemand(demand)
                    .setID(2)
                    .build();

            // Protobuf nesnesini serialize et ve sunucuya gönder
            subscriber.writeTo(outputStream);
            outputStream.flush();

            System.out.println("Abone bilgisi sunucuya gönderildi:\n" + subscriber);
        } catch (IOException e) {
            System.err.println("Sunucu2'ye Baglanilamadi: " + e.getMessage());
        }
        
    }
}
