package de.unifreiburg.iig.bpworkbench2.editor.graph;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.unifreiburg.iig.bpworkbench2.editor.PNEditor;

/**
 *
 */
public class GraphComponent extends mxGraphComponent {

    public GraphComponent(Graph graph) {
        super(graph);

        setGridStyle(mxGraphComponent.GRID_STYLE_LINE);
        setGridColor(MXConstants.bluehigh);
		setBackground(MXConstants.blueBG);

        setGridVisible(true);

    
		getConnectionHandler().setCreateTarget(true);
        mxCodec codec = new mxCodec();
        Document doc = mxUtils.loadDocument(PNEditor.class.getResource(
                "/default-style.xml").toString());
        codec.decode(doc.getDocumentElement(), graph.getStylesheet());
        getGraphControl().addMouseListener(new MouseAdapter() {
        	@Override
            public void mouseReleased(MouseEvent e) {
                Object object = getCellAt(e.getX(), e.getY());
                
                if (object != null && e.getClickCount() == 2) {
                    mxCell cell = (mxCell) object;
                    Object value = ((mxCell)cell).getValue();
            		
            		if(cell.getParent().getValue() instanceof AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?>){
            			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?>)  cell.getParent().getValue();
            			PTPlace place = (PTPlace) n.getPetriNet().getPlace(((mxCell)cell).getId());

                        if (cell.getStyle().contentEquals(MXConstants.PNPlaceShape)) {
                            String marks = JOptionPane.showInputDialog(
                                    "Input new amount of marks");
                            if (marks != null) {
                            	PTMarking initialMarking;
                            	if(n.getPetriNet().getInitialMarking() == null){
    							initialMarking = new PTMarking();
    							}
                            	else initialMarking = (PTMarking) n.getPetriNet().getInitialMarking();
//    							int ressources = ;
    							try {
//									n.getPetriNet().getPlace(cell.getId())
//											.setCapacity(new Integer(marks));
									initialMarking.set(cell.getId(), new Integer(marks));

	    							if (n.getPetriNet() instanceof PTNet) {
	    								PTNet ptNet = (PTNet) n.getPetriNet();
	    								ptNet.setInitialMarking(initialMarking);
	    							}
								} catch (ParameterException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}
    						

                            }
//                            graph.getDataHolder().updateData();
//                            ControlPanel.setTree();
                            refresh();
                        
//                        if (info.isTransition()) {
//                            new InputDialog(info).setVisible(true);
////                            graph.getDataHolder().updateData();
//                            refresh();
//                        }
//                        if (info.isContainer()) {
//                            String name = "     ";
//                            name += JOptionPane.showInputDialog(
//                                    "Input new name");
//                            info.setName(name);
//
//                            refresh();
//                        }
                    }}
                }
                super.mouseClicked(e);
            }
        });
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                Object object = getCellAt(e.getX(), e.getY());
//                if (object != null && e.getClickCount() == 2) {
//                    mxCell cell = (mxCell) object;
//                    Object value = ((mxCell)cell).getValue();
//            		
//            		
//            		if(value instanceof AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?>){
//            			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?>)  value;
//            			CPNPlace place = (CPNPlace) n.getPetriNet().getPlace(((mxCell)cell).getId());
//            			
//            	try {
//					n.getPetriNet().getTransition(((mxCell)cell).getId()).fire();
////					n.getPetriNet().getTransition(((mxCell)cell).getId()).checkState();
//				} catch (PNException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//            			try {
//            				System.out.println(place.getState().multiplicity("black")+ "##############1");
//							int multiplicity = place.getState().decMultiplicity(new String("black"));
////							((AbstractCPNPlace<CPNFlowRelation>) place).getState().setMultiplicity("black", 1);
//							System.out.println(place.getState().multiplicity("black")+ "##############2");
//							CPNMarking m= (CPNMarking) n.getPetriNet().getMarking();
//							m.get(place.getName()).clear();
//							m.clear();
//							System.out.println(place.getState().multiplicity("black")+ "##############2");
//System.out.println(m);
////							System.out.println(m.getClass());
//						} catch (ParameterException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
						}
//            		}
//                    if (!cell.isEdge() && !(cell.getValue() instanceof String)) {
//                        CellInfo info = (CellInfo) cell.getValue();
//                        System.out.println("blub");
//                        if (info.isPlace()) {
//                            String marks = JOptionPane.showInputDialog(
//                                    "Input new amount of marks");
//                            if (marks != null) {
//                                info.setMarks(Integer.parseInt(marks));
//                            }
//                            graph.getDataHolder().updateData();
////                            ControlPanel.setTree();
//                            refresh();
//                        }
//                        if (info.isTransition()) {
//                            new InputDialog(info).setVisible(true);
//                            graph.getDataHolder().updateData();
//                            refresh();
//                        }
//                        if (info.isContainer()) {
//                            String name = "     ";
//                            name += JOptionPane.showInputDialog(
//                                    "Input new name");
//                            info.setName(name);
//
//                            refresh();
//                        }
////                    }
////                }
//                super.mouseClicked(e);
//            }
//        });
//    }

    @Override
    public ImageIcon getFoldingIcon(mxCellState state) {
//        return super.getFoldingIcon(state);
        return null;
    }
    @Override
	protected ConnectionHandler createConnectionHandler()
	{
		return new ConnectionHandler(this);
	}
}
