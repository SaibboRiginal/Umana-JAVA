package sai.banking;

import java.awt.Component;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DocumentHandler implements DocumentListener {

	Atm atm;
	Component element;
	
	public DocumentHandler(Atm atm, Component element) {
		this.atm = atm;
		this.element = element;
	}
	
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		//here		
		if (element.getName().equals("prelievo_importo")) {
			atm.getElements().get("conferma_prelievo").setVisible(true);
		}
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		if (element.getName().equals("prelievo_importo")) {
			atm.getElements().get("conferma_prelievo").setVisible(false);
		}		
	}

}

