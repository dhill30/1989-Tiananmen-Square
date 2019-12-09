package insides;

import java.io.IOException;
import java.util.Properties;

public class Version {
	
	static String version_number = "";
	
	public Version() throws IOException
	{
		final Properties properties = new Properties();
		properties.load(this.getClass().getClassLoader().getResourceAsStream("prop.properties"));
		version_number = properties.getProperty("versionNum");
	}
	
	public String getVersion()
	{
		return version_number;
	}

}
