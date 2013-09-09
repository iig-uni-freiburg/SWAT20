package de.unifreiburg.iig.bpworkbench2.editor.gui;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.unifreiburg.iig.bpworkbench2.editor.soul.CellInfo;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Graph;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.w3c.dom.Document;

/**
 *
 */
public class GraphComponent extends mxGraphComponent {

    public GraphComponent(final Graph graph) {
        super(graph);

        setGridStyle(mxGraphComponent.GRID_STYLE_LINE);
        setGridColor(Color.decode("#dddddd"));
        setGridVisible(true);
		getConnectionHandler().setCreateTarget(true);


        mxCodec codec = new mxCodec();
        Document doc = mxUtils.loadDocument(PNMLEditor.class.getResource(
                "/default-style.xml").toString());
        codec.decode(doc.getDocumentElement(), graph.getStylesheet());
        getGraphControl().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                Object object = getCellAt(e.getX(), e.getY());
                if (object != null && e.getClickCount() == 2) {
                    mxCell cell = (mxCell) object;
                    System.out.println(cell.getValue().getClass());
                    if (!cell.isEdge() && !(cell.getValue() instanceof String)) {
                        CellInfo info = (CellInfo) cell.getValue();
                        System.out.println("blub");
                        if (info.isPlace()) {
                            String marks = JOptionPane.showInputDialog(
                                    "Input new amount of marks");
                            if (marks != null) {
                                info.setMarks(Integer.parseInt(marks));
                            }
                            graph.getDataHolder().updateData();
//                            ControlPanel.setTree();
                            refresh();
                        }
                        if (info.isTransition()) {
                            new InputDialog(info).setVisible(true);
                            graph.getDataHolder().updateData();
                            refresh();
                        }
//                        if (info.isContainer()) {
//                            String name = "     ";
//                            name += JOptionPane.showInputDialog(
//                                    "Input new name");
//                            info.setName(name);
//
//                            refresh();
//                        }
                    }
                }
                super.mouseClicked(e);
            }
        });
    }

    @Override
    public ImageIcon getFoldingIcon(mxCellState state) {
//        return super.getFoldingIcon(state);
        return null;
    }
}
