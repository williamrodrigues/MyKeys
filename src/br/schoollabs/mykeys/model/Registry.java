package br.schoollabs.mykeys.model;

import java.util.Date;

import br.schoollabs.utils.DataSistema;

public class Registry extends ModelEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Registry() {
		this.date = DataSistema.getDataCorrenteDate();
	}

	private String name;
	private String content;
	private Date date;
	private Data data;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

}
