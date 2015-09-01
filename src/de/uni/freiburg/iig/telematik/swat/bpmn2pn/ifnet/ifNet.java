package de.uni.freiburg.iig.telematik.swat.bpmn2pn.ifnet;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.BpmnElement;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.ServiceTask;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.Task;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.UserTask;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.bpmn.Bpmn;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.bpmn.Pair;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.ExclusiveGateway;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.ParallelGateway;

public class ifNet {

	private HashMap<String, String> compatibility = new HashMap<String, String>();

	private HashMap<String, String> prePlaces = new HashMap<String, String>();

	private IFNet ifnet = new IFNet();

	public IFNet getNet() {
		return this.ifnet;
	}

	public void helperFunctions() {
	}

	public void createTransitionWithPreAndPostPlaces(Task t) {
		this.ifnet.addTransition("t_" + t.getName());
		this.ifnet.addPlace("t_" + t.getName() + "pre");
		this.ifnet.addPlace("t_" + t.getName() + "post");
		IFNetFlowRelation f1 = ifnet.addFlowRelationPT("t_" + t.getName()
				+ "pre", "t_" + t.getName());
		IFNetFlowRelation f2 = ifnet.addFlowRelationTP("t_" + t.getName(), "t_"
				+ t.getName() + "post");
		f1.addConstraint("black", 1);
		f2.addConstraint("black", 1);
		if (t instanceof UserTask) {
		} else if (t instanceof ServiceTask) {
		} else {
		}
	}
	public void createTransitionWithPreAndPostPlacesIntelligent2(Task t,
			BpmnElement lastBpmnE) {
		this.ifnet.addTransition("t_" + t.getName());
		this.ifnet.addPlace("t_" + t.getName() + "post");//
		IFNetFlowRelation f2 = ifnet.addFlowRelationTP(
				"t_" + t.getName(), "t_" + t.getName() + "post");//
		f2.addConstraint("black", 1);
		if(lastBpmnE!=null)
			if(lastBpmnE instanceof Task) 
				this.ifnet.addFlowRelationPT("t_"+lastBpmnE.getName()+"post", "t_" + t.getName());	
	}
	public String[] getPreviousElementsIdentifiers(Bpmn bpmn, BpmnElement t) {
		String[] series = {};
		LinkedList<String> ll = new LinkedList<String>();
		BpmnElement mmm = bpmn.findElementByIdTest(t.getName());
		HashSet<String> nu2 = t.getInBound();
		for(String h : nu2) {		
			String prev = (String) bpmn.getEdge(h).getFirst();
			ll.add(prev);
		}
		String[] array = ll.toArray(new String[ll.size()]);
		return array;	
	}

	public String[] getNextElementsIdentifiers(Bpmn bpmn, BpmnElement t) {
		String[] series = {};
		LinkedList<String> ll = new LinkedList<String>();
		BpmnElement mmm = bpmn.findElementByIdTest(t.getName());
		HashSet<String> nu2 = t.getOutBound();
		for(String h : nu2) {
			String next = (String) bpmn.getEdge(h).getSecond();		
			ll.add(next);		
		}
		String[] array = ll.toArray(new String[ll.size()]);
		return array;	
	}
	public void createTransitionWithPreAndPostPlacesIntelligent3(ExclusiveGateway t,
			BpmnElement lastBpmnE, Bpmn bpmn) {
		String previous = null;
		this.ifnet.addTransition("t_" + t.getName());
		for (String uu : getPreviousElementsIdentifiers(bpmn, t)) {			
		}
		HashSet<String> nu2 = t.getOutBound();
		for(String h : nu2) {
			this.ifnet.addTransition("t_" +h+"edge");
		}
		for (String uu : getPreviousElementsIdentifiers(bpmn, t)) {		
			HashMap h = bpmn.getElementsHM();
			BpmnElement j = (BpmnElement) h.get(uu);

			if(j instanceof ParallelGateway) {
				Pair gghhgh = bpmn.getEdge(j.getName());
			}
			BpmnElement u = bpmn.findElementById(uu);
			if(u instanceof BpmnElement) {
				for(String h2 : nu2) {
					if(ifnet.containsPlace("t_" + uu + "post")) {
						IFNetFlowRelation f2 = ifnet.addFlowRelationPT(
								"t_" + uu + "post", "t_" +h2+"edge");
						f2.addConstraint("black", 1);
					}
				}		
			} 	
		}
	}


