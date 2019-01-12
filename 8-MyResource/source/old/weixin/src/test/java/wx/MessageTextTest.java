package wx;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.asynclife.wx.App;
import com.asynclife.wx.enums.MsgType;
import com.asynclife.wx.model.MsgReq;
import com.asynclife.wx.model.MsgRsp;
import com.asynclife.wx.model.TemplateMsg;
import com.asynclife.wx.service.MessageService;
import com.asynclife.wx.service.TemplateService;
import com.asynclife.wx.util.xml.JaxbHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class MessageTextTest {

	/**
	 * 响应消息
	 * 	普通消息：文本/图片/图文
	 * 	事件消息
	 */
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	TemplateService templateService;
	
	@Test
	public void testRspImage() {
		
		MsgReq req = new MsgReq();
		req.setFromUserName("123456");
		req.setToUserName("abcdef");
		req.setCreateTime(String.valueOf(System.currentTimeMillis()/1000));
		req.setMsgType("text");
//		req.setContent("this is a test string");
//		req.setMsgId(UUID.randomUUID().toString());
		
		String rsp = messageService.returnPic(req);
		logger.info(rsp);
	}
	
	/**
	 * 图文消息
	 */
	@Test
	public void testArticle() {
		
		MsgRsp msgRsp = new MsgRsp();
		msgRsp.setFromUserName("111");
		msgRsp.setToUserName("me");
		msgRsp.setCreateTime(String.valueOf(System.currentTimeMillis()/1000));
		msgRsp.setMsgType(MsgType.news.name());
		msgRsp.setArticleCount(1);
		msgRsp.addArticle("A4腰PK水桶腰", "闲的慌", 
				"http://pic.yesky.com/uploadImages/2016/076/19/342A5KJ17765.jpg", 
				"http://www.chinapoluo.com");
		String rsp = JaxbHelper.toXml(msgRsp, MsgRsp.class);
		logger.debug(rsp);
	}
	
	/**
	 * 向指定用户发送模板消息
	 */
	@Test
	public void testSendTemplateMsg() {
		
		TemplateMsg msg = new TemplateMsg();
		
		msg.touser = "ob286xMa2Zbx80hF_ph6Y3w5G1BE";
		msg.template_id = "b2_h6-pREyNIxRu6HQxoDQcmNVC33yBS_InHjsdWb2s";
		msg.url = "http://www.chinapoluo.com";
		
		msg.data.put("first", TemplateService.createMap("恭喜你购买成功！", "#173177"));
		msg.data.put("keynote1", TemplateService.createMap("小米超级电视", "#173177"));
		msg.data.put("keynote2", TemplateService.createMap("1", "#173177"));
		msg.data.put("keynote3", TemplateService.createMap("8800.00", "#173177"));
		msg.data.put("keynote4", TemplateService.createMap(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()), "#173177"));
		msg.data.put("remark", TemplateService.createMap("欢迎再次购买！", "#173177"));
		
		templateService.sendTemplateMsg(msg);
	}
	
	/**
	 * 用户与分组
	 * 群发
	 */
}
