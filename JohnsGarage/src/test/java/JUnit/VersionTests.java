package JUnit;

import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

import insides.Version;

public class VersionTests {

	@Test
	public void versionTest() throws IOException {
		Version v = new Version();
		assertEquals("0.191.0",v.getVersion());
	}	

}
