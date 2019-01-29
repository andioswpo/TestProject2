package com.gf.weixin.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Timestamp;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.gf.statusflow.Util;


public class PostParse {
	private String toUserName = null;
	private String fromUserName = null;
	private Timestamp createTime;
	private String msgType = null;
	private String content = null;
	private String picUrl = null;
	private String mediaId = null;
	private String format = null;
	private String thumbMediaId = null;
	private String locationX = null;
	private String locationY = null;
	private int scale;
	private String label = null;
	private String title = null;
	private String description = null;
	private String url = null;
	private long msgId;

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public void parse(byte[] data)
	{
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(bais);
			if(doc != null)
			{
				Element root = doc.getRootElement();
				if(root != null)
				{
					Element tunEle = root.element("ToUserName");
					if(tunEle != null)
						toUserName = (String)tunEle.getData();
					Element funEle = root.element("FromUserName");
					if(funEle != null)
						fromUserName = (String)funEle.getData();
					Element ctEle = root.element("CreateTime");
					if(ctEle != null)
						createTime = new java.sql.Timestamp(Long.parseLong(ctEle.getText()));
					Element mtEle = root.element("MsgType");
					if(mtEle != null)
						msgType = mtEle.getText();
					Element contentEle = root.element("Content");
					if(contentEle != null)
						content = (String)contentEle.getData();
					
					
					Element picUrlEle = root.element("PicUrl");
					if(picUrlEle != null)
						picUrl = (String)picUrlEle.getData();
					Element mediaIdEle = root.element("MediaId");
					if(mediaIdEle != null)
						mediaId = (String)mediaIdEle.getData();
					Element formatEle = root.element("Format");
					if(formatEle != null)
						format = (String)formatEle.getData();
					Element tMediaIdEle = root.element("ThumbMediaId");
					if(tMediaIdEle != null)
						thumbMediaId = (String)tMediaIdEle.getData();
					Element locationXEle = root.element("Location_X");
					if(locationXEle != null)
						locationX = (String)locationXEle.getData();
					Element locationYEle = root.element("Location_Y");
					if(locationYEle != null)
						locationY = (String)locationYEle.getData();
					Element labelEle = root.element("Label");
					if(labelEle != null)
						label = (String)labelEle.getData();
					Element scaleEle = root.element("Scale");
					if(scaleEle != null)
					{
						String temp = (String)scaleEle.getData();
						try
						{
							if(!"".equals(Util.fmtStr(temp)))
								scale = Integer.parseInt(temp);
						}
						catch(Exception e)
						{
							scale = 0;
						}
					}
					Element titleEle = root.element("Title");
					if(titleEle != null)
						title = (String)titleEle.getData();
					Element descriptionEle = root.element("Description");
					if(descriptionEle != null)
						description = (String)descriptionEle.getData();
					Element urlEle = root.element("Url");
					if(urlEle != null)
						url = (String)urlEle.getData();
					
					Element msgIdEle = root.element("MsgId");
					if(msgIdEle != null)
					{
						String temp = (String)msgIdEle.getData();
						try
						{
							if(!"".equals(Util.fmtStr(temp)))
								msgId = Long.parseLong(temp);
						}
						catch(Exception e)
						{
							msgId = 0;
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
