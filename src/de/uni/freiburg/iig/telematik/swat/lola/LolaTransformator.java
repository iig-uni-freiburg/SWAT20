package de.uni.freiburg.iig.telematik.swat.lola;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;
import de.uni.freiburg.iig.telematik.swat.editor.PTNetEditor;

public class LolaTransformator {

	private PTNet net;

	public LolaTransformator(PTNet net) {
		this.net = net;
	}

	public LolaTransformator(PTNetEditor neteditor) {
		this.net = neteditor.getNetContainer().getPetriNet();
	}

	public String getNetAsLolaFormat() {
		StringBuilder builder = new StringBuilder(1000);
		builder.append(getPlaces());
		builder.append("\n");
		builder.append(getInitialMarking());
		builder.append("\n");
		builder.append(getTransitions());
		return builder.toString();
	}

	/**
	 * @param args
	 * @throws ParameterException
	 * @throws ParserException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, ParserException, ParameterException {
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> loadedNet = null;
		loadedNet = new PNMLParser().parse(new File("/home/richard/richard.pnml"));
		LolaTransformator trans = new LolaTransformator((PTNet) loadedNet.getPetriNet());
		System.out.println(trans.getNetAsLolaFormat());

	}

	private StringBuilder getPlaces() {
		StringBuilder builder=new StringBuilder();
		builder.append("PLACE \n");
		for (PTPlace place : net.getPlaces()) {
			if (place.getCapacity() != -1) {
				//Place has bounded capacity
				builder.append("SAFE " + place.getCapacity() + ": ");
			}
			builder.append(place.getName() + ";");
			builder.append("\n");
		}
		return builder;
	}

	private StringBuilder getInitialMarking() {
		StringBuilder builder = new StringBuilder();
		builder.append("MARKING ");
		boolean first = true;
		//test every place
		for (PTPlace place : net.getPlaces()) {
			if (!first)
				builder.append(",");
			if (net.getInitialMarking().contains(place.getName())) {
				try {
					builder.append(place.getName() + ": " + net.getInitialMarking().get(place.getName()) + " ");
				} catch (ParameterException e) {
				}
			}
		}
		builder.append(";\n");

		return builder;
	}

	private StringBuilder getTransitions() {
		StringBuilder builder = new StringBuilder();
		//builder.append("MARKING ");
		for (PTTransition transition : net.getTransitions(true)) {
			builder.append("TRANSITION "+transition.getName()+"\n");
			builder.append(getIncomingRelationsAsString(transition));
			builder.append("\n");
			builder.append(getOutgoingRelationsAsString(transition));
			builder.append("\n");

		}
		builder.append("\n");
		return builder;
	}

	private StringBuilder getIncomingRelationsAsString(PTTransition transition) {
		StringBuilder builder = new StringBuilder();
		builder.append("CONSUME ");
		boolean first = true;
		for (PTFlowRelation flow : transition.getIncomingRelations()) {
			if (!first)
				builder.append(" , ");
			first = false;
			//From which place?
			builder.append(flow.getSource().getName());
			builder.append(" : ");
			builder.append(flow.getWeight());
		}
		builder.append(" ;");
		return builder;
	}

	private StringBuilder getOutgoingRelationsAsString(PTTransition transition) {
		StringBuilder builder = new StringBuilder();
		builder.append("PRODUCE ");
		boolean first = true;
		for (PTFlowRelation flow : transition.getOutgoingRelations()) {
			if (!first)
				builder.append(" , ");
			first = false;
			//From which place?
			builder.append(flow.getTarget().getName());
			builder.append(" : ");
			builder.append(flow.getWeight());
		}
		builder.append(" ;");
		return builder;
	}

}