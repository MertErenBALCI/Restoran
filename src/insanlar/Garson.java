package insanlar;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import yardimci.Siparis;

public class Garson extends Kisi {
	
	FileWriter fw;
	
	ConcurrentLinkedQueue<Musteri> garsonMusterileri;
	ConcurrentLinkedQueue<Musteri> restoranMusterileri;
	ConcurrentLinkedQueue<Musteri> musteriSirasi;
	ConcurrentLinkedQueue<Siparis> siparisSirasi;
	int masaSayisi;
	
	boolean calisiyor;
	
	enum Durum {
		BOSTA,
		SIPARIS_ALIYOR,
		SIPARIS_ULASTIRIYOR
	};
	
	Durum durum;
	public Garson(int id, FileWriter fw, boolean adimAdimMi, int masaSayisi, ConcurrentLinkedQueue<Musteri> tumMusteriler, ConcurrentLinkedQueue<Musteri> musteriSirasi, ConcurrentLinkedQueue<Siparis> siparisSirasi) {
		super(id, adimAdimMi);
		this.masaSayisi = masaSayisi;
		this.musteriSirasi = musteriSirasi;
		this.siparisSirasi = siparisSirasi;
		this.restoranMusterileri = tumMusteriler;
		this.fw = fw;
		this.garsonMusterileri = new ConcurrentLinkedQueue<Musteri>();
		this.calisiyor = true;
		durum = Durum.BOSTA;
	}

	
	@Override
	public void run() {
		while(calisiyor) {
			while(duraklama) {
				
			}
			
			
			
			switch(durum) {
			case BOSTA:
				garsonBosta();
				break;
				
			case SIPARIS_ALIYOR:
				garsonSiparisAliyor();
				break;
				
			case SIPARIS_ULASTIRIYOR:
				break;
			
			}
			if(adimAdimMi) {
				duraklama = true;
			}
			
		}
	}
	
	private synchronized Musteri siradanMusteriAl() {
		Musteri musteri = musteriSirasi.peek();
		if(musteri != null) {
			if(musteri.getDurum() == Musteri.Durum.MASA_BEKLEMEDE) {
				musteri.setDurum(Musteri.Durum.SIPARIS_BEKLEMEDE);
				return musteriSirasi.poll();
			}
		}
		
		return null;
	}
	
	private void garsonBosta() {
		if(restoranMusterileri.size() < masaSayisi) {
			Musteri musteri = siradanMusteriAl();
			if(musteri != null) {
				System.out.println("Musteri " + musteri.getID() + ", Garson " + id + " için eklendi.");
				try {
					fw.write("Musteri " + musteri.getID() + ", Garson " + id + " için eklendi.");
					fw.write('\n');
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				garsonMusterileri.offer(musteri);
				restoranMusterileri.offer(musteri);
			}
		}
		durum = Durum.SIPARIS_ALIYOR;
	}
	
	public void garsonSiparisAliyor() {
		Musteri musteri = garsonMusterileri.poll();
		if(musteri != null) {

			System.out.println(String.format("Garson %d, Musteri %d'nin siparişini aldı.", id, musteri.id));
			try {
				fw.write(String.format("Garson %d, Musteri %d'nin siparişini aldı.", id, musteri.id));
				fw.write('\n');
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			musteri.setDurum(Musteri.Durum.SIPARIS_BEKLEMEDE);
			siparisSirasi.offer(new Siparis(musteri));
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			durum = Durum.BOSTA;
			
		}
		else {
			durum = Durum.BOSTA;
		}
	}
	
	public void setCalisiyor(boolean running) {
		this.calisiyor = running;
	}
	public boolean getCalisiyor() {
		return calisiyor;
	}
	
}
