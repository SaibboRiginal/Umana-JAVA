package sai.banking;

import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import sai.json.sJson;
import sai.sql.sSql;
import sai.ui.*;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.border.MatteBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;


@SuppressWarnings("unused")
public class Atm {

	private sUI mainframe;
	private sSql sql;
	private Container c;
	private Handler handler;
	private JPanel screen;
	private Timer timer;
	private String codUtente = "";
	private Map<String, Component> elements = new HashMap<String, Component>();
	@SuppressWarnings("rawtypes")
	private Map settingJson;
	
	//private final static String which_db = "locale";
	private final static String which_db = "remoto";
		
	@SuppressWarnings("rawtypes")
	public Atm (String titolo) {
		this.mainframe = new sUI("ATM - Tesla", 500, 500);
		mainframe.setFont(new Font("Hack", Font.PLAIN, 15));
		//mainframe.setAlwaysOnTop(true);
		mainframe.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/photo/t.png")));
		mainframe.setResizable(false);
		mainframe.setTitle("ATM - Tesla");
		this.c = mainframe.getContainer();
		this.c.setBackground(Color.BLACK);
		this.c.setForeground(Color.WHITE);
		mainframe.getContainer().setLayout(null);
		
		this.settingJson = sJson.getJson(getClass().getResourceAsStream("/json/setting.json"));
		Map db_info = (((Map) ((Map) settingJson.get("database")).get(which_db)));
		this.sql = new sSql( 
				((String) (db_info.get("url"))),
				((Long) (db_info.get("port"))).intValue(), 
				((String) (db_info.get("db_name"))),
				((String) (db_info.get("username"))), 
				((String) (db_info.get("password")))
				);
		
		this.handler = new Handler(this, this.sql);

		this.timer = new Timer(this);
		this.handler.vaffaculo(this.timer);
		
		// starting ...
		this.c.add(header());
		this.c.add(info());
		this.screen = login ();
		//this.screen = estratto ();
		this.c.add(this.screen);
		mainframe.getContentPane().setLayout(null);
		mainframe.refresh();
	}

	@SuppressWarnings("rawtypes")
	protected Map getJson () {
		return this.settingJson;
	}
	
	protected Map<String, Component> getElements () {
		return this.elements;
	}
	
	protected JFrame getFrame () {
		return this.mainframe.getMainFrame ();
	}
	
	protected String getcodUtente () {
		return this.codUtente;
	}
	
	protected void setcodUtente (String codUtente) {
		this.codUtente = codUtente;
	}
	
	public void setScreen (JPanel p) {
		this.c.remove(this.screen);
		this.c.add(p); this.screen = p;
		this.mainframe.refresh();
	}
	
	public void updateTime (String time, String date) {
		((JLabel) elements.get("header_time")).setText(date + " " + time);
	}
	
	public JPanel header () {
		JPanel p = new JPanel();
		p.setBounds(0, 0, 500, 28);
		p.setBorder(new MatteBorder(0, 0, 2, 0, new Color(255, 51, 0)));
		p.setBackground(new Color(40,40,40));
		p.setLayout(null);
		
		JLabel user = new JLabel("");
		user.setBounds(422, 0, 78, 27);
		user.setForeground(Color.WHITE);
		user.setFont(new Font("Karla", Font.PLAIN, 14));
		user.setBorder(new EmptyBorder(5,10,5,10));//top,left,bottom,right
		user.setHorizontalAlignment(SwingConstants.RIGHT);
		//((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"_header" 
		user.setName("header_user"); //!!!
		elements.put(user.getName(), user);
		p.add(user);
		
		JLabel time = new JLabel("00:00:00 24-12-2000");
		time.setBounds(0, 0, 500, 27);
		time.setForeground(Color.WHITE);
		time.setFont(new Font("Karla", Font.PLAIN, 14));
		time.setBorder(new EmptyBorder(5,20,5,0));//top,left,bottom,right
		time.setHorizontalAlignment(SwingConstants.CENTER);
		time.setName("header_time"); //!!!
		elements.put(time.getName(), time);
		p.add(time);
		
		return p;
	}
	
	public JPanel info () {
		JPanel p = new JPanel();
		p.setBounds(0, 28, 500, 20);
		p.setBackground(new Color(40,40,40));
		p.setLayout(null);
		
		JLabel cliente = new JLabel("");
		cliente.setBounds(25, 0, 150, 20);
		cliente.setForeground(Color.WHITE);
		cliente.setFont(new Font("Karla", Font.PLAIN, 12));
		cliente.setBorder(new EmptyBorder(5,10,5,10));//top,left,bottom,right
		cliente.setHorizontalAlignment(SwingConstants.CENTER);
		//((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"_header" 
		cliente.setName("info_cliente"); //!!!
		elements.put(cliente.getName(), cliente);
		p.add(cliente);
		
		JLabel contDisp = new JLabel("");
		contDisp.setBounds(200, 0, 150, 20);
		contDisp.setForeground(Color.WHITE);
		contDisp.setFont(new Font("Karla", Font.PLAIN, 12));
		contDisp.setBorder(new EmptyBorder(5,10,5,10));//top,left,bottom,right
		contDisp.setHorizontalAlignment(SwingConstants.LEFT);
		//((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"_header" 
		contDisp.setName("info_disp"); //!!!
		elements.put(contDisp.getName(), contDisp);
		p.add(contDisp);

		JLabel contCont = new JLabel("");
		contCont.setBounds(325, 0, 150, 20);
		contCont.setForeground(Color.WHITE);
		contCont.setFont(new Font("Karla", Font.PLAIN, 12));
		contCont.setBorder(new EmptyBorder(5,10,5,10));//top,left,bottom,right
		contCont.setHorizontalAlignment(SwingConstants.LEFT);
		//((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"_header" 
		contCont.setName("info_cont"); //!!!
		elements.put(contCont.getName(), contCont);
		p.add(contCont);
		
		return p;
	}
	
