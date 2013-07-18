package br.schoollabs.mykeys.dao.sqlite;

import android.content.Context;

/**
 * 
 * @author WilliamRodrigues
 *
 */
public class DatabaseFactory {
	private static DatabaseConnection databaseConnection;
	
	public static void initDatabaseConnection(Context context) {
		databaseConnection = new DatabaseConnection(context);
		databaseConnection.getWritableDatabase();
	}
	
	public static DatabaseConnection getDatabaseConnection() {
		return databaseConnection;
	}
}
