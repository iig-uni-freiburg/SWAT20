package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests;

import de.invation.code.toval.constraint.NumberConstraint;
import de.invation.code.toval.constraint.NumberOperator;
import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.TestGuardDataContainer;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;

public class IFNetTestUtils {
	
	public static IFNet createUsageConflictIFNet() {
		
		// define places
		IFNet ifnet = new IFNet();
		ifnet.addPlace("pIn");
		ifnet.getPlace("pIn").setColorCapacity("black", 1);
		ifnet.addPlace("p1");
		ifnet.getPlace("p1").setColorCapacity("black", 1);				
		ifnet.addPlace("pOut");
		ifnet.getPlace("pOut").setColorCapacity("black", 1);
		
		// initial marking				
		Multiset<String> pInMarking = new Multiset<String>();
		pInMarking.add("black");
		IFNetMarking m = new IFNetMarking();
		m.set("pIn", pInMarking);
		ifnet.setInitialMarking(m);

		// define transitions
		ifnet.addTransition("t1");
		ifnet.addTransition("t2");
		ifnet.addTransition("t3");

		// define flow relation
		IFNetFlowRelation f1 = ifnet.addFlowRelationPT("pIn", "t1");
		IFNetFlowRelation f2 = ifnet.addFlowRelationTP("t1", "p1");
		IFNetFlowRelation f3 = ifnet.addFlowRelationPT("pIn", "t2");
		IFNetFlowRelation f4 = ifnet.addFlowRelationTP("t2", "p1");
		IFNetFlowRelation f5 = ifnet.addFlowRelationPT("p1", "t3");
		IFNetFlowRelation f6 = ifnet.addFlowRelationTP("t3", "pOut");
					
		// define constraints
		f1.addConstraint("black", 1);
		f2.addConstraint("black", 1);
		f3.addConstraint("black", 1);
		f4.addConstraint("black", 1);
		f5.addConstraint("black", 1);
		f6.addConstraint("black", 1);
		
		return ifnet;
	}
	
	public static IFNet createExtendedUsageConflictIFNet() {
		
		// define places
		IFNet ifnet = new IFNet();
		ifnet.addPlace("pIn");
		ifnet.getPlace("pIn").setColorCapacity("black", 1);
		ifnet.addPlace("p1");
		ifnet.getPlace("p1").setColorCapacity("black", 1);
		ifnet.addPlace("p2");
		ifnet.getPlace("p2").setColorCapacity("black", 1);
		ifnet.addPlace("pOut");
		ifnet.getPlace("pOut").setColorCapacity("black", 1);
		
		// initial marking				
		Multiset<String> pInMarking = new Multiset<String>();
		pInMarking.add("black");
		IFNetMarking m = new IFNetMarking();
		m.set("pIn", pInMarking);
		ifnet.setInitialMarking(m);

		// define transitions
		ifnet.addTransition("t1");
		ifnet.addTransition("t2");
		ifnet.addTransition("t3");
		ifnet.addTransition("t4");

		// define flow relation
		IFNetFlowRelation f1 = ifnet.addFlowRelationPT("pIn", "t1");
		IFNetFlowRelation f2 = ifnet.addFlowRelationTP("t1", "p1");
		IFNetFlowRelation f3 = ifnet.addFlowRelationPT("pIn", "t2");
		IFNetFlowRelation f4 = ifnet.addFlowRelationTP("t2", "p1");
		IFNetFlowRelation f5 = ifnet.addFlowRelationPT("p1", "t3");
		IFNetFlowRelation f6 = ifnet.addFlowRelationTP("t3", "p2");
		IFNetFlowRelation f7 = ifnet.addFlowRelationPT("p2", "t4");
		IFNetFlowRelation f8 = ifnet.addFlowRelationTP("t4", "pOut");
					
		// define constraints
		f1.addConstraint("black", 1);
		f2.addConstraint("black", 1);
		f3.addConstraint("black", 1);
		f4.addConstraint("black", 1);
		f5.addConstraint("black", 1);
		f6.addConstraint("black", 1);
		f7.addConstraint("black", 1);
		f8.addConstraint("black", 1);
		
		return ifnet;
	}


	
	public static IFNet create2PlaceIFNet() {
		// define ifnet
		IFNet ifnet = new IFNet();
		ifnet.addPlace("p_in");
		ifnet.getPlace("p_in").setColorCapacity("black", 1);
						
		ifnet.addPlace("p_out");
		ifnet.getPlace("p_out").setColorCapacity("black", 1);
		ifnet.getPlace("p_out").setColorCapacity("red", 1);
						
		Multiset<String> pInMarking = new Multiset<String>();
		pInMarking.add("black");

		IFNetMarking m = new IFNetMarking();
		m.set("p_in", pInMarking);
		ifnet.setInitialMarking(m);

		ifnet.addTransition("t_1");
		ifnet.addTransition("t_2");

		IFNetFlowRelation f1 = ifnet.addFlowRelationPT("p_in", "t_1");
		IFNetFlowRelation f2 = ifnet.addFlowRelationTP("t_1", "p_out");
		IFNetFlowRelation f3 = ifnet.addFlowRelationPT("p_in", "t_2");
		IFNetFlowRelation f4 = ifnet.addFlowRelationTP("t_2", "p_out");
					
		f1.addConstraint("black", 1);
		f2.addConstraint("black", 1);
		f3.addConstraint("black", 2);
		f4.addConstraint("red", 1);
		
		return ifnet;
	}
	
