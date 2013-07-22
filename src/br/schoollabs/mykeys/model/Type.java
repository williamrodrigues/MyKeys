package br.schoollabs.mykeys.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.schoollabs.utils.DataSistema;

public class Type extends ModelEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Type() {
		this.date = DataSistema.getDataCorrenteDate();
	}

	private String name;
	private String content;
	private Date date;
	private List<Data> datas = new ArrayList<Data>();

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

	public List<Data> getDatas() {
		return datas;
	}

	public void setDatas(List<Data> datas) {
		this.datas = datas;
	}

}
