package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxCellEditor;
import com.mxgraph.util.mxConstants;

public class FontStyleAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7450908146578160638L;
	/**
	 * 
	 */
	protected boolean bold;

	/**
	 * 
	 */
	public FontStyleAction(boolean bold) {
		this.bold = bold;
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof mxGraphComponent) {
			mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
			Component editorComponent = null;

			if (graphComponent.getCellEditor() instanceof mxCellEditor) {
				editorComponent = ((mxCellEditor) graphComponent.getCellEditor()).getEditor();
			}

			if (editorComponent instanceof JEditorPane) {
				JEditorPane editorPane = (JEditorPane) editorComponent;
				int start = editorPane.getSelectionStart();
				int ende = editorPane.getSelectionEnd();
				String text = editorPane.getSelectedText();

				if (text == null) {
					text = "";
				}

				try {
					HTMLEditorKit editorKit = new HTMLEditorKit();
					HTMLDocument document = (HTMLDocument) editorPane.getDocument();
					document.remove(start, (ende - start));
					editorKit.insertHTML(document, start, ((bold) ? "<b>" : "<i>") + text + ((bold) ? "</b>" : "</i>"), 0, 0, (bold) ? HTML.Tag.B : HTML.Tag.I);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				editorPane.requestFocus();
				editorPane.select(start, ende);
			} else {
				mxIGraphModel model = graphComponent.getGraph().getModel();
				model.beginUpdate();
				try {
					graphComponent.stopEditing(false);
					graphComponent.getGraph().toggleCellStyleFlags(mxConstants.STYLE_FONTSTYLE, (bold) ? mxConstants.FONT_BOLD : mxConstants.FONT_ITALIC);
				} finally {
					model.endUpdate();
				}
			}
		}
	}
}