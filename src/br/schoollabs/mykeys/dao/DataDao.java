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
	public Data findOwnerName();

	/**
	 * @return Data (Email) 
	 */
	public Data findEmail();

	/**
	 * Buscar o menu para a Home 
	 * @return @List<Type>
	 */
	public List<Data> findAllHomeMenus();

	/**
	 * Buscar todas as Categorias
	 */
	public List<Data> findCategories();
	
	public Data findCategory(String contentCategory);
	
	public void saveKey(String category, Data key);
}
