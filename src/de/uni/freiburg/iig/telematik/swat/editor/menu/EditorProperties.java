package de.uni.freiburg.iig.telematik.swat.editor.menu;

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
	
	public int getDefaultVerticalLabelOffset(){
		return 30;
	}
	
	public int getDefaultHorizontalLabelOffset(){
		return 0;
	}
	
	public int getDefaultTokenSize(){
		return 8;
	}

	public int getDefaultTokenDistance(){
		return 1;
	}


}
