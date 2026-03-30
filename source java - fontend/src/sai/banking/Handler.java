package sai.banking;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JPasswordField;
import javax.swing.JFormattedTextField;

import sai.sql.sSql;
import sai.ui.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Handler extends sHandler.Action {

	private Atm atm;
	private sSql sql;
	private Timer timer;

	public Handler(Atm atm, sSql sql) {
		this.atm = atm;
		this.sql = sql;
	}
	
	//((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("cols"))).get(0))
	//((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))
	public void vaffaculo (Timer t) {
		this.timer = t;
	}
	
	@SuppressWarnings("rawtypes")
	protected void panelInfo (boolean login) {
		Map settingJson = atm.getJson();
		String utente = (login ? ((JTextField) atm.getElements().get("cod_utente")).getText() : atm.getcodUtente());
		String q = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))
	            +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))+" WHERE "
	            +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"='"+utente+"'";

		try {
			String[][] res = sql.query(q);
			if (res[1][0].equals(utente)) {
				if (login) {
					atm.setScreen(atm.index());
					((JLabel) atm.getElements().get("index_user")).setText(res[1][0]);
					atm.setcodUtente(res[1][0]);	
				}
				
				String c = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(3))
            			+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(4))
	            		+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("cols"))).get(1))
	            		+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("cols"))).get(2))
						+" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
	                    +" WHERE "
	                    +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"='"+atm.getcodUtente()+"'"
	                    +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))
	                    	+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
	                    +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(2))
	                    	+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")));
				
				try {
					String[][] info = sql.query(c);
					((JLabel) atm.getElements().get("info_cliente")).setText(info[1][0] + " " + info[1][1]);
					((JLabel) atm.getElements().get("info_disp")).setText("ContoDisp: "+info[1][2]+" €");
					((JLabel) atm.getElements().get("info_cont")).setText("ContoCont: "+info[1][3]+" €");
				} catch (SQLException e1) {
					e1.printStackTrace();
					showMessage("InfoUtente", "Errore recupero dati cliente");
				} catch (ArrayIndexOutOfBoundsException e2) {// create a dialog Box 
					showMessage("InfoUtente", "Errore recupero dati cliente");
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			showMessage("CodUtente", "Codice utente errato");
		} catch (ArrayIndexOutOfBoundsException e2) {// create a dialog Box 
			showMessage("CodUtente", "Codice utente errato");
		} finally {
			((JTextField) atm.getElements().get("cod_utente")).setText("");
		}
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
			case "login": {
				Map settingJson = atm.getJson();
				char[] input = ((JPasswordField) atm.getElements().get("login_password")).getPassword();
				String q = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialidipendente")).get("pk")))
			            +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialidipendente")).get("name")))+" WHERE "
			            +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialidipendente")).get("pk")))+"='"+((JTextField) atm.getElements().get("login_user")).getText()+"' "
			            +"AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("credenzialidipendente")).get("cols"))).get(0))+"='"+new String(input)+"'";
				try {
					String[][] res = sql.query(q);
					if (res[1][0].equals(((JTextField) atm.getElements().get("login_user")).getText())) {
						atm.setScreen(atm.cliente());
						((JLabel) atm.getElements().get("header_user")).setText(res[1][0]);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
					showMessage("LOGIN", "Username o Passwrod errata");
				} catch (ArrayIndexOutOfBoundsException e2) {// create a dialog Box 
					showMessage("LOGIN", "Username o Passwrod errata");
				} finally {
					Arrays.fill(input, '0');
					((JTextField) atm.getElements().get("login_user")).setText("");
					((JPasswordField) atm.getElements().get("login_password")).setText("");
				}
			}	break;
			
			case "utente": {
				panelInfo(true);
			}	break;
			
			case "salva": {
				String id = "";
				Map settingJson = atm.getJson();
				String a = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")))
		                +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))
		                +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))
		                +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
		                +" WHERE "
		                +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"='"+atm.getcodUtente()+"'"
		                +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
		                +" AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(2))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")));
				try {
					id = sql.query(a)[1][0];
				} catch (SQLException e1) {
					showMessage("FIDO", "Cliente non trovato");
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				String q = "UPDATE "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
		                +" SET "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("cols"))).get(5))
		                +"='"+((JTextField) atm.getElements().get("settings_fido")).getText().replace(",", "")+"'"
		                +" WHERE "
		                + ((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")))+"='"+id+"'";	       
				
				try {
					sql.query(q);
				} catch (SQLException e1) {
					e1.printStackTrace();
					showMessage("FIDO", "Errore cambio fido");
				} catch (ArrayIndexOutOfBoundsException e2) {// create a dialog Box 
					showMessage("FIDO", "Errore cambio fido");
				} finally {
					((JTextField) atm.getElements().get("cod_utente")).setText("");
				}

				showMessage("SETTINGS", "Fido salvato");
			}	break;
			
			case "deposito": {
				atm.setScreen(atm.deposito());
			}	break;
			
			case "prelievo": {
				atm.setScreen(atm.prelievo());
			}	break;
			
			case "bonifico": {
				atm.setScreen(atm.bonifico());
			}	break;
			
			case "estratto": {
				atm.setScreen(atm.estratto());
				Map settingJson = atm.getJson();
				String q = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(3))
            			+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(4))
	            		+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("cols"))).get(1))
	            		+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("cols"))).get(2))
						+" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
	                    +" WHERE "
	                    +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"='"+atm.getcodUtente()+"'"
	                    +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))
	                    	+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
	                    +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(2))
	                    	+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")));
				
				String date = String.format("%04d-%02d-01", timer.getRawDate().getYear(), timer.getRawDate().getMonthValue());
				String q1 = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(1))
	            		+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(2))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(3))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))
	                    +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))
	                    +" WHERE "
	                    +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"='"+atm.getcodUtente()+"'"
	                    +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
	                    +" AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(2))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")))
	                    +" AND ("+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(1))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
	                    +" OR "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(2))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))+")"
	                    +" AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))+">'"+date+"'"
						+" ORDER BY " + ((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4));
		        
	        
		        try {
					String[][] res = sql.query(q);
					String[][] res1 = sql.query(q1);
					JTextArea estratto = (JTextArea) atm.getElements().get("estratto");
					estratto.append(String.format("%42s\n", (res[1][0] + " " + res[1][1])));
					estratto.append(String.format("%42s\n", res[1][2]));
					estratto.append(String.format("%42s\n\n", res[1][3]));
					
					boolean first_row = true;
				    String s = String.format("%-13s | %-13s | %-13s\n", "Data", "Accredito", "Addebito");
				    estratto.append(s); 
					for(String[] r : res1) {
						if (first_row){
							first_row = false; continue;
						}	
						estratto.append(String.format("%-13s | %-13s | %-13s\n", r[3], (r[0] != null && !r[0].isEmpty() ? " " : r[2]), (r[0] != null && !r[0].isEmpty() ? r[2] : " ") ));
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e2) {// create a dialog Box 
					e2.printStackTrace();
				}
			}	break;
			
			case "movimento": {
				atm.setScreen(atm.movimento());
				Map settingJson = atm.getJson();
				String q = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(1))
		            		+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(2))
		                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(3))
		                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))
		                    +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))
		                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))
		                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
		                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))
		                    +" WHERE "
		                    +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"='"+atm.getcodUtente()+"'"
		                    +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
		                    +" AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(2))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")))
		                    +" AND ("+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(1))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
		                    +" OR "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(2))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))+")";
				if (((JFormattedTextField) atm.getElements().get("data_inizio")).getValue() != null)
					q += " AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))+">'"+((JTextField) atm.getElements().get("data_inizio")).getText()+"'";
		        if (((JFormattedTextField) atm.getElements().get("data_fine")).getValue() != null)
		        	q += " AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))+"<'"+((JTextField) atm.getElements().get("data_fine")).getText()+"'";
				
		        q += " ORDER BY " + ((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4));
		        
		        try {
					String[][] res = sql.query(q);
					JTextArea estratto = (JTextArea) atm.getElements().get("movimenti");
					boolean first_row = true;
					for(String[] r : res) {
						if (first_row){
							first_row = false; continue;
						}							
						estratto.append(String.format("%-12s | %-12s | %-12s\n", r[3], (r[0] != null && !r[0].isEmpty() ? " " : r[2]), (r[0] != null && !r[0].isEmpty() ? r[2] : " ") ));
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e2) {// create a dialog Box 
					e2.printStackTrace();
				}
			}	break;

			case "send_deposito": {
				String idDA = "";
				Map settingJson = atm.getJson();
				String a = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")))
		                +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))
		                +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))
		                +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
		                +" WHERE "
		                +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"='"+atm.getcodUtente()+"'"
		                +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
		                +" AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(2))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")));
				try {
					idDA = sql.query(a)[1][0];
				} catch (SQLException e1) {
					e1.printStackTrace();
					showMessage("DEPOSITO", "Errore durante deposito");
				}
				String q = "INSERT INTO "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))
		                +" ("+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(1))
		                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(2))
		                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(3))
		                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))+") "
		                +" VALUES ('0'"
		                +", '"+idDA+"'"
		                +", '"+((JTextField) atm.getElements().get("deposito_importo")).getText().replace(",", "")+"'"
		                +", '"+((JLabel) atm.getElements().get("header_time")).getText().split(" ")[0]+"')";
				try {
					sql.query(q);
					showMessage("DEPOSITO", "Deposito importo riuscito");
					panelInfo(false);
				} catch (SQLException e1) {
					e1.printStackTrace();
					showMessage("DEPOSITO", "Errore durante deposito");
				} catch (Exception e2) {// create a dialog Box 
					e2.printStackTrace();
					showMessage("DEPOSITO", "Errore durante deposito");
				}
			}	break;
			
			case "send_bonifico": {
				String idDA = ""; String idDEst = "";
				Map settingJson = atm.getJson();
				String a = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")))
		                +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))
		                +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))
		                +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
		                +" WHERE "
		                +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"='"+atm.getcodUtente()+"'"
		                +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
		                +" AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(2))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")));
				try {
					idDA = sql.query(a)[1][0];
				} catch (SQLException e1) {
					showMessage("BONIFICO", "Errore durante spedizione");
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				String b = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")))
		                +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))
		                +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))
		                +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
		                +" WHERE "
		                +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"='"+((JTextField) atm.getElements().get("bonifico_destinatario")).getText()+"'"
		                +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
		                +" AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(2))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")));
				try {
					idDEst = sql.query(b)[1][0];
				} catch (SQLException e1) {
					showMessage("BONIFICO", "Errore durante spedizione");
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				String q = "INSERT INTO "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))
		                +" ("+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(1))
		                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(2))
		                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(3))
		                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))+") "
		                +" VALUES ('"+idDA+"'"
		                +", '"+idDEst+"'"
		                +", '"+((JTextField) atm.getElements().get("bonifico_importo")).getText().replace(",", "")+"'"
		                +", '"+((JLabel) atm.getElements().get("header_time")).getText().split(" ")[0]+"')";
				try {
					sql.query(q);
					showMessage("BONIFICO", "Bonifico mandato");
					panelInfo(false);
				} catch (SQLException e1) {
					e1.printStackTrace();
					showMessage("BONIFICO", "Errore durante spedizione");
				} catch (Exception e2) {// create a dialog Box 
					e2.printStackTrace();
					showMessage("BONIFICO", "Errore durante spedizione");
				}
			} 	break;
			
			case "logout": {
				((JLabel) atm.getElements().get("header_user")).setText("");
				atm.setScreen(atm.login());
			}	break;
			
			case "exit": {
				atm.setcodUtente("");
				((JLabel) atm.getElements().get("index_user")).setText("");
				((JLabel) atm.getElements().get("info_cliente")).setText("");
				((JLabel) atm.getElements().get("info_disp")).setText("");
				((JLabel) atm.getElements().get("info_cont")).setText("");
				atm.setScreen(atm.cliente());
			}	break;
			
			case "settings": {
				atm.setScreen(atm.settings());
			}	break;
			
			case "prelievo5": {
				((JTextField) atm.getElements().get("prelievo_importo")).setText("5.00");
			} break;
			
			case "prelievo10": {
				((JTextField) atm.getElements().get("prelievo_importo")).setText("10.00");
			} break;
			
			case "prelievo20": {
				((JTextField) atm.getElements().get("prelievo_importo")).setText("20.00");
			} break;
			
			case "prelievo50": {
				((JTextField) atm.getElements().get("prelievo_importo")).setText("50.00");
			} break;
			
			case "prelievo100": {
				((JTextField) atm.getElements().get("prelievo_importo")).setText("100.00");
			} break;
			
			case "prelievo200": {
				((JTextField) atm.getElements().get("prelievo_importo")).setText("200.00");
			} break;
			
			case "prelievo500": {
				((JTextField) atm.getElements().get("prelievo_importo")).setText("500.00");
			} break;
			
			case "prelievo1000": {
				((JTextField) atm.getElements().get("prelievo_importo")).setText("1000.00");
			} break;
			
			case "conferma_prelievo": {
				String idDEst = "";
				Map settingJson = atm.getJson();
				String a = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")))
		                +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))
		                +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))
		                +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
		                +" WHERE "
		                +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"='"+atm.getcodUtente()+"'"
		                +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
		                +" AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(2))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")));
				try {
					idDEst = sql.query(a)[1][0];
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				String q = "INSERT INTO "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))
		                +" ("+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(1))
		                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(2))
		                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(3))
		                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))+") "
		                +" VALUES ('"+idDEst+"'"
		                +", '0'"
		                +", '"+((JTextField) atm.getElements().get("prelievo_importo")).getText().replace(",", "")+"'"
		                +", '"+((JLabel) atm.getElements().get("header_time")).getText().split(" ")[0]+"')";
				try {
					sql.query(q);
					showMessage("PRELIEVO", "Prelievo riuscito");
					panelInfo(false);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}  catch (Exception e2) {// create a dialog Box 
					e2.printStackTrace();
					showMessage("PRELIEVO", "Errore durante prelievo");
				}
			} break;
			
			case "back_index": {
				atm.setScreen(atm.index());
			} break;
			
			default:	// Nothing
				break; 
		}
	}
	
	public void showMessage(String title, String text) {
		JDialog d = new JDialog(atm.getFrame(), title);
		d.getContentPane().setBackground( Color.DARK_GRAY );
		d.getContentPane().setLayout(null);
		d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		d.setResizable(false);
		
        // create a label 
        JLabel l = new JLabel("<html>"+text+"</html>"); 
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Abel", Font.PLAIN, 14));
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setLocation(0, 0);
        l.setSize(200, 45);

        d.getContentPane().add(l); 
        
        JCustom.RoundedJButton b = new JCustom.RoundedJButton(text, 20, new Color(255, 51, 0));
        b.setBackground(Color.GRAY);
        b.setForeground(new Color(255, 51, 0));
        b.setFont(new Font("Hack", Font.BOLD, 16));
        b.setText("OK");
        b.setLocation(25, 45);
        b.setSize(150, 25);
        b.grabFocus();
        b.addActionListener(new sHandler.Action() {
        	@Override
			public void actionPerformed(ActionEvent e) {
				d.dispose();
			}
		});
        b.addKeyListener(new sHandler.Keyboard() {
			@Override
			public void keyTyped(KeyEvent arg0) { d.dispose(); }
			
			@Override
			public void keyReleased(KeyEvent arg0) {}
			
			@Override
			public void keyPressed(KeyEvent arg0) {}
		});
        
        d.getContentPane().add(b); 

        // setsize of dialog 
        int locX = atm.getFrame().getX()+(atm.getFrame().getWidth()/2)-100;
        int locY = atm.getFrame().getY()+(atm.getFrame().getHeight()/2)-50;
        d.setBounds(locX, locY, 200, 100);

        // set visibility of dialog 
        d.setVisible(true); 
	}
	
}
