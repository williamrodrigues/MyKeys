package br.schoollabs.mykeys.dao.sqlite;

import android.content.ContentValues;
import br.schoollabs.mykeys.dao.TypeDao;
import br.schoollabs.mykeys.model.Type;
import br.schoollabs.utils.DataSistema;

public class TypeDaoSqLite extends AbstractDaoSqLite<Type> implements TypeDao {

	public TypeDaoSqLite() {
		this.setEdPackageModel("br.schoollabs.model.");
	}

	@Override
	public void save(Type model) {
		ContentValues values = new ContentValues();
		values.put("date", DataSistema.formatDate(model.getDate(), "dd-MM-yyyy hh:mm:ss"));
		values.put("content", model.getContent());

		database.insert("Type", null, values);
	}
}
