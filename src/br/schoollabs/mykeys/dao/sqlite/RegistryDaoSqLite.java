package br.schoollabs.mykeys.dao.sqlite;

import android.database.Cursor;
import br.schoollabs.mykeys.dao.RegistryDao;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.mykeys.model.Registry;
import br.schoollabs.utils.DataSistema;

public class RegistryDaoSqLite extends AbstractDaoSqLite<Registry> implements RegistryDao {

	public RegistryDaoSqLite() {
		this.setEdPackageModel("br.schoollabs.model.");
	}

	@Override
	public Registry newCursor(Cursor cursor) {
		DataDaoSqLite dataDaoSqLite = new DataDaoSqLite();
		Registry registry = new Registry();

		registry.setId(cursor.getLong(cursor.getColumnIndex("id")));
		registry.setName(cursor.getString(cursor.getColumnIndex("name")));
		registry.setContent(cursor.getString(cursor.getColumnIndex("content")));
		registry.setDate(DataSistema.parseStringToDate2(cursor.getString(cursor.getColumnIndex("date"))));
		registry.setData(dataDaoSqLite.find(cursor.getString(cursor.getColumnIndex("data"))));

		return registry;
	}

	@Override
	public Registry findUserAppByData(Data data) {
		Cursor cursor = database.rawQuery("SELECT r.* FROM Registry r INNER JOIN Data d ON d.id = r.data WHERE r.data = " + data.getId() + " AND d.category IS NOT NULL AND r.name = 'UserApp'", null);

		while (cursor.moveToNext()) {
			return newCursor(cursor);
		}
		return null;
	}

	@Override
	public Registry findPasswordAppByData(Data data) {
		Cursor cursor = database.rawQuery("SELECT r.* FROM Registry r INNER JOIN Data d ON d.id = r.data WHERE r.data = " + data.getId() + " AND d.category IS NOT NULL AND r.name = 'PasswordApp'", null);

		while (cursor.moveToNext()) {
			return newCursor(cursor);
		}
		return null;
	}

}
