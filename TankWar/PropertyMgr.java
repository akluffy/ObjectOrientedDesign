import java.io.IOException;
import java.util.Properties;


public class PropertyMgr {
	static Properties props = new Properties();
	
	static {
		try {
			props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config/shape.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static String getProperty(String key) {
		return props.getProperty(key);
	}
}
