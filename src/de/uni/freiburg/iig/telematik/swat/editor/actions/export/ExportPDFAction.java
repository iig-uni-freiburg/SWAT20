package de.uni.freiburg.iig.telematik.swat.editor.actions.export;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfWriter;
import com.mxgraph.util.mxRectangle;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;



public class ExportPDFAction extends AbstractPNEditorAction {

	public ExportPDFAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "export", IconFactory.getIcon("pdf"));
		// TODO Auto-generated constructor stub
	}



	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		JFileChooser fc = new JFileChooser(getEditor().getFileReference());
		fc.addChoosableFileFilter(new FileFilter() {
			public String getDescription() {
				return "PDF Documents (*.pdf)";
			}
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return f.getName().toLowerCase().endsWith(".pdf");
				}
			}
		});
		fc.setDialogTitle("Save PDF");
		int returnVal = fc.showDialog(getEditor().getGraphComponent(), "save PDF");
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filename = fc.getSelectedFile().getAbsolutePath();
			if (!filename.toLowerCase().endsWith(".pdf"))
				filename += ".pdf";

			PNEditor editor = getEditor();
			PNGraph pnGraph = editor.getGraphComponent().getGraph();

			
			
				JFrame f = new JFrame();
				PNGraphComponent forPrint = new PNGraphComponent(pnGraph) {
				};
				mxRectangle size = forPrint.getGraph().getGraphBounds();
				double space = 4;
				float x = (float) (size.getRectangle().getWidth() + size.getRectangle().getX() + space);
				float y = (float) (size.getRectangle().getHeight() + size.getRectangle().getY() + space);
				Document document = new Document(new Rectangle(x, y));
				PdfWriter writer = null;
				writer = PdfWriter.getInstance(document, new FileOutputStream(filename));

				// set crop of pdf doc = ll=lowerleft; ur=upper right
				float llx = (float) size.getX();
				float lly = 0;
				float urx = x;
				float ury = (float) ((float) size.getRectangle().getHeight() + space*4);
				com.itextpdf.text.Rectangle crop = new com.itextpdf.text.Rectangle(llx, lly, urx, ury);
				writer.setCropBoxSize(crop);

				document.open();

				PdfContentByte canvas = writer.getDirectContent();

				// make pdf-background transparent
				PdfGState gState = new PdfGState();
				gState.setFillOpacity(0.0f);
				canvas.setGState(gState);
				
				forPrint.setGridVisible(false);

				PdfGraphics2D g2 = new PdfGraphics2D(canvas, x, y);

				f.getContentPane().add(forPrint);
				f.pack();
				forPrint.paint(g2);
				g2.dispose();

				document.close();	
		}
		
	}
}