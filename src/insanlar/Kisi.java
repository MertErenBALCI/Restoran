package insanlar;

public abstract class Kisi extends Thread {
	
	int id;
	volatile boolean duraklama;
	boolean adimAdimMi;
	
	public Kisi(int id, boolean adimAdimMi) {
		this.id = id;
		this.duraklama = false;
		this.adimAdimMi = adimAdimMi;
	}
	
	public int getID() {
		return id;
	}
	
	public void setDuraklama(boolean duraklama) {
		this.duraklama = duraklama;
	}
	
	public void run() {
			
	}
}