	public void createTransitionWithPreAndPostPlacesIntelligent(Task t,
			BpmnElement lastBpmnE) {

		if (lastBpmnE == null) {
			this.ifnet.addTransition("t_" + t.getName());
			this.ifnet.addPlace("t_" + t.getName() + "pre");
			if (prePlaces.get(t.getName()) == null) {
				this.ifnet.addPlace("t_" + t.getName() + "post");//
				IFNetFlowRelation f2 = ifnet.addFlowRelationTP(
						"t_" + t.getName(), "t_" + t.getName() + "post");//
				f2.addConstraint("black", 1);

			} else {
				IFNetFlowRelation f2 = ifnet.addFlowRelationTP(
						"t_" + t.getName(), t.getName());//
				f2.addConstraint("black", 1);
			}

			IFNetFlowRelation f1 = ifnet.addFlowRelationPT("t_" + t.getName()
					+ "pre", "t_" + t.getName());

			f1.addConstraint("black", 1);

			if (t instanceof UserTask) {
			} else if (t instanceof ServiceTask) {
			} else {
			}
		} else {

			if (lastBpmnE instanceof Task) {
				Task task = (Task) lastBpmnE;
				this.ifnet.addTransition("t_" + t.getName());
				this.ifnet.addPlace("t_" + t.getName() + "post");//
				IFNetFlowRelation f1 = ifnet.addFlowRelationPT(
						"t_" + task.getName() + "post", "t_" + t.getName());

				prePlaces.put(t.getName(), "t_" + task.getName() + "post");

				IFNetFlowRelation f2 = ifnet.addFlowRelationTP(
						"t_" + t.getName(), "t_" + t.getName() + "post");//
				f1.addConstraint("black", 1);
				f2.addConstraint("black", 1);
				if (t instanceof UserTask) {
				} else if (t instanceof ServiceTask) {
				} else {
				}

			} else {

				this.ifnet.addPlace("t_" + t.getName() + "pre");
				this.ifnet.addTransition("t_" + t.getName());
				if (prePlaces.get(t.getName()) == null) {
					this.ifnet.addPlace("t_" + t.getName() + "post");//
					IFNetFlowRelation f2 = ifnet.addFlowRelationTP(
							"t_" + t.getName(), "t_" + t.getName() + "post");//
					f2.addConstraint("black", 1);

				} else {
					IFNetFlowRelation f2 = ifnet.addFlowRelationTP(
							"t_" + t.getName(), t.getName());//
					f2.addConstraint("black", 1);
				}
				this.ifnet.addPlace("t_" + t.getName() + "post");//
				IFNetFlowRelation f1 = ifnet.addFlowRelationPT(
						"t_" + t.getName() + "pre", "t_" + t.getName());

				prePlaces.put(t.getName(), "t_" + t.getName() + "pre");

				IFNetFlowRelation f2 = ifnet.addFlowRelationTP(
						"t_" + t.getName(), "t_" + t.getName() + "post");//
				if (t instanceof UserTask) {
				} else if (t instanceof ServiceTask) {
				} else {
				}
			}
		}
	}

