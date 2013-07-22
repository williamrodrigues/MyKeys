package br.schoollabs.mykeys.dao.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import br.schoollabs.mykeys.dao.TypeDao;
import br.schoollabs.mykeys.model.Type;
import br.schoollabs.utils.DataSistema;

public class TypeDaoSqLite extends AbstractDaoSqLite<Type> implements TypeDao {

	public TypeDaoSqLite() {
		this.setEdPackageModel("br.schoollabs.model.");
	}

	@Override
	public Type newCursor(Cursor cursor) {
		Type type = new Type();
		type.setId(cursor.getLong(cursor.getColumnIndex("id")));
		type.setName(cursor.getString(cursor.getColumnIndex("name")));
		type.setContent(cursor.getString(cursor.getColumnIndex("content")));
		type.setDate(DataSistema.parseStringToDate2(cursor.getString(cursor.getColumnIndex("date"))));
		return type;
	}

	@Override
	public void save(Type model) {
		ContentValues values = new ContentValues();
		values.put("name", model.getName());
		values.put("content", model.getContent());
		values.put("date", DataSistema.formatDate(model.getDate(), "dd-MM-yyyy hh:mm:ss"));

		database.insert("Type", null, values);
	}

	@Override
	public Type findData() {
		Cursor cursor = database.rawQuery("SELECT id, name, content, date FROM Type WHERE name = 'Data' ORDER BY d.id LIMIT 1", null);
		cursor.moveToFirst();

		Type type = null;
		while (!cursor.isAfterLast()) {
			type = new Type();
			type.setId(cursor.getLong(0));
			type.setName(cursor.getString(1));
			type.setContent(cursor.getString(2));
			type.setDate(DataSistema.parseStringToDate2(cursor.getString(3)));
			cursor.moveToNext();
		}

		return type;
	}

}
