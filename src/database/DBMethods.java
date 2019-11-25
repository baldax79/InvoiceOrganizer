package database;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public abstract class DBMethods {
	
	public LinkedList<LinkedList<Object>> getAllFatture() {
		final String sql = "SELECT * FROM Fatture;";
		LinkedList<LinkedList<Object>> list = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);            
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                LinkedList<Object> temp = new LinkedList<>();
                
                temp.add(rs.getString("ID"));
                temp.add(rs.getString("Fornitore"));
                temp.add(rs.getString("Data"));
                temp.add(rs.getString("DataScadenza"));
                temp.add(rs.getDouble("Importo"));
                temp.add(rs.getString("Periodo"));
                temp.add(rs.getString("Zona"));

                list.add(temp);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        return list;
	}
	
	public LinkedList<String> getAllFornitori() {
		final String sql = "SELECT Nome FROM Fornitori;";
		LinkedList<String> list = new LinkedList<>();
		
		try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
            	String s = rs.getString(1);
            	
            	if (!list.contains(s)) list.add(s);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        return list;
	}
	
	public LinkedList<String> getTipologie(String fornitore) {
		final String sql = "SELECT Tipologia FROM Fornitori WHERE Nome like ?;";
		LinkedList<String> list = new LinkedList<>();
		
		try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, fornitore);
            
            ResultSet rs = st.executeQuery();

            while (rs.next()) list.add(rs.getString(1));
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        return list;
	}
	
	public double getValoreTotaleFatture(String periodo, String zona, String tipologia) {
		final String sql = "SELECT Valore FROM TotaleFatture WHERE Periodo like ? AND Zona like ? AND Tipologia like ?;";
		double result = 0;

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, periodo);
            st.setString(2, "%" + zona + "%");
            st.setString(3, "%" + tipologia + "%");
            
            ResultSet rs = st.executeQuery();

            while (rs.next()) result = rs.getDouble(1);
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        return result;
	}
	
	public String getZonaFattura(String id) {
		final String sql = "SELECT Zona FROM Fatture WHERE ID like ?;";
		String result = "";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, "%" + id + "%");
            
            ResultSet rs = st.executeQuery();

            while (rs.next()) result = rs.getString(1);
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        return result;
	}
	
	public LinkedList<LinkedList<Object>> getAllElemRipartizioniByZonaPeriodo(String zona, String periodo) {
		final String sql = "SELECT * FROM Ripartizioni WHERE Periodo like ? AND Zona like ?;";
		LinkedList<LinkedList<Object>> list = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, periodo);
            st.setString(2, "%" + zona + "%");
            
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                LinkedList<Object> temp = new LinkedList<>();
                
                temp.add(rs.getString("Periodo"));
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
        
        return list;
	}
	
	public LinkedList<Object> getFattura(String id) {
		final String sql = "SELECT * FROM Fatture WHERE ID like ?;";
		LinkedList<Object> list = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, id);
            
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
            	list.add(rs.getString("ID"));
            	list.add(rs.getString("Fornitore"));
            	list.add(rs.getString("Data"));
            	list.add(rs.getString("DataScadenza"));
            	list.add(rs.getDouble("Importo"));
            	list.add(rs.getString("Periodo"));
            	list.add(rs.getString("Zona"));
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        return list;
	}
	
	public LinkedList<LinkedList<Object>> getAllRipartizioni(String periodo) {
		final String sql = "SELECT * FROM Ripartizioni WHERE Periodo like ?;";
		LinkedList<LinkedList<Object>> list = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, periodo);
            
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                LinkedList<Object> temp = new LinkedList<>();
                
                temp.add(rs.getString("Periodo"));
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
        
        return list;
	}
	
	public LinkedList<String> getCategorie() {
		final String sql = "SELECT Categoria FROM Ripartizioni;";
		LinkedList<String> list = new LinkedList<>();
		
		try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);            
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
            	String s = rs.getString(1);
            	
            	if (!list.contains(s)) list.add(s);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        return list;
	}
	
	public LinkedList<String> getFornitori(String tipologia) {
		final String sql = "SELECT Nome FROM Fornitori WHERE Tipologia like ?;";
		LinkedList<String> list = new LinkedList<>();
		
		try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, tipologia);
            
            ResultSet rs = st.executeQuery();

            while (rs.next()) list.add(rs.getString(1));
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        return list;
	}
	
	public String getPathFattura(String id) {
		final String sql = "SELECT PercorsoFile FROM File WHERE ID like ?;";
		String result = "";
		
		try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            
            st.setString(1, id);
            
            ResultSet rs = st.executeQuery();

            while (rs.next()) result = rs.getString(1);
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        return result;
	}
	
	public double twoDecimalNumber(double num) {
        BigDecimal bd = new BigDecimal(num).setScale(2, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
	
	public boolean isAfter(String data, String dataScadenza) {
		String[] dataSplit = data.split("/");
		String[] dataScadenzaSplit = dataScadenza.split("/");
		
		int dataGiorno = Integer.parseInt(dataSplit[0]);
		int dataMese = Integer.parseInt(dataSplit[1]);
		int dataAnno = Integer.parseInt(dataSplit[2]);
		
		int dataScadenzaGiorno = Integer.parseInt(dataScadenzaSplit[0]);
		int dataScadenzaMese = Integer.parseInt(dataScadenzaSplit[1]);
		int dataScadenzaAnno = Integer.parseInt(dataScadenzaSplit[2]);
		
		if (dataScadenzaAnno > dataAnno) return true;
		else if (dataScadenzaAnno == dataAnno) {
			if (dataScadenzaMese > dataMese) return true;
			else if (dataScadenzaMese == dataMese) {
				if (dataScadenzaGiorno > dataGiorno) return true;
				else return false;
			} else return false;
		} else return false;
	}
}
