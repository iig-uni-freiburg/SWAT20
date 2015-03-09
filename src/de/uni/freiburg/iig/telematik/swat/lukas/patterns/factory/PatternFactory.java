package de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Clause;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.NetElementExpression;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Operand;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.PlacePredicate;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Relation;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Role;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.StateExpression;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Token;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Transition;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.UnsupportedRelation;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Absent;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.AtomicPattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.BoundedWith;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ChainLeadsTo;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ChainPrecedes;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CoAbsent;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CoExists;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CompositePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Corequisite;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.DataflowPattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Else;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Exclusive;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Exists;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.InconsistentData;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.InformationFlowPattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.LeadsTo;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.LeadsToChain;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.MSegregated;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.MissingData;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.MutexChoice;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.NeverDestroyed;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.NotDeletedOnTime;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.PBNI;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.PerformedBy;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Precedes;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.PrecedesChain;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.RBoundedWith;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ReadUp;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ResourcePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.SegregatedFrom;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.TwiceDestroyed;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.USegregatedFrom;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Universal;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.WeaklyLostData;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.WeaklyRedData;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.WriteDown;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.XLeadsTo;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;

public class PatternFactory {
	
	private AbstractPetriNet<?,?,?,?,?,?,?> mNet;
	private AbstractPlace<?,?> mOutputPlace;
	
	private HashMap<String, String> mSupportedPatterns;

	public PatternFactory(ViewComponent component) {
		
		mSupportedPatterns = new HashMap<String, String>();
		
		if (component instanceof PNEditorComponent) {
			mNet = ((PNEditorComponent) component).netContainer.getPetriNet();
			mOutputPlace = mNet.getDrainPlaces().iterator().next();
			
			if (mNet instanceof PTNet || mNet instanceof CPN) {
			
				mSupportedPatterns.putAll(AtomicPattern.getPatternDescription());
				mSupportedPatterns.putAll(CompositePattern.getPatternDescription());
				
			} 
			
			if (mNet instanceof IFNet) {
				
				mSupportedPatterns.putAll(AtomicPattern.getPatternDescription());
				mSupportedPatterns.putAll(CompositePattern.getPatternDescription());
				mSupportedPatterns.putAll(DataflowPattern.getPatternDescription());
				mSupportedPatterns.putAll(InformationFlowPattern.getPatternDescription());
				
			}
		} else if (component instanceof LogFileViewer) {
			
			mSupportedPatterns.putAll(ResourcePattern.getPatternDescription());
		}
	}

	public ArrayList<String> getApplicablePatterns() {
		ArrayList<String> supportedPatternList = new ArrayList<String>(mSupportedPatterns.keySet());
		Collections.sort(supportedPatternList);
		return supportedPatternList;
	}
	
