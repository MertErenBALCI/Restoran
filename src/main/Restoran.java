package main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import gui.Window;
import insanlar.*;
import yardimci.Siparis;

public class Restoran extends Thread {

	int masaSayisi = 6;
	int garsonSayisi = 3;
	int asciSayisi = 2;
	int kasaSayisi = 1;
	int toplamMusteriSayisi = 0;
	int kasadakiPara = 0;
	boolean adimAdimMi;
	boolean restoranAcikMi;
	volatile boolean adimda;
	long baslangic;
	
	ConcurrentLinkedQueue<Musteri> musteriSirasi;
	ConcurrentLinkedQueue<Musteri> tumMusteriler;
	ConcurrentLinkedQueue<Siparis> siparisSirasi;
	ConcurrentLinkedQueue<Musteri> kasaSirasi;
	
	Garson[] garsonlar;
	Asci[] ascilar;
	Kasa[] kasalar;
	Window window = null;
	
	FileWriter fw; 
	
	public Restoran(boolean adimAdimMi) {
		this.adimAdimMi = adimAdimMi;
		this.restoranAcikMi = true;
		this.adimda = false;
		baslangic = System.currentTimeMillis();
		try {
			fw = new FileWriter(new File("bado.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		musteriSirasi = new ConcurrentLinkedQueue<Musteri>();
		tumMusteriler = new ConcurrentLinkedQueue<Musteri>();
		siparisSirasi = new ConcurrentLinkedQueue<Siparis>();
		kasaSirasi = new ConcurrentLinkedQueue<Musteri>();
		
		garsonlar = new Garson[garsonSayisi];
		ascilar = new Asci[asciSayisi];
		kasalar = new Kasa[kasaSayisi];
		
	}
	
	private void isYerlestir() {
		for(int i = 0; i < garsonSayisi; i++) {
			garsonlar[i] = new Garson(i, fw, adimAdimMi, masaSayisi, tumMusteriler, musteriSirasi, siparisSirasi);
			garsonlar[i].start();
		}
		for(int i = 0; i < asciSayisi; i++) {
			ascilar[i] = new Asci(i, fw,  adimAdimMi, tumMusteriler, siparisSirasi);
			ascilar[i].start();
		}
		for(int i = 0; i < kasaSayisi; i++) {
			kasalar[i] = new Kasa(i, fw, adimAdimMi, kasaSirasi, tumMusteriler, this);
			kasalar[i].start();
		}
	}
	
	private void garsonBitir() {
		for(int i = 0; i < garsonSayisi; i++) {
			garsonlar[i].setCalisiyor(false);
			try {
				garsonlar[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void paraEkle() {
		kasadakiPara += 1;
	}
	
	
	private void asciBitir() {
		for(int i = 0; i < asciSayisi; i++) {
			ascilar[i].setCalisiyor(false);
			try {
				ascilar[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void kasaBitir() {
		for(int i = 0; i < kasaSayisi; i++) {
			kasalar[i].setCalisiyor(false);
			try {
				kasalar[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void restoranBitir() {
		garsonBitir();
		asciBitir();
		kasaBitir();
		int toplamKjaer = kasadakiPara - masaSayisi - asciSayisi - garsonSayisi;
		System.out.println(" ToplamKjaer = "+ toplamKjaer );
		try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void musteriBitir() {
		for(Musteri musteri : tumMusteriler) {
			musteri.setDurum(Musteri.Durum.AYRILIYOR);
			try {
				musteri.join(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(Musteri musteri : musteriSirasi) {
			musteri.setDurum(Musteri.Durum.AYRILIYOR);
			try {
				musteri.join(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void adimsizRestoranBasla() {
		long anlikZaman = System.currentTimeMillis();
		
		long gecenZaman = anlikZaman - baslangic;
		int musteriSayisi = 0;
		while(gecenZaman < 15000 || restorandaMusteriVarMi()) {
			anlikZaman = System.currentTimeMillis();
			gecenZaman = anlikZaman - baslangic;
			if(gecenZaman % 4000 == 0 && gecenZaman < 15000) {
				int musteriUstLimit = musteriSayisi + 3;
				for(; musteriSayisi < musteriUstLimit; musteriSayisi++) {
					Musteri yeniMusteri = new Musteri(musteriSayisi, fw, adimAdimMi, kasaSirasi, musteriSirasi);
					yeniMusteri.start();
					musteriSirasi.offer(yeniMusteri);
				}
			}
		}
		
		restoranAcikMi = false ;
		
	}
	
	@Override
	public void run() {
		
		while(restoranAcikMi) {
			if(adimAdimMi)
				adimliRestoranBasla();
			else 
				adimsizRestoranBasla();
			
			window.update();
		}
		restoranBitir();
		
		
	}
	
	public void startRestoran() {
		isYerlestir();
	}
	
	private void adimliRestoranBasla() {
			if(adimda) {
				
				for(Garson garson : garsonlar) {
					garson.setDuraklama(false);
				}
				for(Asci asci : ascilar) {
					asci.setDuraklama(false);
				}
				for(Kasa kasa : kasalar) {
					kasa.setDuraklama(false);
				}
				for(Iterator<Musteri> it = tumMusteriler.iterator(); it.hasNext();) {
					Musteri m = it.next();
					m.setDuraklama(false);
				}
				for(Iterator<Musteri> it = musteriSirasi.iterator(); it.hasNext();) {
					Musteri m = it.next();
					m.setDuraklama(false);
				}
				int musteriSayisi = toplamMusteriSayisi;
				int ustMusteriLimit = musteriSayisi + 3;
				for(int i = musteriSayisi; i < ustMusteriLimit; i++) {
					Musteri yeniMusteri = new Musteri(i, fw, adimAdimMi, kasaSirasi, musteriSirasi);
					yeniMusteri.start();
					musteriSirasi.add(yeniMusteri);
					toplamMusteriSayisi = ustMusteriLimit;
				}
				
				adimda = false;
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	
	public void adimAt() {
		adimda = true;
		
	}
	
	private boolean restorandaMusteriVarMi() {
		return (musteriSirasi.size() > 0) || (tumMusteriler.size() > 0);
	}

	public ConcurrentLinkedQueue<Musteri> getMusteriSirasi() {
		return musteriSirasi;
	}

	public void setMusteriSirasi(ConcurrentLinkedQueue<Musteri> musteriSirasi) {
		this.musteriSirasi = musteriSirasi;
	}

	public ConcurrentLinkedQueue<Musteri> getTumMusteriler() {
		return tumMusteriler;
	}

	public void setTumMusteriler(ConcurrentLinkedQueue<Musteri> tumMusteriler) {
		this.tumMusteriler = tumMusteriler;
	}

	public ConcurrentLinkedQueue<Siparis> getSiparisSirasi() {
		return siparisSirasi;
	}

	public void setSiparisSirasi(ConcurrentLinkedQueue<Siparis> siparisSirasi) {
		this.siparisSirasi = siparisSirasi;
	}

	public ConcurrentLinkedQueue<Musteri> getKasaSirasi() {
		return kasaSirasi;
	}

	public void setKasaSirasi(ConcurrentLinkedQueue<Musteri> kasaSirasi) {
		this.kasaSirasi = kasaSirasi;
	}
	
	public void setWindow(Window window) {
		this.window = window;
	}
	
}
