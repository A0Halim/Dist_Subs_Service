package dist_servers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.protos.Capacity;
import com.protos.Configuration;
import com.protos.Demand;
import com.protos.Message;
import com.protos.MethodType;
import com.protos.Response;
import com.protos.Subscriber;

public class GenerateServer {

    public static int ports[] ={
        5001, 5002, 5003
    };

    private int port;
    private int toleranceLevel;

    private Map<Long, Subscriber> clientsData;

    private List<Subscriber> awaitedData;
    private List<Socket> serverSockets;

    GenerateServer(int port) {
        this.port = port;
        this.clientsData = new TreeMap<Long, Subscriber>();
        this.awaitedData = new ArrayList<Subscriber>();
    }
    
    public void startServer() throws IOException {
        connectAdmin(new ServerSocket(this.port));
    }
    
    private void connectOtherServes(int toleranceLevel) {
        for (int port : ports) {
            if (port == this.port)
                continue;
            if (toleranceLevel > 0)
                new Thread(() -> connectServer(port)).start();
            else
                break;
            toleranceLevel--;
        }
    }
    
    private void connectServer(int port) {
        try (Socket connectionServer = new Socket("localhost", port)) {
            serverSockets.add(connectionServer);
        } catch (IOException e) {
            try {
                System.out.println(port + " portu ile baglanti kurulanmadi");
                Thread.sleep(3000);
                connectServer(port);
            } catch (InterruptedException e1) {
                Thread.currentThread().interrupt();
                System.out.println(port + " portu icin baglanti denemesi kesildi.");
            }
        }
    }

    private void sendToServer() {
        try {
            Subscriber data = awaitedData.removeFirst();
            for (Socket socket : serverSockets) {
                OutputStream out = socket.getOutputStream();
                data.writeTo(out);
            }
        } catch (IOException e) {
            System.out.println("Gönderim sirasinda bir hata oluştu");
            e.printStackTrace();
        }
    }

    private void sendData(Subscriber sub) {
        awaitedData.add(sub);
        sendToServer();
    }
    
    private void connectCilent(ServerSocket serverSocket) {
        try (Socket clientSocket = serverSocket.accept()) {
            System.out.println("Bir baglanti kuruldu: " + clientSocket.getRemoteSocketAddress());
            new Thread(() -> {
                String clientAderss = clientSocket.getRemoteSocketAddress().toString();
                try {
                    while (true) {
                        handleClient(clientSocket);
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("Bir bağlanti koptu. Soket adersi : " + clientAderss);
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) throws IOException {
        Subscriber sub = Subscriber.parseFrom(clientSocket.getInputStream());
        if (clientsData.get(sub.getID()) != sub) {
            switch (sub.getDemand()) {
                case SUBS:
                    clientsData.put(sub.getID(), sub);
                    System.out.println("Basariyla abone olundu. Abone ID : " + sub.getID());
                    break;
                case DEL:
                    Long subId = sub.getID();
                    clientsData.remove(subId);
                    System.out.println("Abone basariyla silindi. Abone ID : " + subId);
                    break;
                default:
                    break;
            }
            sendData(sub);
        }
    }
    
    private void connectAdmin(ServerSocket serverSocket) {
        try (Socket clientSocket = serverSocket.accept()) {
            System.out.println("admin clienda bağlandi");
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            Configuration conf = Configuration.parseFrom(getByteFrom(input));
            this.toleranceLevel = conf.getFaultToleranceLevel();
            boolean response = conf.getMethodType() == MethodType.STRT ? true : false;
            System.out.println("config dosayasi alindi. toleranceLevel : " + this.toleranceLevel + " - method : " + response);
            Message message = Message.newBuilder().setDemand(Demand.STRT).setResponse(response ? Response.YEP : Response.NOP).build();
            sendToAdmin(message.toByteArray(), output);
            System.out.println("mesaj gönderlidi");
            if (response) {
                connectOtherServes(toleranceLevel);
                new Thread(() -> connectCilent(serverSocket));
            }
            while (response) {
                System.out.println("istek bekleniyor");
                message = Message.parseFrom(getByteFrom(input));
                System.out.println("istek alındı");
                if (message.getDemand() == Demand.CPCTY) {
                    Capacity capacity = Capacity.newBuilder().setServerXStatus(clientsData.size()).setTimestamp(System.currentTimeMillis()/1000).build();
                    sendToAdmin(capacity.toByteArray(), output);
                    System.out.println("kapasite gönderildi");
                }
            }
            System.out.println("response false");
        } catch (IOException e) {
            System.out.println("Admin bağlantisi kurulamadi/kesildi. Server kapatiliyor");
            System.exit(1);
        }
    }

    private byte[] getByteFrom(DataInputStream input) throws IOException {
        int length = input.readInt();
        byte[] requestBytes = new byte[length];
        input.readFully(requestBytes);
        return requestBytes;
    }

    private void sendToAdmin(byte[] responseBytes, DataOutputStream output) throws IOException {
        output.writeInt(responseBytes.length);
        output.write(responseBytes);
        output.flush();
    }
}
