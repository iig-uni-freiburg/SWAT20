package de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.lola;

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
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class LolaTransformator {

	private PTNet net;
	private String lolaNet;

	public LolaTransformator(PTNet net) {
		this.net = net;
	}

	public LolaTransformator(PNEditorComponent neteditor) {
		this.net = (PTNet) neteditor.getNetContainer().getPetriNet();
	}

	public String getNetAsLolaFormat() {
		if (lolaNet == null) {
			StringBuilder builder = new StringBuilder(1000);
			builder.append(getPlaces());
			builder.append("\r\n");
			builder.append(getInitialMarking());
			builder.append("\r\n");
			builder.append(getTransitions());
			//	builder.append("$end");
			LolaPresenter p = new LolaPresenter(builder.toString());
			p.show();
			lolaNet = builder.toString();
		}
		return lolaNet;
	}

	/**
	 * @param args
	 * @throws ParameterException
	 * @throws ParserException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, ParserException, ParameterException {
		//_Currently:_ Instead of getName() getLabel() is used on places and transitions. This may cause problems as the user may enter illegal characters
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> loadedNet = null;
		loadedNet = new PNMLParser().parse(new File("/home/richard/richard.pnml"));
		LolaTransformator trans = new LolaTransformator((PTNet) loadedNet.getPetriNet());
		System.out.println(trans.getNetAsLolaFormat());

	}

	private StringBuilder getPlaces() {
		StringBuilder builder=new StringBuilder();
		builder.append("PLACE \r\n");
		for (PTPlace place : net.getPlaces()) {
			if (place.getCapacity() != -1) {
				//Place has bounded capacity
				builder.append("SAFE " + place.getCapacity() + ": ");
			}
			builder.append(place.getLabel() + ";");
			builder.append("\r\n");
		}
		return builder;
	}

	private StringBuilder getInitialMarking() {
		StringBuilder builder = new StringBuilder();
		builder.append("MARKING ");
		boolean first = true;
		//test every place
		for (PTPlace place : net.getPlaces()) {
			if (net.getInitialMarking().contains(place.getLabel())) {
				try {
					//test if place has a marking
					net.getInitialMarking().get(place.getLabel());
					if (!first)
						builder.append(", ");
					builder.append(place.getLabel() + ": " + net.getInitialMarking().get(place.getLabel()) + " ");
				} catch (ParameterException e) {
				}
			}
			first = false;
		}
		builder.append(";\r\n");

		return builder;
	}

	private StringBuilder getTransitions() {
		StringBuilder builder = new StringBuilder();
		for (PTTransition transition : net.getTransitions(true)) {
			builder.append("\r\nTRANSITION " + transition.getLabel() + "\r\n");
			builder.append(getIncomingRelationsAsString(transition));
			builder.append("\r\n");
			builder.append(getOutgoingRelationsAsString(transition));
			builder.append("\r\n");

		}
		builder.append("\r\n");
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
			builder.append(flow.getSource().getLabel());
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
			builder.append(flow.getTarget().getLabel());
			builder.append(" : ");
			builder.append(flow.getWeight());
		}
		builder.append(" ;");
		return builder;
	}

}
