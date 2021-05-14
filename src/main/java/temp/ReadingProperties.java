package temp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadingProperties {

	public static void main(String[] args) throws IOException  {
		String path = System.getProperty("user.dir")+"//src//test//resources//project.properties";
		Properties prop = new Properties();
		try {
			FileInputStream fs = new FileInputStream(path);
			prop.load(fs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(prop.getProperty("url"));

	}

}
