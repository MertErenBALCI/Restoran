package gui;

import javax.swing.JComponent;
import javax.swing.JPanel;

import main.Restoran;

public class AnaPanel extends JPanel {

	Restoran restoran;
	
	public AnaPanel(JComponent parent, String name, Restoran restoran) {
		super();
		parent.add(this, name);
		this.restoran = restoran;
	}
	
}
