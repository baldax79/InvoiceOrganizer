package window;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import database.DBManager;

public class WindowPagamento extends WindowMethods {
	
	public final String[] PERIODI = { "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre" };
	public final String[] TIPOLOGIE = { "Telefono", "Egea", "Pulizie", "UtilizzoLocali", "Luce", "Acqua", "SpeseCondominiali", "Manutenzione", "TARI", "IMU" };
	public final String[] ZONE = { "Novara", "Vercelli", "Verbania", "Biella" };

    private DBManager db;
	
	public WindowPagamento(DBManager db) {
		this.db = db;
		
		window();
	}
	
	private void window() {
		JFrame frame = new JFrame("Pagamento bolletta");
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        
        JPanel panelZona = new JPanel(new GridLayout(1, 2));
        JPanel panelTipologia = new JPanel(new GridLayout(1, 2));
        JPanel panelPeriodo = new JPanel(new GridLayout(1, 2));
        JPanel panelBottone = new JPanel(null);
        
        JPanel panelZonaLabel = new JPanel(new GridLayout(1, 1));
        JPanel panelTipologiaLabel = new JPanel(new GridLayout(1, 1));
        JPanel panelPeriodoLabel = new JPanel(new GridLayout(1, 1));
        
        JPanel panelZonaText = new JPanel(null);
        JPanel panelTipologiaText = new JPanel(null);
        JPanel panelPeriodoText = new JPanel(new GridLayout(1, 2));
        
        JPanel panelPeriodo1Text = new JPanel(null);
        JPanel panelPeriodo2Text = new JPanel(null);
        
        JLabel labelZona = new JLabel("Zona", SwingConstants.CENTER);
        JLabel labelTipologia = new JLabel("Tipologia", SwingConstants.CENTER);
        JLabel labelPeriodo = new JLabel("Periodo", SwingConstants.CENTER);
        
        JComboBox<String> textZona = new JComboBox<>(ZONE);
        JComboBox<String> textTipologia = new JComboBox<>(TIPOLOGIE);
        JComboBox<String> textPeriodo1 = new JComboBox<>(PERIODI);
        JComboBox<String> textPeriodo2 = new JComboBox<>(PERIODI);
        
        JButton bottone = new JButton("Paga");
        
        panelZonaLabel.setBackground(Color.WHITE);
        labelZona.setFont(new Font("Arial", Font.BOLD, 25));
        panelTipologiaLabel.setBackground(Color.WHITE);
        labelTipologia.setFont(new Font("Arial", Font.BOLD, 25));
        panelPeriodoLabel.setBackground(Color.WHITE);
        labelPeriodo.setFont(new Font("Arial", Font.BOLD, 25));
        
        panelZonaText.setBackground(Color.WHITE);
        textZona.setBounds(panelZonaText.getX() + 50, panelZonaText.getY() + 21, 150, 40);
        textZona.setFont(new Font("Arial", Font.PLAIN, 20));
        
        panelTipologiaText.setBackground(Color.WHITE);
        textTipologia.setBounds(panelTipologiaText.getX() + 50, panelTipologiaText.getY() + 21, 150, 40);
        textTipologia.setFont(new Font("Arial", Font.PLAIN, 20));
        
        panelPeriodo1Text.setBackground(Color.WHITE);
        textPeriodo1.setBounds(panelPeriodo1Text.getX() + 13, panelPeriodo1Text.getY() + 21, 100, 40);
        textPeriodo1.setFont(new Font("Arial", Font.PLAIN, 20));
        
        panelPeriodo2Text.setBackground(Color.WHITE);
        textPeriodo2.setBounds(panelPeriodo2Text.getX() + 12, panelPeriodo2Text.getY() + 21, 100, 40);
        textPeriodo2.setFont(new Font("Arial", Font.PLAIN, 20));
        
        panelBottone.setBackground(Color.WHITE);
        bottone.setBounds(panelBottone.getX() + 100, panelBottone.getY() + 15, 300, 40);
        bottone.setFont(new Font("Arial", Font.BOLD, 20));
		
		panelZonaLabel.add(labelZona);
		panelTipologiaLabel.add(labelTipologia);
		panelPeriodoLabel.add(labelPeriodo);
		
		panelZonaText.add(textZona);
		panelTipologiaText.add(textTipologia);
		panelPeriodo1Text.add(textPeriodo1);
		panelPeriodo2Text.add(textPeriodo2);
		
		panelPeriodoText.add(panelPeriodo1Text);
		panelPeriodoText.add(panelPeriodo2Text);
		
		panelZona.add(panelZonaLabel);
		panelZona.add(panelZonaText);
		panelTipologia.add(panelTipologiaLabel);
		panelTipologia.add(panelTipologiaText);
		panelPeriodo.add(panelPeriodoLabel);
		panelPeriodo.add(panelPeriodoText);
		
		panelBottone.add(bottone);
		
		panel.add(panelZona);
		panel.add(panelTipologia);
		panel.add(panelPeriodo);
		panel.add(panelBottone);
		
		bottone.addActionListener(e -> {
			String zona = (String) textZona.getSelectedItem();
			String tipologia = (String) textTipologia.getSelectedItem();
			String periodo = textPeriodo1.getSelectedItem() + "/" + textPeriodo2.getSelectedItem();
			
			for (String p : getPeriodi(periodo).split("/")) db.payBolletta(p, zona, tipologia);
			
			frame.dispose();
		});
		
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        frame.getRootPane().getActionMap().put("Enter", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				String zona = (String) textZona.getSelectedItem();
				String tipologia = (String) textTipologia.getSelectedItem();
				String periodo = textPeriodo1.getSelectedItem() + "/" + textPeriodo2.getSelectedItem();
				
				for (String p : getPeriodi(periodo).split("/")) db.payBolletta(p, zona, tipologia);
				
				frame.dispose();
        	}
        });

        frame.getContentPane().add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
        
        closeWindowWithKey(frame);
        
        frame.setIconImage(new ImageIcon("resources/icon.png").getImage());
	}
}