	public JPanel login () {
		JPanel p = new JPanel();
		p.setBounds(0, 1, 500, 500);
		p.setBackground(new Color(40,40,40));
		p.setLayout(null);
		
		JLabel welcome = new JLabel("WELCOME");
		welcome.setForeground(Color.LIGHT_GRAY);
		welcome.setFont(new Font("Hack", Font.BOLD, 40));
		welcome.setHorizontalAlignment(SwingConstants.CENTER);
		welcome.setSize(500, 50);
		welcome.setLocation(0, 75);
		p.add(welcome);
		
		JLabel codutente = new JLabel("Username");
		codutente.setForeground(Color.LIGHT_GRAY);
		codutente.setFont(new Font("Hack", Font.PLAIN, 30));
		codutente.setHorizontalAlignment(SwingConstants.CENTER);
		codutente.setSize(500, 50);
		codutente.setLocation(0, 150);
		p.add(codutente);
		
		JTextField i_codutente = new JTextField(16);
		i_codutente.setBackground(Color.DARK_GRAY);
		i_codutente.setForeground(new Color(255, 51, 0));
		i_codutente.setFont(new Font("Hack", Font.PLAIN, 26));
		i_codutente.setSize(250, 40);
		i_codutente.setLocation(125, 200);
		//(String) ((List) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("cols"))).get(1)+"_login"
		i_codutente.setName("login_user"); //!!!
		elements.put(i_codutente.getName(), i_codutente);
		p.add(i_codutente);
		
		JLabel password = new JLabel("Password");
		password.setForeground(Color.LIGHT_GRAY);
		password.setHorizontalAlignment(SwingConstants.CENTER);
		password.setFont(new Font("Hack", Font.PLAIN, 30));
		password.setSize(500, 50);
		password.setLocation(0, 250);
		p.add(password);
		
		JPasswordField i_password = new JPasswordField (16);
		i_password.setBackground(Color.DARK_GRAY);
		i_password.setForeground(new Color(255, 51, 0));
		i_password.setFont(new Font("Hack", Font.PLAIN, 26));
		i_password.setSize(250, 40);
		i_password.setLocation(125, 300);
		//(String) ((List) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("cols"))).get(0)
		i_password.setName("login_password"); //!!!
		elements.put(i_password.getName(), i_password);
		p.add(i_password);
		
		JCustom.RoundedJButton enter = new JCustom.RoundedJButton("enter", 20, new Color(255, 51, 0));
		enter.setFont(new Font("Hack", Font.PLAIN, 28));
		enter.setForeground(Color.LIGHT_GRAY);
		enter.setSize(150, 50);
		enter.setLocation(175, 375);
		enter.setBackground(new Color(60,60,60));
		enter.setActionCommand("login"); //!!!
		enter.addActionListener(handler);
		p.add(enter);
		
		return p;
	}
	
	public JPanel cliente () {
		JPanel p = new JPanel();
		p.setBounds(0, 1, 500, 500);
		p.setBackground(new Color(40,40,40));
		p.setLayout(null);
		
		JCustom.RoundedJButton back = new JCustom.RoundedJButton("logout", 40, new Color(255, 51, 0));
		back.setHorizontalAlignment(SwingConstants.RIGHT);
		back.setFont(new Font("Hack", Font.PLAIN, 18));
		back.setForeground(Color.LIGHT_GRAY);
		back.setSize(130, 40);
		back.setLocation(-40, 50);
		back.setBackground(new Color(60,60,60));
		back.setActionCommand("logout"); //!!!
		back.setName("logout");
		back.addActionListener(handler);
		back.addMouseListener(new MouseHandler.Hover(back));
		p.add(back);
		
		JLabel welcome = new JLabel("ENTER");
		welcome.setForeground(Color.LIGHT_GRAY);
		welcome.setFont(new Font("Hack", Font.BOLD, 40));
		welcome.setHorizontalAlignment(SwingConstants.CENTER);
		welcome.setSize(500, 50);
		welcome.setLocation(0, 100);
		p.add(welcome);
		
		JLabel codutente = new JLabel("Codice Clienti");
		codutente.setForeground(Color.LIGHT_GRAY);
		codutente.setFont(new Font("Hack", Font.PLAIN, 30));
		codutente.setHorizontalAlignment(SwingConstants.CENTER);
		codutente.setSize(500, 50);
		codutente.setLocation(0, 180);
		p.add(codutente);
		
		JTextField i_codutente = new JTextField(16);
		i_codutente.setBackground(Color.DARK_GRAY);
		i_codutente.setForeground(new Color(255, 51, 0));
		i_codutente.setFont(new Font("Hack", Font.PLAIN, 26));
		i_codutente.setSize(250, 40);
		i_codutente.setLocation(125, 230);
		//(String) ((List) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("cols"))).get(1)+"_login"
		i_codutente.setName("cod_utente"); //!!!
		elements.put(i_codutente.getName(), i_codutente);
		p.add(i_codutente);
		
		JCustom.RoundedJButton enter = new JCustom.RoundedJButton("enter", 20, new Color(255, 51, 0));
		enter.setFont(new Font("Hack", Font.PLAIN, 28));
		enter.setForeground(Color.LIGHT_GRAY);
		enter.setSize(150, 50);
		enter.setLocation(175, 375);
		enter.setBackground(new Color(60,60,60));
		enter.setActionCommand("utente"); //!!!
		enter.addActionListener(handler);
		p.add(enter);
		
		return p;
	}
	
