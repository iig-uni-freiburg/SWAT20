package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;

import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.lattice.RoleLattice;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.lattice.RoleRelation;

public class RolePatternsTest {
	
	protected RBACModel hierarchy;
	
	protected String fileName = "rolePatternTestProcess.mxml";
	
	@Before 
	public void initializeRoleHierarchy() {
	       
		   RoleLattice rl = new RoleLattice(new ArrayList<String>(Arrays.asList("R1", "R2", "R3",
	    		   "R4", "R5", "R6")));
		   
	       rl.addRelation(new RoleRelation("R1", "R2"));
	       rl.addRelation(new RoleRelation("R1", "R3"));
	       rl.addRelation(new RoleRelation("R3", "R4"));
	       rl.addRelation(new RoleRelation("R3", "R5"));
	       rl.addRelation(new RoleRelation("R5", "R6"));
	       ArrayList<String> users = new ArrayList<String>(Arrays.asList("Peter", "Ralf", "Dieter",
	    		   "Günther", "Carlo", "Zikna", "Ivan", "Dankwart"));
	       
	       hierarchy = new RBACModel(rl, users);
	       hierarchy.setRoleMembership("R1", new ArrayList<String>(Arrays.asList("Günther")));
	       hierarchy.setRoleMembership("R2", new ArrayList<String>(Arrays.asList("Peter")));
	       hierarchy.setRoleMembership("R3", new ArrayList<String>(Arrays.asList("Ralf", "Carlo")));
	       hierarchy.setRoleMembership("R4", new ArrayList<String>(Arrays.asList("Dankwart")));
	       hierarchy.setRoleMembership("R5", new ArrayList<String>(Arrays.asList("Ivan", "Zikna")));
	       hierarchy.setRoleMembership("R6", new ArrayList<String>(Arrays.asList("Dieter")));
	       hierarchy.setTransactions(new ArrayList<String>(Arrays.asList("a1", "a2", "a3", "a4", "a5",
	    		   "a6", "a7", "a8")));
	}

}
