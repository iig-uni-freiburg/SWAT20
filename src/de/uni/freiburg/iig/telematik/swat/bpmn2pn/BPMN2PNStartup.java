package de.uni.freiburg.iig.telematik.swat.bpmn2pn;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.bpmn.Pair;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.AbstractGateway;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.ExclusiveGateway;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.InclusiveGateway;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.ParallelGateway;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.StartEvent;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.TerminationEvent;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.ifnet.ifNet;
public class BPMN2PNStartup {
	public static HashSet<String> elementsVisited = new HashSet<String>();
	
	public static PTNet generateIFnet(String filename) throws Exception{
		filename = sanitizeFile(filename);
		BpmnParser bpmnParser = new BpmnParser(filename);
		bpmnParser.generate();
		PTNet ifnet = new PTNet();
		
		HashMap<String, BpmnElement> j = bpmnParser.getAllElements();	
		for(BpmnElement e : j.values()) {
			ifnet=e.create(ifnet);
		}
		HashSet<ExclusiveGateway> j2 = bpmnParser.getExclusiveGateways();	
		for(BpmnElement e :  j2) {
			ifnet=e.create(ifnet);
		}
		HashSet<ParallelGateway> j3 = bpmnParser.getParallelGateways();	
		for(BpmnElement e :  j3) {
			ifnet=e.create(ifnet);
		}
		HashSet<StartEvent> j4 = bpmnParser.getStartEvents();	
		for(BpmnElement e :  j4) {
			ifnet=e.create(ifnet);
		}
		HashSet<TerminationEvent> j5 = bpmnParser.getTerminationEvents();	
		for(BpmnElement e :  j5) {
			ifnet=e.create(ifnet);
		}
		HashSet<Subprocess> j6 = bpmnParser.getSubprocesses();	
		for(BpmnElement e :  j6) {
			ifnet=e.create(ifnet);
		}
		HashSet<InclusiveGateway> j7 = bpmnParser.getInclusiveGateways();	
		for(BpmnElement e :  j7) {
			ifnet=e.create(ifnet);
		}
		//IFNetMarking h8 = ifnet.getMarking(); //forIfnet
		PTMarking h8 = ifnet.getMarking();
		Multiset<String> pInMarking = new Multiset<String>();
		ifnet.setInitialMarking(h8);
		List<String> resulta;
		List<String> resultb1;
		List<String> resultb2;
		String ki = bpmnParser.findStartEvent();
		HashSet<String> tmpHs = new HashSet<String>();
		BpmnElement result = bpmnParser.findElementById(bpmnParser.findStartEvent());
		StartEvent hi2 = ((StartEvent) result);
		for(String h : hi2.getOutBound()) {
			Pair kk = bpmnParser.getEdge(h);
			BpmnElement hk = bpmnParser.findElementById((String) kk.getSecond());
			if(hk != null) {
				connectStartEvent(hi2, hk, h, ifnet);
			}
		}
		tmpHs = result.getOutBound();
		ifNet if1 = new ifNet();
		int i = 0;
		while (!tmpHs.isEmpty()) {
			System.out.println("Start Iteration " + i);
			System.out.println("Current Queue: " + tmpHs.toString()+" containing discovered edges to be traversed");
			i++;
			int size = tmpHs.size();
			String[] str = tmpHs.toArray(new String[size]);
			BpmnElement tmpElement = null;
			for (int k = 0; k < size; k++) {
				String item1 = str[k];
				tmpHs.remove(item1);
				BpmnElement resultr = bpmnParser.findElementByIdTest(item1);
				Pair<String, String> p1 = bpmnParser.getEdge(item1);
				// Cycle detection - the BPMN element has already been visited
				if(p1!=null) {
					String hd = (String) p1.getSecond();
					if (!elementsVisited.contains(hd)) {
						elementsVisited.add(hd);
					} else {
						System.out.println("Element "+hd+" has already been visited! Continue ...");
						break;
					}
				}
				Boolean b1 = bpmnParser.findElementByIdTestContains(item1);
				String hhh=item1;
				Boolean matchingRule=false;
				if (b1) {
					BpmnElement hi = bpmnParser.findElementByIdTest(item1);
					// current event is the terminationevent
					if (hi instanceof TerminationEvent) {						
						HashSet<String> ss=new HashSet<String>();
						String z="";
						List<String> haa = bpmnParser.getDelegationOutLinks(hi.getName());
						for(String hjhj : haa) {
							z=hjhj;
							ss.add(z);
							tmpHs.add(hjhj);
						}
						if(haa.size()==0)
							matchingRule=true;
						for(String h : ss) {
							Pair kk = bpmnParser.getEdge(h);
							BpmnElement hk = bpmnParser.findElementById((String) kk.getSecond());
							if(hk != null) {	
								if((hk instanceof ExclusiveGateway) | (hk instanceof Task)) {
									ifnet.removePlace(hi.getName()+"post");
									ifnet.addFlowRelationTP(hi.getName(), "helperP"+hk.getName());
								}
								if(hk instanceof TerminationEvent) {
									matchingRule=true;
									ifnet.removePlace(hi.getName()+"post");
									ifnet.addFlowRelationTP(hi.getName(), hk.getStart(h));										
								}
							}			
						}
					}
					// current event is the Exclusive Gateway
					if (hi instanceof ExclusiveGateway) {
						for(String h : ((ExclusiveGateway) hi).getOutBound()) {
							Pair kk = bpmnParser.getEdge(h);
							BpmnElement hk = bpmnParser.findElementById((String) kk.getSecond());	
							if(hk != null) {
								if(hk instanceof ExclusiveGateway) {
									ExclusiveGateway kii = (ExclusiveGateway) hk;
									String kee = ((ExclusiveGateway) hi).getEnd(h);
									ifnet.removePlace(kee);
									ifnet.addFlowRelationTP("posthelperT"+h, kii.getStart(h));
									((ExclusiveGateway) hi).renameEnd(kee, kii.getStart(h));																			
								}
								// next element is the Parallel Gateway
								if(hk instanceof ParallelGateway) {
									func1(hi, (ParallelGateway) hk, h, ifnet);
								}
								if(hk instanceof Task) {	
									matchingRule=true;
									func2(hi, (Task) hk, h, ifnet);
								}
								if(hk instanceof TerminationEvent) {
									matchingRule=true;
									func34(hi, (TerminationEvent) hk, h, ifnet);									
								}
								if(hk instanceof Subprocess) {
									BpmnElement k1 = bpmnParser.findElementByIdTest(h);
									for(String hiii : ((Subprocess) hk).getStart()) {
										ifnet.addFlowRelationPT(hi.getEnd(h), hiii);
									}									
								}
							}			
						}
					}
					if (hi instanceof Task) {	
						for(String h : ((Task) hi).getOutBound()) {
							Pair kk = bpmnParser.getEdge(h);
							BpmnElement hk = bpmnParser.findElementById((String) kk.getSecond());	
							if(hk == null) {
								JFrame frame = new JFrame();
								JOptionPane.showMessageDialog(frame,
										"Element"+" '"+kk.getSecond()+"' "+"has not been returned from element search");
							}

							if(hk != null) {

								matchingRule=true;
								if(hk instanceof ExclusiveGateway) {

									matchingRule=true;

									ExclusiveGateway kii = (ExclusiveGateway) hk;
									String kee = ((Task) hi).getEnd(h);
									ifnet.removePlace(kee);
									ifnet.addFlowRelationTP(hi.getName(), kii.getStart(h));
									((Task) hi).renameEnd(kee, kii.getStart(h));																			
								}

								if(hk instanceof ParallelGateway) {
									func1(hi, (ParallelGateway) hk, h, ifnet);	

								}

								if(hk instanceof InclusiveGateway) {
									InclusiveGateway kii = (InclusiveGateway) hk;
									String kee = ((Task) hi).getEnd(h);
									ifnet.removePlace(kee);
									ifnet.addFlowRelationTP(hi.getName(), kii.getStart(h));
								}
								if(hk instanceof Task) {
									matchingRule=true;
									func2(hi, (Task) hk, h, ifnet);
								}
								if(hk instanceof TerminationEvent) {
									matchingRule=true;
									func33(hi, (TerminationEvent) hk, h, ifnet);

								}
								if(hk instanceof Subprocess) {
									func22(hi, (Subprocess) hk, h, ifnet);		

								}


							}											
						}
					}
					if (hi instanceof InclusiveGateway) {
						for(String h : ((InclusiveGateway) hi).getOutBound()) {
							Pair kk = bpmnParser.getEdge(h);
							BpmnElement hk = bpmnParser.findElementById((String) kk.getSecond());	
							if(hk != null) {
								if(hk instanceof TerminationEvent) {
									func200(hi, (TerminationEvent) hk, h, ifnet);	
								}
								if((hk instanceof AbstractGateway) | (hk instanceof Task)) {					
									func200(hi, hk, h, ifnet);	
								}
							}			
						}
					}
					if (hi instanceof ParallelGateway) {
						for(String h : ((ParallelGateway) hi).getOutBound()) {
							Pair kk = bpmnParser.getEdge(h);
							BpmnElement hk = bpmnParser.findElementById((String) kk.getSecond());	
							if(hk != null) {
								if(hk instanceof ExclusiveGateway) {

									ExclusiveGateway kii = (ExclusiveGateway) hk;
									String kee = ((ParallelGateway) hi).getEnd(h);
									ifnet.removePlace(kee);
									ifnet.addFlowRelationTP(hi.getName(), kii.getStart(h));
									((ParallelGateway) hi).renameEnd(kee, kii.getStart(h));																			//???
								}
								if(hk instanceof ParallelGateway) {
									ParallelGateway kii = (ParallelGateway) hk;
									String kee = ((ParallelGateway) hi).getEnd(h);
									ifnet.removePlace(kee);
									ifnet.addFlowRelationTP(hi.getName(), kii.getStart(h));
									((ParallelGateway) hi).renameEnd(kee, kii.getStart(h));																			//???
								}
								if(hk instanceof TerminationEvent) {
									func35(hi, (TerminationEvent) hk, h, ifnet);
								}
								if(hk instanceof Task) { 
									func2(hi, (Task) hk, h, ifnet);	
								}
							}			
						}
					}
					if (hi instanceof StartEvent) {	
						for(String h : ((StartEvent) hi).getOutBound()) {
							Pair kk = bpmnParser.getEdge(h);
							BpmnElement hk=null;
							hk = bpmnParser.findElementById((String) kk.getSecond());		
							if(hk != null) {
								// lane travaseral
								if(hk instanceof StartEvent) {
									String hd =hi.getName()+"post"+h+"[0]";
									ifnet.addFlowRelationPT(hd, hk.getName());
								}
								if(hk instanceof Task) {
									Task kii = (Task) hk;		
									if(ifnet.getPlace(kii.getStart(h))!=null) {
										List<PTFlowRelation> j41 = ifnet.getPlace(kii.getStart(h)).getOutgoingRelations();
										String kee = ((StartEvent) hi).getEnd(h);
										ifnet.removePlace(kii.getStart(h));
										for(PTFlowRelation jjk : j41) {
											ifnet.addFlowRelationPT(kee, jjk.getTransition().getName());
										}									
										((StartEvent) hi).renameEnd(kee, kii.getStart(h));																				
									}
								}
								// @doc Startevent -> Subprocess - resolves to first element within the subprocess - typically start event
								if(hk instanceof Subprocess) {
									Subprocess kii = (Subprocess) hk;		

									List<String> hj= 									bpmnParser.getDelegationInLinks(hk.getName());
									for(String uiuss : hj) {
										ifnet.addFlowRelationPT(hi.getName()+"post"+h+"[0]", uiuss);
									}
								}
							}			
						}
					}
					for (String item2 : hi.getOutBound()) {
						tmpHs.add(item2);
						// LANE RESOLUTION
						if(hi instanceof Task) {							
							HashSet<String> l = ((Task) hi).getOutMessages();
							if(l!=null)
								if(l.size()!=0) {
									for(String ko : l) {
										tmpHs.add(ko);
									}
								}
						}

					}
					tmpElement=hi;
				} else {
					System.out.println("Element has not been indexed"+item1);
					JFrame frame = new JFrame();
					JOptionPane.showMessageDialog(frame,
							"Element with ingoing Sequence Flow"+" '"+item1+"' "+"has not been indexed");
				}
			}
		}	
		System.out.println("\nAll edges have been traversed\n");
		System.out.println("\nConstructing message flow edges ...\n");
		HashMap<String, Pair<String, Pair<String, String>>> messages = bpmnParser.getMessages();
		for(String key : messages.keySet()) {
			Pair<String, Pair<String, String>> data = messages.get(key);
			ifnet.addPlace("p_"+key+"_Message");
			String start = data.getFirst();
			String end =""+data.getSecond();
			ifnet.addFlowRelationTP(start, "p_"+key+"_Message");
			ifnet.addFlowRelationPT("p_"+key+"_Message", end);
		}
		System.out.println("\nMerge End Events ...\n");
		HashSet<TerminationEvent> endEvents = bpmnParser.getEndEvents();
		HashSet<String> endEventsTopHierarchyHS = new HashSet<String>();
		for(TerminationEvent endEvent : endEvents) {
			if(bpmnParser.hierachySubprocessLookup(endEvent.getName())==0) {
				endEventsTopHierarchyHS.add(endEvent.getName());				
			}
		}
		if(endEventsTopHierarchyHS.size()>1) {
			ifnet.addTransition("T_mergeEndEvents");
			ifnet.addPlace("P_end");
			ifnet.addFlowRelationTP("T_mergeEndEvents", "P_end");
			for(String endEventTopHierarchy : endEventsTopHierarchyHS) {
				ifnet.addFlowRelationPT(endEventTopHierarchy+"post", "T_mergeEndEvents");
			}
		}
		
		// add data store to ifnet (experimental implementation) - e.g. for the complex.bpmn sample - doesn't work properly, colored token missing including colored flow relation
		HashSet<Pair> h = bpmnParser.getDataStoreReferences();
		for(Pair hh : h) {
			String dataStoreName=bpmnParser.getDataStore((String) hh.getFirst());
			ifnet.addPlace(dataStoreName);
			List<String> hhl	= (List<String>) hh.getSecond();
			for(String ddd : hhl) {
				PTFlowRelation r1 = ifnet.addFlowRelationTP(ddd, dataStoreName);
				PTFlowRelation r2 = ifnet.addFlowRelationPT(dataStoreName, ddd);
				Multiset<String> pInMarking11 = new Multiset<String>();		
				PTMarking h11 = ifnet.getMarking();
			}
		}
		
		setCapacities(ifnet);
		
		return ifnet;
	}
	
