package yardimci;

import insanlar.Musteri;

public class Siparis {
	
	Musteri musteri;
	long siparisBaslamaZamani;
	
	public Siparis(Musteri musteri) {
		this.musteri = musteri;
		this.siparisBaslamaZamani = 0;
	}
	
	public void setSiparisBaslamaZamani(long siparisBaslamaZamani) {
		this.siparisBaslamaZamani = siparisBaslamaZamani;
	}
	public long getSiparisBaslamaZamani() {
		return siparisBaslamaZamani;
	}
	public Musteri getMusteri() {
		return musteri;
	}
}
