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

public class WindowPDF extends WindowMethods {

	public final String[] PERIODI = { "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre" };

    private DBManager db;
	
	public WindowPDF(DBManager db) {
		this.db = db;
		
		window();
	}
	
	private void window() {
		JFrame frame = new JFrame("PDF");
        frame.setSize(500, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        
        JPanel panelPeriodo = new JPanel(new GridLayout(1, 2));
        JPanel panelBottone = new JPanel(null);
        
        JPanel panelPeriodoLabel = new JPanel(new GridLayout(1, 1));
        JPanel panelPeriodoText = new JPanel(null);
        
        JLabel labelPeriodo = new JLabel("Periodo", SwingConstants.CENTER);
        JComboBox<String> textPeriodo = new JComboBox<>(PERIODI);
        
        JButton bottone = new JButton("Crea PDF");
        
        panelPeriodoLabel.setBackground(Color.WHITE);
        labelPeriodo.setFont(new Font("Arial", Font.BOLD, 25));
        
        panelPeriodoText.setBackground(Color.WHITE);
        textPeriodo.setBounds(panelPeriodoText.getX() + 50, panelPeriodoText.getY() + 21, 150, 40);
        textPeriodo.setFont(new Font("Arial", Font.PLAIN, 20));
        
        panelBottone.setBackground(Color.WHITE);
        bottone.setBounds(panelBottone.getX() + 100, panelBottone.getY() + 15, 300, 40);
        bottone.setFont(new Font("Arial", Font.BOLD, 20));
		
		panelPeriodoLabel.add(labelPeriodo);
		panelPeriodoText.add(textPeriodo);
		
		panelPeriodo.add(panelPeriodoLabel);
		panelPeriodo.add(panelPeriodoText);
		
		panelBottone.add(bottone);
		
		panel.add(panelPeriodo);
		panel.add(panelBottone);
		
		bottone.addActionListener(e -> {
			String periodo = (String) textPeriodo.getSelectedItem();
			
			db.saveData(periodo);
			
			frame.dispose();
		});
		
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        frame.getRootPane().getActionMap().put("Enter", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				String periodo = (String) textPeriodo.getSelectedItem();
				
				db.saveData(periodo);
				
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