	public static IFNet createDeadTransitionNet() {
		
		IFNet ifnet = new IFNet();

		// define places
		ifnet.addPlace("pIn");
		ifnet.getPlace("pIn").setColorCapacity("black", 1);				
		ifnet.addPlace("pOut");
		ifnet.getPlace("pOut").setColorCapacity("black", 1);
		
		// initial marking
		Multiset<String> pInMarking = new Multiset<String>();
		pInMarking.add("black");
		IFNetMarking m = new IFNetMarking();
		m.set("pIn", pInMarking);
		ifnet.setInitialMarking(m);
		
		// create transition and flow relation
		ifnet.addTransition("t1");
		ifnet.addTransition("t2");
		IFNetFlowRelation f1 = ifnet.addFlowRelationPT("pIn", "t1");
		IFNetFlowRelation f2 = ifnet.addFlowRelationTP("t1", "pOut");
		IFNetFlowRelation f3 = ifnet.addFlowRelationPT("pIn", "t2");
		IFNetFlowRelation f4 = ifnet.addFlowRelationTP("t2", "pOut");
		
		// add constraints
		f1.addConstraint("black", 1);
		f2.addConstraint("black", 1);
		f3.addConstraint("black", 2);
		f4.addConstraint("black", 1);
		
		return ifnet ;
	}
	
