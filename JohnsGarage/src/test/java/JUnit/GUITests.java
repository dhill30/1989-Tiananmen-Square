/**
 * Unit tests for the GUI
 * @author James
 */
package JUnit;

import static org.junit.Assert.*;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import graphics.GUIAbout;
import graphics.GUIAddItem;
import graphics.GUIAddProject;
import graphics.GUIItemView;
import graphics.GUIMain;
import graphics.GUIProjectPane;
import graphics.GUIProjectPaneManager;
import graphics.GUIProjectView;
import insides.FileTree;
import insides.Item;
import insides.Project;
import insides.Tab;




public class GUITests {
	
	private static final Project GUIProjectView = null;

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
	public void GUIAddItemTest() {
		Tab testTab = testTree.newTab("test");
		Project testProj = testTree.newProject("testP", testTab);
		GUIProjectView testProjV = new GUIProjectView(testTree, testProj);
		GUIAddItem test = new GUIAddItem(testTree, testProj, testProjV);
	}
	
	@Test
	public void GUIMainTest() {
		GUIMain.main(null);
	}
	
	@Test
	public void ProjectPaneTest() {
		Tab testTab = testTree.newTab("Test");
		GUIProjectPane test = new GUIProjectPane(testTree, testTab);
		Project testProj = testTree.newProject("testProj", testTab);
		JList testing = test.loadProjects();
		assertEquals("testProj", testing.getModel().getElementAt(0).toString());
	}
	
	@Test
	public void ItemViewTest() {
		Path tempFile = Paths.get(ROOTPATH.toString(), "//config.info");
		Tab testTab = testTree.newTab("testTab");
		Project testProject = testTree.newProject("testProject", testTab);
		Item testItem = testTree.newItem("testitem.info", tempFile, testProject);
		GUIItemView test = new GUIItemView(testItem, testTree);
	}
	
	@Test
	public void AboutTest() {
		GUIAbout test = new GUIAbout();
	}
	
	@Test
	public void AddProjectTest() {
		Tab testTab = testTree.newTab("testTab");
		GUIProjectPane testPane = new GUIProjectPane(testTree, testTab);
		GUIAddProject test = new GUIAddProject(testTree, testTab, testPane);
	}
	
	@Test
	public void ProjectPaneManagerTest() {
		GUIProjectPaneManager test = new GUIProjectPaneManager(testTree);
		Tab testTab = testTree.newTab("testTab");
		test.addPane(testTab);
		test.removePane(testTab);
		test.addPane(testTab);
		test.setPane(testTab);
	}

}















