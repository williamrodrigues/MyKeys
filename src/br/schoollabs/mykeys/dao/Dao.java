package br.schoollabs.mykeys.dao;

import java.util.List;

import br.schoollabs.mykeys.model.Model;

public interface Dao<MODEL extends Model> {

	/**
	 * Criar Novo Mode para tipo proprio
	 * 
	 * @return Model
	 */
	public MODEL getNewModel();

	/**
	 * Salva o Model. Inserir e alterar
	 * 
	 * @param model
	 */
	public void save(MODEL model);

	/**
	 * Remove o model
	 * 
	 * @param model
	 */
	public void remove(MODEL model);

	/**
	 * Procura um model por id
	 * 
	 * @param value
	 * @return Model encontrado
	 */
	public MODEL find(String value);

	/**
	 * Busca todos os models
	 * 
	 * @return Model
	 */
	public List<MODEL> findAll();
	
}
