package de.unifreiburg.iig.bpworkbench2.editor.soul;

import java.util.ArrayList;

import com.mxgraph.model.mxCell;

import de.unifreiburg.iig.bpworkbench2.editor.gui.ControlPanel;

public class DataHolder {

	private int[][] matrixDi = new int[0][0], matrixDq = new int[0][0];
	private int[] marking;
	private double[] probabilities, variances, lambdas;
	private boolean[] immediates;
	private boolean[] blocked;
	private String[] columnNames = null, rowNames = null;
	private Graph graph = null;
	private ControlPanel dataWatcher;
	private ArrayList<mxCell> transitions = new ArrayList<mxCell>();

	public DataHolder(Graph graph) {
		this.graph = graph;
	}

	public void clean() {
		matrixDi = new int[0][0];
		matrixDq = new int[0][0];
		marking = null;
		immediates = null;
		columnNames = null;
		rowNames = null;
		probabilities = null;
		variances = null;
		lambdas = null;
		blocked = null;
	}

	public String getRowName(int i) {
		return rowNames[i];
	}

	public int[] getMarking() {
		return marking;
	}

	public int[][] getMatrixDi() {
		return matrixDi;
	}

	public int[][] getMatrixDq() {
		return matrixDq;
	}

	public boolean[] getBlocked() {
		return blocked;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public boolean[] getImmediates() {
		return immediates;
	}

	public String[] getRowNames() {
		return rowNames;
	}

	public int getLastPlaceName() {
		if (columnNames == null) {
			return 0;
		}
		return Integer.parseInt(columnNames[columnNames.length - 1].substring(1));
	}

	public double[] getProbabilities() {
		return probabilities;
	}

	public int getLastTransitionName() {
		if (rowNames == null) {
			return 0;
		}
		return Integer.parseInt(rowNames[rowNames.length - 1].substring(1));
	}

	private void addPlaceName(String name) {
		if (columnNames != null) {
			String[] newColumns = new String[columnNames.length + 1];
			System.arraycopy(columnNames, 0, newColumns, 0, columnNames.length);
			newColumns[newColumns.length - 1] = name;
			columnNames = newColumns;
		} else {
			columnNames = new String[1];
			columnNames[0] = name;
		}
	}

	private void addTransitionName(String name) {
		if (rowNames != null) {
			String[] newTransitions = new String[rowNames.length + 1];
			System.arraycopy(rowNames, 0, newTransitions, 0, rowNames.length);
			newTransitions[newTransitions.length - 1] = name;
			rowNames = newTransitions;
		} else {
			rowNames = new String[1];
			rowNames[0] = name;
		}
	}

	public void updateData() {
		clean();

		// gathering data about places and transitions
		int columns = 0, rows = 0;
		ArrayList<mxCell> places = new ArrayList<mxCell>();
		transitions = new ArrayList<mxCell>();
		Object[] childCells = graph.getChildCells(graph.getDefaultParent());
		for (Object object : childCells) {
			if (object instanceof mxCell) {
				mxCell cell = (mxCell) object;
				if (cell.getValue() instanceof CellInfo) {
					CellInfo info = (CellInfo) cell.getValue();
					if (info.isPlace()) {
						columns++;
						places.add(cell);
						addPlaceName(info.getName());
					}
					if (info.isTransition()) {
						rows++;
						transitions.add(cell);
						addTransitionName(info.getName());
					}
				}
			}
		}
		matrixDi = new int[rows][columns];
		matrixDq = new int[rows][columns];
		marking = new int[columns];
		for (int i = 0; i < places.size(); i++) {
			marking[i] = ((CellInfo) (places.get(i)).getValue()).getMarks();
		}
		immediates = new boolean[rows];
		blocked = new boolean[rows];
		probabilities = new double[rows];
		variances = new double[rows];
		lambdas = new double[rows];
		for (int i = 0; i < transitions.size(); i++) {
			CellInfo info = (CellInfo) transitions.get(i).getValue();
			immediates[i] = info.isImmediate();
			probabilities[i] = info.getProbability();
			variances[i] = info.getVariance();
			blocked[i] = info.isBlocked();
			lambdas[i] = info.getLambda();
		}

		// gathering data about edges
		for (Object object : childCells) {
			if (object instanceof mxCell) {
				mxCell cell = (mxCell) object;
				if (cell.isEdge()) {
					mxCell edgeSource = (mxCell) ((mxCell) cell).getSource();
					mxCell edgeTarget = (mxCell) ((mxCell) cell).getTarget();
					if (edgeSource != null && edgeTarget != null) {
						int a = 0, b = 0;
						boolean found = false;
						for (int i = 0; i < places.size(); i++) {
							if (places.get(i).equals(edgeSource)) {
								b = i;
								found = true;
								break;
							}
						}
						for (int i = 0; i < transitions.size(); i++) {
							if (transitions.get(i).equals(edgeTarget)) {
								a = i;
								found = true;
								break;
							}
						}
						if (found) {
							matrixDi[a][b]++;
						}
						found = false;
						for (int i = 0; i < places.size(); i++) {
							if (places.get(i).equals(edgeTarget)) {
								b = i;
								found = true;
								break;
							}
						}
						for (int i = 0; i < transitions.size(); i++) {
							if (transitions.get(i).equals(edgeSource)) {
								a = i;
								found = true;
								break;
							}
						}
						if (found) {
							matrixDq[a][b]++;
						}
					}
				}
			}
		}
		// dataWatcher.updateMatrix();
	}

	public void setMarking(int[] marking) {
		this.marking = marking;
		Object[] objects = graph.getChildCells(graph.getDefaultParent());
		int i = 0;
		for (Object object : objects) {
			if (object instanceof mxCell) {
				mxCell cell = (mxCell) object;
				if (cell.getValue() instanceof CellInfo) {
					CellInfo info = (CellInfo) cell.getValue();
					if (info.isPlace()) {
						String marks = String.valueOf(marking[i]);
						i++;
						info.setMarks(Integer.parseInt(marks));
					}
				}
			}
		}
		updateData();
		graph.refresh();
	}

	public void setDataWatcher(ControlPanel dataWatcher) {
		this.dataWatcher = dataWatcher;
	}

	public void setBlocked(int index, boolean state) {
		this.blocked[index] = state;
		CellInfo info = (CellInfo) transitions.get(index).getValue();
		info.setBlocked(state);
	}

	public double[] getVariances() {
		return variances;
	}

	public double[] getLambdas() {
		return lambdas;
	}
}
