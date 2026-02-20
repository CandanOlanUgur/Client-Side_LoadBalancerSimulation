public class SimulationRunner {
    //Parametreler
    private static final int K_SERVERS = 5;  // Sunucu sayısı
    private static final int TOTAL_REQUESTS = 10000; // Toplam simüle edilecek istek
    private static final double TAU = 0.05; // Çok büyük ==> kararsız yapı, random selection gibi davranabilir, Çok büyük ==> Aşırı kararlı
    private static final double ALPHA = 0.1; // Öğrenme katsayısı (Yakın geçmişin önemi) Çok küçük ==> Muhafazakarlaşma adapte olmak binlerce istek sürebilir, Çok büyük ==> Aşırı tepkiselleşme,sunucu anlık gürültüler yüzünden defterden silinebilir

    public static void main(String[] args) {
        System.out.println("--- Dağıtık Sistem Yük Dengeleme Simülasyonu Başlıyor ---");

        // 1.Senaryo: Softmax Algoritması ile Simülasyon
        double softmaxTotalLatency = runSoftmaxSimulation();
        double softmaxAvgLatency = softmaxTotalLatency / TOTAL_REQUESTS;

        // 2.Senaryo: Rastgele (Random) Seçim ile Simülasyon (Kıyaslama için)
        double randomTotalLatency = runRandomSimulation();
        double randomAvgLatency = randomTotalLatency / TOTAL_REQUESTS;

        //Sonuçları ekrana Bas
        System.out.println("\n--- Simülasyon Sonuçları ---");
        System.out.printf("Rastgele Seçim Ortalama Gecikme: %.2f ms\n", randomAvgLatency);
        System.out.printf("Softmax Seçimi Ortalama Gecikme: %.2f ms\n", softmaxAvgLatency);

        double improvement = ((randomAvgLatency - softmaxAvgLatency) / randomAvgLatency) * 100;
        System.out.printf("Softmax Algoritmasının Performans Artışı: %%%.2f\n", improvement);
    }

    // --- Softmax Simulasyonu ---
    private static double runSoftmaxSimulation() {
        Server[] servers = createServers();
        SoftmaxLoadBalancer loadBalancer = new SoftmaxLoadBalancer(K_SERVERS, TAU, ALPHA);
        double totalLatency = 0.0;

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            int selectedServerIndex = loadBalancer.selectServer(); // Karar ver
            double latency = servers[selectedServerIndex].getResponseTime(); // İsteği at ve süreyi ölç

            loadBalancer.uptadeQvalue(selectedServerIndex, latency); //Sistemi güncelle
            totalLatency += latency;
        }
        return totalLatency;
    }

    // --- Rastgele (Random) Simülasyonu ---
    private static double runRandomSimulation() {
        Server[] servers = createServers();
        double totalLatency = 0.0;
        java.util.Random random = new java.util.Random();

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            int selectedServerIndex = random.nextInt(K_SERVERS); // Sadece rastgele seç (Öğrenme yok)
            double latency = servers[selectedServerIndex].getResponseTime();
            totalLatency += latency;
        }

        return totalLatency;
    }

    // K adet sunucusu farklı başlangıç değerleriyle başlatan yardımcı metot
    private static Server[] createServers() {
        Server[] servers = new Server[K_SERVERS];
        // Örnek: Farklı başlangıç gecikmeleri, farklı gürültü ve değişim (drift) oranları

        servers[0] = new Server(0, 50.0, 5.0, 1.0);  // Hızlı ama biraz dalgalı
        servers[1] = new Server(1, 100.0, 10.0, 2.0); // Yavaş ve çok dalgalı
        servers[2] = new Server(2, 60.0, 2.0, 0.5); // İstikrarlı ama zamanla değişebilir
        servers[3] = new Server(3, 80.0, 15.0, 3.0); // Çok gürültülü
        servers[4] = new Server(4, 40.0, 3.0, 1.5);  // En hızlısı ama drift oranı yüksek

        return servers;
    }

}
