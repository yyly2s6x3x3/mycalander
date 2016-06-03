package com.example.clander;

public class Mode {

	private String modeid = "";// 模式id
	private String modename = "";// 模式名称

	public Mode() {

	}

	public Mode(String modeid, String modename) {
		this.modeid = modeid;
		this.modename = modename;
	}

	public String getModeid() {
		return modeid;
	}

	public void setModeid(String modeid) {
		this.modeid = modeid;
	}

	public String getModename() {
		return modename;
	}

	public void setModename(String modename) {
		this.modename = modename;
	}

}
