package sai.monitor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.MatteBorder;

import sai.banking.JCustom;
import sai.banking.MouseHandler;
import sai.ui.sHandler;
import sai.ui.sUI;

public class Monitor {

	private sUI mainframe;
	private Container c;
	private Timer timer;
	private BackEnd back;
	private Map<String, Component> elements = new HashMap<String, Component>();
	
	public Monitor (String titolo) {
		this.mainframe = new sUI("ATM", 500, 500);
		mainframe.setFont(new Font("Hack", Font.PLAIN, 15));
		//mainframe.setAlwaysOnTop(true);
		mainframe.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/photo/t.png")));
		mainframe.setResizable(false);
		mainframe.setTitle("Monitor");
		this.c = mainframe.getContainer();
		this.c.setBackground(Color.BLACK);
		this.c.setForeground(Color.WHITE);
		mainframe.getContainer().setLayout(null);
		
		this.back = new BackEnd(this);
		
		this.timer = new Timer(this);
		
		this.back.setTimer(this.timer);
		
		// starting ...
		this.c.add(screen());
		mainframe.getContentPane().setLayout(null);
		mainframe.refresh();
		
		new Thread(() -> this.back.start()).start();
	}
	
	public void addLine (String line, boolean newline) {
		((JTextArea) elements.get("console")).append(""
				+ (!newline ? "<" + timer.getDate() +" " + timer.getTime() + "> " : "")
				+ line + (newline ? "\n" : "")
				);
	}
	
	public void updateTime (String date, String time) {
		((JLabel) elements.get("time")).setText(date + " " + time);
	}
	
	public JPanel screen () {
		JPanel p = new JPanel();
		p.setBounds(0, 0, 500, 500);
		p.setBorder(new MatteBorder(0, 0, 2, 0, new Color(255, 51, 0)));
		p.setBackground(new Color(40,40,40));
		p.setLayout(null);
		
		JLabel clock = new JLabel("1999-01-01 20:20:20");
		clock.setForeground(Color.WHITE);
		clock.setFont(new Font("Hack", Font.PLAIN, 14));
		clock.setSize(200, 15);
		clock.setLocation(20, 5);
		clock.setName("time"); //!!!
	    elements.put(clock.getName(), clock);
	    p.add(clock);
		
		JTextArea console = new JTextArea(16, 5);
		console.setForeground(new Color(0, 204, 0));
		console.setBackground(Color.DARK_GRAY);
		console.setFont(new Font("monospaced", Font.PLAIN, 14));
		console.setLineWrap(true);
		console.setEditable(false); // set textArea non-editable
		console.setName("console"); //!!!
	    elements.put(console.getName(), console);
	    
	    JScrollPane display = new JScrollPane(console);
	    display.setSize(485, 400);
	    display.setLocation(5, 25);
	    display.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    display.setName("terminal");
	    p.add(display);
	    
	    JCustom.RoundedJButton clear = new JCustom.RoundedJButton("clear", 10, new Color(255, 51, 0));
	    clear.setFont(new Font("Hack", Font.PLAIN, 14));
	    clear.setForeground(Color.LIGHT_GRAY);
	    clear.setSize(90, 30);
	    clear.setLocation(390, 435);
	    clear.setBackground(new Color(60,60,60));
		clear.addActionListener(new sHandler.Action() {
			@Override
			public void actionPerformed(ActionEvent e) {
				console.selectAll();
				console.replaceSelection("");
			}
		});
		clear.setName("clear");
		clear.addMouseListener(new MouseHandler.Hover(clear));
		p.add(clear);
				
		return p;
	}
}