	public String getDescOfPattern(String name) {
		return mSupportedPatterns.get(name);
	}
	
	
	public ArrayList<Parameter> getParametersOfPattern(String patternName) {
		ParameterProvider pp = new ParameterProvider();
		return pp.getParameters(patternName);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<CompliancePattern> createPattern(String patternName, ArrayList<Parameter> params) {
	 
		ArrayList<CompliancePattern> patterns = new ArrayList<CompliancePattern>(); 
		IFFlowPatternCreator ifflowPatternCreator = new IFFlowPatternCreator((IFNet) mNet);
		
		if (patternName.equals(Precedes.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(1).getValue().get(0);
			Operand o2 = createOperand(value2);
			if (o1 instanceof Transition && o2 instanceof Transition) {
				patterns.add(new Precedes((Transition) o1, (Transition) o2));
			} else {
				patterns.add(new Precedes((StateExpression) o1, (StateExpression) o2));
			}
			
		} else if (patternName.equals(ChainPrecedes.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(1).getValue().get(0);
			Operand o2 = createOperand(value2);
			ParamValue value3 = params.get(2).getValue().get(0);
			Operand o3 = createOperand(value3);
			patterns.add(new ChainPrecedes((NetElementExpression) o1,
					(NetElementExpression) o2, (NetElementExpression) o3));
			
		} else if (patternName.equals(PrecedesChain.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(1).getValue().get(0);
			Operand o2 = createOperand(value2);
			ParamValue value3 = params.get(2).getValue().get(0);
			Operand o3 = createOperand(value3);
			patterns.add(new PrecedesChain((NetElementExpression) o1, (NetElementExpression) o2,
					(NetElementExpression) o3));
			
		} else if (patternName.equals(LeadsTo.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(1).getValue().get(0);
			Operand o2 = createOperand(value2);
			patterns.add(new LeadsTo((NetElementExpression) o1, (NetElementExpression) o2));
			
			
		} else if (patternName.equals(ChainLeadsTo.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(1).getValue().get(0);
			Operand o2 = createOperand(value2);
			ParamValue value3 = params.get(2).getValue().get(0);
			Operand o3 = createOperand(value3);
			patterns.add(new ChainLeadsTo((NetElementExpression) o1, (NetElementExpression) o2,
					(NetElementExpression) o3));
			
		} else if (patternName.equals(LeadsToChain.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(1).getValue().get(0);
			Operand o2 = createOperand(value2);
			ParamValue value3 = params.get(2).getValue().get(0);
			Operand o3 = createOperand(value3);
			patterns.add(new LeadsToChain((NetElementExpression) o1, 
					(NetElementExpression) o2, (NetElementExpression) o3));
			
		} else if (patternName.equals(XLeadsTo.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(1).getValue().get(0);
			Operand o2 = createOperand(value2);
			patterns.add(new XLeadsTo((NetElementExpression) o1, (NetElementExpression) o2));
			
		} else if (patternName.equals(Else.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(1).getValue().get(0);
			Operand o2 = createOperand(value2);
			ParamValue value3 = params.get(3).getValue().get(0);
			Operand o3 = createOperand(value3);
			patterns.add(new Else((NetElementExpression) o1, (NetElementExpression) o2,
					(NetElementExpression) o3));
			
		} else if (patternName.equals(Absent.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			if (o1 instanceof Transition) {
				patterns.add(new Absent((Transition) o1));
			} else {
				patterns.add(new Absent((StateExpression) o1));
			}
			
		} else if (patternName.equals(Universal.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			if (o1 instanceof Transition) {
				patterns.add(new Universal((Transition) o1, 
						mNet.getSourcePlaces().iterator().next()));
			} else {
				patterns.add(new Universal((StateExpression) o1, 
						mNet.getSourcePlaces().iterator().next()));
			}
			
		} else if (patternName.equals(Exists.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			if (o1 instanceof Transition) {
				patterns.add(new Exists((Transition) o1, mNet.getDrainPlaces().iterator().next()));
			} else {
				patterns.add(new Exists((StateExpression) o1));
			}
			
			
		} else if (patternName.equals(CoExists.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(1).getValue().get(0);
			Operand o2 = createOperand(value2);
			if (o1 instanceof Transition & o2 instanceof Transition) {
				patterns.add(new CoExists((Transition) o1, (Transition) o2, mOutputPlace));
			} else {
				patterns.add(new CoExists((StateExpression) o1, (StateExpression) o2));
			}
			
		} else if (patternName.equals(CoAbsent.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(1).getValue().get(0);
			Operand o2 = createOperand(value2);
			if (o1 instanceof Transition & o2 instanceof Transition) {
				patterns.add(new CoAbsent((Transition) o1, (Transition) o2, mOutputPlace));
			} else {
				patterns.add(new CoAbsent((StateExpression) o1, (StateExpression) o2));
			}
			
		} else if (patternName.equals(Exclusive.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(1).getValue().get(0);
			Operand o2 = createOperand(value2);
			if (o1 instanceof Transition & o2 instanceof Transition) {
				patterns.add(new Exclusive((Transition) o1, (Transition) o2, mOutputPlace));
			} else {
				patterns.add(new Exclusive((StateExpression) o1, (StateExpression) o2));
			}
			
			
		} else if (patternName.equals(Corequisite.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(1).getValue().get(0);
			Operand o2 = createOperand(value2);
			if (o1 instanceof Transition & o2 instanceof Transition) {
				patterns.add(new Corequisite((Transition) o1, (Transition) o2, mOutputPlace));
			} else {
				patterns.add(new Corequisite((StateExpression) o1, (StateExpression) o2));
			}
			
			
		} else if (patternName.equals(MutexChoice.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			ParamValue value2 = params.get(1).getValue().get(0);
			Operand o2 = createOperand(value2);
			if (o1 instanceof Transition & o2 instanceof Transition) {
				patterns.add(new MutexChoice((Transition) o1, (Transition) o2, mOutputPlace));
			} else {
				patterns.add(new MutexChoice((StateExpression) o1, (StateExpression) o2));
			}
			
		} else if (patternName.equals(PerformedBy.NAME)) {
			
			if (mNet instanceof IFNet) {
				
				ParamValue value1 = params.get(0).getValue().get(0);
				Operand o1 = createOperand(value1);
				ParamValue value2 = params.get(1).getValue().get(0);
				Operand o2 = createOperand(value2);
				if (o1 instanceof Transition & o2 instanceof Role) {
					patterns.add(new PerformedBy((Transition) o1, (Role) o2));
				} 
			}
			
			
		} else if (patternName.equals(SegregatedFrom.NAME)) {
			
			
			
		} else if (patternName.equals(USegregatedFrom.NAME)) {
			
			
			
		} else if (patternName.equals(BoundedWith.NAME)) {
			
			
			
		} else if (patternName.equals(MSegregated.NAME)) {
			
			
			
		} else if (patternName.equals(RBoundedWith.NAME)) {
			
		} else if (patternName.equals(MissingData.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			patterns.add(new MissingData((Token) o1, 
					(Collection<RegularIFNetTransition>) mNet.getTransitions()));
			
		} else if (patternName.equals(WeaklyRedData.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			patterns.add(new WeaklyRedData((Token) o1, (Collection<RegularIFNetTransition>) mNet.getTransitions(),
					(Collection<IFNetPlace>) mNet.getDrainPlaces()));
			
		} else if (patternName.equals(WeaklyLostData.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			patterns.add(new WeaklyLostData((Token) o1, 
					(Collection<RegularIFNetTransition>) mNet.getTransitions()));
			
		} else if (patternName.equals(InconsistentData.NAME)) {
			
		} else if (patternName.equals(NeverDestroyed.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			patterns.add(new NeverDestroyed((Token) o1, 
					(Collection<RegularIFNetTransition>) mNet.getTransitions(), 
					(Collection<IFNetPlace>) mNet.getDrainPlaces()));
			
		} else if (patternName.equals(TwiceDestroyed.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			patterns.add(new TwiceDestroyed((Token) o1, 
					(Collection<RegularIFNetTransition>) mNet.getTransitions()));
			
		} else if (patternName.equals(NotDeletedOnTime.NAME)) {
			
			ParamValue value1 = params.get(0).getValue().get(0);
			Operand o1 = createOperand(value1);
			patterns.add(new NotDeletedOnTime((Token) o1, 
					(Collection<RegularIFNetTransition>) mNet.getTransitions()));
			
		} else if (patternName.equals(ReadUp.NAME) || patternName.equals(WriteDown.NAME) || 
				patternName.equals(PBNI.NAME)) {
			
			patterns.addAll(ifflowPatternCreator.createPatterns(patternName));
			
		} else {
			try {
				throw new UnsupportedPattern("The provided pattern is not supported! Extend the "
						+ "implementation of the PatternFactory");
			} catch (UnsupportedPattern e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return patterns;
		
	}
	

	private Operand createOperand(ParamValue value) {
		
		OperandType ot = value.getOperandType();
		String oValue = value.getOperandName();
		Operand op = null;
		
		if (ot.equals(OperandType.STATEPREDICATE)) {
			op = createStatePredicate(oValue);
		} else if (ot.equals(OperandType.TRANSITION)) {
			op = new Transition(oValue);
		} else if (ot.equals(OperandType.ROLE)) {
			op = new Role(oValue);
		} else if (ot.equals(OperandType.TOKEN)) {
			op = new Token(oValue);
		} 
		return op;
		
	}

	private Operand createStatePredicate(String oValue) {
		
		String[] atomicPropositions = oValue.split("&");
		ArrayList<PlacePredicate> atomicPropObjects = new ArrayList<PlacePredicate>();
		
		for (int i = 0; i < atomicPropositions.length; i++) {
			String atomicProp = atomicPropositions[i];
			try {
				Relation rel = getRelation(atomicProp);
				String[] ops = getOperands(atomicProp);
				atomicPropObjects.add(
						new PlacePredicate(ops[0], rel, Integer.parseInt(ops[1])));
			} catch (UnsupportedRelation e) {
				e.printStackTrace();
			}
		}
		
		if (atomicPropObjects.size() == 1) {
			
			return atomicPropObjects.get(0);
			
		} else {
			
			StateExpression sp = atomicPropObjects.get(0);
			for (int i = 1; i < atomicPropObjects.size(); i++) {
				sp = new Clause(sp, atomicPropObjects.get(i));
			}
			return sp;
			
		}
		
		
	}

	private String[] getOperands(String atomicProp) {
		//atomicProp = atomicProp.substring(1, atomicProp.length() - 1);
		String[] ops = atomicProp.split("=|!=|<|>|<=|>=");
		ops[0] = ops[0].replaceAll("\\s+","");
		ops[1] = ops[1].replaceAll("\\s+","");
		return ops;
	}

	private Relation getRelation(String prop) throws UnsupportedRelation {
		
		if (prop.contains("<")) {
			return Relation.SMALLER;
		} else if (prop.contains(">")) {
			return Relation.GREATER;
		} else if (prop.contains("=")) {
			return Relation.EQUALS;
		} else if (prop.contains("<=")) {
			return Relation.SMALLER_EQUAL;
		} else if (prop.contains(">=")) {
			return Relation.GREATER_EQUAL;
		} else if (prop.contains("!=")) {
			return Relation.NOT_EQUAL;
		} else {
			throw new UnsupportedRelation("This relation isn't supported!");
		}
		
	}
	
}
