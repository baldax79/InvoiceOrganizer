package window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import database.DBManager;

public class WindowRipartizioni extends WindowMethods {

	public final String[] COLONNERIPARTIZIONI = { "Zona", "Categoria", "Millesimi", "Telefono", "Egea", "Pulizie", "Utilizzo locali", "Luce", "Acqua", "Spese condominiali", "Manutenzione", "TARI", "IMU" };
	public final String[] PERIODI = { "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre" };
	public final String[] ZONE = { "Novara", "Vercelli", "Verbania", "Biella" };
	
	private DefaultTableModel model;
    private JTable table;
    private DBManager db;
    private JFrame firstFrame;
	
	public WindowRipartizioni(DBManager db) {
		this.db = db;
		
		window();
	}
	
	private void window() {
		firstFrame = new JFrame();
		
		firstFrame.setSize(1000, 500);
		firstFrame.setTitle("Ripartizioni");
		firstFrame.setLocationRelativeTo(null);
        
        menuBar();
        tables();

        firstFrame.setResizable(true);
        firstFrame.setVisible(true);
        
        firstFrame.setIconImage(new ImageIcon("resources/icon.png").getImage());
	}
	
	private void menuBar() {
		JMenuBar bar = new JMenuBar();
		
		JMenu visualizza = new JMenu("Visualizza");
		JMenu ricerca = new JMenu("Ricerca");
		
		LinkedList<JMenuItem> visualizzaPeriodi = new LinkedList<>();
		for (String mese : PERIODI) visualizzaPeriodi.add(new JMenuItem(mese));
		
		JMenuItem zona = new JMenuItem("Zona");
		JMenuItem categoria = new JMenuItem("Categoria");
		
		firstFrame.setJMenuBar(bar);

        bar.setPreferredSize(new Dimension(1000, 30));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
        
        bar.add(visualizza).setFont(new Font("Arial", Font.BOLD, 15));
        bar.add(ricerca).setFont(new Font("Arial", Font.BOLD, 15));
        
        for (JMenuItem item : visualizzaPeriodi) visualizza.add(item).setFont(new Font("Arial", Font.BOLD, 15));
        
        ricerca.add(zona).setFont(new Font("Arial", Font.BOLD, 15));
        ricerca.add(categoria).setFont(new Font("Arial", Font.BOLD, 15));

        for (JMenuItem item : visualizzaPeriodi) {
        	item.addActionListener(e -> {
        		model = new DefaultTableModel(db.getRipartizioni(item.getText()), COLONNERIPARTIZIONI);
        		table.setModel(model);
        	});
        }
        
		zona.addActionListener(e -> searchZonaRipartizioni());
		categoria.addActionListener(e -> searchCategoriaRipartizioni());
	}
	
