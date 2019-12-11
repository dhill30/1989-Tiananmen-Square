package JUnit;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import insides.FileTree;
import insides.Item;
import insides.Project;
import insides.Tab;

public class FIleTreeTests {
	
	Path DEFAULTPATH = Paths.get(System.getProperty("user.dir"));
	
	Path ROOTPATH = Paths.get(DEFAULTPATH.toString(), "//testdata");
	
	FileTree testTree = new FileTree(true);

	
	@Test
	public void testSetUp() {	
		assertTrue("root folder doesn't exist", Files.exists(ROOTPATH));
	}

	@Test
	public void testNewTab() {
		Tab testTab = testTree.newTab("testTab");
		Path temp = Paths.get(ROOTPATH.toString(), "//testTab");
		assertTrue("new tab doesn't exist", Files.exists(temp));
	}
	
	@Test
	public void testNewProject() {
		Tab testTab1 = testTree.newTab("testTab1");
		Project testProject = testTree.newProject("testProject", testTab1);
		Path temp = Paths.get(ROOTPATH.toString(), "//testTab1//testProject");
		assertTrue("new project doesn't exist", Files.exists(temp));
	}
	
	@Test
	public void testNewItem() {
		Path tempFile = Paths.get(ROOTPATH.toString(), "//config.info");
		Tab testTab2 = testTree.newTab("testTab2");
		Project testProject = testTree.newProject("testProject1", testTab2);
		Item testItem = testTree.newItem("testitem.info", tempFile, testProject);
		Path temp = Paths.get(ROOTPATH.toString(), "//testTab2//testProject1//testitem.info");
		assertTrue("new item doesn't exist", Files.exists(temp));
	}s

}
