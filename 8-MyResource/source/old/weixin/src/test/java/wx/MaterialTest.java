package wx;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.asynclife.wx.App;
import com.asynclife.wx.enums.MediaType;
import com.asynclife.wx.service.MaterialSevice;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class MaterialTest {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MaterialSevice materialSevice;
	
	
	String baseDir = "src/main/resources/media/";

	/**
	 * 素材-增删改查
	 */
	@Test
	public void testUploadTempMaterial() {
		
		File file = new File(baseDir, "xiaoxin.jpg");
		
		String response = materialSevice.uploadTempMaterial(file, MediaType.image);
		
		logger.info(response);
	}
	
}
