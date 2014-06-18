package de.uni.freiburg.iig.telematik.swat.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

public class PlusButtonDropdownTest {
	private static JButton plusButton;

	public static void main(String[] args) throws ParameterException, PropertyException, IOException {

		plusButton = new JButton(IconFactory.getIcon("maximize"));
		final String[] itemNames = { "item1", "item2", "item3" };

		plusButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				JPopupMenu popup = new JPopupMenu();

				for (String itemName : itemNames) {

					JMenuItem item = new JMenuItem(itemName);
					item.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							System.out.println("Create New Row");
						}
					});
					popup.add(item);
				}
				popup.show(plusButton, plusButton.getWidth() * 4 / 5, plusButton.getHeight() * 4 / 5);
			}
		});

		JPanel panel = new JPanel();
		panel.add(plusButton);
		new DisplayFrame(panel, true);
	}
}