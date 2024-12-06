package dist_servers;

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
                    System.out.println("Bir kullanici cikis yapti. Soket adersi : " + clientAderss);
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
            Configuration conf = Configuration.parseFrom(clientSocket.getInputStream());
            this.toleranceLevel = conf.getFaultToleranceLevel();
            boolean response = conf.getMethodType() == MethodType.STRT ? true : false;
            Message message = Message.newBuilder().setDemand(Demand.STRT).setResponse(response ? Response.YEP : Response.NOP).build();
            OutputStream out = clientSocket.getOutputStream();
            message.writeTo(out);
            out.flush();
            if (response) {
                connectOtherServes(toleranceLevel);
                connectCilent(serverSocket);
            }
            while (response) {
                message = Message.parseFrom(clientSocket.getInputStream());
                if (message.getDemand() == Demand.CPCTY) {
                    Capacity capacity = Capacity.newBuilder().setServerXStatus(this.port - 5000, clientsData.size()).setTimestamp(System.currentTimeMillis()/1000).build();
                    capacity.writeTo(out);
                    out.flush();
                }
            }
        } catch (IOException e) {
            try {
                System.out.println("Admin bağlantisi kurulamadi/kesildi. Yeniden deneniyor");
                Thread.sleep(3000);
                connectAdmin(serverSocket);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }
}
