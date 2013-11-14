package de.uni.freiburg.iig.telematik.swat.prism.searcher;

import java.io.IOException;

import de.invation.code.toval.validate.ParameterException;

public class MacOSSearcher extends LinuxSearcher {
	
	public static final String MAC_DIR_APPLICATIONS = "Applications";

		public MacOSSearcher() throws IOException, ParameterException {
			super();
		}

		@Override
		protected void addSystemSpecificPaths() {
			super.addSystemSpecificPaths();
			prismPaths.addAll(getPotentialPrismSubdirectories(MAC_DIR_APPLICATIONS));
		}

}