	public JPanel settings () {
		JPanel p = new JPanel();
		p.setBounds(0, 1, 500, 500);
		p.setBackground(new Color(40,40,40));
		p.setLayout(null);
		
		JCustom.RoundedJButton back = new JCustom.RoundedJButton("back ", 40, new Color(255, 51, 0));
		back.setHorizontalAlignment(SwingConstants.RIGHT);
		back.setFont(new Font("Hack", Font.PLAIN, 18));
		back.setForeground(Color.LIGHT_GRAY);
		back.setSize(130, 40);
		back.setLocation(-40, 50);
		back.setBackground(new Color(60,60,60));
		back.setActionCommand("back_index"); //!!!
		back.setName("back_index");
		back.addActionListener(handler);
		back.addMouseListener(new MouseHandler.Hover(back));
		p.add(back);
		
		JLabel welcome = new JLabel("SETTINGS");
		welcome.setForeground(Color.LIGHT_GRAY);
		welcome.setFont(new Font("Hack", Font.BOLD, 40));
		welcome.setHorizontalAlignment(SwingConstants.CENTER);
		welcome.setSize(500, 50);
		welcome.setLocation(0, 45);
		p.add(welcome);
		
		JLabel codutente = new JLabel("Imposta fido");
		codutente.setForeground(Color.LIGHT_GRAY);
		codutente.setFont(new Font("Hack", Font.PLAIN, 22));
		codutente.setHorizontalAlignment(SwingConstants.LEFT);
		codutente.setSize(450, 50);
		codutente.setLocation(25, 150);
		p.add(codutente);
		
		DecimalFormatSymbols custom=new DecimalFormatSymbols();
		custom.setDecimalSeparator('.'); custom.setGroupingSeparator(',');
		DecimalFormat format = new DecimalFormat("###,##0.00");
		format.setDecimalFormatSymbols(custom);
		JCustom.RoundFormattedJTextField i_fido = new JCustom.RoundFormattedJTextField(format, 0, new Color(255, 51, 0));
		i_fido.setBackground(Color.DARK_GRAY);
		i_fido.setForeground(new Color(255, 51, 0));
		i_fido.setFont(new Font("Hack", Font.PLAIN, 26));
		i_fido.setSize(200, 40);
		i_fido.setLocation(250, 155);
		//(String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(3)+"_prelievo"
		i_fido.getDocument().addDocumentListener(new DocumentHandler(this, i_fido));
		i_fido.setName("settings_fido"); //!!!
		elements.put(i_fido.getName(), i_fido);
		p.add(i_fido);
		
		JCustom.RoundedJButton enter = new JCustom.RoundedJButton("salva", 20, new Color(255, 51, 0));
		enter.setFont(new Font("Hack", Font.PLAIN, 28));
		enter.setForeground(Color.LIGHT_GRAY);
		enter.setSize(150, 50);
		enter.setLocation(175, 375);
		enter.setBackground(new Color(60,60,60));
		enter.setActionCommand("salva"); //!!!
		enter.addActionListener(handler);
		p.add(enter);
		
		return p;
	}

	public JPanel index () {
		JPanel p = new JPanel();
		p.setBounds(0, 1, 500, 500);
		p.setBackground(new Color(40,40,40));
		p.setLayout(null);
		
		JCustom.RoundedJButton back = new JCustom.RoundedJButton("exit ", 40, new Color(255, 51, 0));
		back.setHorizontalAlignment(SwingConstants.RIGHT);
		back.setFont(new Font("Hack", Font.PLAIN, 18));
		back.setForeground(Color.LIGHT_GRAY);
		back.setSize(130, 40);
		back.setLocation(-40, 50);
		back.setBackground(new Color(60,60,60));
		back.setActionCommand("exit"); //!!!
		back.setName("exit");
		back.addActionListener(handler);
		back.addMouseListener(new MouseHandler.Hover(back));
		p.add(back);

		JCustom.RoundedJButton settings = new JCustom.RoundedJButton("", 40, new Color(255, 51, 0, 0));
		settings.setIcon(new ImageIcon(JCustom.getScaledImage(getClass().getResource("/photo/settings.png"), 40, 40)));
		settings.setHorizontalAlignment(SwingConstants.CENTER);
		settings.setFont(new Font("Hack", Font.PLAIN, 18));
		settings.setForeground(Color.LIGHT_GRAY);
		settings.setSize(40, 40);
		settings.setLocation(425, 50);
		settings.setBackground(new Color(60,60,60,0));
		settings.setActionCommand("settings"); //!!!
		settings.setName("settings");
		settings.addActionListener(handler);
		settings.addMouseListener(new MouseHandler.Hover(settings));
		p.add(settings);
		
		JLabel welcome = new JLabel("CLIENTE");
		welcome.setForeground(Color.LIGHT_GRAY);
		welcome.setFont(new Font("Hack", Font.BOLD, 40));
		welcome.setHorizontalAlignment(SwingConstants.CENTER);
		welcome.setSize(200, 50);
		welcome.setLocation(150, 75);
		welcome.setBorder(new MatteBorder(0, 0, 2, 0, new Color(255, 51, 0)));
		p.add(welcome);
		
		JLabel utente = new JLabel("utente");
		utente.setForeground(Color.LIGHT_GRAY);
		utente.setFont(new Font("Hack", Font.PLAIN, 30));
		utente.setHorizontalAlignment(SwingConstants.CENTER);
		utente.setSize(500, 50);
		utente.setLocation(0, 120);
		utente.setName("index_user");
		elements.put(utente.getName(), utente);
		p.add(utente);
		
		JCustom.RoundedJButton deposito = new JCustom.RoundedJButton("deposito", 20, new Color(255, 51, 0));
		deposito.setFont(new Font("Hack", Font.PLAIN, 28));
		deposito.setForeground(Color.LIGHT_GRAY);
		deposito.setSize(220, 100);
		deposito.setLocation(-20, 190);
		deposito.setBackground(new Color(60,60,60));
		deposito.setActionCommand("deposito"); //!!!
		deposito.addActionListener(handler);
		deposito.setName("deposito");
		deposito.addMouseListener(new MouseHandler.Hover(deposito));
		p.add(deposito);
		
		JCustom.RoundedJButton prelievo = new JCustom.RoundedJButton("prelievo", 20, new Color(255, 51, 0));
		prelievo.setFont(new Font("Hack", Font.PLAIN, 28));
		prelievo.setForeground(Color.LIGHT_GRAY);
		prelievo.setSize(220, 100);
		prelievo.setLocation(300, 190);
		prelievo.setBackground(new Color(60,60,60));
		prelievo.setActionCommand("prelievo"); //!!!
		prelievo.addActionListener(handler);
		prelievo.setName("prelievo");
		prelievo.addMouseListener(new MouseHandler.Hover(prelievo));
		p.add(prelievo);
		
		JCustom.RoundedJButton bonifico = new JCustom.RoundedJButton("bonifico", 20, new Color(255, 51, 0));
		bonifico.setFont(new Font("Hack", Font.PLAIN, 28));
		bonifico.setForeground(Color.LIGHT_GRAY);
		bonifico.setSize(220, 100);
		bonifico.setLocation(-20, 325);
		bonifico.setBackground(new Color(60,60,60));
		bonifico.setActionCommand("bonifico"); //!!!
		bonifico.addActionListener(handler);
		bonifico.setName("bonifico");
		bonifico.addMouseListener(new MouseHandler.Hover(bonifico));
		p.add(bonifico);
		
		JCustom.RoundedJButton estratto = new JCustom.RoundedJButton("estratto", 20, new Color(255, 51, 0));
		estratto.setFont(new Font("Hack", Font.PLAIN, 28));
		estratto.setForeground(Color.LIGHT_GRAY);
		estratto.setSize(220, 100);
		estratto.setLocation(300, 325);
		estratto.setBackground(new Color(60,60,60));
		estratto.setActionCommand("estratto"); //!!!
		estratto.addActionListener(handler);
		estratto.setName("estratto");
		estratto.addMouseListener(new MouseHandler.Hover(estratto));
		p.add(estratto);
		
		return p;
	}
	