	public void createTransitionWithPreAndPostPlaces(BpmnElement t) {
		this.ifnet.addTransition("t_" + t.getName());
		this.ifnet.addPlace("t_" + t.getName() + "pre");
		this.ifnet.addPlace("t_" + t.getName() + "post");
		IFNetFlowRelation f1 = ifnet.addFlowRelationPT("t_" + t.getName()
				+ "pre", "t_" + t.getName());
		IFNetFlowRelation f2 = ifnet.addFlowRelationTP("t_" + t.getName(), "t_"
				+ t.getName() + "post");
		f1.addConstraint("black", 1);
		f2.addConstraint("black", 1);
	}

	public void createTransitionWithPreAndPostPlaces2(BpmnElement t, Bpmn bpmn,
			IFNet ifnc) {
		ExclusiveGateway g = (ExclusiveGateway) t;
		ExclusiveGateway ga = (ExclusiveGateway) g;
		HashSet<String> nu = ga.getOutBound();
		HashSet<String> nu2 = ga.getInBound();
		int z = nu.size();
		int z2 = nu2.size();
		String previous = null;
		// oben
		for (String uu : nu2) {
			BpmnElement resultr = bpmn.findElementByIdTest(uu);
			if (z2 == 1) {
				for (String a123 : nu2) {
					Pair gghhgh = bpmn.getEdge(a123);
					previous = (String) gghhgh.getFirst();
				}
				HashSet<String> kl = ga.getInBound();
				for (String j : kl) {
					BpmnElement mmm = bpmn.findElementByIdTest(j);
					Pair pp = bpmn.getEdge(j);
					ifnc.addPlace("t_" + pp.getFirst() + "post");
				}
				int i = 1;
				for (String mn : nu) {
					ifnc.addTransition("t_" + ga.getName() + "----" + i);
					BpmnElement resultri = bpmn.findElementByIdTest(mn);
					Pair pp = bpmn.getEdge(mn);
					if (prePlaces.get(pp.getSecond()) == null) {
						ifnc.addPlace("t_" + pp.getSecond() + "pre");
					}
					ifnc.addFlowRelationPT("t_" + previous + "post",
							"t_" + ga.getName() + "----" + i);
					i++;
				}
			}
		}

		int i = 1;
		for (String uu : nu) {
			BpmnElement resultr = bpmn.findElementByIdTest(uu);
			for (String mn : nu2) {
				BpmnElement resultri = bpmn.findElementByIdTest(mn);
				Pair pp = bpmn.getEdge(mn);
				BpmnElement bb = bpmn.findElementByIdTest(resultr.getName());
				AbstractIFNetTransition<IFNetFlowRelation> s = ifnc
						.getTransition("t_" + resultr.getName());
				Collection<IFNetPlace> j = ifnc.getSourcePlaces();
				String kj = null;
				String nom = null;
				for (IFNetPlace h : j) {
					nom = h.getName();
				}
				if (prePlaces.get(resultr.getName()) == null) {
					ifnc.addFlowRelationTP("t_" + ga.getName() + "----" + i,
							"t_" + resultr.getName() + "pre");
				} else {

					ifnc.addFlowRelationTP("t_" + ga.getName() + "----" + i,
							prePlaces.get(resultr.getName()));
				}
				compatibility.put(ga.getName() + "|" + resultr.getName(), "p|"
						+ "t_" + resultr.getName() + "pre");
			}
			i++;
		}

	}

