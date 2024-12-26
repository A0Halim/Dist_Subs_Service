package Clients;

import java.io.IOException;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        Random random = new Random();
        int rastgele_port = random.nextInt(3) + 1;
        CreateClient client1 = new CreateClient(4, "foo bar", "sub", new String[]{"foo, bar"}, rastgele_port);
        try {
            client1.connectServer().sendRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
        
// aşağdaki örnekte birden fazla clientı nasıl kolayca oluşturulduğnu görebilirsiniz
// CreateClient client1 = new CreateClient(1, "foo bar", "sub", new String[]{"foo, bar"}, 3);
// CreateClient client2 = new CreateClient(2, "foo bar", "sub", new String[]{"foo, bar"}, 3);
// CreateClient client3 = new CreateClient(3, "foo bar", "sub", new String[]{"foo, bar"}, 3);

// ayrıca bu şekilde client dosyalarını çalıştırmak için 3 tane dosya çalıştırmayada gerek yok
// ör.
// CreateClient client1 = new CreateClient(1, "foo bar", "sub", new String[]{"foo, bar"}, 1);   // server 1e bağlanır
// CreateClient client2 = new CreateClient(2, "foo bar", "sub", new String[]{"foo, bar"}, 2);   // server 2ye bağlanır
// CreateClient client3 = new CreateClient(3, "foo bar", "sub", new String[]{"foo, bar"}, 3);   // server 3e bağlanır

