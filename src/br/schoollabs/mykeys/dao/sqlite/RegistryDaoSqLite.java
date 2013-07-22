package br.schoollabs.mykeys.dao.sqlite;

import android.database.Cursor;
import br.schoollabs.mykeys.dao.RegistryDao;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.mykeys.model.Registry;
import br.schoollabs.utils.DataSistema;

public class RegistryDaoSqLite extends AbstractDaoSqLite<Registry> implements RegistryDao{

	public RegistryDaoSqLite() {
		this.setEdPackageModel("br.schoollabs.model.");
	}

	@Override
	public Registry findByDataWithImage(Data data) {
		Cursor cursor = database.rawQuery(
				"SELECT r.id, r.content, r.name, r.date FROM Registry r " +
						"INNER JOIN Data d ON d.id = r.data " +
						"WHERE r.name = 'ImageView' AND d.id = " + data.getId(), 
				null);
		cursor.moveToFirst();

		Registry registry = null;
		while(!cursor.isAfterLast()) {
			registry = new Registry();
			registry.setId(cursor.getLong(0));
			registry.setContent(cursor.getString(1));
			registry.setName(cursor.getString(2));
			registry.setDate(DataSistema.parseStringToDate2(cursor.getString(3)));
			cursor.moveToNext();
		}
		
		return registry;
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
	
	
}
