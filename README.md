# Client-Side_LoadBalancerSimulation
Softmax Algoritması kullanan İstemci taraflı Yük Dengeleyeci Simülasyonu


&nbsp;

 
📌 Proje Hakkında
Bu proje, dağıtık sistemlerde (distributed systems) sunucu yükünü optimize etmek amacıyla tasarlanmış istemci taraflı (client-side) bir yük dengeleyici simülasyonudur. Klasik Round-Robin veya Random seçim algoritmaları yerine, pekiştirmeli öğrenme (reinforcement learning) temelli Softmax Action Selection algoritması kullanılmıştır.

Sistem, gerçek dünya senaryolarını taklit etmek amacıyla durağan olmayan (non-stationary) ve gürültülü (noisy) sunucu performanslarını simüle eder.


## 🚀 Öne Çıkan Özellikler
* Non-Stationary Environment: Sunucu performansları sabit değildir; zamanla "Random Walk" modeliyle yavaşlar veya hızlanır.
* Softmax Action Selection: Sunucuların geçmiş performans verilerine dayanarak olasılıksal bir seçim mekanizması sunar.
* Numerical Stability: Üstel fonksiyon hesaplamalarında oluşabilecek "overflow" (taşma) sorunlarını engellemek için Max-Subtraction tekniği uygulanmıştır.
* Reward Modeling: Gecikme süreleri (latency), $1/latency$ formülüyle ödüle (reward) dönüştürülerek maksimizasyon problemi çözülmüştür.



&nbsp;
-
**Eğitim Amaçlıdır**
