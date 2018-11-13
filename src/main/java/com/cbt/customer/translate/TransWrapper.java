package com.cbt.customer.translate;

public class TransWrapper {
	private Trans[] sentences;
	private String src;
	private String server_time;
	private String spell;
	public Trans[] getSentences() {
		return sentences;
	}
	public void setSentences(Trans[] sentences) {
		this.sentences = sentences;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getServer_time() {
		return server_time;
	}
	public void setServer_time(String server_time) {
		this.server_time = server_time;
	}
	public String getSpell() {
		return spell;
	}
	public void setSpell(String spell) {
		this.spell = spell;
	}
	
}
