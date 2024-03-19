package insanlar;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import yardimci.Siparis;

public class Asci extends Kisi {

	int yemekSuresi = 3000;
	int maxSiparisSayisi = 2;

	boolean calisiyor;
	long anlikZaman;
	int siparisSayisi;
	FileWriter fw;
	
	ConcurrentLinkedQueue<Musteri> restoranMusterileri;
	ConcurrentLinkedQueue<Siparis> tumSiparisSirasi;
	Siparis[] siparisler;
	
	public Asci(int id, FileWriter fw, boolean adimAdimMi, ConcurrentLinkedQueue<Musteri> restoranMusterileri, ConcurrentLinkedQueue<Siparis> tumSiparisSirasi) {
		super(id, adimAdimMi);
		this.restoranMusterileri = restoranMusterileri;
		this.tumSiparisSirasi = tumSiparisSirasi;
		this.fw = fw;
		this.siparisler = new Siparis[maxSiparisSayisi];
		for(int i = 0; i < maxSiparisSayisi; i++) {
			siparisler[i] = null;
		}
		
		this.calisiyor = true;
		this.siparisSayisi = 0;
		anlikZaman = System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		while(calisiyor) {
			while(duraklama) {
				
			}
			
			
			anlikZaman = System.currentTimeMillis();
			
			for(int i = 0; i < maxSiparisSayisi; i++) {
				if(siparisler[i] == null) {
					Siparis siparis = tumSiparisSirasi.poll();
					if(siparis != null) {
						System.out.println(String.format("Asci %d, Musteri %d'nin yemeğini yapıyor.", id, siparis.getMusteri().getID()));
						try {
							fw.write(String.format("Asci %d, Musteri %d'nin yemeğini yapıyor.", id, siparis.getMusteri().getID()));
							fw.write('\n');
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						siparis.setSiparisBaslamaZamani(System.currentTimeMillis());
						siparisler[i] = siparis;
					}
				}
				else {
					long gecenZaman = anlikZaman - siparisler[i].getSiparisBaslamaZamani();
					if(gecenZaman > yemekSuresi) {
						Musteri musteri = siparisler[i].getMusteri();
						System.out.println(String.format("Asci %d, Musteri %d'nin yemeğini tamamladı.", id, musteri.getID()));
						try {
							fw.write(String.format("Asci %d, Musteri %d'nin yemeğini tamamladı.", id, musteri.getID()));
							fw.write('\n');
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						musteri.setDurum(Musteri.Durum.YEMEK);
						siparisler[i] = null;
					}
				}
			}
			
			if(adimAdimMi) {
				duraklama = true;
			}
		}
		
		
	}
	
	
	public void setCalisiyor(boolean calisiyor) {
		this.calisiyor = calisiyor;
	}

}
