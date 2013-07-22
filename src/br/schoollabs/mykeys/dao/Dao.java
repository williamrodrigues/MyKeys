package br.schoollabs.mykeys.dao;

import java.util.List;

import android.database.Cursor;
import br.schoollabs.mykeys.model.Model;

public interface Dao<MODEL extends Model> {
	
	/**
	 * Criar Objeto pelo cursor
	 */
	public MODEL newCursor(Cursor cursor);
	
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
	 * @param nameColumn
	 * @param value
	 * @return Model encontrado
	 */
	public MODEL find(String nameColumn, String value);
	
	/**
	 * Procura um model por id
	 * 
	 * @param id
	 * @return Model encontrado
	 */
	public MODEL find(String id);

	/**
	 * Busca todos os models
	 * 
	 * @return Model
	 */
	public List<MODEL> findAll();
	
	/**
	 * Procura todos os models
	 * 
	 * @param nameColumn
	 * @param value
	 * @return List<Model> encontrados
	 */
	public List<MODEL> findAll(String nameColumn, String value);
	
}
