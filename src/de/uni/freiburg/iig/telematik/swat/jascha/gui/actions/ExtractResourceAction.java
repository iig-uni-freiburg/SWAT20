package de.uni.freiburg.iig.telematik.swat.jascha.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import de.uni.freiburg.iig.telematik.swat.jascha.HumanResourceExtractor;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceType;

public class ExtractResourceAction extends AbstractAction {
	
	JFileChooser chooser;
	private ResourceStore store;
	
	public ExtractResourceAction(ResourceStore store) {
		super("from log");
		chooser=new JFileChooser();
		this.store = store;
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			
			@Override
			public String getDescription() {
				return "log format";
			}
			
			@Override
			public boolean accept(File f) {
				String name = f.getName();
				return name.endsWith("mxml") || name.endsWith("xes");
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int returnVal = chooser.showOpenDialog((Component) e.getSource());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			HumanResourceExtractor extractor = new HumanResourceExtractor(chooser.getSelectedFile().toString());
			List<String> resources = extractor.getHumanResources();
			for (String res:resources)
				store.instantiateResource(ResourceType.HUMAN, res);
		}

	}

}
