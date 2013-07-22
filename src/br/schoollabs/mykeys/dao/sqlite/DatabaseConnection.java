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
				"name text, " +
				"content text, " +
				"date text, " +
				"UNIQUE (content) " +
				")");
		
		db.execSQL("CREATE TABLE Data(" +
				"id integer primary key autoincrement, " +
				"name text, " +
				"content text, " +
				"date text, " +
				"ordem integer, " +
				"category integer, " +
				"type integer " +
				")");
		
		db.execSQL("CREATE TABLE Registry(" +
				"id integer primary key autoincrement, " +
				"name text, " +
				"content text, " +
				"date text, " +
				"data integer " +
				")");
		
		/* Dados do Sistema */
		db.execSQL("INSERT INTO Type (id, name, content, date) VALUES (1, 'System', 'Menu', '18-07-2013 10:40:00')");
		db.execSQL("INSERT INTO Data (id, name, content, date, type) VALUES (1, 'Home', 'Nova Senha', '18-07-2013 10:40:00', 1)");
		db.execSQL("INSERT INTO Data (id, name, content, date, type) VALUES (2, 'Home', 'Listar Senhas', '18-07-2013 10:40:00', 1)");
		db.execSQL("INSERT INTO Data (id, name, content, date, type) VALUES (3, 'Home', 'Categorias', '19-07-2013 8:40:00', 1)");
		db.execSQL("INSERT INTO Registry (id, content, name, date, data) VALUES (1, '" + R.drawable.ic_new_key + "', 'ImageView', '18-07-2013 10:40:00', 1)");
		db.execSQL("INSERT INTO Registry (id, content, name, date, data) VALUES (2, '" + R.drawable.ic_list_keys + "', 'ImageView', '18-07-2013 10:40:00', 2)");
		db.execSQL("INSERT INTO Registry (id, content, name, date, data) VALUES (3, '" + R.drawable.ic_categories_keys + "', 'ImageView', '19-07-2013 8:40:00', 3)");
	
		/* Dados para aplicação */
		db.execSQL("INSERT INTO Type (id, name, content, date) VALUES (2, 'Data', 'Dados', '19-07-2013 15:00:00')");
		db.execSQL("INSERT INTO Data (id, name, content, date, type) VALUES (4, 'Category', 'E-mail', '19-07-2013 15:20:00', 2)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Utilizado para fazer upgrade o banco, quando o usuario for
		//atualizar o aplicativo
	}
	
}
