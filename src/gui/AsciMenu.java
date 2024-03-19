package gui;

import java.awt.GridLayout;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import insanlar.Musteri;
import main.Restoran;
import yardimci.Siparis;

public class AsciMenu extends AnaPanel {

	JLabel[] siparisSirasi;
	
	public AsciMenu(JComponent parent, String name, Restoran restoran) {
		super(parent, name, restoran);
		siparisSirasi = new JLabel[3];
		
		this.setLayout(new GridLayout(1, 3));
		
		for(int i = 0; i < 3 ; i++) {
			siparisSirasi[i] = new JLabel();
			this.add(siparisSirasi[i]);
		}
		
	}
	
	
	public void updateLabels() {
		int i = 0;
		for(Iterator<Siparis> it = restoran.getSiparisSirasi().iterator(); i < 3; i++) {
			if(it.hasNext()) {
				Siparis siparis = it.next();
				if(siparis == null) break;
				
				Musteri m = siparis.getMusteri();
				siparisSirasi[i].setText("Musteri " + m.getID() + "'nin siparişi");
			}
			
		}
	}
	
}
