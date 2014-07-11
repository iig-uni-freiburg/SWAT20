package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.Collections;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;

public class PatternFactory {
	
	private IFNet mIFNet;

	public PatternFactory(IFNet net) {
		mIFNet = net;
	}

	public ArrayList<String> getApplicablePatterns(AbstractPetriNet<?,?,?,?,?,?,?> net) {
		
		ArrayList<String> supportedPatterns = new ArrayList<String>();
		if (net instanceof PTNet) {
			supportedPatterns.addAll(AtomicPattern.getPatternNames());
			supportedPatterns.addAll(CompositePattern.getPatternNames());
		} else if (net instanceof CPN) {
			supportedPatterns.addAll(AtomicPattern.getPatternNames());
			supportedPatterns.addAll(CompositePattern.getPatternNames());
		} else if (net instanceof IFNet) {
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
	
	public CompliancePattern createPattern(String patternName, ArrayList<Parameter> params) {
		
		CompliancePattern p = null;
		
		if (patternName.equals(Precedes.NAME)) {
			
			
		} else if (patternName.equals(ChainPrecedes.NAME)) {
			
			
			
		} else if (patternName.equals(LeadsTo.NAME)) {
			
			
			
		} else if (patternName.equals(ChainLeadsTo.NAME)) {
			
			
			
		} else if (patternName.equals(XLeadsTo.NAME)) {
			
			
			
		} else if (patternName.equals(Else.NAME)) {
			
			
			
		} else if (patternName.equals(Absent.NAME)) {
			
			
			
		} else if (patternName.equals(Universal.NAME)) {
			
			
			
		} else if (patternName.equals(Exists.NAME)) {
			
			
			
		} else if (patternName.equals(CoExists.NAME)) {
			
			
			
		} else if (patternName.equals(CoAbsent.NAME)) {
			
			
			
		} else if (patternName.equals(Exclusive.NAME)) {
			
			
			
		} else if (patternName.equals(Corequisite.NAME)) {
			
			
			
		} else if (patternName.equals(MutexChoice.NAME)) {
			
			
			
		} else if (patternName.equals(PerformedBy.NAME)) {
			
			
			
		} else if (patternName.equals(SegregatedFrom.NAME)) {
			
			
			
		} else if (patternName.equals(USegregatedFrom.NAME)) {
			
			
			
		} else if (patternName.equals(BoundedWith.NAME)) {
			
			
			
		} else if (patternName.equals(MSegregated.NAME)) {
			
			
			
		} else if (patternName.equals(RBoundedWith.NAME)) {
			
		} else if (patternName.equals(PerformedBy.NAME)) {
			
		} else if (patternName.equals(SegregatedFrom.NAME)) {
			
		} else if (patternName.equals(USegregatedFrom.NAME)) {
			
		} else if (patternName.equals(BoundedWith.NAME)) {
			
		} else if (patternName.equals(MSegregated.NAME)) {
			
		} else if (patternName.equals(RBoundedWith.NAME)) {
			
		} else if (patternName.equals(MissingData.NAME)) {
			
		} else if (patternName.equals(WeaklyRedData.NAME)) {
			
		} else if (patternName.equals(WeaklyLostData.NAME)) {
			
		} else if (patternName.equals(InconsistentData.NAME)) {
			
		} else if (patternName.equals(NeverDestroyed.NAME)) {
			
		} else if (patternName.equals(TwiceDestroyed.NAME)) {
			
		} else if (patternName.equals(NotDeletedOnTime.NAME)) {
			
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
	
	
}