	public JPanel deposito () {
		JPanel p = new JPanel();
		p.setBounds(0, 1, 500, 500);
		p.setBackground(new Color(40,40,40));
		p.setLayout(null);
		
		JCustom.RoundedJButton back = new JCustom.RoundedJButton("back ", 40, new Color(255, 51, 0));
		back.setHorizontalAlignment(SwingConstants.RIGHT);
		back.setFont(new Font("Hack", Font.PLAIN, 18));
		back.setForeground(Color.LIGHT_GRAY);
		back.setSize(130, 40);
		back.setLocation(-40, 50);
		back.setBackground(new Color(60,60,60));
		back.setActionCommand("back_index"); //!!!
		back.setName("back_index");
		back.addActionListener(handler);
		back.addMouseListener(new MouseHandler.Hover(back));
		p.add(back);
		
		JLabel icon = new JLabel("");
		icon.setIcon(new ImageIcon(JCustom.getScaledImage(getClass().getResource("/photo/deposito.png"), 200, 200)));
		icon.setForeground(Color.LIGHT_GRAY);
		icon.setFont(new Font("Hack", Font.PLAIN, 30));
		icon.setHorizontalAlignment(SwingConstants.CENTER);
		icon.setSize(500, 150);
		icon.setLocation(0, 180);
		p.add(icon);
		
		JLabel info = new JLabel("Inserisci deposito");
		info.setForeground(Color.LIGHT_GRAY);
		info.setFont(new Font("Hack", Font.PLAIN, 30));
		info.setHorizontalAlignment(SwingConstants.CENTER);
		info.setSize(500, 50);
		info.setLocation(0, 100);
		p.add(info);

		DecimalFormatSymbols custom=new DecimalFormatSymbols();
		custom.setDecimalSeparator('.'); custom.setGroupingSeparator(',');
		DecimalFormat format = new DecimalFormat("###,##0.00");
		format.setDecimalFormatSymbols(custom);
		JCustom.RoundFormattedJTextField i_deposito = new JCustom.RoundFormattedJTextField(format, 0, new Color(255, 51, 0));
		i_deposito.setBackground(Color.DARK_GRAY);
		i_deposito.setForeground(new Color(255, 51, 0));
		i_deposito.setFont(new Font("Hack", Font.PLAIN, 26));
		i_deposito.setSize(220, 40);
		i_deposito.setLocation(65, 350);
		//(String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(3)+"_deposito"
		i_deposito.setName("deposito_importo"); //!!!
		elements.put(i_deposito.getName(), i_deposito);
		p.add(i_deposito);
		
		JCustom.RoundedJButton deposit = new JCustom.RoundedJButton("deposita", 0, new Color(255, 51, 0));
		deposit.setFont(new Font("Hack", Font.PLAIN, 20));
		deposit.setForeground(Color.LIGHT_GRAY);
		deposit.setSize(140, 40);
		deposit.setLocation(285, 350);
		deposit.setBackground(new Color(60,60,60));
		deposit.setActionCommand("send_deposito"); //!!!
		deposit.addActionListener(handler);
		deposit.setName("send_deposito");
		deposit.addMouseListener(new MouseHandler.Hover(deposit));
		p.add(deposit);
		
		return p;
	}
	

