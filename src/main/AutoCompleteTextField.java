package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class AutoCompleteTextField extends JTextField implements DocumentListener, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private LinkedList<String> data;
	
	public AutoCompleteTextField(int columns, LinkedList<String> data) {
		super(columns);
		this.data = data;
		Collections.sort(data);
		
		getDocument().addDocumentListener(this);
		addActionListener(this);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if (e.getLength() != 1) return;
		
		int pos = e.getOffset();
		String prefix = null;
		
		try {
			prefix = this.getText(0, pos + 1);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
		
		int index = Collections.binarySearch(data, prefix);
		
		if (index < 0 && -index <= data.size()) {
			String match = data.get(-index - 1);
			
			if (match.startsWith(prefix)) SwingUtilities.invokeLater(new AutoCompletion(pos, match));
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		setCaretPosition(getSelectionEnd());
	}
	
	private class AutoCompletion implements Runnable {

		private int pos;
		private String completion;
		
		public AutoCompletion(int pos, String completion) {
			this.pos = pos;
			this.completion = completion;
		}
		
		@Override
		public void run() {
			setText(completion);
			setCaretPosition(completion.length());
			moveCaretPosition(pos + 1);
		}
	}
}
