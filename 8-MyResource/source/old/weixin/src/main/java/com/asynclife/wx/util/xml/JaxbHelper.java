package com.asynclife.wx.util.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.asynclife.wx.model.MsgReq;

public class JaxbHelper {
	
	public static String toXml(Object source, Class<?> cls) {
		return toXml(source, cls, true);
	}
	
	public static String toXml(Object source, Class<?> cls, boolean prettyPrint) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(cls);
			Marshaller marshaller = jaxbContext.createMarshaller();

			// output pretty printed
			 if( prettyPrint ) { 
	            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        }
			 
			StringWriter stringWriter = new StringWriter();
			
			marshaller.marshal(source, stringWriter);
			
			String output = stringWriter.toString().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
			
			return output;
			
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T xml2Bean(String xml, Class<T> cls) {

		T t = null;
		try {
			JAXBContext jaxb = JAXBContext.newInstance(cls);
			Unmarshaller u = jaxb.createUnmarshaller();

			t = (T) u.unmarshal(new StringReader(xml));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public static void main(String[] args) {
		
		MsgReq req = new MsgReq();
		req.setFromUserName("test");
		req.setCreateTime(""+System.currentTimeMillis()/1000);
		
		System.out.println(toXml(req, req.getClass(), true));
		
	}
}
