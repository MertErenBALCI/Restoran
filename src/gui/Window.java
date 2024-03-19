package gui;

import javax.swing.*;

import main.Restoran;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    GarsonMenu contentPanel1;
    AsciMenu contentPanel2;
    JButton b;

    public Window(Restoran restoran) {
        setTitle("Side-by-Side Menu Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        JPanel menuPanel = new JPanel(new GridLayout(1, 3));
        JButton menu1Button = new JButton("Garson");
        JButton menu2Button = new JButton("Aşçı");
        JButton menu3Button = new JButton("Kasa");

        menu1Button.addActionListener(new MenuButtonListener("Garson"));
        menu2Button.addActionListener(new MenuButtonListener("Aşçı"));
        menu3Button.addActionListener(new MenuButtonListener("Kasa"));

        menuPanel.add(menu1Button);
        menuPanel.add(menu2Button);
        menuPanel.add(menu3Button);

       
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

       
        contentPanel1 = new GarsonMenu(cardPanel, "Garson", restoran);
        contentPanel2 = new AsciMenu(cardPanel, "Aşçı", restoran);
        JPanel contentPanel3 = new JPanel();
        
        JLabel text1 = new JLabel("2");
        JLabel text2 = new JLabel("1");
        
        contentPanel2.add(text2);
        contentPanel3.add(text1);
        
        b = new JButton("İlerle");
    	contentPanel1.add(b, BorderLayout.SOUTH);
    	b.setVisible(true);

        contentPanel1.revalidate();

       
        cardPanel.add(contentPanel1, "Garson");
        cardPanel.add(contentPanel2, "Aşçı");
        cardPanel.add(contentPanel3, "Kasa");

       
        cardLayout.show(cardPanel, "Garson");

        
        add(menuPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

       
        setSize(600, 400);

       
        setVisible(true);
    }
    
    public JButton getButton() {
    	return b;
    }

    public void update() {
    	contentPanel1.updateLabels();
    	contentPanel2.updateLabels();
    }
    
    private class MenuButtonListener implements ActionListener {
        private String menuName;

        public MenuButtonListener(String menuName) {
            this.menuName = menuName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
           
            cardLayout.show(cardPanel, menuName);
        }
    }

}