	public JPanel prelievo () {
		JPanel p = new JPanel();
		p.setBounds(0, 1, 500, 500);
		p.setBackground(new Color(40,40,40));
		p.setLayout(null);
		
		JCustom.RoundedJButton back = new JCustom.RoundedJButton("back ", 40, new Color(255, 51, 0));
		back.setHorizontalAlignment(SwingConstants.RIGHT);
		back.setFont(new Font("Hack", Font.PLAIN, 18));
		back.setForeground(Color.LIGHT_GRAY);
		back.setSize(130, 40);
		back.setLocation(-40, 50);
		back.setBackground(new Color(60,60,60));
		back.setActionCommand("back_index"); //!!!
		back.setName("back_index");
		back.addActionListener(handler);
		back.addMouseListener(new MouseHandler.Hover(back));
		p.add(back);
		
		JLabel info = new JLabel("Esegui prelievo");
		info.setForeground(Color.LIGHT_GRAY);
		info.setFont(new Font("Hack", Font.PLAIN, 30));
		info.setHorizontalAlignment(SwingConstants.CENTER);
		info.setSize(500, 50);
		info.setLocation(0, 100);
		p.add(info);
		
		JCustom.RoundedJButton prelievo = new JCustom.RoundedJButton("5 € ", 0, new Color(255, 51, 0));
		prelievo.setHorizontalAlignment(SwingConstants.RIGHT);
		prelievo.setFont(new Font("Hack", Font.PLAIN, 20));
		prelievo.setForeground(Color.LIGHT_GRAY);
		prelievo.setSize(140, 50);
		prelievo.setLocation(-10, 175);
		prelievo.setBackground(new Color(60,60,60));
		prelievo.setActionCommand("prelievo5"); //!!!
		prelievo.addActionListener(handler);
		prelievo.setName("prelievo5");
		prelievo.addMouseListener(new MouseHandler.Hover(prelievo));
		p.add(prelievo);
		
		JCustom.RoundedJButton prelievo1 = new JCustom.RoundedJButton("10 € ", 0, new Color(255, 51, 0));
		prelievo1.setHorizontalAlignment(SwingConstants.RIGHT);
		prelievo1.setFont(new Font("Hack", Font.PLAIN, 20));
		prelievo1.setForeground(Color.LIGHT_GRAY);
		prelievo1.setSize(140, 50);
		prelievo1.setLocation(-10, 250);
		prelievo1.setBackground(new Color(60,60,60));
		prelievo1.setActionCommand("prelievo10"); //!!!
		prelievo1.addActionListener(handler);
		prelievo1.setName("prelievo10");
		prelievo1.addMouseListener(new MouseHandler.Hover(prelievo1));
		p.add(prelievo1);
		
		JCustom.RoundedJButton prelievo2 = new JCustom.RoundedJButton("20 € ", 0, new Color(255, 51, 0));
		prelievo2.setHorizontalAlignment(SwingConstants.RIGHT);
		prelievo2.setFont(new Font("Hack", Font.PLAIN, 20));
		prelievo2.setForeground(Color.LIGHT_GRAY);
		prelievo2.setSize(140, 50);
		prelievo2.setLocation(-10, 325);
		prelievo2.setBackground(new Color(60,60,60));
		prelievo2.setActionCommand("prelievo20"); //!!!
		prelievo2.addActionListener(handler);
		prelievo2.setName("prelievo20");
		prelievo2.addMouseListener(new MouseHandler.Hover(prelievo2));
		p.add(prelievo2);
		
		JCustom.RoundedJButton prelievo3 = new JCustom.RoundedJButton("50 € ", 0, new Color(255, 51, 0));
		prelievo3.setHorizontalAlignment(SwingConstants.RIGHT);
		prelievo3.setFont(new Font("Hack", Font.PLAIN, 20));
		prelievo3.setForeground(Color.LIGHT_GRAY);
		prelievo3.setSize(140, 50);
		prelievo3.setLocation(-10, 400);
		prelievo3.setBackground(new Color(60,60,60));
		prelievo3.setActionCommand("prelievo50"); //!!!
		prelievo3.addActionListener(handler);
		prelievo3.setName("prelievo50");
		prelievo3.addMouseListener(new MouseHandler.Hover(prelievo3));
		p.add(prelievo3);

		JCustom.RoundedJButton prelievo4 = new JCustom.RoundedJButton("€ 100 ", 0, new Color(255, 51, 0));
		prelievo4.setHorizontalAlignment(SwingConstants.LEFT);
		prelievo4.setFont(new Font("Hack", Font.PLAIN, 20));
		prelievo4.setForeground(Color.LIGHT_GRAY);
		prelievo4.setSize(140, 50);
		prelievo4.setLocation(370, 175);
		prelievo4.setBackground(new Color(60,60,60));
		prelievo4.setActionCommand("prelievo100"); //!!!
		prelievo4.addActionListener(handler);
		prelievo4.setName("prelievo100");
		prelievo4.addMouseListener(new MouseHandler.Hover(prelievo4));
		p.add(prelievo4);
		
		JCustom.RoundedJButton prelievo5 = new JCustom.RoundedJButton("€ 200 ", 0, new Color(255, 51, 0));
		prelievo5.setHorizontalAlignment(SwingConstants.LEFT);
		prelievo5.setFont(new Font("Hack", Font.PLAIN, 20));
		prelievo5.setForeground(Color.LIGHT_GRAY);
		prelievo5.setSize(140, 50);
		prelievo5.setLocation(370, 250);
		prelievo5.setBackground(new Color(60,60,60));
		prelievo5.setActionCommand("prelievo200"); //!!!
		prelievo5.addActionListener(handler);
		prelievo5.setName("prelievo200");
		prelievo5.addMouseListener(new MouseHandler.Hover(prelievo5));
		p.add(prelievo5);
		
		JCustom.RoundedJButton prelievo6 = new JCustom.RoundedJButton("€ 500 ", 0, new Color(255, 51, 0));
		prelievo6.setHorizontalAlignment(SwingConstants.LEFT);
		prelievo6.setFont(new Font("Hack", Font.PLAIN, 20));
		prelievo6.setForeground(Color.LIGHT_GRAY);
		prelievo6.setSize(140, 50);
		prelievo6.setLocation(370, 325);
		prelievo6.setBackground(new Color(60,60,60));
		prelievo6.setActionCommand("prelievo500"); //!!!
		prelievo6.addActionListener(handler);
		prelievo6.setName("prelievo500");
		prelievo6.addMouseListener(new MouseHandler.Hover(prelievo6));
		p.add(prelievo6);
		
		JCustom.RoundedJButton prelievo7 = new JCustom.RoundedJButton("€ 1000 ", 0, new Color(255, 51, 0));
		prelievo7.setHorizontalAlignment(SwingConstants.LEFT);
		prelievo7.setFont(new Font("Hack", Font.PLAIN, 20));
		prelievo7.setForeground(Color.LIGHT_GRAY);
		prelievo7.setSize(140, 50);
		prelievo7.setLocation(370, 400);
		prelievo7.setBackground(new Color(60,60,60));
		prelievo7.setActionCommand("prelievo1000"); //!!!
		prelievo7.addActionListener(handler);
		prelievo7.setName("prelievo1000");
		prelievo7.addMouseListener(new MouseHandler.Hover(prelievo7));
		p.add(prelievo7);
		
		JLabel info1 = new JLabel("Quantita");
		info1.setForeground(Color.LIGHT_GRAY);
		info1.setFont(new Font("Hack", Font.PLAIN, 24));
		info1.setHorizontalAlignment(SwingConstants.CENTER);
		info1.setSize(240, 50);
		info1.setLocation(130, 210);
		p.add(info1);

		DecimalFormatSymbols custom=new DecimalFormatSymbols();
		custom.setDecimalSeparator('.'); custom.setGroupingSeparator(',');
		DecimalFormat format = new DecimalFormat("###,##0.00");
		format.setDecimalFormatSymbols(custom);
		JCustom.RoundFormattedJTextField i_prelievo = new JCustom.RoundFormattedJTextField(format, 0, new Color(255, 51, 0));
		i_prelievo.setBackground(Color.DARK_GRAY);
		i_prelievo.setForeground(new Color(255, 51, 0));
		i_prelievo.setFont(new Font("Hack", Font.PLAIN, 26));
		i_prelievo.setSize(180, 40);
		i_prelievo.setLocation(160, 260);
		//(String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(3)+"_prelievo"
		i_prelievo.getDocument().addDocumentListener(new DocumentHandler(this, i_prelievo));
		i_prelievo.setName("prelievo_importo"); //!!!
		elements.put(i_prelievo.getName(), i_prelievo);
		p.add(i_prelievo);
		
		JCustom.RoundedJButton preliev = new JCustom.RoundedJButton("conferma", 0, new Color(255, 51, 0));
		preliev.setFont(new Font("Hack", Font.PLAIN, 20));
		preliev.setForeground(Color.LIGHT_GRAY);
		preliev.setSize(140, 40);
		preliev.setLocation(180, 340);
		preliev.setBackground(new Color(60,60,60));
		preliev.setActionCommand("conferma_prelievo"); //!!!
		preliev.addActionListener(handler);
		preliev.setName("conferma_prelievo");
		preliev.addMouseListener(new MouseHandler.Hover(preliev));
		elements.put(preliev.getName(), preliev);
		preliev.setVisible(false);
		p.add(preliev);
		
		return p;
	}
	
