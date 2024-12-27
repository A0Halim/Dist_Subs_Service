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
