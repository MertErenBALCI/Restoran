package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import gui.Window;
import insanlar.*;

public class MainClass {

	public static void main(String[] args) {

       
		Restoran r = new Restoran(true);
        Window w = new Window(r);
        r.setWindow(w);
		JButton b = w.getButton();
			b.addActionListener(
						new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								
								
								r.adimAt();
							}
						}
					
					);
		
		r.startRestoran();
		r.start();
	}

}