	public void createTransitionWithPreAndPostPlaces3(BpmnElement t, Bpmn bpmn,
			IFNet ifnc, BpmnElement lastBpmnE) {
		ParallelGateway g = (ParallelGateway) t;
		ParallelGateway ga = (ParallelGateway) g;
		HashSet<String> nu = ga.getOutBound();
		HashSet<String> nu2 = ga.getInBound();
		int z = nu.size();
		int z2 = nu2.size();
		for (String uu : nu2) {
			BpmnElement resultr = bpmn.findElementByIdTest(uu);
			if (z2 == 1) {
				ifnc.addTransition("t_" + ga.getName());
				HashSet<String> kl = ga.getInBound();
				for (String j : kl) {
					BpmnElement mmm = bpmn.findElementByIdTest(j);
					Pair pp = bpmn.getEdge(j);
					compatibility.get(pp.getFirst() + "|" + ga.getName());
					String u = compatibility.get(pp.getFirst() + "|"
							+ ga.getName());
					ifnc.addFlowRelationPT(u.substring(2, u.length())
							, "t_" + ga.getName());
				}
				for (String mn : nu) {
					BpmnElement resultri = bpmn.findElementByIdTest(mn);
					Pair pp = bpmn.getEdge(mn);
					ifnc.addPlace("t_" + pp.getSecond() + "pre");
					ifnc.addFlowRelationTP("t_" + pp.getFirst(),
							"t_" + pp.getSecond() + "pre");
				}
			}
		}
		for (String uu : nu) {
			BpmnElement resultr = bpmn.findElementByIdTest(uu);
			if (z == 1) {
				ifnc.addTransition("t_" + ga.getName());
				ifnc.addPlace("t_" + resultr.getName() + "pre");
				ifnc.addFlowRelationTP("t_" + ga.getName(),
						"t_" + resultr.getName() + "pre");
				for (String mn : nu2) {
					BpmnElement resultri = bpmn.findElementByIdTest(mn);
					Pair pp = bpmn.getEdge(mn);
					ifnc.addFlowRelationPT("t_" + pp.getFirst() + "post", "t_"
							+ ga.getName());
				}
			}
		}
	}

	public void connectStartEvent(String start, String end) {
		this.ifnet.addTransition("t_" + start);
		this.ifnet.addPlace("t_" + start + "pre");
		IFNetFlowRelation f1 = ifnet.addFlowRelationPT("t_" + start + "pre",
				"t_" + start);
		IFNetFlowRelation f2 = ifnet.addFlowRelationTP("t_" + start, "t_" + end
				+ "pre");
	}

	public void test(BpmnElement t, Bpmn bpmn, IFNet ifnc) {
		ExclusiveGateway g = (ExclusiveGateway) t;
		ExclusiveGateway ga = (ExclusiveGateway) g;
		HashSet<String> nu = ga.getOutBound();
		HashSet<String> nu2 = ga.getInBound();
		int z = nu.size();
		int z2 = nu2.size();
		for (String uu : nu2) {
			BpmnElement resultr = bpmn.findElementByIdTest(uu);
			if (z2 == 1) {
				ifnc.addTransition("t_" + ga.getName());
				HashSet<String> kl = ga.getInBound();
				for (String j : kl) {
					BpmnElement mmm = bpmn.findElementByIdTest(j);
					Pair pp = bpmn.getEdge(j);
					ifnc.addPlace("t_" + pp.getFirst() + "post");
					ifnc.addFlowRelationPT("t_" + pp.getFirst() + "post", "t_"
							+ ga.getName());
				}
				for (String mn : nu) {
					BpmnElement resultri = bpmn.findElementByIdTest(mn);
					Pair pp = bpmn.getEdge(mn);
					ifnc.addPlace("t_" + pp.getSecond() + "pre");
					ifnc.addFlowRelationTP("t_" + pp.getFirst(),
							"t_" + pp.getSecond() + "pre");
				}
			}
		}
		for (String uu : nu) {
			BpmnElement resultr = bpmn.findElementByIdTest(uu);
			if (z == 1) {
				ifnc.addTransition("t_" + ga.getName());
				ifnc.addPlace("t_" + resultr.getName() + "pre");
				ifnc.addFlowRelationTP("t_" + ga.getName(),
						"t_" + resultr.getName() + "pre");
				for (String mn : nu2) {
					BpmnElement resultri = bpmn.findElementByIdTest(mn);
					Pair pp = bpmn.getEdge(mn);
					ifnc.addFlowRelationPT("t_" + pp.getFirst() + "post", "t_"
							+ ga.getName());
				}
			}
		}

	}
}
