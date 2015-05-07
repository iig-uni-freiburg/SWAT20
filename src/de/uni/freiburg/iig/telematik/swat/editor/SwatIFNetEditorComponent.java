package de.uni.freiburg.iig.telematik.swat.editor;

import java.io.IOException;

import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CWNProperties;
import de.uni.freiburg.iig.telematik.swat.editor.graph.SwatIFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.menu.IFNetToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.AbstractPropertyCheckAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.IFNetEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.IFNetGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.AbstractToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.CPNToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.TransitionPopupMenu;

public class SwatIFNetEditorComponent extends IFNetEditorComponent {

	private static final long serialVersionUID = 3873685137931170597L;

	public SwatIFNetEditorComponent() {
		super();
	}

	public SwatIFNetEditorComponent(GraphicalIFNet netContainer, boolean askForLayout) {
		super(netContainer, askForLayout);
	}

	public SwatIFNetEditorComponent(GraphicalIFNet netContainer, LayoutOption layoutOption) {
		super(netContainer, layoutOption);
	}

	public SwatIFNetEditorComponent(GraphicalIFNet netContainer) {
		super(netContainer);
	}
	
	@Override
	protected PNGraphComponent createGraphComponent() {
		return new IFNetGraphComponent(new SwatIFNetGraph(getNetContainer(), getPNProperties()));

	}

	@Override
	protected AbstractToolBar createNetSpecificToolbar() throws EditorToolbarException {
		return new IFNetToolBar(this, JToolBar.HORIZONTAL);
	}
	
	@Override
	public JPopupMenu getTransitionPopupMenu() {
		try {
			return new SwatTransitionPopupMenu(this);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	@Override
	protected void setPropertyChecksUnknown() {
		//Need to add Property Checks to IFNetEditorComponent
//		((IFNetToolBar)getEditorToolbar()).getCheckValidityAction().setFillColor(AbstractPropertyCheckAction.PropertyUnknownColor );
////		((CPNToolBar)getEditorToolbar()).getCheckSoundnessAction().setFillColor(AbstractPropertyCheckAction.PropertyUnknownColor);
//		((IFNetToolBar)getEditorToolbar()).getCheckBoundednessAction().setFillColor(AbstractPropertyCheckAction.PropertyUnknownColor);
//		((IFNetToolBar)getEditorToolbar()).getCheckCWNStructureAction().setFillColor(AbstractPropertyCheckAction.PropertyUnknownColor);
//		((IFNetToolBar)getEditorToolbar()).getCheckCWNSoundnessAction().setFillColor(AbstractPropertyCheckAction.PropertyUnknownColor);
//		getPropertyCheckView().updateCWNProperties(new CWNProperties());
	}
	
}
