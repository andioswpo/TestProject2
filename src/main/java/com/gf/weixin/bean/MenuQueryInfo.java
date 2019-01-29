package com.gf.weixin.bean;

import java.util.ArrayList;
import java.util.List;

public class MenuQueryInfo {
	private List<MenuInfo> buttons = new ArrayList<MenuInfo>();

	public List<MenuInfo> getButtons() {
		return buttons;
	}

	public void setButtons(List<MenuInfo> buttons) {
		this.buttons = buttons;
	}
}
