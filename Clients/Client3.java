package Clients;

import java.io.IOException;

public class Client3 {
    public static void main(String[] args) {
        CreateClient client1 = new CreateClient(1, "foo bar", "sub", new String[]{"foo, bar"}, 3);
        try {
            client1.connectServer().sendRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
        
// Neden bunu yaptım?
// Burda yapmaya çalıştığım şey clientı bir standarta oturtmak
// aşağdaki örnekte birden fazla clientı nasıl kolayca oluşturulduğnu görebilirsiniz
//
// CreateClient client1 = new CreateClient(1, "foo bar", "sub", new String[]{"foo, bar"}, 3);
// CreateClient client2 = new CreateClient(2, "foo bar", "sub", new String[]{"foo, bar"}, 3);
// CreateClient client3 = new CreateClient(3, "foo bar", "sub", new String[]{"foo, bar"}, 3);
//
// ayrıca bu şekilde client dosyalarını çalıştırmak için 3 tane dosya çalıştırmayada gerek yok
// ör.
// CreateClient client1 = new CreateClient(1, "foo bar", "sub", new String[]{"foo, bar"}, 1);   // server 1e bağlanır
// CreateClient client2 = new CreateClient(2, "foo bar", "sub", new String[]{"foo, bar"}, 2);   // server 2ye bağlanır
// CreateClient client3 = new CreateClient(3, "foo bar", "sub", new String[]{"foo, bar"}, 3);   // server 3e bağlanır
//
// Avantajları bunlada sınırlı değil eğer clientı tekrar kullanmak istersek burdada kolaylık sağlıyor
// ör1.     client farklı bir istekte bulunuyor
// CreateClient client1 = new CreateClient(1, "foo bar", "sub", new String[]{"foo, bar"}, 1);   // server 1e bağlanır
// client1.demand = "del";
// try {
//     client1.sendRequest();
// } catch (IOException e) {
//     e.printStackTrace();
// }
//
// ör2.      client farklı bir severa bağlanıyor
// try {
//     client1.disconnectServer();
//     client1.serverNo = 2;
//     client1.connectServer();
// } catch (IOException e) {
//     e.printStackTrace();
// }
//
// vb.
//
// Ayrıca kod okunabilirliği artıyor yeni şeyler eklemek ve düzenlemek kolaylaşıyor
// Yaptığım şey kısaca kodu OOPye uygun bir şekle getirmekti
// Hatırlatma : bu kısmı silin
