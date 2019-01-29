package com.gf.weixin.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@TableName("hk_postmsg")
public class WxPostMsgInfo implements java.io.Serializable
{
	@TableId(type=IdType.AUTO)	
	private String id = null;
	private Date createTime = new Date();
	private String code = null;
	private String msgType = null;
	private String content = null;
	private String title = null;
	private String description = null;
	private String musicUrl = null;
	private String hqMusicUrl = null;
	private boolean selected = false;
	private String attId = null;
	private String flag = null;
	@TableField(exist=false)
	private List<WxPostMsgItemInfo> itemList = new ArrayList<WxPostMsgItemInfo>();
	@TableField(exist=false)
	private String msgTypeName = null;
	//活动消息，规则定义中选择返回消息为活动而非消息
	//系统根据活动定义构建消息，actId是对应活动的ID
	@TableField(exist=false)
	private String actId = null;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAttId() {
		return attId;
	}
	public void setAttId(String attId) {
		this.attId = attId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getMusicUrl() {
		return musicUrl;
	}
	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}
	public String getHqMusicUrl() {
		return hqMusicUrl;
	}
	public void setHqMusicUrl(String hqMusicUrl) {
		this.hqMusicUrl = hqMusicUrl;
	}
	public boolean getSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public List<WxPostMsgItemInfo> getItemList() {
		return itemList;
	}
	public void setItemList(List<WxPostMsgItemInfo> itemList) {
		this.itemList = itemList;
	}
	
	public String toString()
	{
		return "WxPostMsgInfo[id="+id+",itemList="+itemList+"]";
	}
	public String getMsgTypeName() {
		return msgTypeName;
	}
	public void setMsgTypeName(String msgTypeName) {
		this.msgTypeName = msgTypeName;
	}
	public String getActId() {
		return actId;
	}
	public void setActId(String actId) {
		this.actId = actId;
	}
}