	public static IFNet create6PlaceIFNet() {
		// create the net
		IFNet net = new IFNet();

		// add places
		net.addPlace("pIn");
		net.getPlace("pIn").setColorCapacity("black", 1);

		net.addPlace("p0");
		net.getPlace("p0").setColorCapacity("black", 1);
		net.getPlace("p0").setColorCapacity("green", 1);

		net.addPlace("p1");
		net.getPlace("p1").setColorCapacity("black", 1);
		net.getPlace("p1").setColorCapacity("red", 1);

		net.addPlace("p2");
		net.getPlace("p2").setColorCapacity("black", 1);
		net.getPlace("p2").setColorCapacity("green", 1);

		net.addPlace("p3");
		net.getPlace("p3").setColorCapacity("black", 1);
		net.getPlace("p3").setColorCapacity("blue", 1);

		net.addPlace("pOut");
		net.getPlace("pOut").setColorCapacity("black", 1);
		
		net.addPlace("p4");
		net.getPlace("p4").setColorCapacity("black", 1);
		net.getPlace("p4").setColorCapacity("yellow", 1);

		// set marking
		Multiset<String> pInMarking = new Multiset<String>();
		pInMarking.add("black");
		IFNetMarking sm = new IFNetMarking();
		sm.set("pIn", pInMarking);
		net.setInitialMarking(sm);

		// add the transitions
		net.addTransition("tIn");
		net.addTransition("t0");
		net.addTransition("tOut");
		net.addDeclassificationTransition("td");
		net.addTransition("t1");

		// add flowrelations
		IFNetFlowRelation f1 = net.addFlowRelationPT("p0", "t0");
		IFNetFlowRelation f2 = net.addFlowRelationPT("p1", "t0");
		IFNetFlowRelation f3 = net.addFlowRelationTP("t0", "p2");
		IFNetFlowRelation f4 = net.addFlowRelationTP("t0", "p3");
		net.addFlowRelationPT("pIn", "tIn");
		IFNetFlowRelation f6 = net.addFlowRelationTP("tIn", "p0");
		IFNetFlowRelation f7 = net.addFlowRelationTP("tIn", "p1");

		IFNetFlowRelation f8 = net.addFlowRelationPT("p2", "tOut");
		IFNetFlowRelation f9 = net.addFlowRelationPT("p3", "tOut");
		net.addFlowRelationTP("tOut", "pOut");
		
		// add flowrelations
		IFNetFlowRelation f11 = net.addFlowRelationPT("p1", "td");
		IFNetFlowRelation f12 = net.addFlowRelationTP("td", "p4");
		IFNetFlowRelation f13 = net.addFlowRelationPT("p4", "t1");
		net.addFlowRelationTP("t1", "pOut");
		
		// add constraints
		f1.addConstraint("green", 1);
		f2.addConstraint("red", 1);
		f3.addConstraint("green", 1);
		f4.addConstraint("blue", 1);
		f6.addConstraint("green", 1);
		f7.addConstraint("red", 1);
		f8.addConstraint("green", 1);
		f9.addConstraint("blue", 1);
		f11.addConstraint("red", 1);
		f12.addConstraint("yellow", 1);
		f13.addConstraint("yellow", 1);
			
		return net;
	}
	
	public static IFNet create6PlaceIFnetWithAccessModes() {
		
		IFNet net = create6PlaceIFNet();
		
		RegularIFNetTransition tIn = (RegularIFNetTransition) net.getTransition("tIn");
		tIn.addAccessMode("green", AccessMode.CREATE);
		tIn.addAccessMode("red", AccessMode.CREATE);
		tIn.addAccessMode("red", AccessMode.WRITE);
		
		
		RegularIFNetTransition t0 = (RegularIFNetTransition) net.getTransition("t0");
		t0.addAccessMode("green", AccessMode.WRITE);
		t0.addAccessMode("blue", AccessMode.CREATE);
		t0.addAccessMode("blue", AccessMode.WRITE);
		t0.addAccessMode("red", AccessMode.READ);
		t0.addAccessMode("red", AccessMode.DELETE);
		
		RegularIFNetTransition t1 = (RegularIFNetTransition) net.getTransition("tOut");
		t1.addAccessMode("yellow", AccessMode.READ);
		t1.addAccessMode("yellow", AccessMode.DELETE);
		
		RegularIFNetTransition tOut = (RegularIFNetTransition) net.getTransition("tOut");
		tOut.addAccessMode("blue", AccessMode.READ);
		tOut.addAccessMode("blue", AccessMode.DELETE);
		tOut.addAccessMode("green", AccessMode.READ);
		tOut.addAccessMode("green", AccessMode.WRITE);
		
		//Labeling labels = new Labeling(net, new ArrayList<String>(Arrays.asList("Karl", "Peter", "Hans")));
		Labeling labels = new Labeling();
		labels.setRequireContext(false);
		
		labels.setSubjectClearance("Peter", SecurityLevel.HIGH);
		labels.setSubjectClearance("Hans", SecurityLevel.LOW);
		labels.setSubjectClearance("Karl", SecurityLevel.LOW);
		
		labels.setAttributeClassification("red", SecurityLevel.HIGH);
		labels.setAttributeClassification("green", SecurityLevel.LOW);
		labels.setAttributeClassification("blue", SecurityLevel.LOW);
		labels.setAttributeClassification("yellow", SecurityLevel.LOW);
		
		labels.setActivityClassification("tIn", SecurityLevel.LOW);
		labels.setActivityClassification("td", SecurityLevel.HIGH);
		labels.setActivityClassification("t1", SecurityLevel.LOW);
		labels.setActivityClassification("t0", SecurityLevel.LOW);
		labels.setActivityClassification("tOut", SecurityLevel.HIGH);
		
		net.setAnalysisContext(new AnalysisContext(labels));
		
		
		return net;
	}
	
