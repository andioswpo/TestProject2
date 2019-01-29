package com.gf.weixin.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class EventParse {
	private EventInfo event = new EventInfo();
	
	public EventInfo getEvent()
	{
		return event;
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
						event.setToUserName(tunEle.getText());
					Element funEle = root.element("FromUserName");
					if(funEle != null)
						event.setFromUserName(funEle.getText());
					Element ctEle = root.element("CreateTime");
					if(ctEle != null)
						event.setCreateTime(new java.sql.Timestamp(Long.parseLong(ctEle.getText())));
					Element mtEle = root.element("MsgType");
					if(mtEle != null)
						event.setMsgType(mtEle.getText());
					Element eEle = root.element("Event");
					if(eEle != null)
						event.setEvent(eEle.getText());
					Element ekEle = root.element("EventKey");
					if(ekEle != null)
						event.setEventKey(ekEle.getText());
					Element tEle = root.element("Ticket");
					if(tEle != null)
						event.setTicket(tEle.getText());
					Element lEle = root.element("Latitude");
					if(lEle != null)
						event.setLatitude(lEle.getText());
					Element loEle = root.element("Longitude");
					if(loEle != null)
						event.setLongitude(loEle.getText());
					Element pEle = root.element("Precision");
					if(pEle != null)
						event.setPrecision(pEle.getText());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
