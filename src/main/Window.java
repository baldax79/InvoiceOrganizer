package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import database.DBManager;
import window.WindowErrore;
import window.WindowFatture;
import window.WindowPDF;
import window.WindowPagamento;
import window.WindowRipartizioni;

public class Window extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private DBManager db;

	public Window() {
		db = new DBManager();
		
		window();
	}
	
	private void window() {
		setSize(600, 400);
        setTitle("Invoice Organizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        elements();

        setResizable(false);
        setVisible(true);
        
        setIconImage(new ImageIcon("resources/icon.png").getImage());
	}
	
	private void elements() {
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.add(north(), BorderLayout.NORTH);
		panel.add(south(), BorderLayout.SOUTH);
		panel.add(center(), BorderLayout.CENTER);
		panel.add(lateral(), BorderLayout.WEST);
		panel.add(lateral(), BorderLayout.EAST);
		
		getContentPane().add(panel);
	}
	
	private JPanel north() {
		JPanel panel = new JPanel(new GridLayout(1, 1));
		
		panel.setBackground(Color.BLACK);
		panel.setPreferredSize(new Dimension(getWidth(), 70));
		
		JLabel label = new JLabel(getTitle().toUpperCase(), SwingConstants.CENTER);
		label.setFont(new Font("Arial", Font.BOLD, 40));
		label.setForeground(Color.WHITE);
		
		panel.add(label);
		
		return panel;
	}
	
	private JPanel south() {
		JPanel panel = new JPanel(new GridLayout(1, 1));
		
		panel.setPreferredSize(new Dimension(getWidth(), 70));
		panel.setBackground(Color.BLACK);
		
		JLabel label = new JLabel("Info", SwingConstants.CENTER);
		
		label.setFont(new Font("Arial", Font.BOLD, 20));
		label.setForeground(Color.WHITE);
		
		label.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (Desktop.isDesktopSupported()) {
        		    try {
        		        File myFile = new File("resources/README.txt");
        		        Desktop.getDesktop().open(myFile);
        		    } catch (IOException ex) {
        		    	ex.printStackTrace();
        		    }
        		}
			}
		});
		
		panel.add(label);
		
		return panel;
	}
	
	private JPanel center() {
		JPanel panel = new JPanel(new GridLayout(2, 2));
		
		JPanel panelTopLeft = new JPanel(new GridLayout(1, 1));
		JPanel panelTopRight = new JPanel(new GridLayout(1, 1));
		JPanel panelBottomLeft = new JPanel(new GridLayout(1, 1));
		JPanel panelBottomRight = new JPanel(new GridLayout(1, 1));
		
		panelTopLeft.setBackground(Color.WHITE);
		panelTopRight.setBackground(Color.WHITE);
		panelBottomLeft.setBackground(Color.WHITE);
		panelBottomRight.setBackground(Color.WHITE);
		
		JLabel fatture = new JLabel("Fatture", SwingConstants.CENTER);
		JLabel ripartizioni = new JLabel("Ripartizioni", SwingConstants.CENTER);
		JLabel pagamento = new JLabel("Pagamento", SwingConstants.CENTER);
		JLabel pdf = new JLabel("PDF", SwingConstants.CENTER);
		
		fatture.setFont(new Font("Arial", Font.BOLD, 35));
		ripartizioni.setFont(new Font("Arial", Font.BOLD, 35));
		pagamento.setFont(new Font("Arial", Font.BOLD, 35));
		pdf.setFont(new Font("Arial", Font.BOLD, 35));
		
		fatture.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new WindowFatture(db);
			}
		});
		ripartizioni.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new WindowRipartizioni(db);
			}
		});
		pagamento.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (db.getAllFatture().size() != 0) new WindowPagamento(db);
				else new WindowErrore("Non ci sono bollette da pagare.");
			}
		});
		pdf.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new WindowPDF(db);
			}
		});
		
		panelTopLeft.add(fatture);
		panelTopRight.add(ripartizioni);
		panelBottomLeft.add(pagamento);
		panelBottomRight.add(pdf);
		
		panel.add(panelTopLeft);
		panel.add(panelTopRight);
		panel.add(panelBottomLeft);
		panel.add(panelBottomRight);
		
		return panel;
	}
	
	private JPanel lateral() {
		JPanel panel = new JPanel(new GridLayout(1, 1));
		
		panel.setBackground(Color.WHITE);
		panel.setPreferredSize(new Dimension(70, getHeight() - 140));
		
		return panel;
	}
}