	public static IFNet createParallelExecIFNet() {
		
		IFNet net = new IFNet();
		
		// add places
		net.addPlace("pIn");
		net.getPlace("pIn").setColorCapacity("black", 1);

		net.addPlace("p1");
		net.getPlace("p1").setColorCapacity("black", 2);
		net.getPlace("p1").setColorCapacity("red", 2);
		net.getPlace("p1").setColorCapacity("green", 1);

		net.addPlace("p2");
		net.getPlace("p2").setColorCapacity("black", 1);
		net.getPlace("p2").setColorCapacity("red", 1);

		net.addPlace("p3");
		net.getPlace("p3").setColorCapacity("black", 1);
		net.getPlace("p3").setColorCapacity("yellow", 1);
		
		net.addPlace("p4");
		net.getPlace("p4").setColorCapacity("black", 1);
		net.getPlace("p4").setColorCapacity("green", 1);
		
		net.addPlace("p5");
		net.getPlace("p5").setColorCapacity("black", 1);
		net.getPlace("p5").setColorCapacity("green", 1);
		
		net.addPlace("pOut");
		net.getPlace("pOut").setColorCapacity("black", 1);
		
		// set marking
		Multiset<String> pInMarking = new Multiset<String>();
		pInMarking.add("black");
		IFNetMarking sm = new IFNetMarking();
		sm.set("pIn", pInMarking);
		net.setInitialMarking(sm);

		// add the transitions
		net.addTransition("tIn");
		net.addTransition("t1");
		net.addTransition("t2");
		net.addTransition("t3");
		net.addTransition("t4");
		net.addTransition("tOut");

		// add flowrelations
		IFNetFlowRelation f1 = net.addFlowRelationPT("pIn", "tIn");
		IFNetFlowRelation f2 = net.addFlowRelationPT("p1", "t1");
		IFNetFlowRelation f3 = net.addFlowRelationPT("p1", "t3");
		IFNetFlowRelation f4 = net.addFlowRelationPT("p2", "t2");
		IFNetFlowRelation f5 = net.addFlowRelationPT("p3", "tOut");
		IFNetFlowRelation f6 = net.addFlowRelationPT("p4", "t4");
		IFNetFlowRelation f7 = net.addFlowRelationPT("p5", "tOut");
		
		IFNetFlowRelation f8 = net.addFlowRelationTP("tIn", "p1");
		IFNetFlowRelation f9 = net.addFlowRelationTP("t1", "p2");
		IFNetFlowRelation f10 = net.addFlowRelationTP("t3", "p4");
		IFNetFlowRelation f11 = net.addFlowRelationTP("t2", "p3");
		IFNetFlowRelation f12 = net.addFlowRelationTP("t4", "p5");
		IFNetFlowRelation f13 = net.addFlowRelationTP("tOut", "pOut");
				
		// add constraints
		f1.addConstraint("black", 1);
		
		f2.addConstraint("black", 1);
		f2.addConstraint("red", 1);
		
		f3.addConstraint("black", 1);
		f3.addConstraint("green", 1);
		f3.addConstraint("red", 1);
		
		f4.addConstraint("black", 1);
		f4.addConstraint("red", 1);
		
		f5.addConstraint("black", 1);
		f5.addConstraint("yellow", 1);
		
		f6.addConstraint("black", 1);
		f6.addConstraint("green", 1);
		
		f7.addConstraint("black", 1);
		f7.addConstraint("green", 1);
		
		f8.addConstraint("black", 2);
		f8.addConstraint("green", 1);
		f8.addConstraint("red", 2);
		
		f9.addConstraint("black", 1);
		f9.addConstraint("red", 1);
		
		f10.addConstraint("black", 1);
		f10.addConstraint("green", 1);
		
		f11.addConstraint("black", 1);
		f11.addConstraint("yellow", 1);
		
		f12.addConstraint("black", 1);
		f12.addConstraint("green", 1);
		
		f13.addConstraint("black", 1);
		
		// set access modes
		RegularIFNetTransition tIn = (RegularIFNetTransition) net.getTransition("tIn");
		tIn.addAccessMode("green", AccessMode.CREATE);
		tIn.addAccessMode("green", AccessMode.WRITE);
		tIn.addAccessMode("red", AccessMode.CREATE);
		
		RegularIFNetTransition t1 = (RegularIFNetTransition) net.getTransition("t1");
		t1.addAccessMode("red", AccessMode.WRITE);
		
		RegularIFNetTransition t2 = (RegularIFNetTransition) net.getTransition("t1");
		t2.addAccessMode("red", AccessMode.READ);
		t2.addAccessMode("red", AccessMode.DELETE);
		t2.addAccessMode("red", AccessMode.CREATE);
		
		RegularIFNetTransition t3 = (RegularIFNetTransition) net.getTransition("t3");
		t3.addAccessMode("green", AccessMode.WRITE);
		t3.addAccessMode("red", AccessMode.READ);
		
		RegularIFNetTransition t4 = (RegularIFNetTransition) net.getTransition("t4");
		t4.addAccessMode("green", AccessMode.DELETE);
		t4.addAccessMode("green", AccessMode.CREATE);
		
		RegularIFNetTransition tOut = (RegularIFNetTransition) net.getTransition("tOut");
		tOut.addAccessMode("green", AccessMode.DELETE);
		
		//Labeling labels = new Labeling(net, new ArrayList<String>(Arrays.asList("Karl", "Peter", "Hans")));
		Labeling labels = new Labeling();
		labels.setRequireContext(false);
		
		labels.setSubjectClearance("Peter", SecurityLevel.HIGH);
		labels.setSubjectClearance("Hans", SecurityLevel.LOW);
		labels.setSubjectClearance("Karl", SecurityLevel.LOW);
		
		labels.setAttributeClassification("red", SecurityLevel.LOW);
		labels.setAttributeClassification("green", SecurityLevel.LOW);
		labels.setAttributeClassification("yellow", SecurityLevel.LOW);
		
		labels.setActivityClassification("tIn", SecurityLevel.LOW);
		labels.setActivityClassification("t1", SecurityLevel.LOW);
		labels.setActivityClassification("t2", SecurityLevel.LOW);
		labels.setActivityClassification("t3", SecurityLevel.LOW);
		labels.setActivityClassification("t4", SecurityLevel.LOW);
		labels.setActivityClassification("tOut", SecurityLevel.LOW);
		
		net.setAnalysisContext(new AnalysisContext(labels));
		
		return net;
	}
	
