package graphics;

import javax.swing.JFrame;

import insides.FileTree;
import insides.Project;

public class GUIProjectView extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 426473126064216924L;
	
	private FileTree theFileTree;
	
	private Project theProject;
	
	public GUIProjectView(FileTree fileTree, Project project)
	{
		theFileTree = fileTree;
		theProject = project;
		
		
	}
}
