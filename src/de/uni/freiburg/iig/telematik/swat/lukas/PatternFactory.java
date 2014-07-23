package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;

public class PatternFactory {
	
	private AbstractPetriNet<?,?,?,?,?,?,?> mNet;

	public PatternFactory(AbstractPetriNet<?,?,?,?,?,?,?> net) {
		mNet = net;
	}

	public ArrayList<String> getApplicablePatterns() {
		
		ArrayList<String> supportedPatterns = new ArrayList<String>();
		if (mNet instanceof PTNet) {
			supportedPatterns.addAll(AtomicPattern.getPatternNames());
			supportedPatterns.addAll(CompositePattern.getPatternNames());
		} else if (mNet instanceof CPN) {
			supportedPatterns.addAll(AtomicPattern.getPatternNames());
			supportedPatterns.addAll(CompositePattern.getPatternNames());
		} else if (mNet instanceof IFNet) {
			supportedPatterns.addAll(AtomicPattern.getPatternNames());
			supportedPatterns.addAll(CompositePattern.getPatternNames());
			supportedPatterns.addAll(DataflowPattern.getPatternNames());
			supportedPatterns.addAll(ResourcePattern.getPatternNames());
		}
		
		Collections.sort(supportedPatterns);
		return supportedPatterns;
		
	}
	
	public ArrayList<Parameter> getParametersOfPattern(String patternName) {
		ParameterProvider pp = new ParameterProvider();
		return pp.getParameters(patternName);
	}
	
	
	/*
	 * 
	 * */
	
	@SuppressWarnings("unchecked")
	public CompliancePattern createPattern(String patternName, ArrayList<Parameter> params) {
		
		CompliancePattern p = null;
		
		if (patternName.equals(Precedes.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(0).getValue().get(0);
			Operand o2 = createOperand(value2);
			p = new Precedes((NetElementExpression) o1, (NetElementExpression) o2);
			
		} else if (patternName.equals(ChainPrecedes.NAME)) {
			
			
			
		} else if (patternName.equals(LeadsTo.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(0).getValue().get(0);
			Operand o2 = createOperand(value2);
			p = new LeadsTo((NetElementExpression) o1, (NetElementExpression) o2);
			
			
		} else if (patternName.equals(ChainLeadsTo.NAME)) {
			
			
			
		} else if (patternName.equals(XLeadsTo.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(0).getValue().get(0);
			Operand o2 = createOperand(value2);
			p = new XLeadsTo((NetElementExpression) o1, (NetElementExpression) o2);
			
		} else if (patternName.equals(Else.NAME)) {
			
			
			
		} else if (patternName.equals(Absent.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			p = new Absent((NetElementExpression) o1);
			
		} else if (patternName.equals(Universal.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			p = new Universal((NetElementExpression) o1);
			
		} else if (patternName.equals(Exists.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			p = new Exists((NetElementExpression) o1);
			
			
		} else if (patternName.equals(CoExists.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(0).getValue().get(0);
			Operand o2 = createOperand(value2);
			p = new CoExists((NetElementExpression) o1, (NetElementExpression) o2);
			
		} else if (patternName.equals(CoAbsent.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(0).getValue().get(0);
			Operand o2 = createOperand(value2);
			p = new CoAbsent((NetElementExpression) o1, (NetElementExpression) o2);
			
		} else if (patternName.equals(Exclusive.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(0).getValue().get(0);
			Operand o2 = createOperand(value2);
			p = new Exclusive((NetElementExpression) o1, (NetElementExpression) o2);
			
			
		} else if (patternName.equals(Corequisite.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(0).getValue().get(0);
			Operand o2 = createOperand(value2);
			p = new Corequisite((NetElementExpression) o1, (NetElementExpression) o2);
			
			
		} else if (patternName.equals(MutexChoice.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(0).getValue().get(0);
			Operand o2 = createOperand(value2);
			p = new MutexChoice((NetElementExpression) o1, (NetElementExpression) o2);
			
		} else if (patternName.equals(PerformedBy.NAME)) {
			
			
			
		} else if (patternName.equals(SegregatedFrom.NAME)) {
			
			
			
		} else if (patternName.equals(USegregatedFrom.NAME)) {
			
			
			
		} else if (patternName.equals(BoundedWith.NAME)) {
			
			
			
		} else if (patternName.equals(MSegregated.NAME)) {
			
			
			
		} else if (patternName.equals(RBoundedWith.NAME)) {
			
		} else if (patternName.equals(MissingData.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			p = new MissingData((Token) o1, (Collection<RegularIFNetTransition>) mNet.getTransitions());
			
		} else if (patternName.equals(WeaklyRedData.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			p = new WeaklyRedData((Token) o1, (Collection<RegularIFNetTransition>) mNet.getTransitions(),
					(Collection<IFNetPlace>) mNet.getDrainPlaces());
			
		} else if (patternName.equals(WeaklyLostData.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			p = new WeaklyLostData((Token) o1, (Collection<RegularIFNetTransition>) mNet.getTransitions());
			
		} else if (patternName.equals(InconsistentData.NAME)) {
			
		} else if (patternName.equals(NeverDestroyed.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			p = new NeverDestroyed((Token) o1, (Collection<RegularIFNetTransition>) mNet.getTransitions(), 
					(Collection<IFNetPlace>) mNet.getDrainPlaces());
			
		} else if (patternName.equals(TwiceDestroyed.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			p = new TwiceDestroyed((Token) o1, (Collection<RegularIFNetTransition>) mNet.getTransitions());
			
		} else if (patternName.equals(NotDeletedOnTime.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			p = new NotDeletedOnTime((Token) o1, (Collection<RegularIFNetTransition>) mNet.getTransitions());
			
		} else {
			try {
				throw new UnsupportedPattern("The provided pattern is not supported! Extend the "
						+ "implementation of the PatternFactory");
			} catch (UnsupportedPattern e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return p;
		
	}

	private Operand createOperand(ParamValue value) {
		
		OperandType ot = value.getOperandType();
		String oValue = value.getOperandName();
		Operand op = null;
		if (ot.equals(OperandType.STATEPREDICATE)) {
			
		} else if (ot.equals(OperandType.TRANSITION)) {
			op = new Transition(oValue);
		} else if (ot.equals(OperandType.ROLE)) {
			op = new Role(oValue);
		} else if (ot.equals(OperandType.TOKEN)) {
			op = new Token(oValue);
		}
		return op;
		
	}

}