	private static void setCapacities(PTNet ifnet) {
		for(PTPlace place:ifnet.getPlaces()){
			place.setCapacity(1);
		}
		
		for(PTFlowRelation flow:ifnet.getFlowRelations()){
			flow.setConstraint(1);
		}
		
	}

	private static String sanitizeFile(String filename) throws IOException {
		File file = new File(filename);
		File tempfile = File.createTempFile("bpmn", ".bpmn");
		SanitizeBPMN.sanitize(file, tempfile);
		return tempfile.getAbsolutePath();
	}

	public static void main(String[] args) throws Exception {
		PTNet ifnet = new PTNet();
		ifNet ifn = new ifNet();
		IFNet ifnc = ifn.getNet();
		if(args.length!=1) {
			System.out.println("Error! Invalid argument.\nUsage: java -jar BPMN2IFnet.jar model.bpmn");
			System.exit(1);
		}
		BpmnParser bpmnParser = new BpmnParser(args[0]);
		String filename=args[0];
		bpmnParser.generate();
		HashMap<String, BpmnElement> j = bpmnParser.getAllElements();	
		for(BpmnElement e : j.values()) {
			ifnet=e.create(ifnet);
		}
		HashSet<ExclusiveGateway> j2 = bpmnParser.getExclusiveGateways();	
		for(BpmnElement e :  j2) {
			ifnet=e.create(ifnet);
		}
		HashSet<ParallelGateway> j3 = bpmnParser.getParallelGateways();	
		for(BpmnElement e :  j3) {
			ifnet=e.create(ifnet);
		}
		HashSet<StartEvent> j4 = bpmnParser.getStartEvents();	
		for(BpmnElement e :  j4) {
			ifnet=e.create(ifnet);
		}
		HashSet<TerminationEvent> j5 = bpmnParser.getTerminationEvents();	
		for(BpmnElement e :  j5) {
			ifnet=e.create(ifnet);
		}
		HashSet<Subprocess> j6 = bpmnParser.getSubprocesses();	
		for(BpmnElement e :  j6) {
			ifnet=e.create(ifnet);
		}
		HashSet<InclusiveGateway> j7 = bpmnParser.getInclusiveGateways();	
		for(BpmnElement e :  j7) {
			ifnet=e.create(ifnet);
		}
		PTMarking h8 = ifnet.getMarking();
		Multiset<String> pInMarking = new Multiset<String>();
		ifnet.setInitialMarking(h8);
		List<String> resulta;
		List<String> resultb1;
		List<String> resultb2;
		String ki = bpmnParser.findStartEvent();
		HashSet<String> tmpHs = new HashSet<String>();
		BpmnElement result = bpmnParser.findElementById(bpmnParser.findStartEvent());
		StartEvent hi2 = ((StartEvent) result);
		for(String h : hi2.getOutBound()) {
			Pair kk = bpmnParser.getEdge(h);
			BpmnElement hk = bpmnParser.findElementById((String) kk.getSecond());
			if(hk != null) {
				connectStartEvent(hi2, hk, h, ifnet);
			}
		}
		tmpHs = result.getOutBound();
		ifNet if1 = new ifNet();
		int i = 0;
		while (!tmpHs.isEmpty()) {
			System.out.println("Start Iteration " + i);
			System.out.println("Current Queue: " + tmpHs.toString()+" containing discovered edges to be traversed");
			i++;
			int size = tmpHs.size();
			String[] str = tmpHs.toArray(new String[size]);
			BpmnElement tmpElement = null;
			for (int k = 0; k < size; k++) {
				String item1 = str[k];
				tmpHs.remove(item1);
				BpmnElement resultr = bpmnParser.findElementByIdTest(item1);
				Pair<String, String> p1 = bpmnParser.getEdge(item1);
				// Cycle detection - the BPMN element has already been visited
				if(p1!=null) {
					String hd = (String) p1.getSecond();
					if (!elementsVisited.contains(hd)) {
						elementsVisited.add(hd);
					} else {
						System.out.println("Element "+hd+" has already been visited! Continue ...");
						break;
					}
				}
				Boolean b1 = bpmnParser.findElementByIdTestContains(item1);
				String hhh=item1;
				Boolean matchingRule=false;
				if (b1) {
					BpmnElement hi = bpmnParser.findElementByIdTest(item1);
					// current event is the terminationevent
					if (hi instanceof TerminationEvent) {						
						HashSet<String> ss=new HashSet<String>();
						String z="";
						List<String> haa = bpmnParser.getDelegationOutLinks(hi.getName());
						for(String hjhj : haa) {
							z=hjhj;
							ss.add(z);
							tmpHs.add(hjhj);
						}
						if(haa.size()==0)
							matchingRule=true;
						for(String h : ss) {
							Pair kk = bpmnParser.getEdge(h);
							BpmnElement hk = bpmnParser.findElementById((String) kk.getSecond());
							if(hk != null) {	
								if((hk instanceof ExclusiveGateway) | (hk instanceof Task)) {
									ifnet.removePlace(hi.getName()+"post");
									ifnet.addFlowRelationTP(hi.getName(), "helperP"+hk.getName());
								}
								if(hk instanceof TerminationEvent) {
									matchingRule=true;
									ifnet.removePlace(hi.getName()+"post");
									ifnet.addFlowRelationTP(hi.getName(), hk.getStart(h));										
								}
							}			
						}
					}
					// current event is the Exclusive Gateway
					if (hi instanceof ExclusiveGateway) {
						for(String h : ((ExclusiveGateway) hi).getOutBound()) {
							Pair kk = bpmnParser.getEdge(h);
							BpmnElement hk = bpmnParser.findElementById((String) kk.getSecond());	
							if(hk != null) {
								if(hk instanceof ExclusiveGateway) {
									ExclusiveGateway kii = (ExclusiveGateway) hk;
									String kee = ((ExclusiveGateway) hi).getEnd(h);
									ifnet.removePlace(kee);
									ifnet.addFlowRelationTP("posthelperT"+h, kii.getStart(h));
									((ExclusiveGateway) hi).renameEnd(kee, kii.getStart(h));																			
								}
								// next element is the Parallel Gateway
								if(hk instanceof ParallelGateway) {
									func1(hi, (ParallelGateway) hk, h, ifnet);
								}
								if(hk instanceof Task) {	
									matchingRule=true;
									func2(hi, (Task) hk, h, ifnet);
								}
								if(hk instanceof TerminationEvent) {
									matchingRule=true;
									func34(hi, (TerminationEvent) hk, h, ifnet);									
								}
								if(hk instanceof Subprocess) {
									BpmnElement k1 = bpmnParser.findElementByIdTest(h);
									for(String hiii : ((Subprocess) hk).getStart()) {
										ifnet.addFlowRelationPT(hi.getEnd(h), hiii);
									}									
								}
							}			
						}
					}
					if (hi instanceof Task) {	
						for(String h : ((Task) hi).getOutBound()) {
							Pair kk = bpmnParser.getEdge(h);
							BpmnElement hk = bpmnParser.findElementById((String) kk.getSecond());	
							if(hk == null) {
								JFrame frame = new JFrame();
								JOptionPane.showMessageDialog(frame,
										"Element"+" '"+kk.getSecond()+"' "+"has not been returned from element search");
							}

							if(hk != null) {

								matchingRule=true;
								if(hk instanceof ExclusiveGateway) {

									matchingRule=true;

									ExclusiveGateway kii = (ExclusiveGateway) hk;
									String kee = ((Task) hi).getEnd(h);
									ifnet.removePlace(kee);
									ifnet.addFlowRelationTP(hi.getName(), kii.getStart(h));
									((Task) hi).renameEnd(kee, kii.getStart(h));																			
								}

								if(hk instanceof ParallelGateway) {
									func1(hi, (ParallelGateway) hk, h, ifnet);	

								}

								if(hk instanceof InclusiveGateway) {
									InclusiveGateway kii = (InclusiveGateway) hk;
									String kee = ((Task) hi).getEnd(h);
									ifnet.removePlace(kee);
									ifnet.addFlowRelationTP(hi.getName(), kii.getStart(h));
								}
								if(hk instanceof Task) {
									matchingRule=true;
									func2(hi, (Task) hk, h, ifnet);
								}
								if(hk instanceof TerminationEvent) {
									matchingRule=true;
									func33(hi, (TerminationEvent) hk, h, ifnet);

								}
								if(hk instanceof Subprocess) {
									func22(hi, (Subprocess) hk, h, ifnet);		

								}


							}											
						}
					}
					if (hi instanceof InclusiveGateway) {
						for(String h : ((InclusiveGateway) hi).getOutBound()) {
							Pair kk = bpmnParser.getEdge(h);
							BpmnElement hk = bpmnParser.findElementById((String) kk.getSecond());	
							if(hk != null) {
								if(hk instanceof TerminationEvent) {
									func200(hi, (TerminationEvent) hk, h, ifnet);	
								}
								if((hk instanceof AbstractGateway) | (hk instanceof Task)) {					
									func200(hi, hk, h, ifnet);	
								}
							}			
						}
					}
					if (hi instanceof ParallelGateway) {
						for(String h : ((ParallelGateway) hi).getOutBound()) {
							Pair kk = bpmnParser.getEdge(h);
							BpmnElement hk = bpmnParser.findElementById((String) kk.getSecond());	
							if(hk != null) {
								if(hk instanceof ExclusiveGateway) {

									ExclusiveGateway kii = (ExclusiveGateway) hk;
									String kee = ((ParallelGateway) hi).getEnd(h);
									ifnet.removePlace(kee);
									ifnet.addFlowRelationTP(hi.getName(), kii.getStart(h));
									((ParallelGateway) hi).renameEnd(kee, kii.getStart(h));																			//???
								}
								if(hk instanceof ParallelGateway) {
									ParallelGateway kii = (ParallelGateway) hk;
									String kee = ((ParallelGateway) hi).getEnd(h);
									ifnet.removePlace(kee);
									ifnet.addFlowRelationTP(hi.getName(), kii.getStart(h));
									((ParallelGateway) hi).renameEnd(kee, kii.getStart(h));																			//???
								}
								if(hk instanceof TerminationEvent) {
									func35(hi, (TerminationEvent) hk, h, ifnet);
								}
								if(hk instanceof Task) { 
									func2(hi, (Task) hk, h, ifnet);	
								}
							}			
						}
					}
					if (hi instanceof StartEvent) {	
						for(String h : ((StartEvent) hi).getOutBound()) {
							Pair kk = bpmnParser.getEdge(h);
							BpmnElement hk=null;
							hk = bpmnParser.findElementById((String) kk.getSecond());		
							if(hk != null) {
								// lane travaseral
								if(hk instanceof StartEvent) {
									String hd =hi.getName()+"post"+h+"[0]";
									ifnet.addFlowRelationPT(hd, hk.getName());
								}
								if(hk instanceof Task) {
									Task kii = (Task) hk;		
									if(ifnet.getPlace(kii.getStart(h))!=null) {
										List<PTFlowRelation> j41 = ifnet.getPlace(kii.getStart(h)).getOutgoingRelations();
										String kee = ((StartEvent) hi).getEnd(h);
										ifnet.removePlace(kii.getStart(h));
										for(PTFlowRelation jjk : j41) {
											ifnet.addFlowRelationPT(kee, jjk.getTransition().getName());
										}									
										((StartEvent) hi).renameEnd(kee, kii.getStart(h));																				
									}
								}
								// @doc Startevent -> Subprocess - resolves to first element within the subprocess - typically start event
								if(hk instanceof Subprocess) {
									Subprocess kii = (Subprocess) hk;		

									List<String> hj= 									bpmnParser.getDelegationInLinks(hk.getName());
									for(String uiuss : hj) {
										ifnet.addFlowRelationPT(hi.getName()+"post"+h+"[0]", uiuss);
									}
								}
							}			
						}
					}
					for (String item2 : hi.getOutBound()) {
						tmpHs.add(item2);
						// LANE RESOLUTION
						if(hi instanceof Task) {							
							HashSet<String> l = ((Task) hi).getOutMessages();
							if(l!=null)
								if(l.size()!=0) {
									for(String ko : l) {
										tmpHs.add(ko);
									}
								}
						}

					}
					tmpElement=hi;
				} else {
					System.out.println("Element has not been indexed"+item1);
					JFrame frame = new JFrame();
					JOptionPane.showMessageDialog(frame,
							"Element with ingoing Sequence Flow"+" '"+item1+"' "+"has not been indexed");
				}
			}
		}	
		System.out.println("\nAll edges have been traversed\n");
		System.out.println("\nConstructing message flow edges ...\n");
		HashMap<String, Pair<String, Pair<String, String>>> messages = bpmnParser.getMessages();
		for(String key : messages.keySet()) {
			Pair<String, Pair<String, String>> data = messages.get(key);
			ifnet.addPlace("p_"+key+"_Message");
			String start = data.getFirst();
			String end =""+data.getSecond();
			ifnet.addFlowRelationTP(start, "p_"+key+"_Message");
			ifnet.addFlowRelationPT("p_"+key+"_Message", end);
		}
		System.out.println("\nMerge End Events ...\n");
		HashSet<TerminationEvent> endEvents = bpmnParser.getEndEvents();
		HashSet<String> endEventsTopHierarchyHS = new HashSet<String>();
		for(TerminationEvent endEvent : endEvents) {
			if(bpmnParser.hierachySubprocessLookup(endEvent.getName())==0) {
				endEventsTopHierarchyHS.add(endEvent.getName());				
			}
		}
		if(endEventsTopHierarchyHS.size()>1) {
			ifnet.addTransition("T_mergeEndEvents");
			ifnet.addPlace("P_end");
			ifnet.addFlowRelationTP("T_mergeEndEvents", "P_end");
			for(String endEventTopHierarchy : endEventsTopHierarchyHS) {
				ifnet.addFlowRelationPT(endEventTopHierarchy+"post", "T_mergeEndEvents");
			}
		}
		
		// add data store to ifnet (experimental implementation) - e.g. for the complex.bpmn sample - doesn't work properly, colored token missing including colored flow relation
		HashSet<Pair> h = bpmnParser.getDataStoreReferences();
		for(Pair hh : h) {
			String dataStoreName=bpmnParser.getDataStore((String) hh.getFirst());
			ifnet.addPlace(dataStoreName);
			List<String> hhl	= (List<String>) hh.getSecond();
			for(String ddd : hhl) {
				PTFlowRelation r1 = ifnet.addFlowRelationTP(ddd, dataStoreName);
				PTFlowRelation r2 = ifnet.addFlowRelationPT(dataStoreName, ddd);
				Multiset<String> pInMarking11 = new Multiset<String>();		
				PTMarking h11 = ifnet.getMarking();
			}
		}
		
		
		System.out.print("\nWriting resulting PNML file to filesystem ... ");
		PNSerialization.serialize(ifnet, PNSerializationFormat.PNML,
				filename);
		System.out.println("Done.");
		System.exit(1);
	}
	// connect start event
	private static void connectStartEvent(StartEvent hi2, BpmnElement hk, String h,
			PTNet ifnet) {
		if(ifnet.getPlace(hk.getStart(h))==null) {
			ifnet.addPlace(hi2.getName()+"post");
			ifnet.addFlowRelationTP(hi2.getName(), hi2.getName()+"post");
			ifnet.addFlowRelationPT(hi2.getName()+"post", hk.getStart(h));
		} else {
			ifnet.addFlowRelationTP(hi2.getName(), hk.getStart(h));
		}
		ifnet.removePlace(hi2.getEnd(h));
	}
	
