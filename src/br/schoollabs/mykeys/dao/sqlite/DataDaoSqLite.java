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
		TypeDaoSqLite typeDaoSqLite = new TypeDaoSqLite();
		Data data = new Data();

		data.setId(cursor.getLong(cursor.getColumnIndex("id")));
		data.setName(cursor.getString(cursor.getColumnIndex("name")));
		data.setContent(cursor.getString(cursor.getColumnIndex("content")));
		data.setDate(DataSistema.parseStringToDate2(cursor.getString(cursor.getColumnIndex("date"))));
		data.setOrdem(cursor.getInt(cursor.getColumnIndex("ordem")));
		data.setType(typeDaoSqLite.find(cursor.getString(cursor.getColumnIndex("type"))));
		data.setCategory(find(cursor.getString(cursor.getColumnIndex("category"))));

		return data;
	}

	@Override
	public void save(Data model) {
		ContentValues values = new ContentValues();
		values.put("name", model.getName());
		values.put("content", model.getContent());
		values.put("date", DataSistema.formatDate(model.getDate(), "dd-MM-yyyy hh:mm:ss"));

		Cursor cursor = database.rawQuery("SELECT id FROM Type WHERE name = '" + model.getType().getName() + "' AND content ='" + model.getType().getContent() + "' ORDER BY id LIMIT 1",
				null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			values.put("type", cursor.getLong(0));
			cursor.moveToNext();
		}

		database.insert("Data", null, values);
	}

	@Override
	public Data findPWD() {
		Cursor cursor = database.rawQuery(
				"SELECT d.id, d.name, d.content, d.date FROM Type t INNER JOIN Data d ON d.type = t.id WHERE t.name = 'System' AND d.name = 'Password' ORDER BY d.id LIMIT 1", null);
		cursor.moveToFirst();

		Data pwd = null;
		while (!cursor.isAfterLast()) {
			pwd = new Data();
			pwd.setId(cursor.getLong(0));
			pwd.setName(cursor.getString(1));
			pwd.setContent(cursor.getString(2));
			pwd.setDate(DataSistema.parseStringToDate2(cursor.getString(3)));
			cursor.moveToNext();
		}

		return pwd;
	}

	@Override
	public Data findOwnerName() {
		Cursor cursor = database.rawQuery(
				"SELECT d.id, d.name, d.content, d.date FROM Type t INNER JOIN Data d ON d.type = t.id WHERE t.name = 'System' AND d.name = 'OwnerName' ORDER BY d.id LIMIT 1", null);
		cursor.moveToFirst();

		Data username = null;
		while (!cursor.isAfterLast()) {
			username = new Data();
			username.setId(cursor.getLong(0));
			username.setName(cursor.getString(1));
			username.setContent(cursor.getString(2));
			username.setDate(DataSistema.parseStringToDate2(cursor.getString(3)));
			cursor.moveToNext();
		}

		return username;
	}

	@Override
	public Data findEmail() {
		Cursor cursor = database.rawQuery(
				"SELECT d.id, d.name, d.content, d.date FROM Type t INNER JOIN Data d ON d.type = t.id WHERE t.name = 'System' AND d.name = 'Email' ORDER BY d.id LIMIT 1", null);
		cursor.moveToFirst();

		Data email = null;
		while (!cursor.isAfterLast()) {
			email = new Data();
			email.setId(cursor.getLong(0));
			email.setName(cursor.getString(1));
			email.setContent(cursor.getString(2));
			email.setDate(DataSistema.parseStringToDate2(cursor.getString(3)));
			cursor.moveToNext();
		}

		return email;
	}

	@Override
	public List<Data> findAllHomeMenus() {
		Cursor cursor = database.rawQuery(
				"SELECT d.id, d.name, d.content, d.date FROM Data d INNER JOIN Type t ON d.type = t.id WHERE t.name = 'System' AND t.content = 'Menu' AND d.name = 'Home'", null);
		cursor.moveToFirst();

		List<Data> datas = new ArrayList<Data>();
		while (!cursor.isAfterLast()) {
			Data data = new Data();
			data.setId(cursor.getLong(0));
			data.setName(cursor.getString(1));
			data.setContent(cursor.getString(2));
			data.setDate(DataSistema.parseStringToDate2(cursor.getString(3)));

			datas.add(data);

			cursor.moveToNext();
		}

		return datas;
	}

	@Override
	public List<Data> findCategories() {
		Cursor cursor = database.rawQuery(
				"SELECT d.id, d.name, d.content, d.date FROM Data d INNER JOIN Type t ON d.type = t.id WHERE t.name = 'Data' AND t.content = 'Dados' AND d.name = 'Category'", null);
		cursor.moveToFirst();

		List<Data> datas = new ArrayList<Data>();
		while (!cursor.isAfterLast()) {
			Data data = new Data();
			data.setId(cursor.getLong(0));
			data.setName(cursor.getString(1));
			data.setContent(cursor.getString(2));
			data.setDate(DataSistema.parseStringToDate2(cursor.getString(3)));

			datas.add(data);

			cursor.moveToNext();
		}

		return datas;
	}

	@Override
	public Data findCategory(String contentCategory) {
		Cursor cursor = database.rawQuery("SELECT d.id, d.name, d.content, d.date FROM Data d WHERE d.name = 'Category' AND d.content = '" + contentCategory + "' ORDER BY d.id LIMIT 1",
				null);
		cursor.moveToFirst();

		Data category = null;
		while (!cursor.isAfterLast()) {
			category = new Data();
			category.setId(cursor.getLong(0));
			category.setName(cursor.getString(1));
			category.setContent(cursor.getString(2));
			category.setDate(DataSistema.parseStringToDate2(cursor.getString(3)));
			cursor.moveToNext();
		}

		return category;
	}

	@Override
	public Boolean saveKey(Data key) {
		try {
			if (checkData(key) == null) {
				ContentValues values = new ContentValues();
				values.put("name", key.getName());
				values.put("content", key.getContent());
				values.put("category", key.getCategory().getId().toString());
				values.put("type", "2");
				values.put("date", DataSistema.formatDate(key.getDate(), "dd-MM-yyyy hh:mm:ss"));

				// Inserindo Nova Senha
				database.insert("Data", null, values);

				// Buscando Id da nova senha
				key.setId(checkData(key));

				// Inseridado os registro da nova senha
				for (Registry registry : key.getRegistries()) {
					values = new ContentValues();

					values.put("name", registry.getName());
					values.put("content", registry.getContent());
					values.put("data", key.getId());
					values.put("date", DataSistema.formatDate(key.getDate(), "dd-MM-yyyy hh:mm:ss"));

					// Inserindo registro da Nova Senha
					database.insert("Registry", null, values);
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private Long checkData(Data data) {
		Cursor cursor = database.rawQuery("SELECT d.id FROM Data d WHERE d.name = '" + data.getName() + "' AND d.content = '" + data.getContent() + "' AND type = 2 ORDER BY d.id", null);
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
			RegistryDaoSqLite registryDaoSqLite = new RegistryDaoSqLite();
			Data data = newCursor(cursor);

			data.getRegistries().addAll(registryDaoSqLite.findAll("data", data.getId().toString()));

			datas.add(data);
		}
		cursor.close();

		return datas;
	}
}
