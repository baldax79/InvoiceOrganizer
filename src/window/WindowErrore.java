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
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class WindowErrore extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public WindowErrore(String errore) {
		window(errore);
	}

	private void window(String errore) {
		setTitle("Errore!");
        setSize(500, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));

        JPanel panelErroreLabel = new JPanel(new GridLayout(1, 1));
        JPanel panelBottone = new JPanel(null);

        JLabel labelErrore = new JLabel(errore, SwingConstants.CENTER);
        JButton bottone = new JButton("Ok");

        panelErroreLabel.setBackground(Color.WHITE);
        labelErrore.setFont(new Font("Arial", Font.PLAIN, 20));

        panelBottone.setBackground(Color.WHITE);
		bottone.setBounds(panelBottone.getX() + 100, panelBottone.getY() + 21, 300, 40);
		bottone.setFont(new Font("Arial", Font.BOLD, 20));
		
		panelErroreLabel.add(labelErrore);
		panelBottone.add(bottone);

        panel.add(panelErroreLabel);
        panel.add(panelBottone);

        bottone.addActionListener(e -> dispose());
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        getRootPane().getActionMap().put("Enter", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				dispose();
        	}
        });

        getContentPane().add(panel);
        setResizable(false);
        setVisible(true);
        
        setIconImage(new ImageIcon("resources/icon.png").getImage());
	}
}
