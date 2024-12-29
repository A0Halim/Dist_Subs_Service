# Sistem programlama ödevi

## Nasıl başlatılmalı

Plotting ve java serverları açıldıktan sonra admin client çalıştırılmalıdır daha sonrasında java clientları çalıştırabilirsiniz.

## Programda neler oluyor

Admin öncelikle plottera bağlanmayı deniyor bağlanamazsa bu işlem tekrarlanıyor. Bir sonraki işlemi serverlara bağlanmak oluyor. Serverlar, başlama emrini aldıktan sonra birbirlerine bağlanmaları için 3 saniye müddet tanınmışır. Bu surenin sonunda bağlanabildi ya da bağlanamadı diğer serverlara bağlanma denemesi kesilir. Server bağlantıları gerçekleştikten sonra serverlar, client isteklerini işleyebilir ve admin isteklerine yanıt verebilir bir durumda oluyor. Clientlar tek bir istek göndericek şekilde tasarlanmışır farklı istekler için manuel düzenleme gereklidir. Tüm program çalışır durumdadır.

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

### ServerX.java özellikleri

- [ ] admin_client.rb ile başlama
- [x] hata toleransı 1 prensibiyle çalışma
- [x] hata toleransı 1 prensibiyle çalışma
- [ ] ...
- [ ] ...
- [ ] Uranus
- [ ] Neptune
- [ ] Comet Haley

### plotter.py özellikleri

- Plotter.py, bir TCP sunucusu olarak çalışmaktadır ve 5004 portunda gelen bağlantıları dinlemektedir.
- Admin.rb'den gelen veri, boyutuyla beraber gönderilmekte ve Capacity.proto formatına uygun olarak alınmaktadır.
- Alınan veriler, her sunucuya ait bir kuyrukta saklanır.
- Kuyruklar, veri alındıkça güncellenir ve her bir sunucu için en son veriler saklanır.
- Sunucu adı, Capacity mesajındaki server_id ile belirlenir ve her sunucuya ait kapasite verileri ayrı ayrı işlenir.
- Plotter.py, bu veriyi çözümledikten sonra bir grafik üzerinde kapasite durumlarını görselleştirmektedir.
- Sunucular, farklı renklerle temsil edilmektedir.
- Grafik gerçek zamanlı olarak güncellenmektedir.
- Hata durumları için gerekli try-except mekanizmaları oluşturulmuştur.

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
- Numara, İsim
- Numara, İsim

### Sunum Videosu Linki

