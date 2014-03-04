package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.fop.svg.PDFGraphics2D;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.mxgraph.swing.mxGraphComponent;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;



public class ExportAction extends AbstractPNEditorAction {

	public ExportAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "export", IconFactory.getIcon("pdf"));
		// TODO Auto-generated constructor stub
	}

	public void actionPerformed(ActionEvent e)
	{
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
			Dimension size = editor.getGraphComponent().getSize();
			PNGraph pnGraph = editor.getGraphComponent().getGraph();
			
			//unselect selected nodes, otherwise would be selected in pdf too
			pnGraph.setSelectionCell(new Object() {});

			// hide gird, background white
			editor.getGraphComponent().getViewport().setBackground(Color.WHITE);
			editor.getGraphComponent().setGridColor(new Color(255, 255, 255));
			editor.getGraphComponent().setGridVisible(false);

			
			try {
				Document document = new Document(new Rectangle(size.width, size.height));
				PdfWriter writer = null;
				writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
				document.open();
				PdfContentByte canvas = writer.getDirectContent();
				PdfGraphics2D g2 = new PdfGraphics2D(canvas, size.width, size.height);
				editor.getGraphComponent().paint(g2);
				g2.dispose();
				document.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}
		

			// initial grid setting
			editor.getGraphComponent().getViewport().setBackground(MXConstants.blueBG);
			editor.getGraphComponent().setGridColor(MXConstants.bluehigh);
			editor.getGraphComponent().setGridVisible(true);

		}
	}
}