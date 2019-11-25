package window;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import database.DBManager;
import main.AutoCompleteTextField;
import main.DateLabelFormatter;

public class WindowFatture extends WindowMethods {
	
	public final String[] COLONNEFATTURE = { "ID", "Fornitore", "Data", "Data scadenza", "Importo" };
	public final String[] PERIODI = { "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre" };
	public final String[] TIPOLOGIE = { "Telefono", "Egea", "Pulizie", "UtilizzoLocali", "Luce", "Acqua", "SpeseCondominiali", "Manutenzione", "TARI", "IMU" };
	public final String[] ZONE = { "Novara", "Vercelli", "Verbania", "Biella" };
	
	private DefaultTableModel model;
    private JTable table;
    private DBManager db;
    private JFrame firstFrame;
    private JFileChooser fc;
	
	public WindowFatture(DBManager db) {
		this.db = db;
		fc = new JFileChooser();
		
		window();
	}
	
	private void window() {
		firstFrame = new JFrame();
		
		firstFrame.setSize(1000, 500);
		firstFrame.setTitle("Fatture");
		firstFrame.setLocationRelativeTo(null);
        
        menuBar();
        tables();

        firstFrame.setResizable(true);
        firstFrame.setVisible(true);
        
        firstFrame.setIconImage(new ImageIcon("resources/icon.png").getImage());
	}
	
	private void menuBar() {
		JMenuBar bar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenu ricerca = new JMenu("Ricerca");
		
		JMenuItem aggiungi = new JMenuItem("Aggiungi");
		JMenuItem esci = new JMenuItem("Esci");
		
		JMenuItem periodo = new JMenuItem("Periodo");
		JMenuItem fornitore = new JMenuItem("Fornitore");
		JMenuItem dataScadenza = new JMenuItem("Data scadenza");
		
		firstFrame.setJMenuBar(bar);

        bar.setPreferredSize(new Dimension(1000, 30));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
        
        bar.add(file).setFont(new Font("Arial", Font.BOLD, 15));
        bar.add(ricerca).setFont(new Font("Arial", Font.BOLD, 15));
        
        file.add(aggiungi).setFont(new Font("Arial", Font.BOLD, 15));
        file.add(esci).setFont(new Font("Arial", Font.BOLD, 15));
        
        ricerca.add(periodo).setFont(new Font("Arial", Font.BOLD, 15));
        ricerca.add(fornitore).setFont(new Font("Arial", Font.BOLD, 15));
        ricerca.add(dataScadenza).setFont(new Font("Arial", Font.BOLD, 15));
        
        aggiungi.addActionListener(e -> addFattura());
        esci.addActionListener(e -> firstFrame.dispose());
        
		periodo.addActionListener(e -> {
			if (db.getAllFatture().size() == 0) new WindowErrore("Non ci sono fatture da ricercare");
			else searchPeriodoFatture();
        });
		fornitore.addActionListener(e -> {
			if (db.getAllFatture().size() == 0) new WindowErrore("Non ci sono fatture da ricercare");
			else searchFornitoreFatture();
		});
		dataScadenza.addActionListener(e -> {
			if (db.getAllFatture().size() == 0) new WindowErrore("Non ci sono fatture da ricercare");
			else searchDataScadenzaFatture();
		});
	}
	
