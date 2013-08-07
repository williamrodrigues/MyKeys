package br.schoollabs.mykeys.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import br.schoollabs.mykeys.dao.DataDao;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.mykeys.model.Registry;
import br.schoollabs.utils.DataSistema;

public class DataDaoSqLite extends AbstractDaoSqLite<Data> implements DataDao {

	public DataDaoSqLite() {
		this.setEdPackageModel("br.schoollabs.model.");
	}

	@Override
	public Data newCursor(Cursor cursor) {
		Data data = new Data();

		data.setId(cursor.getLong(cursor.getColumnIndex("id")));
		data.setName(cursor.getString(cursor.getColumnIndex("name")));
		data.setContent(cursor.getString(cursor.getColumnIndex("content")));
		data.setDate(DataSistema.parseStringToDate2(cursor.getString(cursor.getColumnIndex("date"))));
		data.setOrdem(cursor.getInt(cursor.getColumnIndex("ordem")));
		data.setType(((TypeDaoSqLite) instanceDaoSqLite("Type")).find(cursor.getString(cursor.getColumnIndex("type"))));
		data.setCategory(find(cursor.getString(cursor.getColumnIndex("category"))));
		return data;
	}

	@Override
	public void save(Data model) {
		ContentValues values = new ContentValues();
		values.put("id", model.getId());
		values.put("name", model.getName());
		values.put("content", model.getContent());
		values.put("ordem", model.getOrdem());
		if (model.getCategory() != null) {
			values.put("category", model.getCategory().getId().toString());
		}
		if (model.getType() != null) {
			values.put("type", ((TypeDaoSqLite) instanceDaoSqLite("Type")).find(model.getType().getId().toString()).getId());
		}
		values.put("date", DataSistema.formatDate(model.getDate(), "dd-MM-yyyy hh:mm:ss"));

		// Salvando a Senha
		if (model.getId() == null) {
			database.insert("Data", null, values);
		} else {
			database.update("Data", values, "id = " + model.getId(), null);
		}

		// Buscando Id
		model.setId(checkData(model));

		// Inseridado os registro da nova senha
		for (Registry registry : model.getRegistries()) {
			values = new ContentValues();

			values.put("id", registry.getId());
			values.put("name", registry.getName());
			values.put("content", registry.getContent());
			values.put("data", model.getId());
			values.put("date", DataSistema.formatDate(model.getDate(), "dd-MM-yyyy hh:mm:ss"));

			// Salvando registro da Senha
			if (registry.getId() == null) {
				database.insert("Registry", null, values);
			} else {
				database.update("Registry", values, "id = " + registry.getId(), null);
			}
		}
	}

