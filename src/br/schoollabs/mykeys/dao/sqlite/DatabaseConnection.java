package br.schoollabs.mykeys.dao.sqlite;

import br.schoollabs.mykeys.R;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author WilliamRodrigues
 *
 */
public class DatabaseConnection extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "mykeys.db";
	private static final int DATABASE_VERSION = 1;

	public DatabaseConnection(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("Criando a base de dados...");
		
		db.execSQL("CREATE TABLE Type(" +
				"id integer primary key autoincrement, " +
				"content text, " +
				"date text, " +
				"UNIQUE (content) " +
				")");
		
		db.execSQL("CREATE TABLE Data(" +
				"id integer primary key autoincrement, " +
				"content text, " +
				"date text," +
				"type integer " +
				")");
		
		db.execSQL("CREATE TABLE Registry(" +
				"id integer primary key autoincrement, " +
				"name text, " +
				"content text, " +
				"date text, " +
				"data integer " +
				")");
		
		db.execSQL("INSERT INTO Type (id, content, date) VALUES (1, 'HomeMenus', '18-07-2013 10:40:00')");
		db.execSQL("INSERT INTO Data (id, content, date, type) VALUES (1, 'Nova Senha', '18-07-2013 10:40:00', 1)");
		db.execSQL("INSERT INTO Data (id, content, date, type) VALUES (2, 'Listar Senhas', '18-07-2013 10:40:00', 1)");
		db.execSQL("INSERT INTO Registry (id, content, name, date, data) VALUES (1, '" + R.drawable.ic_new_key + "', 'ImageView', '18-07-2013 10:40:00', 1)");
		db.execSQL("INSERT INTO Registry (id, content, name, date, data) VALUES (2, '" + R.drawable.ic_list_keys + "', 'ImageView', '18-07-2013 10:40:00', 2)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Utilizado para fazer upgrade o banco, quando o usuario for
		//atualizar o aplicativo
	}
	
}
