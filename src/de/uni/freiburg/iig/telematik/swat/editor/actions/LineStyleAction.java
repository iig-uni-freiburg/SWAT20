package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Map;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

@SuppressWarnings("serial")
public class LineStyleAction extends AbstractPNEditorAction {
	private Image dot;
	private Image dash;
	private Image solid;
	boolean isDot = false;
	int iterator = 1; 

	public LineStyleAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Line", IconFactory.getIcon("solid"));
		solid = IconFactory.getIcon("solid").getImage();
		dash = IconFactory.getIcon("dash").getImage();
		dot = IconFactory.getIcon("dot").getImage();
	}

	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();

		Map<String, Object> style = graph.getCellStyle(graph.getSelectionCell());
		double width = mxUtils.getDouble(style, mxConstants.STYLE_STROKEWIDTH);

		iterator = iterator%3;
		if(iterator == 0){
			if (graph.isLabelSelected()) {
				graph.setCellStyles(MXConstants.LABEL_LINE_STYLE, "solid");
			} else{
			graph.setCellStyles(MXConstants.LINE_STYLE, "solid");}
			super.getIcon().setImage(this.solid);	
		}
		if(iterator == 1){
			if (graph.isLabelSelected()) {
				graph.setCellStyles(MXConstants.LABEL_LINE_STYLE, "dash");
			} else{
			graph.setCellStyles(MXConstants.LINE_STYLE, "dash");}
			super.getIcon().setImage(this.dash);	
		}
		if(iterator == 2){
			if (graph.isLabelSelected()) {
				graph.setCellStyles(MXConstants.LABEL_LINE_STYLE, "dot");
			} else{
			graph.setCellStyles(MXConstants.LINE_STYLE, "dot");}
			super.getIcon().setImage(this.dot);	
		}
			
			
//		if(iterator == 1){
//			if (graph.isLabelSelected()) {
//				graph.setCellStyles("labeldashed", "1");
//				graph.setCellStyles("labeldashedpattern", width*4 + " " + width*2);
//			} else {
//				graph.setCellStyles(mxConstants.STYLE_DASHED, "1");	
//				graph.setCellStyles(mxConstants.STYLE_DASH_PATTERN, width*4 + " " + width*2);
//			}
//			super.getIcon().setImage(this.dash);	
//			}
//
//			
//		if(iterator == 2){
//			if (graph.isLabelSelected()) {
//				graph.setCellStyles("labeldashed", "1");
//				graph.setCellStyles("labeldashedpattern", width +" "+ width);
//			} else {
//				graph.setCellStyles(mxConstants.STYLE_DASHED, "1");	
//				graph.setCellStyles(mxConstants.STYLE_DASH_PATTERN, width +" "+ width);
//			}
//			super.getIcon().setImage(this.dot);	
//			}
//		if(iterator == 0){
//			if (graph.isLabelSelected()) {
//				graph.setCellStyles("labeldashed", "0");
//			} else {
//				graph.setCellStyles(mxConstants.STYLE_DASHED, "0");	
//			}
//			super.getIcon().setImage(this.line);	
//		}
//			
//			
//		if(iterator == 1){
//			if (graph.isLabelSelected()) {
//				graph.setCellStyles("labeldashed", "1");
//				graph.setCellStyles("labeldashedpattern", width*4 + " " + width*2);
//			} else {
//				graph.setCellStyles(mxConstants.STYLE_DASHED, "1");	
//				graph.setCellStyles(mxConstants.STYLE_DASH_PATTERN, width*4 + " " + width*2);
//			}
//			super.getIcon().setImage(this.dash);	
//			}
//
//			
//		if(iterator == 2){
//			if (graph.isLabelSelected()) {
//				graph.setCellStyles("labeldashed", "1");
//				graph.setCellStyles("labeldashedpattern", width +" "+ width);
//			} else {
//				graph.setCellStyles(mxConstants.STYLE_DASHED, "1");	
//				graph.setCellStyles(mxConstants.STYLE_DASH_PATTERN, width +" "+ width);
//			}
//			super.getIcon().setImage(this.dot);	
//			}
		iterator++;
		
		}

}