	@Override
	public void remove(Data model) {
		try {
			if (model != null && model.getId() != null) {
				/* Deletar os filhos */
				List<Data> keys = findAll("category", model.getId().toString());
				for (Data key : keys) {
					for (Registry registry : key.getRegistries()) {
						database.delete("Registry", "id = " + registry.getId(), null);
					}
					database.delete("Data", "id = " + key.getId(), null);
				}
				/* Deletar os registros do model */
				for (Registry registry : model.getRegistries()) {
					database.delete("Registry", "id = " + registry.getId(), null);
				}
				database.delete("Data", "id = " + model.getId(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Data findPWD() {
		Cursor cursor = database.rawQuery("SELECT d.* FROM Type t INNER JOIN Data d ON d.type = t.id WHERE t.name = 'System' AND d.name = 'Password' ORDER BY d.id LIMIT 1", null);

		Data pwd = null;
		while (cursor.moveToNext()) {
			pwd = newCursor(cursor);
		}

		return pwd;
	}

	@Override
	public Data findOwnerName() {
		Cursor cursor = database.rawQuery("SELECT d.* FROM Type t INNER JOIN Data d ON d.type = t.id WHERE t.name = 'System' AND d.name = 'OwnerName' ORDER BY d.id LIMIT 1", null);

		Data username = null;
		while (cursor.moveToNext()) {
			username = newCursor(cursor);
		}

		return username;
	}

	@Override
	public Data findEmail() {
		Cursor cursor = database.rawQuery("SELECT d.* FROM Type t INNER JOIN Data d ON d.type = t.id WHERE t.name = 'System' AND d.name = 'Email' ORDER BY d.id LIMIT 1", null);

		Data email = null;
		while (cursor.moveToNext()) {
			email = newCursor(cursor);
		}

		return email;
	}

	@Override
	public List<Data> findCategories() {
		Cursor cursor = database.rawQuery("SELECT d.* FROM Data d INNER JOIN Type t ON d.type = t.id WHERE t.name = 'Data' AND t.content = 'Dados' AND d.name = 'Category' ORDER BY d.ordem",
				null);

		List<Data> datas = new ArrayList<Data>();
		while (cursor.moveToNext()) {
			Data data = newCursor(cursor);

			datas.add(data);
		}

		return datas;
	}

	@Override
	public Data findCategory(String contentCategory) {
		Cursor cursor = database.rawQuery("SELECT d.* FROM Data d WHERE d.name = 'Category' AND d.content = '" + verificaAspasSimples(contentCategory) + "' ORDER BY d.id LIMIT 1", null);

		Data category = null;
		while (cursor.moveToNext()) {
			category = newCursor(cursor);
		}

		return category;
	}

	@Override
	public Boolean saveKey(Data key) {
		try {
			if (key.getId() != null || checkData(key) == null) {
				save(key);
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private Long checkData(Data data) {
		Cursor cursor = database.rawQuery("SELECT d.id FROM Data d WHERE d.name = '" + data.getName() + "' AND d.content = '" + verificaAspasSimples(data.getContent())
				+ "' AND type = 2 ORDER BY d.id", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			return cursor.getLong(0);
		}
		return null;
	}

	@Override
	public List<Data> findAll() {
		Cursor cursor = database.rawQuery("SELECT DISTINCT id, name, content, date, ordem, type, category FROM Data WHERE type = 2 AND category IS NOT NULL ORDER BY content", null);

		List<Data> datas = new ArrayList<Data>();

		while (cursor.moveToNext()) {
			Data data = newCursor(cursor);

			data.getRegistries().addAll(((RegistryDaoSqLite) instanceDaoSqLite("Registry")).findAll("data", data.getId().toString()));

			datas.add(data);
		}
		cursor.close();

		return datas;
	}

	@Override
	public List<Data> findAll(String nameColumn, String value) {
		List<Data> models = new ArrayList<Data>();
		Cursor cursor = database.rawQuery("SELECT DISTINCT * FROM " + getModelClassName() + " WHERE " + nameColumn + " = " + verificaAspasSimples(value), null);

		while (cursor.moveToNext()) {
			Data data = newCursor(cursor);

			data.getRegistries().addAll(((RegistryDaoSqLite) instanceDaoSqLite("Registry")).findAll("data", data.getId().toString()));
			models.add(data);
		}

		return models;
	}

	@Override
	public Integer findMaxOrdem() {
		Cursor cursor = database.rawQuery("SELECT MAX(ordem) AS ordem FROM Data", null);
		while (cursor.moveToNext()) {
			return cursor.getInt(cursor.getColumnIndex("ordem"));
		}
		return null;
	}

	@Override
	public List<Data> findSettings() {
		List<Data> models = new ArrayList<Data>();
		Cursor cursor = database.rawQuery("SELECT d.* FROM Data d INNER JOIN Type t ON t.id = d.type WHERE t.name = 'System' AND (d.name='OwnerName' OR d.name='Email' OR d.name='Password')", null);

		while (cursor.moveToNext()) {
			Data data = newCursor(cursor);

			data.getRegistries().addAll(((RegistryDaoSqLite) instanceDaoSqLite("Registry")).findAll("data", data.getId().toString()));
			models.add(data);
		}

		return models;
	}
}
