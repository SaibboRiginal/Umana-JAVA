package sai.monitor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import sai.json.sJson;
import sai.sql.sSql;

public class BackEnd {

	Monitor m;
	private sSql sql;
	private Timer timer;
	@SuppressWarnings("rawtypes")
	private Map settingJson;
	private List<String> list_ids = new ArrayList<String>();

	private int day = -1;
	private int month = -1;
	
	private final static int costo_abbonamento = 5;
	
	//private final static String which_db = "locale";
	private final static String which_db = "remoto";
	
	private final static boolean DEBUG = true;
	
	@SuppressWarnings("rawtypes")
	public BackEnd (Monitor monitor) {
		this.m = monitor;
		this.settingJson = sJson.getJson(Monitor.class.getResource("/json/setting.json").getPath());
		Map db_info = (((Map) ((Map) settingJson.get("database")).get(which_db)));
		this.sql = new sSql( 
				((String) (db_info.get("url"))),
				((Long) (db_info.get("port"))).intValue(), 
				((String) (db_info.get("db_name"))),
				((String) (db_info.get("username"))), 
				((String) (db_info.get("password")))
				);
	}
	
	public void start () {
		//Thread 1
		while (true) {
			if (!DEBUG) {
				if (timer.getRawTime().getHour() == 0 && timer.getRawDate().getDayOfYear() != day) {
					day = timer.getRawDate().getDayOfYear();
					updateIDs();
				} 
				if (timer.getRawDate().getMonthValue() != month) {
					month = timer.getRawDate().getMonthValue();
					abbonamenti();
				} 
				if (timer.getRawDate().getMonthValue() == 12 && timer.getRawDate().getDayOfMonth() == 31) {
					capitalizazione();
				}
			} else {
				updateIDs();
				abbonamenti();
				capitalizazione();
				try {
					TimeUnit.SECONDS.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {	e.printStackTrace(); }
		}
	}
	
	public void setTimer (Timer t) {
		this.timer = t;
	}
	
	@SuppressWarnings("rawtypes")
	protected void updateIDs () {
		String a = "SELECT DISTINCT "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")))
                +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")));
		try {
			boolean first_row = true;
			for(String[] r : sql.query(a)) {
				if (first_row){
					first_row = false; continue;
				}							
				this.list_ids.add(r[0]);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		tassi(); // >>>
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected void tassi () {	
		m.addLine("Calcolo interessi... ", false);
		for(String id : list_ids) {
			String c = "SELECT DISTINCT "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("cols"))).get(1))
					+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("cols"))).get(3))
					+" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))
                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))
                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
                    +" WHERE "
                    +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))+"='"+id+"'"
                    +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))
                    	+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
                    +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(2))
                    	+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")));
			
			try {
				String[][] info = sql.query(c);
				double saldo = Double.valueOf(info[1][0]);
				int idTasso = Integer.valueOf(info[1][1]);
				
				String t = "SELECT DISTINCT "+((String) (((Map) ((Map) settingJson.get("tables")).get("tasso")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("tasso")).get("cols"))).get(1))
		        		+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("tasso")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("tasso")).get("cols"))).get(2))
		                +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("tasso")).get("name")))
		                +" WHERE "+((String) (((Map) ((Map) settingJson.get("tables")).get("tasso")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("tasso")).get("pk")))+"='"+idTasso+"'";
				try {
					String[][] res = sql.query(t);
					double tasso = (saldo > 0 ? Double.valueOf(res[1][0]) : Double.valueOf(res[1][1]));

					double interessi = (saldo*tasso)/36500;
					
					String u = "INSERT INTO "+((String) (((Map) ((Map) settingJson.get("tables")).get("interessi")).get("name")))
			                +" ("+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("interessi")).get("cols"))).get(0))
			                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("interessi")).get("cols"))).get(1))
			                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("interessi")).get("cols"))).get(2))+") "
			                +" VALUES ('"+id+"'"
			                +", '"+interessi+"'"
			                +", '"+timer.getDate()+"')"
			                +" ON DUPLICATE KEY UPDATE "
			                + ((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("interessi")).get("cols"))).get(2))
			                +" = VALUES("
			                + ((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("interessi")).get("cols"))).get(2))+ ")";
					try {
						sql.query(u);
					} catch (SQLException e1) {
						e1.printStackTrace();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				//
			} catch (ArrayIndexOutOfBoundsException e2) {// create a dialog Box 
				e2.printStackTrace();
				//
			}
			
		}
		m.addLine("FATTO", true);
		bonifici(); // >>>
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected void bonifici () {	  // 0 da convalidare esistenza 1 salto 2 salto
		m.addLine("Convalida bonifici... ", false);
		for(String id : list_ids) {
			String c = "SELECT DISTINCT "+((String) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("cols"))).get(0))
					+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("cols"))).get(3))
					+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("cols"))).get(4))
					+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("cols"))).get(5))
					+" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("name")))
                    +" WHERE "
                    +((String) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("cols"))).get(2))+"='"+id+"'";
			
			try {
				String[][] info = sql.query(c);
				boolean first_row = true;
				for(String[] r : info) {
					if (first_row){
						first_row = false; continue;
					}
					int idBonifico = Integer.valueOf(r[0]);
					int check = Integer.valueOf(r[2]);
					String date = timer.getDate();
					
					if(check == 0) {
						String IBAN = "";
						String b = "SELECT DISTINCT "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("cols"))).get(6))
				                +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
				                +" WHERE "
				                +((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")))+"='"+id+"'";
						try {
							IBAN = sql.query(b)[1][0];
						} catch (SQLException e1) {
							e1.printStackTrace();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
						
						String q = "UPDATE "+((String) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("name")))
				                +" SET "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("cols"))).get(4))
				                +"='"+(IBAN != "" ? "1" : "2")+"'"
				                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("cols"))).get(6))
				                +"='"+date+"'"
				                +" WHERE "
				                + ((String) (((Map) ((Map) settingJson.get("tables")).get("convalidabonifici")).get("pk")))+"='"+idBonifico+"'";	       
						
						try {
							sql.query(q);
						} catch (SQLException e1) {
							e1.printStackTrace();
						} catch (ArrayIndexOutOfBoundsException e2) {// create a dialog Box 
							e2.printStackTrace();
						}
					}
				}					
			} catch (SQLException e1) {
				e1.printStackTrace();
				//
			} catch (ArrayIndexOutOfBoundsException e2) {// create a dialog Box 
				e2.printStackTrace();
				//
			}
			
		}
		m.addLine("FATTO", true);
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected void capitalizazione () {	
		m.addLine("Interessi annuali... ", false); 
		for(String id : list_ids) {
			Double saldo = 0.0;
			String q = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("interessi")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("interessi")).get("cols"))).get(1))
	                +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("interessi")).get("name")))
	                +" WHERE "+((String) (((Map) ((Map) settingJson.get("tables")).get("interessi")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("interessi")).get("cols"))).get(0))+"='"+id+"'";
			try {
				String[][] res = sql.query(q);
				boolean first_row = true;
				for(String[] r : res) {
					if (first_row){
						first_row = false; continue;
					}
					saldo += Double.valueOf(r[0]);
				}		
				if (saldo != 0){
					String u = "INSERT INTO "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))
			                +" ("+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(1))
			                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(2))
			                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(3))
			                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))+") "
			                +" VALUES ('"+(saldo < 0 ? id : "0")+"'"
			                +", '"+(saldo > 0 ? id : "0")+"'"
			                +", '"+Math.abs(saldo)+"'"
			                +", '"+timer.getDate()+"')";
					try {
						sql.query(u);
					} catch (SQLException e1) {
						e1.printStackTrace();
					} catch (Exception e2) {// create a dialog Box 
						e2.printStackTrace();
					}	
				}			
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		m.addLine("FATTO", true);
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected void abbonamenti () { // da abbonamenti a bonifichi? 	
		m.addLine("Scalo costo abbonamenti... ", false);
		for(String id : list_ids) {
			String q = "SELECT DISTINCT "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("cols"))).get(4))
					+" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))
                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))
                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
                    +" WHERE "
                    +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))+"='"+id+"'"
                    +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))
                    	+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
                    +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(2))
                    	+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")));

			try {
				String[][] res = sql.query(q);
				
				boolean abbonamento_check = (res[1][0].equals("1") ? true : false);
				if (!abbonamento_check) {
					String u = "INSERT INTO "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))
			                +" ("+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(1))
			                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(2))
			                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(3))
			                +", "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))+") "
			                +" VALUES ('"+id+"'"
			                +", '0'"
			                +", '"+costo_abbonamento+"'"
			                +", '"+timer.getDate()+"')";	                
					try {
						sql.query(u);
					} catch (SQLException e1) {
						e1.printStackTrace();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		m.addLine("FATTO", true);
	}
	
}
