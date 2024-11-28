package Clients;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import com.protos.Subscriber;
import com.protos.DemandType;

public class Client1 {
    private static final String HOST = "localhost";
    private static final int PORT1 = 5001; // server1'in portu

    public static void main(String[] args) {
        connectionOfServer(HOST, PORT1);
    }

    public static void connectionOfServer(String host, int port) {
        try (Socket socket = new Socket(host, port);
             OutputStream outputStream = socket.getOutputStream();
             Scanner oku = new Scanner(System.in)) {

            System.out.println("Sunucu1'e Basariyla Baglanildi...");

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
                    .setNameSurname("Hikmet Bal")
                    .setStartDate(System.currentTimeMillis())
                    .setLastAccessed(System.currentTimeMillis())
                    .addAllInterests(Arrays.asList("Coding", "Science", "Art"))
                    .setIsOnline(true)
                    .setID(1)
                    .setDemand(demand)
                    .build();
                    
            // Protobuf nesnesini serialize et ve sunucuya gönder
            subscriber.writeTo(outputStream);
            outputStream.flush();

            System.out.println("Abone bilgisi sunucuya gönderildi:\n"+ subscriber);
        } catch (IOException e) {
            System.err.println("Sunucu1'e Baglanilamadi: " + e.getMessage());
        }
    }
}

