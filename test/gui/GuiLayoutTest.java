/**
 * 
 */
package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author richy
 * 
 */
@SuppressWarnings("serial")
public class GuiLayoutTest extends JFrame {
	JPanel panel1 = new JPanel(true);
	JPanel properties = new JPanel();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Gui test = new Gui();
		// test.setVisible(true);
		externGui test = new externGui();
		for (JButton button : test.buttons) {
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Action performed: "
							+ e.getActionCommand() + " bei " + e.getWhen());
				}
			});
		}

		test.buttons.get(1).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Button hat zwei Handler!");

			}
		});
		test.addSecondTopLine();
	}

	public GuiLayoutTest() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		panel1.setLayout(new BorderLayout());
		panel1.add(new JButton("Bla"), BorderLayout.NORTH);
		this.getContentPane().add(panel1);
		this.setSize(400, 900);
	}

}

class externGui extends JFrame {
	JPanel panel = new JPanel(true);
	public ArrayList<JButton> buttons = new ArrayList<JButton>();

	public externGui() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		panel.setLayout(new BorderLayout());
		buttons.add(new JButton("Button oben"));
		buttons.add(new JButton("Button mitte"));
		buttons.add(new JButton("Button links"));
		buttons.add(new JButton("Button rechts"));
		buttons.add(new JButton("Button unten"));

		panel.add(buttons.get(0), BorderLayout.NORTH);
		panel.add(buttons.get(1), BorderLayout.CENTER);
		panel.add(buttons.get(2), BorderLayout.WEST);
		panel.add(buttons.get(3), BorderLayout.EAST);
		panel.add(buttons.get(4), BorderLayout.SOUTH);

		getContentPane().add(panel);

		setSize(300, 600);
		setVisible(true);
	}

	public void addSecondTopLine() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		// ehamaligen, oberen Button übernehmen
		topPanel.add(buttons.get(0));
		// zweiten Button mit gleichem Handler wie button(0)
		JButton zweiterButton = new JButton("zweiter, oberer Button");
		transferActionListener(buttons.get(0), zweiterButton);
		topPanel.add(zweiterButton);
		panel.add(topPanel, BorderLayout.NORTH);

	}

	private void transferActionListener(JButton source, JButton target) {
		for (ActionListener l : source.getActionListeners()) {
			target.addActionListener(l);
		}
	}
}
