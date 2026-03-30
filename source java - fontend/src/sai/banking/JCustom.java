package sai.banking;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

public class JCustom {
	
	public static Image getScaledImage(URL srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(new ImageIcon(srcImg).getImage(), 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
	
	public static class RoundedJButton extends JButton implements Border {
		private static final long serialVersionUID = 776360349869650241L;
		private int radius;
	    private Shape shape;
	    private Color b;

	    public RoundedJButton(String text, int radius, Color border) {
	    	super(text);
	        this.radius = radius;
	        this.b = border;
	        setOpaque(false);
	    }
	    @Override
		protected void paintComponent(Graphics g) {
	        g.setColor(getBackground());
	        g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
	        super.paintComponent(g);
	    }
	    @Override
		protected void paintBorder(Graphics g) {
	    	g.setColor(b);
	    	g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
	    }
	    @Override
		public boolean contains(int x, int y) {
	        if (shape == null || !shape.getBounds().equals(getBounds())) {
	            shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, radius, radius);
	        }
	        return shape.contains(x, y);
	    }
	    
		@Override
		public Insets getBorderInsets(Component c) {	return null;	}
		@Override
		public boolean isBorderOpaque() {	return false;	}
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {		}
	}
	
	public static class RoundJTextField extends JTextField implements Border {
		private static final long serialVersionUID = 7872669292559518216L;
		private int radius;
	    private Shape shape;
	    private Color b;
	    
	    public RoundJTextField(int size, int radius, Color border) {
	    	super(size);
	        setOpaque(false);
	        this.radius = radius;
	        this.b = border;
	    }
	    /*
	    public RoundFormattedJTextField(NumberFormatter/MaskFormatter format, int radius, Color border) {
	    	super(format);
	        setOpaque(false);
	        this.radius = radius;
	        this.b = border;
	    }*/
	    @Override
		protected void paintComponent(Graphics g) {
	         g.setColor(getBackground());
	         g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
	         super.paintComponent(g);
	    }
	    @Override
		protected void paintBorder(Graphics g) {
	         g.setColor(b);
	         g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
	    }
	    @Override
		public boolean contains(int x, int y) {
	         if (shape == null || !shape.getBounds().equals(getBounds())) {
	             shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, radius, radius);
	         }
	         return shape.contains(x, y);
	    }
	    
		@Override
		public Insets getBorderInsets(Component c) {	return null;	}
		@Override
		public boolean isBorderOpaque() {	return false;	}
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {		}
	}
	
	public static class RoundFormattedJTextField extends JFormattedTextField implements Border {
		private static final long serialVersionUID = 698949904794032883L;
		private int radius;
	    private Shape shape;
	    private Color b;
	    
	    public RoundFormattedJTextField(DecimalFormat format, int radius, Color border) {
	    	super(format);
	        setOpaque(false);
	        this.radius = radius;
	        this.b = border;
	    }
	    public RoundFormattedJTextField(MaskFormatter format, int radius, Color border) {
	    	super(format);
	        setOpaque(false);
	        this.radius = radius;
	        this.b = border;
	    }
	    public RoundFormattedJTextField(NumberFormatter format, int radius, Color border) {
	    	super(format);
	        setOpaque(false);
	        this.radius = radius;
	        this.b = border;
	    }
	    @Override
		protected void paintComponent(Graphics g) {
	         g.setColor(getBackground());
	         g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
	         super.paintComponent(g);
	    }
	    @Override
		protected void paintBorder(Graphics g) {
	         g.setColor(b);
	         g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
	    }
	    @Override
		public boolean contains(int x, int y) {
	         if (shape == null || !shape.getBounds().equals(getBounds())) {
	             shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, radius, radius);
	         }
	         return shape.contains(x, y);
	    }
	    
		@Override
		public Insets getBorderInsets(Component c) {	return null;	}
		@Override
		public boolean isBorderOpaque() {	return false;	}
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {		}
	}
	
	public static class JFormattedDateTextField extends JFormattedTextField { // set raggio e colore
		private static final long serialVersionUID = -1218696546379812141L;
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		private int radius;
	    private Shape shape;
	    private Color b;
		  
		public JFormattedDateTextField(int radius, Color border) {
			super();
	        setOpaque(false);
	        this.radius = radius;
	        this.b = border;
			MaskFormatter maskFormatter = null;
			try {
				maskFormatter = new MaskFormatter("####-##-##");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		  
			maskFormatter.setPlaceholderCharacter(' ');
			setFormatterFactory(new DefaultFormatterFactory(maskFormatter));
			this.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					if (getFocusLostBehavior() == JFormattedTextField.PERSIST)
						setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
	            	}
		   
	            public void focusLost(FocusEvent e) {
	               try {
	                  Date date = (Date) format.parseObject(getText());
	                  setValue(format.format(date));
	               } catch (ParseException pe) {
	                  setFocusLostBehavior(JFormattedTextField.PERSIST);
	                  setText("");
	                  setValue(null);
	               }
            	}
			});
		}
		
		protected void paintComponent(Graphics g) {
	         g.setColor(getBackground());
	         g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
	         super.paintComponent(g);
	    }
	    protected void paintBorder(Graphics g) {
	         g.setColor(b);
	         g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
	    }
	    public boolean contains(int x, int y) {
	         if (shape == null || !shape.getBounds().equals(getBounds())) {
	             shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, radius, radius);
	         }
	         return shape.contains(x, y);
	    }
	}

}