	private void tables() {
		JPanel panel = new JPanel(new GridLayout(1, 1));

        model = new DefaultTableModel(db.getFatture(), COLONNEFATTURE);
        table = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(table);

        table.setBackground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setAutoCreateRowSorter(true);
        
        addPopupMenu(table);

        tableScrollPane.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK));

        panel.add(tableScrollPane);
		
        firstFrame.getContentPane().add(panel);
	}

	private void addPopupMenu(JTable table) {
		JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem removePM = new JMenuItem("Rimuovi");
        JMenuItem filePM = new JMenuItem("Visualizza file");
        
        popupMenu.add(removePM);
        popupMenu.add(filePM);
        
        table.setComponentPopupMenu(popupMenu);
        
        removePM.addActionListener(e -> {
    		int row = table.getSelectedRow();
    		
    		if (table.isRowSelected(row)) {
    			String id = (String) table.getModel().getValueAt(row, 0);
    			
    			alertWindowRemove(id);
    		} else new WindowErrore("Selezionare una riga.");
    	});
        filePM.addActionListener(e -> {
    		int row = table.getSelectedRow();
    		
    		if (table.isRowSelected(row)) {
    			if (Desktop.isDesktopSupported()) {
        		    try {
        		    	String id = (String) model.getValueAt(row, 0);
        		        File myFile = new File(db.getPathFattura(id));
        		        try {
        		        	Desktop.getDesktop().open(myFile);
        		        } catch (IllegalArgumentException exx) {
        		        	new WindowErrore("Il file e' stato spostato. Si prega di reinserire la fattura.");
        		        }
        		    } catch (IOException ex) {
        		    	ex.printStackTrace();
        		    }
        		}
    		} else new WindowErrore("Selezionare una riga.");
    	});
	}
	
	private void addFattura() {
		JFrame frame = new JFrame("Aggiungi fattura");
		frame.setSize(600, 900);
        frame.setLocationRelativeTo(null);
        
		JPanel panel = new JPanel(new GridLayout(9, 1));
		panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
		
		JPanel panelID = new JPanel(new GridLayout(1, 2));
		JPanel panelFornitore = new JPanel(new GridLayout(1, 2));
		JPanel panelData = new JPanel(new GridLayout(1, 2));
		JPanel panelDataScandenza = new JPanel(new GridLayout(1, 2));
		JPanel panelImporto = new JPanel(new GridLayout(1, 2));
		JPanel panelPeriodo = new JPanel(new GridLayout(1, 2));
		JPanel panelZona = new JPanel(new GridLayout(1, 2));
		JPanel panelFile = new JPanel(new GridLayout(1, 2));
		JPanel panelBottone = new JPanel(null);
		
		JPanel panelIDLabel = new JPanel(new GridLayout(1, 1));
		JPanel panelFornitoreLabel = new JPanel(new GridLayout(1, 1));
		JPanel panelDataLabel = new JPanel(new GridLayout(1, 1));
		JPanel panelDataScandenzaLabel = new JPanel(new GridLayout(1, 1));
		JPanel panelImportoLabel = new JPanel(new GridLayout(1, 1));
		JPanel panelPeriodoLabel = new JPanel(new GridLayout(1, 1));
		JPanel panelZonaLabel = new JPanel(new GridLayout(1, 1));
		JPanel panelFileLabel = new JPanel(new GridLayout(1, 1));
		
		JPanel panelIDText = new JPanel(null);
		JPanel panelFornitoreText = new JPanel(new GridLayout(1, 2));
		JPanel panelDataText = new JPanel(null);
		JPanel panelDataScadenzaText = new JPanel(null);
		JPanel panelImportoText = new JPanel(null);
		JPanel panelPeriodoText = new JPanel(new GridLayout(1, 2));
		JPanel panelZonaText = new JPanel(null);
		JPanel panelFileText = new JPanel(new GridLayout(1, 2));
		
		JPanel panelFornitoreNomeText = new JPanel(null);
		JPanel panelFornitoreTipoText = new JPanel(null);
		JPanel panelPeriodo1Text = new JPanel(null);
		JPanel panelPeriodo2Text = new JPanel(null);
		JPanel panelFilePathText = new JPanel(null);
		JPanel panelFileBottone = new JPanel(null);
		
		JLabel labelID = new JLabel("ID", SwingConstants.CENTER);
		JLabel labelFornitore = new JLabel("Fornitore", SwingConstants.CENTER);
		JLabel labelData = new JLabel("Data", SwingConstants.CENTER);
		JLabel labelDataScandenza = new JLabel("Data scandenza", SwingConstants.CENTER);
		JLabel labelImporto = new JLabel("Importo", SwingConstants.CENTER);
		JLabel labelPeriodo = new JLabel("Periodo", SwingConstants.CENTER);
		JLabel labelZona = new JLabel("Zona", SwingConstants.CENTER);
		JLabel labelFile = new JLabel("File", SwingConstants.CENTER);
		
		JTextField textID = new JTextField(10);
		AutoCompleteTextField textFornitoreNome = new AutoCompleteTextField(10, db.getAllFornitori());
		JComboBox<String> textFornitoreTipo = new JComboBox<>(TIPOLOGIE);
        JDatePickerImpl textData = setDatePicker();
        JDatePickerImpl textDataScadenza = setDatePicker();
        JTextField textImporto = new JTextField(10);
        JComboBox<String> textPeriodo1 = new JComboBox<>(PERIODI);
        JComboBox<String> textPeriodo2 = new JComboBox<>(PERIODI);
        JComboBox<String> textZona = new JComboBox<>(ZONE);
        JTextField textFilePath = new JTextField(10);
        JButton bottoneFile = new JButton("Cerca");
        
        JButton bottone = new JButton("Aggiungi");
        
        panelIDLabel.setBackground(Color.WHITE);
        labelID.setFont(new Font("Arial", Font.BOLD, 25));
        panelFornitoreLabel.setBackground(Color.WHITE);
        labelFornitore.setFont(new Font("Arial", Font.BOLD, 25));
        panelDataLabel.setBackground(Color.WHITE);
        labelData.setFont(new Font("Arial", Font.BOLD, 25));
        panelDataScandenzaLabel.setBackground(Color.WHITE);
        labelDataScandenza.setFont(new Font("Arial", Font.BOLD, 25));
        panelImportoLabel.setBackground(Color.WHITE);
        labelImporto.setFont(new Font("Arial", Font.BOLD, 25));
        panelPeriodoLabel.setBackground(Color.WHITE);
        labelPeriodo.setFont(new Font("Arial", Font.BOLD, 25));
        panelZonaLabel.setBackground(Color.WHITE);
        labelZona.setFont(new Font("Arial", Font.BOLD, 25));
        panelFileLabel.setBackground(Color.WHITE);
        labelFile.setFont(new Font("Arial", Font.BOLD, 25));
        
        panelIDText.setBackground(Color.WHITE);
        textID.setBounds(panelIDText.getX() + 50, panelIDText.getY() + 21, 200, 40);
        textID.setFont(new Font("Arial", Font.BOLD, 20));
		
        panelFornitoreNomeText.setBackground(Color.WHITE);
        textFornitoreNome.setBounds(panelFornitoreNomeText.getX() + 10, panelFornitoreNomeText.getY() + 21, 130, 40);
        textFornitoreNome.setFont(new Font("Arial", Font.BOLD, 20));
        
        panelFornitoreTipoText.setBackground(Color.WHITE);
        textFornitoreTipo.setBounds(panelFornitoreTipoText.getX() + 10, panelFornitoreTipoText.getY() + 21, 130, 40);
        textFornitoreTipo.setFont(new Font("Arial", Font.PLAIN, 20));
        
        panelDataText.setBackground(Color.WHITE);
        textData.setBounds(panel.getX() + 50, panel.getY() + 21, 200, 40);
        textData.getComponent(0).setPreferredSize(new Dimension(160, 40));
        textData.getComponent(1).setPreferredSize(new Dimension(40, 40));
        textData.setBackground(Color.WHITE);
        textData.getComponent(0).setBackground(Color.WHITE);
		JFormattedTextField textD = textData.getJFormattedTextField();
		textD.setFont(new Font("Arial", Font.PLAIN, 20));
		
		panelDataScadenzaText.setBackground(Color.WHITE);
        textDataScadenza.setBounds(panel.getX() + 50, panel.getY() + 21, 200, 40);
        textDataScadenza.getComponent(0).setPreferredSize(new Dimension(160, 40));
        textDataScadenza.getComponent(1).setPreferredSize(new Dimension(40, 40));
        textDataScadenza.setBackground(Color.WHITE);
        textDataScadenza.getComponent(0).setBackground(Color.WHITE);
		JFormattedTextField textDS = textDataScadenza.getJFormattedTextField();
		textDS.setFont(new Font("Arial", Font.PLAIN, 20));
		
		panelImportoText.setBackground(Color.WHITE);
		textImporto.setBounds(panelImportoText.getX() + 50, panelImportoText.getY() + 21, 200, 40);
		textImporto.setFont(new Font("Arial", Font.BOLD, 20));
        
        panelPeriodo1Text.setBackground(Color.WHITE);
        textPeriodo1.setBounds(panelPeriodo1Text.getX() + 10, panelPeriodo1Text.getY() + 21, 130, 40);
        textPeriodo1.setFont(new Font("Arial", Font.PLAIN, 20));
        
        panelPeriodo2Text.setBackground(Color.WHITE);
        textPeriodo2.setBounds(panelPeriodo2Text.getX() + 10, panelPeriodo2Text.getY() + 21, 130, 40);
        textPeriodo2.setFont(new Font("Arial", Font.PLAIN, 20));
        
        panelZonaText.setBackground(Color.WHITE);
        textZona.setBounds(panelZonaText.getX() + 85, panelZonaText.getY() + 21, 130, 40);
        textZona.setFont(new Font("Arial", Font.PLAIN, 20));
        
        panelFilePathText.setBackground(Color.WHITE);
        textFilePath.setBounds(panelFilePathText.getX() + 10, panelFilePathText.getY() + 21, 130, 40);
        textFilePath.setFont(new Font("Arial", Font.BOLD, 20));
        
        panelFileBottone.setBackground(Color.WHITE);
        bottoneFile.setBounds(panelFileBottone.getX() + 10, panelFileBottone.getY() + 21, 130, 40);
        bottoneFile.setFont(new Font("Arial", Font.BOLD, 20));

        panelBottone.setBackground(Color.WHITE);
        bottone.setBounds(panelBottone.getX() + 150, panelBottone.getY() + 15, 300, 40);
        bottone.setFont(new Font("Arial", Font.BOLD, 20));
        
        panelFornitoreNomeText.add(textFornitoreNome);
        panelFornitoreTipoText.add(textFornitoreTipo);
        panelPeriodo1Text.add(textPeriodo1);
        panelPeriodo2Text.add(textPeriodo2);
        panelFilePathText.add(textFilePath);
        panelFileBottone.add(bottoneFile);
        
        panelIDText.add(textID);
        panelFornitoreText.add(panelFornitoreNomeText);
        panelFornitoreText.add(panelFornitoreTipoText);
        panelDataText.add(textData);
        panelDataScadenzaText.add(textDataScadenza);
        panelImportoText.add(textImporto);
        panelPeriodoText.add(panelPeriodo1Text);
        panelPeriodoText.add(panelPeriodo2Text);
        panelZonaText.add(textZona);
        panelFileText.add(panelFilePathText);
        panelFileText.add(panelFileBottone);
        
        panelIDLabel.add(labelID);
        panelFornitoreLabel.add(labelFornitore);
        panelDataLabel.add(labelData);
        panelDataScandenzaLabel.add(labelDataScandenza);
        panelImportoLabel.add(labelImporto);
        panelPeriodoLabel.add(labelPeriodo);
        panelZonaLabel.add(labelZona);
        panelFileLabel.add(labelFile);
        
        panelID.add(panelIDLabel);
        panelID.add(panelIDText);
        panelFornitore.add(panelFornitoreLabel);
        panelFornitore.add(panelFornitoreText);
        panelData.add(panelDataLabel);
        panelData.add(panelDataText);
        panelDataScandenza.add(panelDataScandenzaLabel);
        panelDataScandenza.add(panelDataScadenzaText);
        panelImporto.add(panelImportoLabel);
        panelImporto.add(panelImportoText);
        panelPeriodo.add(panelPeriodoLabel);
        panelPeriodo.add(panelPeriodoText);
        panelZona.add(panelZonaLabel);
        panelZona.add(panelZonaText);
        panelFile.add(panelFileLabel);
        panelFile.add(panelFileText);
        
        panelBottone.add(bottone);
        
        panel.add(panelID);
        panel.add(panelFornitore);
        panel.add(panelData);
        panel.add(panelDataScandenza);
        panel.add(panelImporto);
        panel.add(panelPeriodo);
        panel.add(panelZona);
        panel.add(panelFile);
        panel.add(panelBottone);
        
        bottoneFile.addActionListener(e -> {
        	int result = fc.showOpenDialog(null);
        	
        	if (result == JFileChooser.APPROVE_OPTION) {
        		File file = fc.getSelectedFile();
        		
        		textFilePath.setText(file.getAbsolutePath());
        	}
        });
        
        bottone.addActionListener(e -> {
        	String id = textID.getText();
        	String fornitore = textFornitoreNome.getText() + "/" + textFornitoreTipo.getSelectedItem();
        	String data = textData.getJFormattedTextField().getText();
        	String dataScadenza = textDataScadenza.getJFormattedTextField().getText();
        	String importo = textImporto.getText().replace(",", ".");
        	String periodo = textPeriodo1.getSelectedItem() + "/" + textPeriodo2.getSelectedItem();
        	String zona = (String) textZona.getSelectedItem();
        	String path = textFilePath.getText();
        	
        	if (id.equals("")) new WindowErrore("Inserire l'ID in modo corretto.");
        	else if (fornitore.split("/")[0].equals("")) new WindowErrore("Inserire il fornitore in modo corretto.");
        	else if (data.equals("") || dataScadenza.equals("")) new WindowErrore("Inserire le date in modo corretto.");
        	else if (!db.isAfter(data, dataScadenza)) new WindowErrore("Inserire le date in modo corretto.");
        	else if (!isNumeric(importo)) new WindowErrore("Inserire l'importo in modo correto.");
        	else if (path.equals("")) new WindowErrore("Inserire il file in modo corretto.");
        	else {
        		db.addFattura(id, fornitore, data, dataScadenza, Double.parseDouble(importo), getPeriodi(periodo), zona, path);
				
				model = new DefaultTableModel(db.getFatture(), COLONNEFATTURE);
				table.setModel(model);
				addPopupMenu(table);
				
		    	frame.dispose();
        	}
        });
		
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        frame.getRootPane().getActionMap().put("Enter", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				String id = textID.getText();
	        	String fornitore = textFornitoreNome.getText() + "/" + textFornitoreTipo.getSelectedItem();
	        	String data = textData.getJFormattedTextField().getText();
	        	String dataScadenza = textDataScadenza.getJFormattedTextField().getText();
	        	String importo = textImporto.getText().replace(",", ".");
	        	String periodo = textPeriodo1.getSelectedItem() + "/" + textPeriodo2.getSelectedItem();
	        	String zona = (String) textZona.getSelectedItem();
	        	String path = textFilePath.getText();
	        	
	        	if (id.equals("")) new WindowErrore("Inserire l'ID in modo corretto.");
	        	else if (fornitore.split("/")[0].equals("")) new WindowErrore("Inserire il fornitore in modo corretto.");
	        	else if (data.equals("") || dataScadenza.equals("")) new WindowErrore("Inserire le date in modo corretto.");
	        	else if (!db.isAfter(data, dataScadenza)) new WindowErrore("Inserire le date in modo corretto.");
	        	else if (!isNumeric(importo)) new WindowErrore("Inserire l'importo in modo correto.");
	        	else if (path.equals("")) new WindowErrore("Inserire il file in modo corretto.");
	        	else {
	        		db.addFattura(id, fornitore, data, dataScadenza, Double.parseDouble(importo), getPeriodi(periodo), zona, path);
					
					model = new DefaultTableModel(db.getFatture(), COLONNEFATTURE);
					table.setModel(model);
					addPopupMenu(table);
					
			    	frame.dispose();
	        	}
        	}
        });

        frame.getContentPane().add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
        
        closeWindowWithKey(frame);
        
        frame.setIconImage(new ImageIcon("resources/icon.png").getImage());
	}
	
	public void searchPeriodoFatture() {
		JFrame frame = new JFrame("Ricerca periodo");
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
        
        JButton bottone = new JButton("Ricerca");
        
        panelPeriodoLabel.setBackground(Color.WHITE);
        labelPeriodo.setFont(new Font("Arial", Font.BOLD, 25));
        
        panelPeriodoText.setBackground(Color.WHITE);
		textPeriodo.setBounds(panelPeriodoText.getX() + 25, panelPeriodoText.getY() + 21, 200, 40);
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
			
			model = new DefaultTableModel(db.searchFattureByPeriodo(periodo), COLONNEFATTURE);
            table.setModel(model);
            addPopupMenu(table);

            frame.dispose();
		});
		
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        frame.getRootPane().getActionMap().put("Enter", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				String periodo = (String) textPeriodo.getSelectedItem();
				
				model = new DefaultTableModel(db.searchFattureByPeriodo(periodo), COLONNEFATTURE);
	            table.setModel(model);
	            addPopupMenu(table);

	            frame.dispose();
        	}
        });
		
        frame.getContentPane().add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
        
        closeWindowWithKey(frame);
        
        frame.setIconImage(new ImageIcon("resources/icon.png").getImage());
	}
	
	public void searchFornitoreFatture() {
		JFrame frame = new JFrame("Ricerca fornitore");
        frame.setSize(500, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        
        JPanel panelFornitore = new JPanel(new GridLayout(1, 2));
        JPanel panelBottone = new JPanel(null);
        
        JPanel panelFornitoreLabel = new JPanel(new GridLayout(1, 1));
        JPanel panelFornitoreText = new JPanel(null);
        
        JLabel labelFornitore = new JLabel("Fornitore", SwingConstants.CENTER);
        
        JComboBox<String> textFornitore = new JComboBox<>(convertToVector(db.getAllFornitori()));
        
        JButton bottone = new JButton("Ricerca");
        
        panelFornitoreLabel.setBackground(Color.WHITE);
        labelFornitore.setFont(new Font("Arial", Font.BOLD, 25));
        
        panelFornitoreText.setBackground(Color.WHITE);
        textFornitore.setBounds(panelFornitoreText.getX() + 25, panelFornitoreText.getY() + 21, 200, 40);
        textFornitore.setFont(new Font("Arial", Font.PLAIN, 20));

        panelBottone.setBackground(Color.WHITE);
        bottone.setBounds(panelBottone.getX() + 100, panelBottone.getY() + 15, 300, 40);
        bottone.setFont(new Font("Arial", Font.BOLD, 20));
		
		panelFornitoreLabel.add(labelFornitore);
		panelFornitoreText.add(textFornitore);
		
		panelFornitore.add(panelFornitoreLabel);
		panelFornitore.add(panelFornitoreText);
		panelBottone.add(bottone);
		
		panel.add(panelFornitore);
		panel.add(panelBottone);
		
		bottone.addActionListener(e -> {
			String fornitore = (String) textFornitore.getSelectedItem();
			
			model = new DefaultTableModel(db.searchFattureByFornitori(fornitore), COLONNEFATTURE);
            table.setModel(model);
            addPopupMenu(table);
            
            frame.dispose();
		});
		
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        frame.getRootPane().getActionMap().put("Enter", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				String fornitore = (String) textFornitore.getSelectedItem();
				
				model = new DefaultTableModel(db.searchFattureByFornitori(fornitore), COLONNEFATTURE);
	            table.setModel(model);
	            addPopupMenu(table);

	            frame.dispose();
        	}
        });

        frame.getContentPane().add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
        
        closeWindowWithKey(frame);
        
        frame.setIconImage(new ImageIcon("resources/icon.png").getImage());
	}

	public void searchDataScadenzaFatture() {
		JFrame frame = new JFrame("Ricerca data scadenza");
        frame.setSize(500, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        
        JPanel panelDataScadenza = new JPanel(new GridLayout(1, 2));
        JPanel panelBottone = new JPanel(null);
        
        JPanel panelDataScadenzaLabel = new JPanel(new GridLayout(1, 1));
        JPanel panelDataScadenzaText = new JPanel(null);
        
        JLabel labelDataScadenza = new JLabel("Data scadenza", SwingConstants.CENTER);
        
        UtilDateModel modelDataScadenza = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanelDS = new JDatePanelImpl(modelDataScadenza, p);
        JDatePickerImpl textDataScadenza = new JDatePickerImpl(datePanelDS, new DateLabelFormatter());
        
        JButton bottone = new JButton("Ricerca");
        
        panelDataScadenzaLabel.setBackground(Color.WHITE);
        labelDataScadenza.setFont(new Font("Arial", Font.BOLD, 25));
        
        panelDataScadenzaText.setBackground(Color.WHITE);
        textDataScadenza.setBounds(panel.getX() + 25, panel.getY() + 21, 200, 40);
        textDataScadenza.getComponent(0).setPreferredSize(new Dimension(160, 40));
        textDataScadenza.getComponent(1).setPreferredSize(new Dimension(40, 40));
        textDataScadenza.setBackground(Color.WHITE);
        textDataScadenza.getComponent(0).setBackground(Color.WHITE);
		JFormattedTextField text = textDataScadenza.getJFormattedTextField();
		text.setFont(new Font("Arial", Font.PLAIN, 20));

        panelBottone.setBackground(Color.WHITE);
        bottone.setBounds(panelBottone.getX() + 100, panelBottone.getY() + 15, 300, 40);
        bottone.setFont(new Font("Arial", Font.BOLD, 20));
		
		panelDataScadenzaLabel.add(labelDataScadenza);
		panelDataScadenzaText.add(textDataScadenza);
		
		panelDataScadenza.add(panelDataScadenzaLabel);
		panelDataScadenza.add(panelDataScadenzaText);
		panelBottone.add(bottone);
		
		panel.add(panelDataScadenza);
		panel.add(panelBottone);
		
		bottone.addActionListener(e -> {
			String dataScadenza = textDataScadenza.getJFormattedTextField().getText();
			
			model = new DefaultTableModel(db.searchFattureByDataScadenza(dataScadenza), COLONNEFATTURE);
            table.setModel(model);
            addPopupMenu(table);

            frame.dispose();
		});
		
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        frame.getRootPane().getActionMap().put("Enter", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				String dataScadenza = textDataScadenza.getJFormattedTextField().getText();
				
				model = new DefaultTableModel(db.searchFattureByDataScadenza(dataScadenza), COLONNEFATTURE);
	            table.setModel(model);
	            addPopupMenu(table);

	            frame.dispose();
        	}
        });
		
        frame.getContentPane().add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
        
        closeWindowWithKey(frame);
        
        frame.setIconImage(new ImageIcon("resources/icon.png").getImage());
	}
	
	private void alertWindowRemove(String id) {
		JFrame frame = new JFrame("Sicuro?");
		frame.setSize(700, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        
        JPanel panelLabel = new JPanel(new GridLayout(1, 1));
        JPanel panelBottoni = new JPanel(new GridLayout(1, 2));
        
        JPanel panelBottoneSi = new JPanel(null);
        JPanel panelBottoneNo = new JPanel(null);
        
        JLabel label = new JLabel("Sicuro di voler rimuovere la fattura " + id + "?", SwingConstants.CENTER);
        JButton bottoneSi = new JButton("Si");
        JButton bottoneNo = new JButton("No");
        
        panelLabel.setBackground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 25));
        
        panelBottoneNo.setBackground(Color.WHITE);
        bottoneNo.setBounds(panelBottoneNo.getX() + 105, panelBottoneNo.getY() + 15, 200, 40);
        bottoneNo.setFont(new Font("Arial", Font.BOLD, 20));

        panelBottoneSi.setBackground(Color.WHITE);
        bottoneSi.setBounds(panelBottoneSi.getX() + 35, panelBottoneSi.getY() + 15, 200, 40);
        bottoneSi.setFont(new Font("Arial", Font.BOLD, 20));
        
        panelBottoneSi.add(bottoneSi);
        panelBottoneNo.add(bottoneNo);
        
        panelLabel.add(label);
        panelBottoni.add(panelBottoneNo);
        panelBottoni.add(panelBottoneSi);
        
        panel.add(panelLabel);
        panel.add(panelBottoni);
        
        bottoneNo.addActionListener(e -> frame.dispose());
        bottoneSi.addActionListener(e -> {
        	db.removeFattura(id);
			
			model = new DefaultTableModel(db.getFatture(), COLONNEFATTURE);
            table.setModel(model);
            addPopupMenu(table);
            
            frame.dispose();
        });
		
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        frame.getRootPane().getActionMap().put("Enter", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				db.removeFattura(id);
				
				model = new DefaultTableModel(db.getFatture(), COLONNEFATTURE);
	            table.setModel(model);
	            addPopupMenu(table);
	            
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