	private void tables() {
		JPanel panel = new JPanel(new GridLayout(1, 1));

        model = new DefaultTableModel(db.getRipartizioni("Gennaio"), COLONNERIPARTIZIONI);
        table = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(table);

        table.setBackground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setAutoCreateRowSorter(true);

        tableScrollPane.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK));

        panel.add(tableScrollPane);
		
        firstFrame.getContentPane().add(panel);
	}
	
	public void searchZonaRipartizioni() {
		JFrame frame = new JFrame("Ricerca zona");
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        
        JPanel panelZona = new JPanel(new GridLayout(1, 2));
        JPanel panelPeriodo = new JPanel(new GridLayout(1, 2));
        JPanel panelBottone = new JPanel(null);
        
        JPanel panelZonaLabel = new JPanel(new GridLayout(1, 1));
        JPanel panelPeriodoLabel = new JPanel(new GridLayout(1, 1));
        
        JPanel panelZonaText = new JPanel(null);
        JPanel panelPeriodoText = new JPanel(null);
        
        JLabel labelZona = new JLabel("Zona", SwingConstants.CENTER);
        JLabel labelPeriodo = new JLabel("Periodo", SwingConstants.CENTER);
        
        JComboBox<String> textZona = new JComboBox<>(ZONE);
        JComboBox<String> textPeriodo = new JComboBox<>(PERIODI);
        
        JButton bottone = new JButton("Ricerca");
        
        panelZonaLabel.setBackground(Color.WHITE);
        labelZona.setFont(new Font("Arial", Font.BOLD, 25));
        panelPeriodoLabel.setBackground(Color.WHITE);
        labelPeriodo.setFont(new Font("Arial", Font.BOLD, 25));
        
        panelZonaText.setBackground(Color.WHITE);
        textZona.setBounds(panelZonaText.getX() + 50, panelZonaText.getY() + 21, 150, 40);
        textZona.setFont(new Font("Arial", Font.PLAIN, 20));

        panelPeriodoText.setBackground(Color.WHITE);
        textPeriodo.setBounds(panelPeriodoText.getX() + 50, panelPeriodoText.getY() + 21, 150, 40);
        textPeriodo.setFont(new Font("Arial", Font.PLAIN, 20));
        
        panelBottone.setBackground(Color.WHITE);
        bottone.setBounds(panelBottone.getX() + 100, panelBottone.getY() + 15, 300, 40);
        bottone.setFont(new Font("Arial", Font.BOLD, 20));
		
		panelZonaLabel.add(labelZona);
		panelPeriodoLabel.add(labelPeriodo);
		
		panelZonaText.add(textZona);
		panelPeriodoText.add(textPeriodo);
		
		panelZona.add(panelZonaLabel);
		panelZona.add(panelZonaText);
		panelPeriodo.add(panelPeriodoLabel);
		panelPeriodo.add(panelPeriodoText);
		panelBottone.add(bottone);
		
		panel.add(panelZona);
		panel.add(panelPeriodo);
		panel.add(panelBottone);
		
		bottone.addActionListener(e -> {
			String zona = (String) textZona.getSelectedItem();
			String periodo = (String) textPeriodo.getSelectedItem();
			
			model = new DefaultTableModel(db.searchRipartizioniByZona(zona, periodo), COLONNERIPARTIZIONI);
            table.setModel(model);

            frame.dispose();
		});
		
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        frame.getRootPane().getActionMap().put("Enter", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				String zona = (String) textZona.getSelectedItem();
				String periodo = (String) textPeriodo.getSelectedItem();
				
				model = new DefaultTableModel(db.searchRipartizioniByZona(zona, periodo), COLONNERIPARTIZIONI);
	            table.setModel(model);

	            frame.dispose();
        	}
        });

        frame.getContentPane().add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
        
        closeWindowWithKey(frame);
        
        frame.setIconImage(new ImageIcon("resources/icon.png").getImage());
	}
	
	public void searchCategoriaRipartizioni() {
		JFrame frame = new JFrame("Ricerca categoria");
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        
        JPanel panelCategoria = new JPanel(new GridLayout(1, 2));
        JPanel panelPeriodo = new JPanel(new GridLayout(1, 2));
        JPanel panelBottone = new JPanel(null);
        
        JPanel panelCategoriaLabel = new JPanel(new GridLayout(1, 1));
        JPanel panelPeriodoLabel = new JPanel(new GridLayout(1, 1));
        
        JPanel panelCategoriaText = new JPanel(null);
        JPanel panelPeriodoText = new JPanel(null);
        
        JLabel labelCategoria = new JLabel("Categoria", SwingConstants.CENTER);
        JLabel labelPeriodo = new JLabel("Periodo", SwingConstants.CENTER);
        
        JComboBox<String> textCategoria = new JComboBox<>(convertToVector(db.getCategorie()));
        JComboBox<String> textPeriodo = new JComboBox<>(PERIODI);
        
        JButton bottone = new JButton("Ricerca");
        
        panelCategoriaLabel.setBackground(Color.WHITE);
        labelCategoria.setFont(new Font("Arial", Font.BOLD, 25));
        panelPeriodoLabel.setBackground(Color.WHITE);
        labelPeriodo.setFont(new Font("Arial", Font.BOLD, 25));
        
        panelCategoriaText.setBackground(Color.WHITE);
        textCategoria.setBounds(panelCategoriaText.getX() + 50, panelCategoriaText.getY() + 21, 150, 40);
        textCategoria.setFont(new Font("Arial", Font.PLAIN, 20));
        
        panelPeriodoText.setBackground(Color.WHITE);
        textPeriodo.setBounds(panelPeriodoText.getX() + 50, panelPeriodoText.getY() + 21, 150, 40);
        textPeriodo.setFont(new Font("Arial", Font.PLAIN, 20));
        
        panelBottone.setBackground(Color.WHITE);
        bottone.setBounds(panelBottone.getX() + 100, panelBottone.getY() + 15, 300, 40);
        bottone.setFont(new Font("Arial", Font.BOLD, 20));
		
		panelCategoriaLabel.add(labelCategoria);
		panelPeriodoLabel.add(labelPeriodo);
		
		panelCategoriaText.add(textCategoria);
		panelPeriodoText.add(textPeriodo);
		
		panelCategoria.add(panelCategoriaLabel);
		panelCategoria.add(panelCategoriaText);
		panelPeriodo.add(panelPeriodoLabel);
		panelPeriodo.add(panelPeriodoText);
		panelBottone.add(bottone);
		
		panel.add(panelCategoria);
		panel.add(panelPeriodo);
		panel.add(panelBottone);
		
		bottone.addActionListener(e -> {
			String categoria = (String) textCategoria.getSelectedItem();
			String periodo = (String) textPeriodo.getSelectedItem();
			
			model = new DefaultTableModel(db.searchRipartizioniByCategoria(categoria, periodo), COLONNERIPARTIZIONI);
            table.setModel(model);

            frame.dispose();
		});
		
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        frame.getRootPane().getActionMap().put("Enter", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				String categoria = (String) textCategoria.getSelectedItem();
				String periodo = (String) textPeriodo.getSelectedItem();
				
				model = new DefaultTableModel(db.searchRipartizioniByCategoria(categoria, periodo), COLONNERIPARTIZIONI);
	            table.setModel(model);

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
