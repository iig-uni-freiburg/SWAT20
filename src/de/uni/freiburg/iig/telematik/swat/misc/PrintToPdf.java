package de.uni.freiburg.iig.telematik.swat.misc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.org.apache.xml.internal.utils.RawCharacterHandler;


public class PrintToPdf {
	
	static int inch = Toolkit.getDefaultToolkit().getScreenResolution();
	static float pixelToPoint = (float) 72 / (float) inch;
	
	 public static void paintToPDF(JPanel content, File file) {
		    try {
		      content.setBounds(0, 0, (int) convertToPixels(612 - 58), (int) convertToPixels(792 - 60));

		      Document document = new Document();
		      FileOutputStream fos = new FileOutputStream(file);
		      PdfWriter writer = PdfWriter.getInstance(document, fos);

		      //document.setPageSize(new Rectangle(612, 792));
		      document.setPageSize(new com.itextpdf.text.Rectangle(612,792));
		      document.open();
		      PdfContentByte cb = writer.getDirectContent();

		      cb.saveState();
		      cb.concatCTM(1, 0, 0, 1, 0, 0);

		      DefaultFontMapper mapper = new DefaultFontMapper();
		      mapper.insertDirectory("c:/windows/fonts");

		      Graphics2D g2 = cb.createGraphics(612, 792, mapper, true, .95f);

		      AffineTransform at = new AffineTransform();
		      at.translate(convertToPixels(20), convertToPixels(20));
		      at.scale(pixelToPoint, pixelToPoint);

		      g2.transform(at);

		      g2.setColor(Color.WHITE);
		      g2.fill(content.getBounds());

		      Rectangle alloc = getVisibleEditorRect(content);
		      //ta.getUI().getRootView(ta).paint(g2, alloc);
		      content.paint(g2);

		      g2.setColor(Color.BLACK);
		      g2.draw(content.getBounds());

		      g2.dispose();
		      cb.restoreState();
		      document.close();
		      fos.flush();
		      fos.close();
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		  }
	 
	  public float convertToPoints(int pixels) {
		    return (float) (pixels * pixelToPoint);
		  }

		  public static float convertToPixels(int points) {
		    return (float) (points / pixelToPoint);
		  }
		  

		  protected static Rectangle getVisibleEditorRect(JPanel content) {
		    java.awt.Rectangle alloc = content.getBounds();
		    if ((alloc.width > 0) && (alloc.height > 0)) {
		      alloc.x = alloc.y = 0;
		      Insets insets = content.getInsets();
		      alloc.x += insets.left;
		      alloc.y += insets.top;
		      alloc.width -= insets.left + insets.right;
		      alloc.height -= insets.top + insets.bottom;
		      return alloc;
		    }
		    return null;
		  }
		  
		  

}
