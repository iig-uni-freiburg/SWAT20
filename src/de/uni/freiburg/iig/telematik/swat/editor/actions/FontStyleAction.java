package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JToolBar;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxCellEditor;
import com.mxgraph.util.mxConstants;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphComponent;

public class FontStyleAction extends AbstractPNEditorAction {
	
	private static final long serialVersionUID = 7450908146578160638L;
	protected boolean bold = false;
	
	public FontStyleAction(PNEditor editor, String name, Icon icon) throws ParameterException {
		super(editor, name, icon);
		if(name.equals("Bold"))
			this.bold = true;
		
	}

	public void actionPerformed(ActionEvent e) {
		
			PNGraphComponent graphComponent = getEditor().getGraphComponent();
			Component editorComponent = null;
			if (getEditor().getGraphComponent().getCellEditor() instanceof mxCellEditor) {
				editorComponent = getEditor();
				
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