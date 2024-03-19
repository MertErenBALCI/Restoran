package gui;

import java.awt.GridLayout;
import java.util.Iterator;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import insanlar.Musteri;
import main.Restoran;

public class GarsonMenu extends AnaPanel {
	
	JLabel[] musteriSirasi;
	
	public GarsonMenu(JComponent parent, String name, Restoran restoran) {
		super(parent, name, restoran);
		musteriSirasi = new JLabel[3];
		
		this.setLayout(new GridLayout(1, 3));
		
		for(int i = 0; i < 3 ; i++) {
			musteriSirasi[i] = new JLabel();
			this.add(musteriSirasi[i]);
		}
		
	}
	
	
	public void updateLabels() {
		int i = 0;
		for(Iterator<Musteri> it = restoran.getMusteriSirasi().iterator(); i < 3; i++) {
			if(it.hasNext()) {
				Musteri musteri = it.next();
				if(musteri == null) break;
				
				musteriSirasi[i].setText("Musteri " + musteri.getID());
			}
			
		}
	}
	
}
