package insanlar;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import yardimci.Siparis;

public class Musteri extends Kisi {
	
	public enum Durum {
		MASA_BEKLEMEDE,
		SIPARIS_BEKLEMEDE,
		YEMEK,
		KASA_BEKLEMEDE,
		AYRILIYOR
	};
	
	Durum durum;
	boolean restoranda;
	
	long anlikZaman;
	long restoranaGirisZamani;
	FileWriter fw;

	ConcurrentLinkedQueue<Musteri> kasaSirasi;
	ConcurrentLinkedQueue<Musteri> musteriSirasi;
	

	public Musteri(int id, FileWriter fw, boolean adimAdimMi, ConcurrentLinkedQueue<Musteri> kasaSirasi, ConcurrentLinkedQueue<Musteri> musteriSirasi) {
		super(id, adimAdimMi);
		this.kasaSirasi = kasaSirasi;
		this.musteriSirasi = musteriSirasi;
		this.fw = fw;
		durum = Durum.MASA_BEKLEMEDE;
		restoranda = true;
		anlikZaman = System.currentTimeMillis();
		restoranaGirisZamani = System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		while(restoranda) {
			while(duraklama) {
			}
			
		
			
			switch(durum) {
			case MASA_BEKLEMEDE:
				musteriBekliyor();
				break;
			case SIPARIS_BEKLEMEDE:
				break;
			case YEMEK:
				musteriYemekYiyor();
				break;
			case KASA_BEKLEMEDE:
				break;
			case AYRILIYOR:
				musteriAyriliyor();
				break;
			}
			
			if(adimAdimMi) {
				duraklama = true;
			}
		}
	}
	
	public synchronized void kasaSirasinaKendiniEkle() {
		kasaSirasi.add(this);
	}
	
	public void musteriBekliyor() {
		anlikZaman = System.currentTimeMillis();
		long gecenZaman = anlikZaman - restoranaGirisZamani;
		if(gecenZaman > 20000) {
			System.out.println(String.format("Musteri %d çok bekledi ve restorandan ayrılıyor.", id));
			try {
				fw.write(String.format("Musteri %d çok bekledi ve restorandan ayrılıyor.", id));
				fw.write('\n');
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			durum = Durum.AYRILIYOR;
		}
	}
	public void musteriYemekYiyor() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(String.format("Musteri %d yemeğini bitirdi.", id));
		try {
			fw.write(String.format("Musteri %d yemeğini bitirdi.", id));
			fw.write('\n');
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kasaSirasinaKendiniEkle();
		durum = Durum.KASA_BEKLEMEDE;
	}
	public void musteriAyriliyor() {
		musteriSirasi.remove(this);
		this.restoranda = false;
	}
	
	public Durum getDurum() {
		return durum;
	}
	public void setDurum(Durum durum) {
		this.durum = durum;
	}
	
	public boolean getRestoranda() {
		return restoranda;
	}
	public void setRestoranda(boolean restoranda) {
		this.restoranda = restoranda;
	}

}
