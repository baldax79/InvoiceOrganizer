package window;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import main.DateLabelFormatter;

public abstract class WindowMethods {
	
	public String getPeriodi(String string) {
		String[] mesi = { "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre" };
		Map<String, String> map = new HashMap<>();
		
		for (String mese : mesi) map.put(mese, mese.substring(0, 3).toUpperCase());
		
		LinkedList<String> list = new LinkedList<>();
		String[] periodi = string.split("/");
		String date1 = map.get(periodi[0]) + "-2015";
		String date2 = map.get(periodi[1]) + "-2015";
		
		DateFormat formater = new SimpleDateFormat("MMM-yyyy");

        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();

        try {
            beginCalendar.setTime(formater.parse(date1));
            finishCalendar.setTime(formater.parse(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (beginCalendar.before(finishCalendar)) {
	        while (beginCalendar.before(finishCalendar)) {
	            String date = formater.format(beginCalendar.getTime()).toUpperCase();
	            String month = date.split("-")[0];
	            
	            for (Entry<String, String> entry : map.entrySet()) {
	                if (entry.getValue().equals(month)) {
	                    list.add(entry.getKey());
	                }
	            }
	            
	            beginCalendar.add(Calendar.MONTH, 1);
	        }
        } else {
        	date2 = map.get(periodi[1]) + "-2016";
        	try {
                beginCalendar.setTime(formater.parse(date1));
                finishCalendar.setTime(formater.parse(date2));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        	while (beginCalendar.before(finishCalendar)) {
	            String date = formater.format(beginCalendar.getTime()).toUpperCase();
	            String month = date.split("-")[0];
	            
	            for (Entry<String, String> entry : map.entrySet()) {
	                if (entry.getValue().equals(month)) {
	                    list.add(entry.getKey());
	                }
	            }
	            
	            beginCalendar.add(Calendar.MONTH, 1);
	        }
        }
        
        list.add(periodi[1]);
        
        String result = "";
        
        for (int i = 0; i < list.size(); i++) {
        	if (i == 0) result += list.get(i);
        	else result += "/" + list.get(i);
        }
		
		return result;
	}
	
	public boolean isNumeric(String string) {
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
	
	public JDatePickerImpl setDatePicker() {
		UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        
        JDatePanelImpl date = new JDatePanelImpl(model, p);
        
        return new JDatePickerImpl(date, new DateLabelFormatter());
	}
	
	public void closeWindowWithKey(JFrame frame) {
    	frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        frame.getRootPane().getActionMap().put("Cancel", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
        		frame.dispose();
        	}
        });
    }
	
	public String[] convertToVector(LinkedList<String> list) {
		String[] result = new String[list.size()];

        for (int i = 0; i < result.length; i++) result[i] = list.get(i);

        return result;
	}

}
