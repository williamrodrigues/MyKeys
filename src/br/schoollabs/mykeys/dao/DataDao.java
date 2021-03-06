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
	 * Buscar todas as Categorias
	 */
	public List<Data> findCategories();

	public Data findCategory(String contentCategory);

	public Boolean saveKey(Data key);

	public Integer findMaxOrdem();

	public Data findSettings();

	/**
	 * Busca as categorias com as ordens maiores que o paramentro
	 * 
	 * @param ordem
	 * @return
	 */
	public Data findPreviousOrder(Integer ordem);

	/**
	 * Busca as categorias com as ordens maiores que o paramentro
	 * 
	 * @param ordem
	 * @return
	 */
	public Data findLaterOrder(Integer ordem);
}