	public JPanel bonifico () {
		JPanel p = new JPanel();
		p.setBounds(0, 1, 500, 500);
		p.setBackground(new Color(40,40,40));
		p.setLayout(null);
		
		JCustom.RoundedJButton back = new JCustom.RoundedJButton("back ", 40, new Color(255, 51, 0));
		back.setHorizontalAlignment(SwingConstants.RIGHT);
		back.setFont(new Font("Hack", Font.PLAIN, 18));
		back.setForeground(Color.LIGHT_GRAY);
		back.setSize(130, 40);
		back.setLocation(-40, 50);
		back.setBackground(new Color(60,60,60));
		back.setActionCommand("back_index"); //!!!
		back.setName("back_index");
		back.addActionListener(handler);
		back.addMouseListener(new MouseHandler.Hover(back));
		p.add(back);
		
		JLabel info = new JLabel("Manda bonifico");
		info.setForeground(Color.LIGHT_GRAY);
		info.setFont(new Font("Hack", Font.PLAIN, 30));
		info.setHorizontalAlignment(SwingConstants.CENTER);
		info.setSize(500, 50);
		info.setLocation(0, 100);
		p.add(info);
		
		JLabel desinatario = new JLabel("Destinatario");
		desinatario.setForeground(Color.LIGHT_GRAY);
		desinatario.setFont(new Font("Hack", Font.PLAIN, 18));
		desinatario.setHorizontalAlignment(SwingConstants.LEFT);
		desinatario.setSize(150, 40);
		desinatario.setLocation(50, 200);
		p.add(desinatario);

		JCustom.RoundJTextField i_destinatario = new JCustom.RoundJTextField(16, 0, new Color(255, 51, 0));
		i_destinatario.setBackground(Color.DARK_GRAY);
		i_destinatario.setForeground(new Color(255, 51, 0));
		i_destinatario.setFont(new Font("Hack", Font.PLAIN, 26));
		i_destinatario.setSize(250, 40);
		i_destinatario.setLocation(200, 200);
		i_destinatario.setName("bonifico_destinatario"); //!!!
		elements.put(i_destinatario.getName(), i_destinatario);
		i_destinatario.addFocusListener(new sHandler.Focus() {
			@SuppressWarnings("rawtypes")
			@Override
			public void focusLost(FocusEvent arg0) {
				if ((i_destinatario.getText() != null && !i_destinatario.getText().isEmpty())) {
					Map settingJson = getJson();
					String b = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))+"."+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))
			                +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))
			                +" WHERE "
			                +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"='"+i_destinatario.getText()+"'";
					try {
						String check = sql.query(b)[1][0];
						if (check.isEmpty() || check == null) {
							handler.showMessage("BONIFICO", "Destinatario non trovato");
							i_destinatario.setText("");						
						} else if (check.equals(getcodUtente())) {
							handler.showMessage("BONIFICO", "Non ti puoi pagare da solo");
							i_destinatario.setText("");		
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
						handler.showMessage("BONIFICO", "Destinatario non trovato");
						i_destinatario.setText("");
					} catch (Exception e2) {
						e2.printStackTrace();
						handler.showMessage("BONIFICO", "Destinatario non trovato");
						i_destinatario.setText("");
					}
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {	}
		});
		p.add(i_destinatario);
		
		JLabel importo = new JLabel("Importo");
		importo.setForeground(Color.LIGHT_GRAY);
		importo.setFont(new Font("Hack", Font.PLAIN, 18));
		importo.setHorizontalAlignment(SwingConstants.LEFT);
		importo.setSize(150, 40);
		importo.setLocation(50, 280);
		p.add(importo);
		
		DecimalFormatSymbols custom=new DecimalFormatSymbols();
		custom.setDecimalSeparator('.'); custom.setGroupingSeparator(',');
		DecimalFormat format = new DecimalFormat("###,##0.00");
		format.setDecimalFormatSymbols(custom);
		JCustom.RoundFormattedJTextField i_importo_bonifico = new JCustom.RoundFormattedJTextField(format, 0, new Color(255, 51, 0));
		i_importo_bonifico.setBackground(Color.DARK_GRAY);
		i_importo_bonifico.setForeground(new Color(255, 51, 0));
		i_importo_bonifico.setFont(new Font("Hack", Font.PLAIN, 26));
		i_importo_bonifico.setSize(150, 40);
		i_importo_bonifico.setLocation(200, 280);
		i_importo_bonifico.setName("bonifico_importo"); //!!!
		elements.put(i_importo_bonifico.getName(), i_importo_bonifico);
		p.add(i_importo_bonifico);
		
		JCustom.RoundedJButton send_bonifico = new JCustom.RoundedJButton("spedisci", 20, new Color(255, 51, 0));
		send_bonifico.setFont(new Font("Hack", Font.PLAIN, 20));
		send_bonifico.setForeground(Color.LIGHT_GRAY);
		send_bonifico.setSize(150, 45);
		send_bonifico.setLocation(300, 370);
		send_bonifico.setBackground(new Color(60,60,60));
		send_bonifico.setActionCommand("send_bonifico"); //!!!
		send_bonifico.addActionListener(handler);
		send_bonifico.setName("send_bonifico");
		send_bonifico.addMouseListener(new MouseHandler.Hover(send_bonifico));
		p.add(send_bonifico);
		
		return p;
	}
	
	public JPanel view_bonifici () { // FARE LA VIEW DEI BONIFICI CON IL LORO STATO
		JPanel p = new JPanel();
		p.setBounds(0, 1, 500, 500);
		p.setBackground(new Color(40,40,40));
		p.setLayout(null);

		JCustom.RoundedJButton change = new JCustom.RoundedJButton("Movimenti", 40, new Color(255, 51, 0));
		change.setFont(new Font("Hack", Font.PLAIN, 16));
		change.setForeground(Color.LIGHT_GRAY);
		change.setSize(150, 40);
		change.setLocation(325, 50);
		change.setBackground(new Color(60,60,60));
		change.setActionCommand("movimento"); //!!!
		change.addActionListener(handler);
		change.addMouseListener(new MouseHandler.Hover(change));
		p.add(change);
		
		JLabel info = new JLabel("Estratto");
		info.setForeground(Color.LIGHT_GRAY);
		info.setFont(new Font("Hack", Font.PLAIN, 30));
		info.setHorizontalAlignment(SwingConstants.CENTER);
		info.setSize(350, 50);
		info.setLocation(25, 45);
		p.add(info);
		
		JCustom.RoundedJButton back = new JCustom.RoundedJButton("back ", 40, new Color(255, 51, 0));
		back.setHorizontalAlignment(SwingConstants.RIGHT);
		back.setFont(new Font("Hack", Font.PLAIN, 18));
		back.setForeground(Color.LIGHT_GRAY);
		back.setSize(130, 40);
		back.setLocation(-40, 50);
		back.setBackground(new Color(60,60,60));
		back.setActionCommand("back_index"); //!!!
		back.setName("back_index");
		back.addActionListener(handler);
		back.addMouseListener(new MouseHandler.Hover(back));
		p.add(back);

	    JTextArea i_estratto = new JTextArea(16, 5);
	    i_estratto.setForeground(Color.WHITE);
	    i_estratto.setBackground(Color.DARK_GRAY);
	    i_estratto.setFont(new Font("monospaced", Font.PLAIN, 16));
	    i_estratto.setLineWrap(true);
	    i_estratto.setEditable(false); // set textArea non-editable
	    i_estratto.setName("estratto"); //!!!
	    elements.put(i_estratto.getName(), i_estratto);
	    
	    JScrollPane display = new JScrollPane(i_estratto);
	    display.setSize(480, 330);
	    display.setLocation(10, 130);
	    display.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    display.setName("estratto_conto_lista");
	    p.add(display);
		
		return p;
	}
	
	public JPanel estratto () {
		JPanel p = new JPanel();
		p.setBounds(0, 1, 500, 500);
		p.setBackground(new Color(40,40,40));
		p.setLayout(null);

		JCustom.RoundedJButton change = new JCustom.RoundedJButton("Movimenti", 40, new Color(255, 51, 0));
		change.setFont(new Font("Hack", Font.PLAIN, 16));
		change.setForeground(Color.LIGHT_GRAY);
		change.setSize(150, 40);
		change.setLocation(325, 50);
		change.setBackground(new Color(60,60,60));
		change.setActionCommand("movimento"); //!!!
		change.addActionListener(handler);
		change.addMouseListener(new MouseHandler.Hover(change));
		p.add(change);
		
		JLabel info = new JLabel("Estratto");
		info.setForeground(Color.LIGHT_GRAY);
		info.setFont(new Font("Hack", Font.PLAIN, 30));
		info.setHorizontalAlignment(SwingConstants.CENTER);
		info.setSize(350, 50);
		info.setLocation(25, 45);
		p.add(info);
		
		JCustom.RoundedJButton back = new JCustom.RoundedJButton("back ", 40, new Color(255, 51, 0));
		back.setHorizontalAlignment(SwingConstants.RIGHT);
		back.setFont(new Font("Hack", Font.PLAIN, 18));
		back.setForeground(Color.LIGHT_GRAY);
		back.setSize(130, 40);
		back.setLocation(-40, 50);
		back.setBackground(new Color(60,60,60));
		back.setActionCommand("back_index"); //!!!
		back.setName("back_index");
		back.addActionListener(handler);
		back.addMouseListener(new MouseHandler.Hover(back));
		p.add(back);

	    JTextArea i_estratto = new JTextArea(16, 5);
	    i_estratto.setForeground(Color.WHITE);
	    i_estratto.setBackground(Color.DARK_GRAY);
	    i_estratto.setFont(new Font("monospaced", Font.PLAIN, 16));
	    i_estratto.setLineWrap(true);
	    i_estratto.setEditable(false); // set textArea non-editable
	    i_estratto.setName("estratto"); //!!!
	    elements.put(i_estratto.getName(), i_estratto);
	    
	    JScrollPane display = new JScrollPane(i_estratto);
	    display.setSize(480, 330);
	    display.setLocation(10, 130);
	    display.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    display.setName("estratto_conto_lista");
	    p.add(display);
		
		return p;
	}
	
	public JPanel movimento () {
		JPanel p = new JPanel();
		p.setBounds(0, 1, 500, 500);
		p.setBackground(new Color(40,40,40));
		p.setLayout(null);
		
		JCustom.RoundedJButton change = new JCustom.RoundedJButton("Estratto", 40, new Color(255, 51, 0));
		change.setFont(new Font("Hack", Font.PLAIN, 16));
		change.setForeground(Color.LIGHT_GRAY);
		change.setSize(150, 40);
		change.setLocation(325, 50);
		change.setBackground(new Color(60,60,60));
		change.setActionCommand("estratto"); //!!!
		change.addActionListener(handler);
		change.addMouseListener(new MouseHandler.Hover(change));
		p.add(change);

		JLabel info = new JLabel("Movimenti");
		info.setForeground(Color.LIGHT_GRAY);
		info.setFont(new Font("Hack", Font.PLAIN, 30));
		info.setHorizontalAlignment(SwingConstants.CENTER);
		info.setSize(350, 50);
		info.setLocation(25, 45);
		p.add(info);
		
		JCustom.RoundedJButton back = new JCustom.RoundedJButton("back ", 40, new Color(255, 51, 0));
		back.setHorizontalAlignment(SwingConstants.RIGHT);
		back.setFont(new Font("Hack", Font.PLAIN, 18));
		back.setForeground(Color.LIGHT_GRAY);
		back.setSize(130, 40);
		back.setLocation(-40, 50);
		back.setBackground(new Color(60,60,60));
		back.setActionCommand("back_index"); //!!!
		back.setName("back_index");
		back.addActionListener(handler);
		back.addMouseListener(new MouseHandler.Hover(back));
		p.add(back);

		JLabel dataInizio = new JLabel("Da: ");
		dataInizio.setForeground(Color.LIGHT_GRAY);
		dataInizio.setFont(new Font("Hack", Font.PLAIN, 18));
		dataInizio.setHorizontalAlignment(SwingConstants.LEFT);
		dataInizio.setSize(50, 40);
		dataInizio.setLocation(25, 90);
		p.add(dataInizio);
		
		JCustom.JFormattedDateTextField i_dataInizio = new JCustom.JFormattedDateTextField(20, new Color(255, 51, 0));
		i_dataInizio.setForeground(new Color(255, 51, 0));
		i_dataInizio.setBackground(Color.DARK_GRAY);
		i_dataInizio.setFont(new Font("Karla", Font.PLAIN, 22));
		i_dataInizio.setLocation(75, 95);
		i_dataInizio.setSize(150, 30);
		i_dataInizio.setName("data_inizio"); //!!!
		elements.put(i_dataInizio.getName(), i_dataInizio);
		i_dataInizio.addFocusListener(new sHandler.Focus() {
			@SuppressWarnings("rawtypes")
			@Override
			public void focusLost(FocusEvent arg0) {Map settingJson = getJson();
				String q = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(1))
	            		+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(2))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(3))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))
	                    +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))
	                    +" WHERE "
	                    +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"='"+getcodUtente()+"'"
	                    +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
	                    +" AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(2))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")))
	                    +" AND ("+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(1))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
	                    +" OR "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(2))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))+")";
				
				if (((JFormattedTextField) getElements().get("data_inizio")).getValue() != null)
					q += " AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))+">='"+((JTextField) getElements().get("data_inizio")).getText()+"'";
		        if (((JFormattedTextField) getElements().get("data_fine")).getValue() != null)
		        	q += " AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))+"<='"+((JTextField) getElements().get("data_fine")).getText()+"'";
				
		        q += " ORDER BY " + ((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4));
		        
		        try {
					String[][] res = sql.query(q);
					JTextArea estratto = (JTextArea) getElements().get("movimenti");
					boolean first_row = true;
					estratto.selectAll();
					estratto.replaceSelection("");
					for(String[] r : res) {
						if (first_row){
							String s = String.format("%-12s | %-12s | %-12s\n", "Data", "Accredito", "Addebito");
							estratto.append(s); 
							first_row = false; continue;
						}							
						estratto.append(String.format("%-12s | %-12s | %-12s\n", r[3], (r[0] != null && !r[0].isEmpty() ? " " : r[2]), (r[0] != null && !r[0].isEmpty() ? r[2] : " ") ));
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e2) {// create a dialog Box 
					e2.printStackTrace();
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {	}
		});
		p.add(i_dataInizio);

		JLabel dataFine = new JLabel("A: ");
		dataFine.setForeground(Color.LIGHT_GRAY);
		dataFine.setFont(new Font("Hack", Font.PLAIN, 18));
		dataFine.setHorizontalAlignment(SwingConstants.LEFT);
		dataFine.setSize(50, 40);
		dataFine.setLocation(250, 90);
		p.add(dataFine);
	    
		JCustom.JFormattedDateTextField i_dataFine = new JCustom.JFormattedDateTextField(20, new Color(255, 51, 0));
		i_dataFine.setForeground(new Color(255, 51, 0));
		i_dataFine.setBackground(Color.DARK_GRAY);
		i_dataFine.setFont(new Font("Karla", Font.PLAIN, 22));
		i_dataFine.setLocation(300, 95);
		i_dataFine.setSize(150, 30);
		i_dataFine.setName("data_fine"); //!!!
		elements.put(i_dataFine.getName(), i_dataFine);
		i_dataFine.addFocusListener(new sHandler.Focus() {
			@SuppressWarnings("rawtypes")
			@Override
			public void focusLost(FocusEvent arg0) {Map settingJson = getJson();
				String q = "SELECT "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(1))
	            		+", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(2))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(3))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))+"."+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))
	                    +" FROM "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("name")))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("name")))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("name")))
	                    +", "+((String) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("name")))
	                    +" WHERE "
	                    +((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk")))+"='"+getcodUtente()+"'"
	                    +" AND "+((String) (((Map) ((Map) settingJson.get("tables")).get("credenzialiutente")).get("pk1")))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
	                    +" AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("cols"))).get(2))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("conto")).get("pk")))
	                    +" AND ("+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(1))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))
	                    +" OR "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(2))+"="+((String) (((Map) ((Map) settingJson.get("tables")).get("anagrafica")).get("pk")))+")";
				
				if (((JFormattedTextField) getElements().get("data_inizio")).getValue() != null)
					q += " AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))+">='"+((JTextField) getElements().get("data_inizio")).getText()+"'";
		        if (((JFormattedTextField) getElements().get("data_fine")).getValue() != null)
		        	q += " AND "+((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4))+"<='"+((JTextField) getElements().get("data_fine")).getText()+"'";
				
		        q += " ORDER BY " + ((String) ((List) (((Map) ((Map) settingJson.get("tables")).get("movimento")).get("cols"))).get(4));
		        
		        try {
					String[][] res = sql.query(q);
					JTextArea estratto = (JTextArea) getElements().get("movimenti");
					boolean first_row = true;
					estratto.selectAll();
					estratto.replaceSelection("");
					for(String[] r : res) {
						if (first_row){
							String s = String.format("%-12s | %-12s | %-12s\n", "Data", "Accredito", "Addebito");
							estratto.append(s); 
							first_row = false; continue;
						}							
						estratto.append(String.format("%-12s | %-12s | %-12s\n", r[3], (r[0] != null && !r[0].isEmpty() ? " " : r[2]), (r[0] != null && !r[0].isEmpty() ? r[2] : " ") ));
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e2) {// create a dialog Box 
					e2.printStackTrace();
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {	}
		});
		p.add(i_dataFine);
		
	    JTextArea i_estratto = new JTextArea(16, 5);
	    i_estratto.setForeground(Color.WHITE);
	    i_estratto.setBackground(Color.DARK_GRAY);
	    i_estratto.setFont(new Font("monospaced", Font.PLAIN, 16));
	    i_estratto.setLineWrap(true);
	    i_estratto.setEditable(false); // set textArea non-editable
	    i_estratto.setName("movimenti"); //!!!
	    elements.put(i_estratto.getName(), i_estratto);
	    
	    JScrollPane display = new JScrollPane(i_estratto);
	    display.setSize(480, 330);
	    display.setLocation(10, 130);
	    display.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    display.setName("estratto_conto_lista");
	    p.add(display);
	    
	    String s = String.format("%-12s | %-12s | %-12s\n", "Data", "Accredito", "Addebito");
	    i_estratto.append(s); 
		
		return p;
	}	
}
