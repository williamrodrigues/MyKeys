package br.schoollabs.mykeys.dao;

import java.util.List;

import br.schoollabs.mykeys.model.Data;

public interface DataDao extends Dao<Data> {
	
	/** 
	 * @return Data (PWD)
	 */
	public Data findPWD();
	
	/**
	 * @return Data (Username) 
	 */
	public Data findUsername();

	/**
	 * @return Data (Email) 
	 */
	public Data findEmail();

	/**
	 * Buscar o menu para a Home 
	 * @return @List<Type>
	 */
	public List<Data> findAllHomeMenus();

}
