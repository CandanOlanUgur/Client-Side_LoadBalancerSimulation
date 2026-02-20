import java.util.Random;

public class SoftmaxLoadBalancer {
    private double[] qValues; // Load balancerın her bir sunucu için tuttuğu karne notu
    private double tau; // Sıcaklık (Temperature): Keşif (exploration) oranını belirler
    private double alpha; // Öğrenme Katsayısı (Step-size): Yakın zamanlı verilere verilecek önem
    private int k; // Toplam sunucu sayısı

    private Random random;

    // Sınıfın kurucu metodu
    public SoftmaxLoadBalancer(int k, double tau, double alpha) {
        this.k = k;
        this.tau = tau;
        this.alpha = alpha; //Genelde 0.1 gibi bir değer seçilir
        this.qValues = new double[k];
        this.random = new Random();

        //Başlangıçta tüm sunucular hakkında hiçbir fikir yok, q değerleri 0
        for (int i = 0; i < k; i++) {
            qValues[i] = 0.0;
        }
    }

    //ADIM 1: Softmax formülüne göre bir sunucu seçimi
    public int selectServer() {
        // 1. Önce Q değerleri içindeki en büyük değeri (Max) bul
        double maxQ = qValues[0];
        for (double q: qValues) {
            if (q > maxQ) maxQ = q;
        }

        double[] probabilities = new double[k];
        double sumExp = 0.0;

        //Payda için toplamı ve her bir e^(Q/tau) değerini hesapla
        for (int i = 0; i < k; i++) {
            probabilities[i] = Math.exp((qValues[i] - maxQ) / tau);//Hamburgercilere puanlarına göre seçim ihtimali verme işlemi
            sumExp += probabilities[i];
            /* qValues[i]:                   Hamburgercinin puanı
                      tau:                   Algoritmanın macera arama katsayısı
                      Math.exp(), e üzeri x: Bu fonksiyon, puanı düşük olanla yüksek olan arasındaki
                            farkı "belirginleştirir". Yani puanı birazcık bile yüksek olanı,
                            olasılık tablosunda çok daha öne çıkarır.
             */

        }

        //Rulet Tekerleği (Roulette wheel) mantığı ile seçim
        double rand = random.nextDouble(); // 0.0 ile 1.0 arası rastgele bir sayı
        double cumulativeProbability = 0.0;

        for (int i = 0; i < k; i++) {
            double prob = probabilities[i] / sumExp; // Sunucunun seçilme olasılığı
            cumulativeProbability += prob;

            // Eğer rastgele sayımız bu olasılık dilimize denk geldiyse, o sunucuyu seç
            if (rand <= cumulativeProbability) {
                return i;
            }
        }

        //Çok nadir görülen ondalık yuvarlama hataları için son sunucuyu varsayılan olarak dön
        return k - 1;
    }

    //ADIM 2: İsteğin sonucu belli olduktan sonra Q değerini güncelle
    public void uptadeQvalue(int serverIndex, double latency) {
        // Maliyeti (gecikme) ödüle çevirme
        // Gecikme ne kadar düşükse, ödül o kadar büyük olmalı
        double reward = 1.0 / latency;

        //Non-stationary (zamanla değişen) ortamlar içn güncelleme formülü: her bir hamburgercinin değerlendirme notu
        qValues[serverIndex] = qValues[serverIndex] + alpha * (reward - qValues[serverIndex]);
        /*Eğer bu işlem olmasaydı, sunucunun verdiği cevabı alır ve hemen unuturdun.
         Bu satır sayesinde "Eski notum şuydu, yeni gelen cevapla
         bunu biraz güncelleyeyim" diyorsun.*/

    }

}
