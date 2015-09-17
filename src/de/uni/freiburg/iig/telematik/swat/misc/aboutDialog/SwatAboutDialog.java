package de.uni.freiburg.iig.telematik.swat.misc.aboutDialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;


public class SwatAboutDialog extends JDialog{
	
	private static final long serialVersionUID = -4406207910205498068L;
	private static String version="2.0.1";
	private static String[] devs={"Lange, Adrian","Holderer, Julius","Stocker, Thomas","Zahoransky, Richard"};
	private static String centerFormatString="<html><div style=\"text-align: center;\">%s</html>";
	
	public static void main(String args[]){
		SwatAboutDialog dialog = new SwatAboutDialog(null);
		dialog.setLocationByPlatform(true);
		dialog.setVisible(true);
	}
	
	public SwatAboutDialog(JFrame parent){
		super(parent, "About Dialog",true);
		setSize(400, 180);
		setModal(false);
		
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(getSWAT20Label());
		box.add(new JLabel("Version: "+version));
		box.add(new JLabel("developed by: "));
		Arrays.sort(devs);
		addLabelsToComponent(devs, box);
		getContentPane().add(box, "Center");
		addLicenseButton(box);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	private void addLabelsToComponent(String[] array, Box box){
		for (String s: array){
			box.add(getCenteredLabel(s));
		}
	}
	
	private JLabel getSWAT20Label(){
		JLabel swat = getCenteredLabel("SWAT 2.0");
		swat.setFont(new Font("Serif",Font.BOLD, 18));
		return swat;
	}
	
	private JLabel getCenteredLabel(String s){
		return new JLabel(String.format(centerFormatString, s),SwingConstants.CENTER);
	}
	
	private void addLicenseButton(Box box){
		JButton result = new JButton("Licenses");
		result.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new LicenceDialog().setVisible(true);
				
			}
		});
		box.add(result);
	}

}

/**
*
* @author richard
*/
class LicenceDialog extends JDialog{
   
   private static String license="SWAT is licensed under the BSD 3-Clause license.\n" +
"\n" +
"It is based on software from the Department of Telematics of the\n" +
"Institute of Computer Science and Social Studies, University of\n" +
"Freiburg, namely TOVAL (http://sourceforge.net/p/toval), JAGAL\n" +
"(http://sourceforge.net/p/jagal), SEWOL\n" +
"(https://sourceforge.net/projects/jawl/), and SEPIA\n" +
"(https://sourceforge.net/projects/sepiaframework/) and encloses the\n" +
"following libraries:\n" +
"- JGraphX (https://github.com/jgraph/jgraphx)\n" +
"- OpenXES, (http://www.xes-standard.org/openxes/)\n" +
"- Spex (http://code.deckfour.org/Spex/)\n" +
"- Google Guava (https://github.com/google/guava)\n" +
"- XStream (http://xstream.codehaus.org/)\n" +
"- Jung 2 (http://jung.sourceforge.net/)\n" +
"- XML Schema Object Model (https://xsom.java.net/)\n" +
"- XML Datatypes Library (xsdlib)\n" +
"- isorelax (http://iso-relax.sourceforge.net/)\n" +
"- hamcrest (https://github.com/hamcrest)\n" +
"- Multi Schema Validator (https://msv.java.net/)\n" +
"- relaxng-Datatype (https://sourceforge.net/projects/relaxng/)\n" +
"- iText® (https://sourceforge.net/projects/itext/)\n" +
"\n" +
"\n" +
"\n" +
"\n" +
"- JDOM (http://www.jdom.org/)\n" +
"- SCIFF (http://lia.deis.unibo.it/research/sciff/)\n" +
"- Apache Commons\n" +
"  - Exec (https://commons.apache.org/proper/commons-exec/)\n" +
"  - Math (http://commons.apache.org/proper/commons-math/)\n" +
"- jFreeChart (http://www.jfree.org/jfreechart/)\n" +
"- jFreeSVG (http://www.jfree.org/jfreesvg/)\n" +
"- PanelMatic (https://kenai.com/projects/panelmatic)\n" +
"- XML Pull (http://www.xmlpull.org/)\n" +
"\n" +
"\n" +
"Copyright (c) 2015, IIG Telematics, Uni Freiburg\n" +
"All rights reserved.\n" +
"\n" +
"Redistribution and use in source and binary forms, with or without\n" +
"modification, are permitted (subject to the limitations in the disclaimer\n" +
"below) provided that the following conditions are met:\n" +
"\n" +
"* Redistributions of source code must retain the above copyright notice,\n" +
"this\n" +
"  list of conditions and the following disclaimer.\n" +
"* Redistributions in binary form must reproduce the above copyright notice,\n" +
"  this list of conditions and the following disclaimer in the documentation\n" +
"  and/or other materials provided with the distribution.\n" +
"* Neither the name of IIG Telematics, Uni Freiburg nor the names of its\n" +
"  contributors may be used to endorse or promote products derived from\n" +
"  this software without specific prior written permission.\n" +
"\n" +
"NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED\n" +
"BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND\n" +
"CONTRIBUTORS \"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,\n" +
"BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND\n" +
"FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE\n" +
"COPYRIGHT HOLDER OR CONTRIBUTORS BELIABLE FOR ANY DIRECT, INDIRECT,\n" +
"INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT\n" +
"NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF\n" +
"USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON\n" +
"ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n" +
"(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF\n" +
"THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.";
   

   
   LicenceDialog (){
       setSize(550, 500);
       setLocationByPlatform(true);
       JTextArea text = new JTextArea(license);
       JScrollPane scrollPane = new JScrollPane(text);
       setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       getContentPane().add(scrollPane);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }
   
}
