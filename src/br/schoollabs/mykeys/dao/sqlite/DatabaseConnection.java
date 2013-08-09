package br.schoollabs.mykeys.dao.sqlite;

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
		db.execSQL("INSERT INTO Type (id, name, content, date) VALUES (1, 'System', 'Dados do Sistema', '18-07-2013 10:40:00')");
		db.execSQL("INSERT INTO Data (id, name, content, date, ordem, type) VALUES (1, 'Settings', 'Configurações do Sistema', '09-08-2013 10:20:00', 0, 1)");
		db.execSQL("INSERT INTO Registry (id, name, content, date, data) VALUES (1, 'CategorySettings', 'Categorias', '09-08-2013 10:20:00', 1)");
		db.execSQL("INSERT INTO Registry (id, name, content, date, data) VALUES (2, 'BackupRestore', 'Cópia de Segurança e Restauração', '09-08-2013 10:20:00', 1)");
		db.execSQL("INSERT INTO Registry (id, name, content, date, data) VALUES (3, 'UserRegisteringTheInSystem', 'Usuário', '09-08-2013 10:20:00', 1)");
	
		/* Dados para Aplicação */
		db.execSQL("INSERT INTO Type (id, name, content, date) VALUES (2, 'Data', 'Dados', '19-07-2013 15:00:00')");
		db.execSQL("INSERT INTO Data (id, name, content, date, ordem, type) VALUES (2, 'Category', 'E-mail', '19-07-2013 15:20:00', 1, 2)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Utilizado para fazer upgrade o banco, quando o usuario for
		//atualizar o aplicativo
	}
	
}
