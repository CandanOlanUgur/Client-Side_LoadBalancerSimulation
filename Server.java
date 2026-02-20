import java.util.Random; //nextGaussian()

public class Server {
    private int id;
    private double trueLatency; // ağda tıkanıklık vb olmadan saf temel, isteğe cevap verme süresi | İKLİM
    private double noiseStdDev; //
    private double driftStdDev; // Zamanla değişimin şiddeti (Non-stationary)
    private Random random;

    public Server(int id, double initialLatency, double noiseStdDev, double driftStdDev) {
        this.id = id;
        this.trueLatency = initialLatency;
        this.noiseStdDev = noiseStdDev;
        this.driftStdDev = driftStdDev;
        this.random = new Random();
    }

    //İstemciden gelen isteği karşılayan ve yanıt süresini döndüren ana metot
    public double getResponseTime() {
        // 1. Gürültülü yanıt süresini hesapla: Gerçek değer + (gauss gürültüsü * sapma)
        double actualLatency = trueLatency + (random.nextGaussian() * noiseStdDev); //NOT: random.nextGaussian standart sapma normal dağılıma uygun rasgele sayılar üretir

        //Gecikme süresi olarak sıfırın altına düşemez, minimum 1.0 ms
        if (actualLatency < 1.0) {
            actualLatency = 1.0;
        }

        // sunucunun temel yanıt süresi değişti ==> burası, softmax algoritmasının sürekli öğrenmesine sebep olur
        trueLatency += random.nextGaussian() * driftStdDev;
        //!Hamburgercinin aşçısının değişmesi veya dükkanın yoğunlaşması
        //

        //Gerçek ortalama gecikmenin de aşırı düşmesini engelleme
        if (trueLatency < 1.0) {
            trueLatency = 1.0;
        }

        return actualLatency;
    }

    public int getId() {
        return id;
    }
}
