package htmlunit.test;

import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HtmlUnitSimpleTest {
	
	Logger logger = LoggerFactory.getLogger(HtmlUnitSimpleTest.class);
	
	@Test
	public void homePage() throws Exception {
	    try (final WebClient webClient = new WebClient()) {
	        final HtmlPage page = webClient.getPage("http://htmlunit.sourceforge.net");
	        Assert.assertEquals("HtmlUnit – Welcome to HtmlUnit", page.getTitleText());

	        final String pageAsXml = page.asXml();
	        Assert.assertTrue(pageAsXml.contains("<body class=\"topBarDisabled\">"));

	        final String pageAsText = page.asText();
	        Assert.assertThat(pageAsText, both(containsString("GUI-Less browser for Java programs"))
	        							.and(containsString("Support for the HTTP and HTTPS protocols")));
	    }
	}
}
