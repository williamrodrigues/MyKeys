package br.schoollabs.mykeys.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.schoollabs.utils.DataSistema;

public class Data extends ModelEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String content;
	private Date date;
	private Type type;
	private List<Registry> registries = new ArrayList<Registry>();

	public Data() {
		this.date = DataSistema.getDataCorrenteDate();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Registry> getRegistries() {
		return registries;
	}

	public void setRegistries(List<Registry> registries) {
		this.registries = registries;
	}

}
