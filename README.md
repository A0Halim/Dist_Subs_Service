# Sistem programlama ödevi

## Nasıl başlatılmalı

Plotting ve java serverları açıldıktan sonra admin client çalıştırılmalıdır daha sonrasında java clientları çalıştırabilirsiniz.

## Programda neler oluyor

Admin öncelikle plottera bağlanmayı deniyor bağlanamazsa bu işlem tekrarlanıyor. Bir sonraki işlemi serverlara bağlanmak oluyor. Serverlar, başlama emrini aldıktan sonra birbirlerine bağlanmaları için 3 saniye müddet tanınmışır. Bu surenin sonunda bağlanabildi ya da bağlanamadı diğer serverlara bağlanma denemesi kesilir. Server bağlantıları gerçekleştikten sonra serverlar, client isteklerini işleyebilir ve admin isteklerine yanıt verebilir bir durumda oluyor. Clientlar tek bir istek göndericek şekilde tasarlanmışır farklı istekler için manuel düzenleme gereklidir ve serverlarda tek bir bağlantıdan tek bir istek alıcak şekilde tasarlanmıştır. Tüm program çalışır durumdadır.

## Olası sorunlar

### Kütüphane sorunları

Gerekli tüm python ve ruby kütüphanelerinin yüklü olduğundan emin olun.

### Java protobuf

Bu sorunu aşmak için javanın protobuf kütüphanesi manuel kurulmuştur.

#### Kurulum rehberi

![resimli anlatım](./images/Ekran%20görüntüsü%202024-12-24%20223038.png)

Son olarak değişiklikleri uygulayın. Bu şekilde protobufı kullanmak için vs koddaki

Run -> Start Debugging (F5) veya Run -> Run Without Debugging (Ctrl + F5)

Yollarıyla serverları çalıştırabilirsiniz.

### Client özellikleri

- [x] Client.java dosyasında, aynı pakette olan CreateClient.java dosyasının içindeki CreateClient constructoruna parametre yollanarak client oluşturulur.
- [x] Constructor'a yollanan parametreler: [Id, nameSurname, TALEP (sub or del), AllInterest, serverNo].
- [x] ServerNo parametresi Client.javadan random olarak yollanır. Eğer serverNo parametrede verilmezse overload constructor metodunda varsayılan olarak client, random bir sunucuya bağlatılır.
- [x] Parametrelerden girilen abone bilgileri, rastgele seçilen sunucuya talep (sub or del) ile birlikte yollanır.
- [x] Abonelik bilgileri yollandıktan sonra feedback olarak "Abone bilgisi sunucuya gönderildi" mesajı kullanıcıya gösterilir.
- [x] Sunucudan da client'e feedback mesajı yollanır.

### ServerX.java özellikleri

- [x] Admin_client.rb den başlama emri alalındıktan sonra çalımaya başlama
- [x] adminden gelen isteğin başarılı yada başarısız donuçlandığında dair geri dönüş
- [x] Hata toleransı 0 prensibiyle çalışma
- [x] Hata toleransı 1 prensibiyle çalışma
- [x] Hata toleransı 2 prensibiyle çalışma
- [x] Tüm iletişim işlemleri için proto kullanımı
- [x] Serverlardan birinin bağlantısı koptuğununda diğer serverlar çalışmaya devam edebilir
- [x] Hata yönetimi
- [x] Clientler SUB ve DEL işlemlerini gerçekleştirebilir
- [x] Adminden gelen kapasite isteklerine yanıt verebilir
- [x] Gelen isteklerin sunuculardan mı Clientlerdan mı geldiğinin ayrımını yapabilme
- [x] Serverlar birbirleri arasında arası yedekleme yapabilir
- [x] Senkronizasyon sağlanmıştır aynıanda pek çok isteğe yanıt verebilir

### plotter.py özellikleri

- [x] Plotter.py, 5004 portunda bir TCP sunucusu olarak çalışır ve Admin.rb'den gelen bağlantıları dinler.
- [x] Admin.rb, veriyi boyutuyla birlikte gönderir ve bu veri, Capacity.proto formatına uygun olarak alınır.
- [x] Alınan veriler, her bir sunucuya ait ayrı bir kuyrukta saklanır. Veri alındıkça kuyruklar güncellenir ve her sunucu için en son veriler korunur.
- [x] Sunucu adı, Capacity mesajındaki server_id ile belirlenir ve her sunucuya ait kapasite verileri ayrı ayrı işlenir.
- [x] Plotter.py, alınan veriyi çözümledikten sonra matplotlib ile gerçek zamanlı bir grafik üzerinde kapasite durumlarını görselleştirir.
- [x] Sunucular, her biri için farklı renklerle temsil edilir. Bu sayede her sunucu, görselde açıkça ayırt edilebilir.
- [x] Grafik sürekli olarak güncellenir, böylece sunuculardan alınan yeni veriler görselleştirilir.
- [x] Hata durumları için gerekli try-except blokları oluşturulmuştur.

### admin.rb özellikleri

- [x] Admin.rb client durumunda çalışmaktadır ve sırasıyla 5004(python), 5001, 5002 ve 5003(java) portlarına bağlanmaya çalışır ve en az 1 java sunucusu, python bağlantısı sağlandığı takdirde sunuculara veri göndermeye başlayabilir.
- [x] Admin.rb ve sunucular arası veri gönderimi protobuf ile sağlanmaktadır ve veri gönderimi sırasında gönderilen veri, boyutuyla ile beraber gönderilmektedir.
- [x] Admin.rb kendi makinesinde bulunan dist_subs.conf dosyasından fault_tolerance_level değerini okuyup bu değeri bağlantısı sağlanan her bir java sunucusuna da göndermektedir.
- [x] Okunan değer yanında ayrıca java sunucularına başlama emrini de göndermektedir. Java sunucuları bu emri doğru almadığı takdirde sunucular çalışmaya başlamamaktadır.
- [x] Java sunucuları ise bu başlama emrine karşılık olarak başlamaya hazır olup olmadıkları durumunu messsage tipinde protonun response "YEP" veya "NOP" değerleri ile admin.rb'ye bildirirler.
- [x] Java sunucuları başlamaya hazır durumda ise admin.rb her bir sunucudan kapasite bilgisini ister. Daha sonra ise capacity tipinde bir nesne oluşturur ve bu nesneye server_id ataması yaparak bu proto nesnesini java serverlarına gönderir.
- [x] Java sunuculuar yaptığı kontroller sonrasında admin.rb'nin yaptığı kapasite isteğine karşılık olarak kapasite bilgilerini admin.rb'ye gönderir.
- [x] Admin.rb java sunucularından aldığı kapasite bilgilerini server_id bilgileriyle birlikte python sunucusuna gönderir.
- [x] Hata durumları için gerekli begin rescue mekanizmaları oluşturulmuştur.

### Ekip üyeleri

- 22060388, Ömer Faruk DERİN
- 22060662, Mahmut ÖZTÜRK
- 22060339, Abdulhalim BAYRAK
- 22060371, Batuhan AYDIN

### Sunum Videosu Linki
https://www.youtube.com/watch?v=ahbmXw2MQh0