	public static IFNet createIFNetWithGuards() {
        
		IFNet net = new IFNet();
		
		// add places
		net.addPlace("pIn");
		net.getPlace("pIn").setColorCapacity("black", 1);

		net.addPlace("p1");
		net.getPlace("p1").setColorCapacity("black", 1);
		net.getPlace("p1").setColorCapacity("red", 1);

		net.addPlace("p2");
		net.getPlace("p2").setColorCapacity("black", 1);
		net.getPlace("p2").setColorCapacity("red", 2);

		net.addPlace("p3");
		net.getPlace("p3").setColorCapacity("black", 2);
		net.getPlace("p3").setColorCapacity("red", 2);
		
		net.addPlace("pOut");
		net.getPlace("pOut").setColorCapacity("black", 1);
		
		// set marking
		Multiset<String> pInMarking = new Multiset<String>();
		pInMarking.add("black");
		IFNetMarking sm = new IFNetMarking();
		sm.set("pIn", pInMarking);
		net.setInitialMarking(sm);

		// add the transitions
		net.addTransition("tIn");
		net.addTransition("t1");
		net.addTransition("t2");
		net.addTransition("t3");
		net.addTransition("t4");
		net.addTransition("tOut");

		// add flowrelations
		IFNetFlowRelation f1 = net.addFlowRelationPT("pIn", "tIn");
		IFNetFlowRelation f2 = net.addFlowRelationPT("p1", "t1");
		IFNetFlowRelation f3 = net.addFlowRelationPT("p1", "t2");
		IFNetFlowRelation f4 = net.addFlowRelationPT("p2", "t3");
		IFNetFlowRelation f5 = net.addFlowRelationPT("p2", "t4");
		IFNetFlowRelation f6 = net.addFlowRelationPT("p3", "tOut");
		
		IFNetFlowRelation f8 = net.addFlowRelationTP("tIn", "p1");
		IFNetFlowRelation f9 = net.addFlowRelationTP("t1", "p2");
		IFNetFlowRelation f10 = net.addFlowRelationTP("t2", "p2");
		IFNetFlowRelation f11 = net.addFlowRelationTP("t3", "p3");
		IFNetFlowRelation f12 = net.addFlowRelationTP("t4", "p3");
		IFNetFlowRelation f13 = net.addFlowRelationTP("tOut", "pOut");
				
		// add constraints
		f1.addConstraint("black", 1);
		
		f2.addConstraint("black", 1);
		f2.addConstraint("red", 1);
		
		f3.addConstraint("black", 1);
		f3.addConstraint("red", 1);
		
		f4.addConstraint("black", 1);
		f4.addConstraint("red", 1);
		
		f5.addConstraint("black", 1);
		f5.addConstraint("red", 1);
		
		f6.addConstraint("black", 2);
		f6.addConstraint("red", 2);
		
		f8.addConstraint("black", 1);
		f8.addConstraint("red", 1);
		
		f9.addConstraint("black", 1);
		f9.addConstraint("red", 2);
		
		f10.addConstraint("black", 1);
		f10.addConstraint("red", 1);
		
		f11.addConstraint("black", 1);
		f11.addConstraint("red", 1);
		
		f12.addConstraint("black", 1);
		f12.addConstraint("red", 1);
		
		f13.addConstraint("black", 1);
		
		RegularIFNetTransition tIn = (RegularIFNetTransition) net.getTransition("tIn");
		tIn.addAccessMode("red", AccessMode.CREATE);
		
		RegularIFNetTransition t1 = (RegularIFNetTransition) net.getTransition("t1");
		t1.addAccessMode("red", AccessMode.WRITE);
		
		RegularIFNetTransition tOut = (RegularIFNetTransition) net.getTransition("tOut");
		tOut.addAccessMode("red", AccessMode.DELETE);
		
		AbstractRegularIFNetTransition<IFNetFlowRelation> t3 = 
				(AbstractRegularIFNetTransition<IFNetFlowRelation>) net.getTransition("t3");
		TestGuardDataContainer tgdc1 = new TestGuardDataContainer(net.getTokenColors());
		t3.setGuardDataContainer(tgdc1);
		t3.addGuard(new NumberConstraint("red", NumberOperator.LARGER_EQUAL, 2));
		
		
		AbstractRegularIFNetTransition<IFNetFlowRelation> t4 = 
				(AbstractRegularIFNetTransition<IFNetFlowRelation>) net.getTransition("t4");
		TestGuardDataContainer tgdc2 = new TestGuardDataContainer(net.getTokenColors());
		t4.setGuardDataContainer(tgdc2);
		t4.addGuard(new NumberConstraint("red", NumberOperator.SMALLER, 2));
		
		return net;

	}

}
