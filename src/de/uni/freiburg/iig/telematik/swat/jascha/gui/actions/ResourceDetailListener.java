package de.uni.freiburg.iig.telematik.swat.jascha.gui.actions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.JOptionPane;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.Resource;

public class ResourceDetailListener implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
		if( e.getClickCount()==2 && e.getSource() instanceof JList){
			JList<IResource> source = (JList<IResource>) e.getSource();
			Resource res = (Resource) source.getSelectedValue();
			JOptionPane.showMessageDialog(source, res.getDetailString());
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
