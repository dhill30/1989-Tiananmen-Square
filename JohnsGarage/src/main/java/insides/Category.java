package insides;

import java.nio.file.Path;

public class Category extends Folder<Item> {

	private static final long serialVersionUID = -2533846762155616499L;

	public Category(Path path, String name)
	{
		super(path, name);
	}
}