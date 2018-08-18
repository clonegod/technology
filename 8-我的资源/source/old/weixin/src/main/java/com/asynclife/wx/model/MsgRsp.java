package com.asynclife.wx.model;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.asynclife.wx.util.xml.AdapterXmlCDATA;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class MsgRsp extends MsgBase {

	private Image Image;
	
	@XmlJavaTypeAdapter(AdapterXmlCDATA.class)
	private String Content;
	
	private int ArticleCount;
	
	@XmlElementWrapper(name="Articles")
	@XmlElement(name="item")
	private List<Article> Articles; 
	
	public MsgRsp() {
		
	}
	
	public MsgRsp(MsgReq req) {
		this.setFromUserName(req.getToUserName());
		this.setToUserName(req.getFromUserName());
		this.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
	}

	public Image getImage() {
		return Image;
	}

	public void setImage(String imageId) {
		Image = new Image(imageId);
	}
	
	public void setContent(String content) {
		Content = content;
	}

	public String getContent() {
		return Content;
	}

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public void addArticle(String title, String description, String picUrl, String url) {
		if(this.Articles == null) {
			this.Articles = new LinkedList<Article>();
		}
		this.Articles.add(new Article(title, description, picUrl, url));
	}

	
}

@XmlAccessorType(XmlAccessType.FIELD)
class Image {
	@XmlJavaTypeAdapter(AdapterXmlCDATA.class)
	public String MediaId;
	
	public Image() {}

	public Image(String mediaId) {
		super();
		MediaId = mediaId;
	}

}

@XmlAccessorType(XmlAccessType.FIELD)
class Article {
	@XmlJavaTypeAdapter(AdapterXmlCDATA.class)
	public String Title;
	
	@XmlJavaTypeAdapter(AdapterXmlCDATA.class)
	public String Description;
	
	@XmlJavaTypeAdapter(AdapterXmlCDATA.class)
	public String PicUrl;
	
	@XmlJavaTypeAdapter(AdapterXmlCDATA.class)
	public String Url;
	
	public Article() {
		super();
	}
	
	public Article(String title, String description, String picUrl, String url) {
		super();
		Title = title;
		Description = description;
		PicUrl = picUrl;
		Url = url;
	}
}