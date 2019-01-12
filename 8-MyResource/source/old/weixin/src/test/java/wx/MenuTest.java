package wx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.asynclife.wx.App;
import com.asynclife.wx.service.MenuService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class MenuTest {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MenuService menuService;
	
	@Test
	public void testCreateMenu() {
		menuService.createMenu();
	}
}
