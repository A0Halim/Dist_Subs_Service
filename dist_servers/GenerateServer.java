package dist_servers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.protos.Capacity;
import com.protos.Configuration;
import com.protos.Demand;
import com.protos.Message;
import com.protos.MethodType;
import com.protos.Response;
import com.protos.Subscriber;

public class GenerateServer {

    public static final int ports[] = {
        5001, 5002, 5003
    };

    private int serverId;
    private int port;
    private int toleranceLevel;
    private boolean isCanStart = false;
    private volatile boolean serverConnectionTime = true;

    private ServerSocket serverSocket;
    private List<Socket> serverSockets;
    
    private Map<Long, Subscriber> clientsData;

    GenerateServer(int serverId) {
        this.serverId = serverId;
        this.port = ports[serverId - 1];
        this.clientsData = new TreeMap<Long, Subscriber>();
        this.serverSockets = new ArrayList<Socket>();
    }
    
    public void startServer() throws IOException {
        serverSocket = new ServerSocket(this.port);
        connectAdmin();
    }
    
    private void connectOtherServes(int toleranceLevel) {
        for (int port : ports) {
            if (port == this.port)
                continue;
            if (toleranceLevel > 0)
            {
                new Thread(() -> {sendServerSocket(port);}).start();
            } else {
                break;
            }
            toleranceLevel--;
        }
        Thread thread = new Thread(() -> {acceptServerSocket();});
        thread.start();
        try {
            thread.join(2000);
            if (thread.isAlive()) {
                serverConnectionTime = false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void sendServerSocket(int port) {
        try {
            serverSockets.add(new Socket("localhost", port));
            System.out.println("servrera bağlandı");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptServerSocket() {
        try {
            serverSocket.setSoTimeout(1000);
            while (serverConnectionTime) {
                Socket socket = serverSocket.accept();
                System.out.println("bir servera bağlandı");
                new Thread(() -> {handleServer(socket);}).start();
            }
        } catch (IOException e) {
            try {
                serverSocket.setSoTimeout(0);
                System.out.println("Diğer serverlara bağlanma zamanı doldu.");
            } catch (SocketException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void handleServer(Socket socket) {
        try {
            while (isCanStart) {
                Subscriber sub = Subscriber.parseFrom(get(socket));
                addClient(sub);
            }
        } catch (IOException e) {
            System.out.println("diğer serverlardan biri ile bağlant koptu");
            e.printStackTrace();
        }
    }

    private synchronized void sendToServer(Subscriber subscriber) throws IOException {
        for (Socket socket : serverSockets) {
            send(subscriber, socket);
        }
    }

    private synchronized void addClient(Subscriber subscriber) {
        clientsData.put(subscriber.getID(), subscriber);
    }

    private synchronized void removeClient(Long subId) {
        clientsData.remove(subId);
    }

    private void handleClient(Socket clientSocket) throws IOException {
        Subscriber sub = Subscriber.parseFrom(get(clientSocket));
        if (clientsData.get(sub.getID()) != sub) {
            switch (sub.getDemand()) {
                case SUBS:
                    addClient(sub);
                    System.out.println("Basariyla abone olundu. Abone ID : " + sub.getID());
                    break;
                case DEL:
                    Long subId = sub.getID();
                    removeClient(subId);
                    System.out.println("Abone basariyla silindi. Abone ID : " + subId);
                    break;
                default:
                    System.out.println("anlamsız istek");
                    break;
            }
            sendToServer(sub);
            System.out.println(clientsData.size());
        }
    }

    private void connectCilents() {
        new Thread(() -> {
            while (isCanStart) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Bir baglanti kuruldu: " + clientSocket.getRemoteSocketAddress());
                    new Thread(() -> {
                        try {
                            handleClient(clientSocket);
                        } catch (IOException e) {
                            System.out.println("client patladı");
                            //e.printStackTrace();
                        }
                    }).start();
                } catch (Exception e) {
                    System.out.println("bişeyler olmadı");
                }
            }
        }).start();
    }
    
    private void connectAdmin() {
        try (Socket clientSocket = serverSocket.accept()) {
            System.out.println("admin clienda bağlandi");
            Configuration conf = Configuration.parseFrom(get(clientSocket));
            Message message = createMessage(conf);
            send(message, clientSocket);
            System.out.println("mesaj gönderlidi");
            handleAdmin(clientSocket);
        } catch (IOException | InterruptedException e) {
            System.out.println("Admin bağlantisi kurulamadi/kesildi/sorun oluştu. Server kapatiliyor");
            System.exit(1);
        }
    }
    
    private void handleAdmin(Socket adminSocket) throws InvalidProtocolBufferException, IOException, InterruptedException {
        Message message;
        Capacity recivedCapacity;
        if (isCanStart) {
            connectOtherServes(toleranceLevel);
            Thread.sleep(1000);
            connectCilents();
            System.out.println("Admin isteklerine yanıt verilebilir durumda");
            while (isCanStart) {
                message = Message.parseFrom(get(adminSocket));
                recivedCapacity = Capacity.parseFrom(get(adminSocket));
                if (message.getDemand() == Demand.CPCTY && recivedCapacity.getServerId() == serverId) {
                    Capacity capacity = createCapacity();
                    send(capacity, adminSocket);
                }
            }
        }
    }

    //MessageLite protolordaki toByteArray fonksiyonunu barındıran arayüzdür
    public static <T extends MessageLite> void send(T response, Socket socket) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        byte[] responseBytes = response.toByteArray();
        output.writeInt(responseBytes.length);
        output.write(responseBytes);
    }

    public static byte[] get(Socket socket) throws IOException {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        int length = input.readInt();
        byte[] requestBytes = new byte[length];
        input.readFully(requestBytes);
        return requestBytes;
    }

    private Message createMessage(Configuration configuration) {
        toleranceLevel = configuration.getFaultToleranceLevel();
        isCanStart = configuration.getMethodType() == MethodType.STRT ? true : false;
        return Message.newBuilder().setDemand(Demand.STRT).setResponse(isCanStart ? Response.YEP : Response.NOP).build();
    }

    private Capacity createCapacity() {
        return Capacity.newBuilder().setServerXStatus(clientsData.size()).setTimestamp(System.currentTimeMillis()/1000).setServerId(serverId).build();
    }
}
