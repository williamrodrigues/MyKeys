package br.schoollabs.mykeys;

import br.schoollabs.mykeys.dao.sqlite.DataDaoSqLite;
import br.schoollabs.mykeys.dao.sqlite.TypeDaoSqLite;
import br.schoollabs.mykeys.model.Data;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NewCategoryActivity extends Activity {
	private DataDaoSqLite dataDaoSqLite = new DataDaoSqLite();
	private Data category;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_category);

		category = new Data();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_category, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_new_category_save) {
			saveCategory();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveCategory() {
		TypeDaoSqLite typeDaoSqLite = new TypeDaoSqLite();

		category.setName("Category");
		category.setContent(((EditText) findViewById(R.id.editNewCategory)).getText().toString());
		category.setOrdem(Integer.parseInt(((EditText) findViewById(R.id.editNewCategoryOrdem)).getText().toString()));
		category.setType(typeDaoSqLite.find("name", "Data"));

		Data ordem = dataDaoSqLite.find("ordem", category.getOrdem().toString());

		if (dataDaoSqLite.find("content", category.getContent() + " AND category IS NOT NULL") == null && ordem == null) {
			try {
				dataDaoSqLite.save(category);

				Toast.makeText(this, "Nova categoria inserida com sucesso!!!", Toast.LENGTH_SHORT).show();

				finish();
			} catch (Exception e) {
				Toast.makeText(this, "Erro ao salvar esta categoria!!!", Toast.LENGTH_SHORT).show();
			}
		} else {
			if (ordem != null) {
				Toast.makeText(this, "Esta ordem já existe nas categorias!!!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Já existe uma categoria este nome!!!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public Data getCategory() {
		return category;
	}

	public void setCategory(Data category) {
		this.category = category;
	}
}
