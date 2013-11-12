package de.unifreiburg.iig.bpworkbench2.editor;

public class EditorProperties {
	
	private static EditorProperties instance = null;
	
	public static EditorProperties getInstance(){
		if(instance == null){
			instance = new EditorProperties();
		}
		return instance;
	}
	
	public int getDefaultPlaceSize(){
		return 30;
	}
	
	public int getDefaultTransitionWidth(){
		return 30;
	}
	
	public int getDefaultTransitionHeight(){
		return 30;
	}

}
