/**
 * Unit tests for the file tree
 * @author James
 */
package JUnit;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import insides.FileTree;
import insides.Item;
import insides.Project;
import insides.Tab;

public class FileTreeTests {
	
	Path DEFAULTPATH = Paths.get(System.getProperty("user.dir"));
	
	Path ROOTPATH = Paths.get(DEFAULTPATH.toString(), "//testdata");
	
	FileTree testTree;
	
	@Before
	public void setup()
	{
		testTree = new FileTree(true);
	}
	
	@After
	public void reset()
	{
		List<Tab> tabs = testTree.getTabs();
		while(tabs.size() > 0)
		{
			testTree.delete(tabs.get(0), testTree.getRoot());
		}
	}
	
	@Test 
	public void testSetUp() {	
		assertTrue("root folder doesn't exist", Files.exists(ROOTPATH));
	}
	
	@Test
	public void testGetTabs() {
		testTree.newTab("Projects");
		testTree.newTab("testGFile");
		assertEquals("[Projects, testGFile]", testTree.getTabs().toString());
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
	}
	
	@Test
	public void testDelete() {
		Tab testDelete = testTree.newTab("testDelete");
		Project testProject = testTree.newProject("testProject", testDelete);
		Path temp = Paths.get(ROOTPATH.toString(), "//testDelete//testProject//");
		testTree.delete(testProject, testDelete);
		assertTrue("new project doesn't exist", !Files.exists(temp));
	}

//	@Test
//	public void testImportitem() {
//		Path tempFile = Paths.get(ROOTPATH.toString(), "//config.info");
//		Tab testTab3 = testTree.newTab("testTab3");
//		Project testProject = testTree.newProject("testProject", testTab3);
//		testTree.importItem(tempFile, "config.info", testProject);
//		Path temp = Paths.get(ROOTPATH.toString(), "//testTab3//testProject//config.info");
//		assertTrue("new item doesn't exist", Files.exists(temp));
//	}
	
	@Test
	public void testProperty() {
		Path tempFile = Paths.get(ROOTPATH.toString(), "//config.info");
		Tab testProp = testTree.newTab("testProp");
		Project testProject = testTree.newProject("testProject1", testProp);
		Item testItem = testTree.newItem("testitem.info", tempFile, testProject);
		testTree.changeProperty(testItem, "desc", "testing");
		assertEquals("{desc=testing}", testTree.getProperties(testItem).toString());
	}
	
//	@Test
//	public void testGFile() {
//		Tab testTab = testTree.newTab("testGFile");
//		testTab.changeName("newTestName");
//		assertEquals("newTestName", testTab.getName());
//	}
	
	
}
