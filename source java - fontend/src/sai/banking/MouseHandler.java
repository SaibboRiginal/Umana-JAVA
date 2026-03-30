package sai.banking;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import sai.ui.*;

public class MouseHandler {

	public static class Hover extends sHandler.MouseClick {
		private Component element;
		private Color color_backup;
		private Font font_backup;

		public Hover(Component element) {
			this.element = element;
		}

		@Override
		public void mouseClicked(MouseEvent e) {		
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			color_backup = element.getBackground();
			element.setBackground(new Color(100,100,100));
			font_backup = element.getFont();
			Font bigger =  element.getFont().deriveFont((float) (element.getFont().getSize() + 2));
			element.setFont(bigger);
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			element.setBackground(color_backup);		
			element.setFont(font_backup);		
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
		
	}
	
	public static class Click extends sHandler.MouseClick {
		@SuppressWarnings("unused")
		private Component element;

		public Click(Component element) {
			this.element = element;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			//HERE
		}

		@Override
		public void mouseEntered(MouseEvent e) {			
		}

		@Override
		public void mouseExited(MouseEvent e) {	
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
		
	}
	
}