	/*
	 * bunch definition rules of how two fragements connect each other accessed within the main program
	 * 
	 */
	private static void func1(BpmnElement hi, BpmnElement kii, String h, PTNet ifnet) {								
		List<PTFlowRelation> j41 = ifnet.getPlace(kii.getStart(h)).getOutgoingRelations();
		String kee = hi.getEnd(h);
		ifnet.removePlace(kii.getStart(h));
		for(PTFlowRelation jjk : j41) {
			ifnet.addFlowRelationPT(kee, jjk.getTransition().getName());
		}									
		hi.renameEnd(kee, kii.getStart(h));
	}
	private static void func2(BpmnElement hi, BpmnElement kii, String h, PTNet ifnet) {		
		String kee = hi.getEnd(h);
		//List<IFNetFlowRelation> j41 = ifnet.getPlace(kee).getIncomingRelations();
		List<PTFlowRelation> j41 = ifnet.getPlace(kee).getIncomingRelations();
		ifnet.removePlace(kee);
		for(PTFlowRelation jjk : j41) {
			ifnet.addFlowRelationTP(jjk.getTransition().getName(), ((Task) kii).getStart(h));
		}									
		hi.renameEnd(kee, kii.getStart(h));	
	}
	private static void func200(BpmnElement hi, BpmnElement kii, String h, PTNet ifnet) {		
		String kee = hi.getEnd(h);
		List<PTFlowRelation> j41 = ifnet.getPlace(kee).getIncomingRelations();
		ifnet.removePlace(kee);
		for(PTFlowRelation jjk : j41) {
			ifnet.addFlowRelationTP(jjk.getTransition().getName(), kii.getStart(h));
		}									
		hi.renameEnd(kee, kii.getStart(h));	
	}
	private static void func22(BpmnElement hi, BpmnElement kii, String h, PTNet ifnet) {		
		String kee = hi.getEnd(h);
		List<PTFlowRelation> j41 = ifnet.getPlace(kee).getIncomingRelations();
		for(PTFlowRelation jjk : j41) {
			ifnet.addFlowRelationPT(kee, ((Subprocess) kii).getStart(h));
		}									
		hi.renameEnd(kee, kii.getStart(h));	
	}
	private static void func33(BpmnElement hi, BpmnElement kii, String h, PTNet ifnet) {
		ifnet.removePlace(hi.getEnd(h));
		ifnet.addFlowRelationTP(hi.getName(), kii.getStart(h));
	}	
	private static void func34(BpmnElement hi, BpmnElement kii, String h, PTNet ifnet) {
		ifnet.removePlace(hi.getEnd(h));
		ifnet.addFlowRelationTP("posthelperT"+h, kii.getStart(h));
	}
	private static void func35(BpmnElement hi, BpmnElement kii, String h, PTNet ifnet) {
		ifnet.removePlace(hi.getEnd(h));
		ifnet.addFlowRelationTP(hi.getName(), kii.getStart(h));
	}
}