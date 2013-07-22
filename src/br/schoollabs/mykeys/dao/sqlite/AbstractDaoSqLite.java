package br.schoollabs.mykeys.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.schoollabs.mykeys.dao.Dao;
import br.schoollabs.mykeys.model.Model;

public abstract class AbstractDaoSqLite<MODEL extends Model> implements Dao<MODEL> {

	protected DatabaseConnection dbHelper;
	protected SQLiteDatabase database;

	protected String modelClassName;
	protected String edPackageModel;

	// ****************************** Contrutor ******************************
	public AbstractDaoSqLite() {
		dbHelper = DatabaseFactory.getDatabaseConnection();
		database = dbHelper.getWritableDatabase();
	}

	// ******************************Gets e Sets******************************
	public String getModelClassName() {
		String classname = this.getClass().getSimpleName();
		if (classname.endsWith("DaoSqLite")) {
			return classname.substring(0, classname.length() - 9);
		}
		return null;
	}

	public void setModelClassName(String modelClassName) {
		this.modelClassName = modelClassName;
	}

	public String getEdPackageModel() {
		return edPackageModel;
	}

	public void setEdPackageModel(String edPackageModel) {
		this.edPackageModel = edPackageModel;
	}

	// ****************************** Metodos ******************************
	@SuppressWarnings("unchecked")
	@Override
	public MODEL getNewModel() {
		try {
			String className = getEdPackageModel() + getModelClassName();
			return (MODEL) Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	@Override
	public void save(MODEL model) {
		// TODO Auto-generated method stub
	}

	@Override
	public void remove(MODEL model) {
		// TODO Auto-generated method stub
	}

	@Override
	public MODEL find(String nameColumn, String value) {
		Cursor cursor = database.rawQuery("SELECT DISTINCT * FROM " + getModelClassName() + " WHERE " + nameColumn + " = " + value + " ORDER BY id LIMIT 1", null);
		while (cursor.moveToNext()) {
			return newCursor(cursor);
		}
		return null;
	}

	@Override
	public List<MODEL> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MODEL> findAll(String nameColumn, String value) {
		List<MODEL> models = new ArrayList<MODEL>();
		Cursor cursor = database.rawQuery("SELECT DISTINCT * FROM " + getModelClassName() + " WHERE " + nameColumn + " = " + value, null);
		
		while (cursor.moveToNext()) {
			models.add(newCursor(cursor));
		}
		
		return models;
	}

	@Override
	public MODEL find(String id) {
		Cursor cursor = database.rawQuery("SELECT DISTINCT * FROM " + getModelClassName() + " WHERE id = " + id, null);
		while (cursor.moveToNext()) {
			return newCursor(cursor);
		}
		return null;
	}
}
