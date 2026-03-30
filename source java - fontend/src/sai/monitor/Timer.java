package sai.monitor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class Timer {

	private LocalTime time;
	private LocalDate date;
	private Monitor m;
	
	private static final DateTimeFormatter time_format = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static final DateTimeFormatter date_format = DateTimeFormatter.ofPattern("YYYY-MM-dd");
	
	public Timer (Monitor m) {
		this.m = m;
		
		this.time = LocalTime.now();
		this.date = LocalDate.now();
		
		new Thread(() -> this.tick()).start();
	}
	
	public void tick () {
		while (true) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.time = LocalTime.now();
			this.date = LocalDate.now();
			m.updateTime(this.time.format(time_format), this.date.format(date_format));
		}
	}
	
	public String getTime () {
		return this.time.format(time_format);
	}
	
	public String getDate () {
		return this.date.format(date_format);
	}

	public LocalTime getRawTime () {
		return this.time;
	}
	
	public LocalDate getRawDate () {
		return this.date;
	}
}
