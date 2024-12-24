package Clients;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

import com.protos.DemandType;
import com.protos.Subscriber;

public class CreateClient {

    private static final String HOST = "localhost";
    private static final int[] PORTS = {
        5001, 5002, 5003
    };

    private int Id;
    public String nameSurname;
    public String demand;
    public String[] AllInterests;
    public int serverNo;

    private Socket socket;

    //clientte port verilmezse varsayılan olarak rastgele sunucuya baglanilacak
    private static final Random random = new Random();

    CreateClient(int Id, String nameSurname, String demand, String[] AllInterests, int serverNo) {
        this.Id = Id;
        this.nameSurname = nameSurname;
        this.demand = demand.toUpperCase();
        this.AllInterests = AllInterests;
        this.serverNo = serverNo;
    }

    CreateClient(int Id, String nameSurname, String demand, String[] AllInterests) {
        //server bilgisi verilmezse varsayılan değer olarak rastgele sunucuya baglanilacak
        this(Id, nameSurname, demand, AllInterests, random.nextInt(PORTS.length) + 1);
    }

    public CreateClient connectServer() throws IOException {
        socket = new Socket(HOST, PORTS[serverNo - 1]);
        System.out.println("Sunucu"+ serverNo +"'e Basariyla Baglanildi...");
        return this;
    }

    public void disconnectServer() throws IOException {
        // soketleri kontrollü kapatma (daha güvenli)
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Sunucudan basariyla cikis yapildi...");
            }
        } catch (IOException e) {
            System.err.println("Bağlantiyi kapatma sirasinda bir hata oluştu...");
        } finally {
            socket = null;
        }
    }
    
    public void sendRequest() throws IOException {
        Subscriber subscriber = generateSubRequest();
        send(subscriber);
        System.out.println("Abone bilgisi sunucuya gönderildi:\n" + subscriber);
    }

    private Subscriber generateSubRequest() {
        return Subscriber.newBuilder()
        .setNameSurname(nameSurname)
        .setStartDate(System.currentTimeMillis())
        .setLastAccessed(System.currentTimeMillis())
        .addAllInterests(Arrays.asList(AllInterests))
        .setIsOnline(true)
        .setDemand(convertDemandType(demand))
        .setID(Id)
        .build();
    }

    private DemandType convertDemandType(String demand) {
        switch (demand) {
            case "SUB": 
                return DemandType.SUBS;
            case "DEL":
                return DemandType.DEL;
            default:
                throw new IllegalArgumentException("Geçersiz istek");
        }
    }

    private void send(Subscriber subscriber) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        byte[] responseBytes = subscriber.toByteArray();
        output.writeInt(responseBytes.length);
        output.write(responseBytes);
        output.flush();
    }
}
