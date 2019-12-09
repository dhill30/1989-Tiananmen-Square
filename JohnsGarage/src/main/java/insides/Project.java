/**
 * A representation of a Project, used by the GUI to construct UI components.
 * Last Edited: 12/4/2019
 * @author Sam
 */
package insides;

import java.nio.file.Path;

public class Project extends Folder<Item>{
	private static final long serialVersionUID = 579981190786066767L;
	
	/**
	 * Initializes the Project.
	 * Last Edited: 12/4/2019
	 * @param path
	 * @param name
	 */
	public Project(Path path, String name)
	{
		super(path, name);
	}

}
