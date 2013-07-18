package br.schoollabs.mykeys.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import br.schoollabs.mykeys.dao.DataDao;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.utils.DataSistema;

public class DataDaoSqLite extends AbstractDaoSqLite<Data> implements DataDao{

	public DataDaoSqLite() {
		this.setEdPackageModel("br.schoollabs.model.");
	}
	
	@Override
	public void save(Data model) {
		ContentValues values = new ContentValues();
		values.put("date", DataSistema.formatDate(model.getDate(), "dd-MM-yyyy hh:mm:ss"));
		values.put("content", model.getContent());
		
		Cursor cursor = database.rawQuery("SELECT id FROM Type WHERE content = '" + model.getType().getContent() + "' ORDER BY id LIMIT 1", null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			values.put("type", cursor.getLong(0));
			cursor.moveToNext();
		}
		
		database.insert("Data", null, values);
	}

	@Override
	public Data findPWD() {
		Cursor cursor = database.rawQuery("SELECT d.id, d.content, d.date FROM Type t INNER JOIN Data d ON d.type = t.id WHERE t.content = 'Password' ORDER BY d.id LIMIT 1", null);
		cursor.moveToFirst();

		Data pwd = null;
		while(!cursor.isAfterLast()) {
			pwd = new Data();
			pwd.setId(cursor.getLong(0));
			pwd.setContent(cursor.getString(1));
			pwd.setDate(DataSistema.parseStringToDate2(cursor.getString(2)));
			cursor.moveToNext();
		}
		
		return pwd;
	}

	@Override
	public Data findUsername() {
		Cursor cursor = database.rawQuery("SELECT d.id, d.content, d.date FROM Type t INNER JOIN Data d ON d.type = t.id WHERE t.content = 'Username' ORDER BY d.id LIMIT 1", null);
		cursor.moveToFirst();

		Data username = null;
		while(!cursor.isAfterLast()) {
			username = new Data();
			username.setId(cursor.getLong(0));
			username.setContent(cursor.getString(1));
			username.setDate(DataSistema.parseStringToDate2(cursor.getString(2)));
			cursor.moveToNext();
		}
		
		return username;
	}

	@Override
	public Data findEmail() {
		Cursor cursor = database.rawQuery("SELECT d.id, d.content, d.date FROM Type t INNER JOIN Data d ON d.type = t.id WHERE t.content = 'EmailUser' ORDER BY d.id LIMIT 1", null);
		cursor.moveToFirst();

		Data pwd = null;
		while(!cursor.isAfterLast()) {
			pwd = new Data();
			pwd.setId(cursor.getLong(0));
			pwd.setContent(cursor.getString(1));
			pwd.setDate(DataSistema.parseStringToDate2(cursor.getString(2)));
			cursor.moveToNext();
		}
		
		return pwd;
	}

	@Override
	public List<Data> findAllHomeMenus() {
		Cursor cursor = database.rawQuery("SELECT d.id, d.content, d.date FROM Data d INNER JOIN Type t ON d.type = t.id WHERE t.content = 'HomeMenus'", null);
		cursor.moveToFirst();

		List<Data> datas = new ArrayList<Data>();
		while (!cursor.isAfterLast()) {
			Data data = new Data();
			data.setId(cursor.getLong(0));
			data.setContent(cursor.getString(1));
			data.setDate(DataSistema.parseStringToDate2(cursor.getString(2)));
			
			datas.add(data);
			
			cursor.moveToNext();
		}

		return datas;
	}

}
