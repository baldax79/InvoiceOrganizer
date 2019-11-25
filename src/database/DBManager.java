package database;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class DBManager extends DBMethods {
	
	public Object[][] getFatture() {
        Object[][] obj = new Object[getAllFatture().size()][];
        int i = 0;

        for (LinkedList<Object> l : getAllFatture()) {
        	obj[i] = new Object[] { l.get(0), l.get(1), l.get(2), l.get(3), l.get(4) };
        	
            i++;
        }

        return obj;
	}
	
	public Object[][] getRipartizioni(String periodo) {
		Object[][] obj = new Object[getAllRipartizioni(periodo).size()][];
        int i = 0;

        for (LinkedList<Object> l : getAllRipartizioni(periodo)) {
        	obj[i] = new Object[14];
        	
        	for (int j = 1; j < 14; j++) obj[i][j - 1] = l.get(j);
        	
        	i++;
        }

        return obj;
	}
	
	public void addFattura(String id, String fornitore, String data, String dataScadenza, double importo, String periodo, String zona, String path) {
		final String sql = "INSERT INTO Fatture(ID, Fornitore, Data, DataScadenza, Importo, Periodo, Zona) VALUES (?, ?, ?, ?, ?, ?, ?);";
		String[] p = periodo.split("/");
		String[] f = fornitore.split("/");
		String nomeFornitore = f[0];
		String tipologiaFornitore = f[1];
		
        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, id);
            st.setString(2, nomeFornitore);
            st.setString(3, data);
            st.setString(4, dataScadenza);
            st.setDouble(5, importo);
            st.setString(6, periodo);
            st.setString(7, zona);

            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        if (!getAllFornitori().contains(nomeFornitore) || !getTipologie(nomeFornitore).contains(tipologiaFornitore)) addFornitore(nomeFornitore, tipologiaFornitore);
        addAreaFattura(id);
        for (String mese : p) {
        	double valore = getValoreTotaleFatture(mese, zona, tipologiaFornitore);
        	
        	updateTotaleFatture(mese, zona, tipologiaFornitore, valore + importo);
        }
        addFileFattura(id, path);
	}
	
	private void addFornitore(String nome, String tipologia) {
		final String sql = "INSERT INTO Fornitori(Nome, Tipologia) VALUES (?, ?);";
		
		try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, nome);
            st.setString(2, tipologia);

            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}
	
	private void addAreaFattura(String id) {
		final String sql = "INSERT INTO AreaFatture(ID, Zona) VALUES (?, ?);";
		
		try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, id);
            st.setString(2, getZonaFattura(id));

            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}
	
	private void addFileFattura(String id, String path) {
		final String sql = "INSERT INTO File(ID, PercorsoFile) VALUES (?, ?);";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, id);
            st.setString(2, path);

            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}
	
	public void removeFattura(String id) {
		final String sql = "DELETE FROM Fatture WHERE ID like ?;";

		String fornitore = (String) getFattura(id).get(1);
		double importo = (double) getFattura(id).get(4);
		String periodo = (String) getFattura(id).get(5);
		String zona = (String) getFattura(id).get(6);
		
		String[] p = periodo.split("/");
		
		removeAreaFatture(id);
		for (String mese : p) {
			for (String tipologia : getTipologie(fornitore)) {
				double valore = getValoreTotaleFatture(mese, zona, tipologia);
				
				if (valore != 0) updateTotaleFatture(mese, zona, tipologia, valore - importo);
			}
		}
		removeFileFattura(id);
		
		try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, "%" + id + "%");

            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}
	
	private void removeAreaFatture(String id) {
		final String sql = "DELETE FROM AreaFatture WHERE ID like ?;";
		
		try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, "%" + id + "%");

            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}
	
	private void removeFileFattura(String id) {
		final String sql = "DELETE FROM File WHERE ID like ?;";
		
		try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, "%" + id + "%");

            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}
	
	private void updateTotaleFatture(String periodo, String zona, String tipologia, double valore) {
		final String sql = "UPDATE TotaleFatture SET Valore = ? WHERE Periodo like ? AND Zona like ? AND Tipologia like ?;";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setDouble(1, valore);
            st.setString(2, periodo);
            st.setString(3, "%" + zona + "%");
            st.setString(4, "%" + tipologia + "%");

            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        updateRipartizioni(periodo, zona, tipologia);
	}
	
	private void updateRipartizioni(String periodo, String zona, String tipologia) {
		for (LinkedList<Object> obj : getAllElemRipartizioniByZonaPeriodo(zona, periodo)) {
			final String sql = "UPDATE Ripartizioni SET " + tipologia + " = ? WHERE Zona like ? AND Millesimi = ? AND Periodo like ?;";
			double totale = getValoreTotaleFatture(periodo, zona, tipologia);
	
			try {
	            Connection conn = DBConnect.getInstance().getConnection();
	            PreparedStatement st = conn.prepareStatement(sql);
	           
	            st.setDouble(1, twoDecimalNumber((totale * (double) obj.get(3))/1000));
	            st.setString(2, "%" + zona + "%");
	            st.setDouble(3, twoDecimalNumber((double) obj.get(3)));
	            st.setString(4, "%" + periodo + "%");
	
	            st.executeUpdate();
	            st.close();
	            conn.close();
	        } catch (SQLException e) {
	        	e.printStackTrace();
	        }
		}
	}

	public Object[][] searchFattureByFornitori(String nome) {
		final String sql = "SELECT * FROM Fatture WHERE Fornitore like ?;";
		LinkedList<LinkedList<Object>> list = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, "%" + nome + "%");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                LinkedList<Object> temp = new LinkedList<>();
                
                temp.add(rs.getString("ID"));
                temp.add(rs.getString("Fornitore"));
                temp.add(rs.getString("Data"));
                temp.add(rs.getString("DataScadenza"));
                temp.add(rs.getDouble("Importo"));
                temp.add(rs.getBoolean("Pagata"));

                list.add(temp);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        Object[][] obj = new Object[list.size()][];
        int i = 0;

        for (LinkedList<Object> l : list) {
            String id = (String) l.get(0);
            String fornitore = (String) l.get(1);
            String data = (String) l.get(2);
            String dataScadenza = (String) l.get(3);
            double importo = (double) l.get(4);
            boolean pagata = (boolean) l.get(5);

            obj[i] = new Object[] { id, fornitore, data, dataScadenza, importo, pagata };
            i++;
        }

        return obj;
	}

	public Object[][] searchFattureByDataScadenza(String limite) {
		final String sql = "SELECT * FROM Fatture;";
		LinkedList<LinkedList<Object>> list = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
            	String id = rs.getString("ID");
            	String dataScadenza = (String) getFattura(id).get(3);
            	
            	if (isAfter(dataScadenza, limite)) {
	                LinkedList<Object> temp = new LinkedList<>();
	                
	                temp.add(id);
	                temp.add(rs.getString("Fornitore"));
	                temp.add(rs.getString("Data"));
	                temp.add(rs.getString("DataScadenza"));
	                temp.add(rs.getDouble("Importo"));
	                temp.add(rs.getBoolean("Pagata"));
	
	                list.add(temp);
            	}
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        Object[][] obj = new Object[list.size()][];
        int i = 0;

        for (LinkedList<Object> l : list) {
            String id = (String) l.get(0);
            String fornitore = (String) l.get(1);
            String data = (String) l.get(2);
            String dataScadenza = (String) l.get(3);
            double importo = (double) l.get(4);
            boolean pagata = (boolean) l.get(5);

            obj[i] = new Object[] { id, fornitore, data, dataScadenza, importo, pagata };
            i++;
        }

        return obj;
	}

	public Object[][] searchFattureByPeriodo(String periodo) {
		final String sql = "SELECT * FROM Fatture WHERE Periodo like ?;";
		LinkedList<LinkedList<Object>> list = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, "%" + periodo + "%");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                LinkedList<Object> temp = new LinkedList<>();
                
                temp.add(rs.getString("ID"));
                temp.add(rs.getString("Fornitore"));
                temp.add(rs.getString("Data"));
                temp.add(rs.getString("DataScadenza"));
                temp.add(rs.getDouble("Importo"));
                temp.add(rs.getBoolean("Pagata"));

                list.add(temp);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        Object[][] obj = new Object[list.size()][];
        int i = 0;

        for (LinkedList<Object> l : list) {
            String id = (String) l.get(0);
            String fornitore = (String) l.get(1);
            String data = (String) l.get(2);
            String dataScadenza = (String) l.get(3);
            double importo = (double) l.get(4);
            boolean pagata = (boolean) l.get(5);

            obj[i] = new Object[] { id, fornitore, data, dataScadenza, importo, pagata };
            i++;
        }

        return obj;
	}
	
	public Object[][] searchRipartizioniByZona(String string, String periodo) {
		final String sql = "SELECT * FROM Ripartizioni WHERE Zona like ? AND Periodo like ?;";
		LinkedList<LinkedList<Object>> list = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, "%" + string + "%");
            st.setString(2, periodo);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
            	LinkedList<Object> temp = new LinkedList<>();

                temp.add(rs.getString("Zona"));
                temp.add(rs.getString("Categoria"));
                temp.add(rs.getDouble("Millesimi"));
                temp.add(twoDecimalNumber(rs.getDouble("Telefono")));
                temp.add(twoDecimalNumber(rs.getDouble("Egea")));
                temp.add(twoDecimalNumber(rs.getDouble("Pulizie")));
                temp.add(twoDecimalNumber(rs.getDouble("UtilizzoLocali")));
                temp.add(twoDecimalNumber(rs.getDouble("Luce")));
                temp.add(twoDecimalNumber(rs.getDouble("Acqua")));
                temp.add(twoDecimalNumber(rs.getDouble("SpeseCondominiali")));
                temp.add(twoDecimalNumber(rs.getDouble("Manutenzione")));
                temp.add(twoDecimalNumber(rs.getDouble("TARI")));
                temp.add(twoDecimalNumber(rs.getDouble("IMU")));

                list.add(temp);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        Object[][] obj = new Object[list.size()][];
        int i = 0;

        for (LinkedList<Object> l : list) {
            String zona = (String) l.get(0);
            String categoria = (String) l.get(1);
            double millesimi = (double) l.get(2);
            double telefono = (double) l.get(3);
            double egea = (double) l.get(4);
            double pulizie = (double) l.get(5);
            double utilizzoLocali = (double) l.get(6);
            double luce = (double) l.get(7);
            double acqua = (double) l.get(8);
            double speseCondominiali = (double) l.get(9);
            double manutenzione = (double) l.get(10);
            double TARI = (double) l.get(11);
            double IMU = (double) l.get(12);
        	
        	obj[i] = new Object[] { zona, categoria, millesimi, telefono, egea, pulizie, utilizzoLocali, luce, acqua, speseCondominiali, manutenzione, TARI, IMU };
            i++;
        }

        return obj;
	}
	
	public Object[][] searchRipartizioniByCategoria(String string, String periodo) {
		final String sql = "SELECT * FROM Ripartizioni WHERE Categoria like ? AND Periodo like ?;";
		LinkedList<LinkedList<Object>> list = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, "%" + string + "%");
            st.setString(2, periodo);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
            	LinkedList<Object> temp = new LinkedList<>();

                temp.add(rs.getString("Zona"));
                temp.add(rs.getString("Categoria"));
                temp.add(rs.getDouble("Millesimi"));
                temp.add(twoDecimalNumber(rs.getDouble("Telefono")));
                temp.add(twoDecimalNumber(rs.getDouble("Egea")));
                temp.add(twoDecimalNumber(rs.getDouble("Pulizie")));
                temp.add(twoDecimalNumber(rs.getDouble("UtilizzoLocali")));
                temp.add(twoDecimalNumber(rs.getDouble("Luce")));
                temp.add(twoDecimalNumber(rs.getDouble("Acqua")));
                temp.add(twoDecimalNumber(rs.getDouble("SpeseCondominiali")));
                temp.add(twoDecimalNumber(rs.getDouble("Manutenzione")));
                temp.add(twoDecimalNumber(rs.getDouble("TARI")));
                temp.add(twoDecimalNumber(rs.getDouble("IMU")));

                list.add(temp);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        Object[][] obj = new Object[list.size()][];
        int i = 0;

        for (LinkedList<Object> l : list) {
            String zona = (String) l.get(0);
            String categoria = (String) l.get(1);
            double millesimi = (double) l.get(2);
            double telefono = (double) l.get(3);
            double egea = (double) l.get(4);
            double pulizie = (double) l.get(5);
            double utilizzoLocali = (double) l.get(6);
            double luce = (double) l.get(7);
            double acqua = (double) l.get(8);
            double speseCondominiali = (double) l.get(9);
            double manutenzione = (double) l.get(10);
            double TARI = (double) l.get(11);
            double IMU = (double) l.get(12);
        	
        	obj[i] = new Object[] { zona, categoria, millesimi, telefono, egea, pulizie, utilizzoLocali, luce, acqua, speseCondominiali, manutenzione, TARI, IMU };
            i++;
        }

        return obj;
	}

	public void payBolletta(String periodo, String zona, String tipologia) {
		updateTotaleFatture(periodo, zona, tipologia, 0.0);
        updateRipartizioni(periodo, zona, tipologia);
	}
	
	public void saveData(String periodo) { 
		try {
			String data = new SimpleDateFormat("dd_MM_yyyy").format(new Date());
			PdfDocument pdfDoc = new PdfDocument(new PdfWriter("resources/saves/ripartizioni_" + periodo + "_" + data +".pdf"));
			Document doc = new Document(pdfDoc);
			String[] colonne = { "Zona", "Categoria", "Millesimi", "Telefono", "Egea", "Pulizie", "Utilizzo locali", "Luce", "Acqua", "Spese condominiali", "Manutenzione", "TARI", "IMU" };
			Table table = new Table(colonne.length);
			
			for (String s : colonne) table.addCell(new Cell().add(new Paragraph(s).setFontSize(7)));
			for (LinkedList<Object> list : getAllRipartizioni(periodo)) {
				for (int i = 1; i < list.size(); i++) table.addCell(new Cell().add(new Paragraph(list.get(i) + "").setFontSize(7)));
			}
			doc.add(table);
	 
			doc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
}
