package insanlar;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import main.Restoran;

public class Kasa extends Kisi {
	
	
	Restoran restoran;
	FileWriter fw;
	ConcurrentLinkedQueue<Musteri> kasaSirasi;
	ConcurrentLinkedQueue<Musteri> restoranMusterileri;
	
	boolean calisiyor;
	
	
	public Kasa(int id, FileWriter fw, boolean adimAdimMi, ConcurrentLinkedQueue<Musteri> kasaSirasi, ConcurrentLinkedQueue<Musteri> restoranMusterileri, Restoran restoran) {
		super(id, adimAdimMi);
		this.restoran = restoran;
		this.kasaSirasi = kasaSirasi;
		this.fw = fw;
		this.restoranMusterileri = restoranMusterileri;
		this.calisiyor = true;
	}
	
	@Override
	public void run() {
		
		while(calisiyor) {
			while(duraklama) {
				
			}
			
			
			if(kasaSirasi.size() > 0) {
				siradanOdemeAl();
				restoran.paraEkle();
				
				
				
			}
			
			if(adimAdimMi) {
				duraklama = true;
			}
		}
	}
	
	
	public void siradanOdemeAl() {
		Musteri musteri = kasaSirasi.poll();
		if(musteri != null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(String.format("Musteri %d'den ödeme alındı, müşteri restorandan ayrıldı", musteri.getID()));
			try {
				fw.write(String.format("Musteri %d'den ödeme alındı, müşteri restorandan ayrıldı", musteri.getID()));
				fw.write('\n');
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			restoranMusterileri.remove(musteri);
			musteri.setDurum(Musteri.Durum.AYRILIYOR);
		}
	}
	
	public void setCalisiyor(boolean calisiyor) {
		this.calisiyor = calisiyor;
	}
}
