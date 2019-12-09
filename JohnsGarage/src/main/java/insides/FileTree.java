/**
 * The Filetree is responsible for all IO duties and property handling of all the classes that inherit from GFile.
 * This can be thought of as the controller.
 * Last Edited: 12/4/2019
 * @author Sam
 */
package insides;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class FileTree {
	
	private Folder<Tab> _root;
	private static final Path DEFAULTPATH = Paths.get(System.getProperty("user.dir"));
	private static final Path ROOTPATH = Paths.get(DEFAULTPATH.toString(), "//data");
	private static final Path PROJECTSPATH = Paths.get(ROOTPATH.toString(), "//Projects");
	private static final Path CONFIGPATH = Paths.get(ROOTPATH.toString(), "//config.info");
	
	private Map<GFile, Map<String, String>> _itemProperties;
	
	/**
	 * Initializes values for the FileTree class.
	 * Last Edited: 12/4/2019
	 * @author Sam
	 * @param file
	 */
	public FileTree()
	{
		try
		{
			if(!Files.exists(ROOTPATH)) //Fresh install
			{
				Files.createDirectory(ROOTPATH);
				Files.createDirectories(PROJECTSPATH);
			}
			if(!Files.exists(CONFIGPATH))
				{
					Files.createFile(CONFIGPATH);
					_itemProperties = new HashMap<GFile, Map<String, String>>();
				}
			else if(CONFIGPATH.toFile().length() != 0L)
			{
				File config = CONFIGPATH.toFile();
				FileInputStream f = new FileInputStream(config);
				BufferedInputStream b = new BufferedInputStream(f);
				ObjectInputStream reader = new ObjectInputStream(b);
				_itemProperties = (Map<GFile, Map<String, String>>) reader.readObject();
				reader.close();
			}
			build();
		}
		catch (IOException e)
		{
			System.out.println("Building tree failed:" + e.getMessage());
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Deserializing config failed:" + e.getMessage());
		}
	}
	
	/**
	 * Closes the FileTree and writes the current value of the properties map to the config file.
	 * Should be called before the program is terminated.
	 * Last Edited: 12/4/2019
	 * @author Sam
	 */
	public void close()
	{
		try
		{
			File config = CONFIGPATH.toFile();
			FileOutputStream fout = new FileOutputStream(config);
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(_itemProperties);
			oout.close();
		}
		catch(IOException e)
		{
			System.out.println("Serializing config failed:" + e.getMessage());
		}
	}

	/**
	 * Recursive helper to build the FileTree.
	 * Last Edited: 12/4/2019
	 * @author Sam
	 */
	private void build()
	{
		_root = new Folder<Tab>(ROOTPATH, "root");
		buildHelper(ROOTPATH, _root ,  0);
	}
	/*
	 * Jim added the "\\" for all the paths so it would work
	 * Very nice thank you -Sam
	 */

	/**
	 * Takes a GFile and recursively deletes both its children and itself.
	 * Calling this method will eliminate the reference passed to it. It will not delete the parent, only remove the reference to the deleted file from the list of contents.
	 * Last Edited: 12/4/2019
	 * @param file
	 * @param parent
	 * @author Sam
	 */
	public void delete(GFile file, Folder parent)
    {
        try
        {
            if(!file.getClass().getSimpleName().equals("Item"))
            {
                Folder fold = (Folder) file;
                List<Folder> contents = fold.getContents();
                while(!contents.isEmpty())
                {
                    delete(contents.get(0), (Folder) file);
                }
            }
            Files.delete(file.getPath());
            parent.getContents().remove(file);
            file = null;
        }
        catch (IOException e)
        {
            System.out.println("Problem deleting files: " + e.getMessage());
        }
    }
	
	/**
	 * Imports an Item from the FileSystem using a path, and putting it into the internal representation. 
	 * It puts the item into the folder represented by the parent, with the name of the name selected plus the extension.
	 * Last Edited: 12/4/2019
	 * @author Sam
	 * @param path
	 * @param nameplusext
	 * @param parent
	 */
	public void importItem(Path path, String nameplusext, Category parent)
	{
		try
		{
			Files.copy(path, Paths.get(parent.getPath().toString(), nameplusext));
		}
		catch(FileAlreadyExistsException e)
		{
			System.out.println("A File already exists in this location with this name. The new file was not copied." + e.getMessage());
		}
		catch(IOException e)
		{
			System.out.println("Problem in importItem: " + e.getMessage());
		}
		
	}
	
	/**
	 * Recursively exports the contents of a GFile or folder to a specified destination.
	 * Last Edited: 12/4/2019
	 * @author Sam
	 * @param src
	 * @param dest
	 */
	public void export(final GFile src, final Path dest)
	{
		try
		{
			final Path source = src.getPath();
			Files.walkFileTree(source, new FileVisitor<Path>() { //anonymous class that is used only in walkfiletree

				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					Path newpath = dest.resolve(source.relativize(dir));
					try
					{
						Files.copy(dir, newpath);
					}
					catch(FileAlreadyExistsException e)
					{
						//If the file already exists, just continue on.
					}
					return FileVisitResult.CONTINUE;
				}

				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.copy(source, dest.resolve(source.relativize(file)));
					return FileVisitResult.CONTINUE;
				}

				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		}
		catch(IOException e)
		{
			System.out.println("Problem Exporting files: " + e.getMessage());
		}
	}
	
	/**
	 * Returns a Map of Property Names to Values for a given GFile.
	 * To change, call changeProperty.
	 * Last Edited: 12/4/2019
	 * @param item
	 * @author Sam
	 * @return The Map of Properties
	 */
	public Map<String, String> getProperties(GFile item)
	{
		return _itemProperties.get(item);
	}
	
	/**
	 * Puts a new value or overwrites an old value for a property of a GFile.
	 * Last Edited: 12/4/2019
	 * @author Sam
	 * @param target
	 * @param property
	 * @param value
	 */
	public void changeProperty(GFile target, String property, String value)
	{
		if(!_itemProperties.containsKey(target)) _itemProperties.put(target, new HashMap<String, String>());
		_itemProperties.get(target).put(property, value);
	}
	
	/**
	 * Builds and returns a new Tab object. Changes are immediately made within the FileSystem.
	 * Last Edited: 12/4/2019
	 * @author Sam
	 * @param name
	 * @return The new Tab
	 */
	public Tab newTab(String name)
	{
		try
		{
			Path temppath = Paths.get(_root.getPath().toString() + "\\" + name);
			System.out.println(temppath.toString());
			Files.createDirectory(temppath);
			System.out.println("1");
			Tab ret = new Tab(temppath, name);
			_root.add(ret);
			System.out.println("1");
			return ret;
			
		}
		catch (IOException e)
		{
			System.out.println("Problem making new Tab: " + e.getMessage());
		}
		return null;
	}
	/**
	 * Builds and returns a new Project object. Changes are immediately made within the FileSystem.
	 * Last Edited: 12/4/2019
	 * @author Sam
	 * @param name
	 * @param parent
	 * @return The new Project.
	 */
	public Project newProject(String name, Tab parent)
	{
		try
		{
			Path temppath = Paths.get(parent.getPath().toString() + "\\" + name);
			Files.createDirectory(temppath);
			Project ret = new Project(temppath, name);
			parent.add(ret);
			return ret;
			
		}
		catch (IOException e)
		{
			System.out.println("Problem making new Project: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Builds and returns a new Category object. Changes are immediately made within the FileSystem.
	 * Last Edited: 12/4/2019
	 * @param name
	 * @param parent
	 * @return The new Category.
	 */
	public Category newCategory(String name, Project parent)
	{
		try
		{
			Path temppath = Paths.get(parent.getPath().toString() + "\\" + name);
			Files.createDirectory(temppath);
			Category ret = new Category(temppath, name);
			return ret;
			
		}
		catch (IOException e)
		{
			System.out.println("Problem making new category: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Builds and returns a new Item object. Changes are immediately made within the FileSystem.
	 * Last Edited: 12/4/2019
	 * @author Sam
	 * @param nameplusext
	 * @param itempath
	 * @param parent
	 * @return the new Item.
	 */
	public Item newItem(String nameplusext, Path itempath, Category parent)
	{
		try
		{
			Path temppath = Paths.get(parent.getPath().toString() + "\\" + nameplusext);
			Files.copy(itempath, temppath);
			Item ret = new Item(temppath, nameplusext);
			return ret;
			
		}
		catch (IOException e)
		{
			System.out.println("Problem making new Item: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * An easy way to get access to the highest level of folder.
	 * Last Edited: 12/4/2019
	 * @author Sam
	 * @return the Tabs.
	 */
	public List<Tab> getTabs()
	{
		return _root.getContents();
	}
	
	/**
	 * A recursive helper to build the FileTree. The current layer corresponds to the current depth of the call in the FileTree.
	 * This means that we can conclude the type of the folder based upon the depth.
	 * Last Edited: 12/4/2019
	 * @author Sam
	 * @param curPath
	 * @param parent
	 * @param layer
	 */
	private void buildHelper(Path curPath, Folder parent, int layer)
	{
		File[] files = curPath.toFile().listFiles();
		GFile temp = new GFile();
		if(layer < 3) //if we are not yet at the item level
		{
			for(File f : files)
			{
				if(f.getName().equals("config.info")) continue;
				switch (layer)
				{
					case 0: 
						temp = new Tab(f.toPath(), f.getName());
						break;
					case 1:
						temp = new Project(f.toPath(), f.getName());
						break;
					case 2: 
						temp = new Category(f.toPath(), f.getName());
						break;
				}
				parent.add(temp);
				buildHelper(temp.getPath(), (Folder) temp,  layer + 1);
			}
		}
		else
		{
			for(File f : files)
			{
				temp = new Item(f.toPath(), f.getName());
				parent.add(temp);
			}
		}
	}
	
	/**
	 * Returns a reference to the root. Really should only be used when deleting tabs.
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 * @return the root
	 */
	public Folder<Tab> getRoot()
	{
		return _root;
	}
}
