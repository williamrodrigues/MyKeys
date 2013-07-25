package br.schoollabs.mykeys;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import br.schoollabs.mykeys.dao.sqlite.DataDaoSqLite;
import br.schoollabs.mykeys.dao.sqlite.TypeDaoSqLite;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.utils.Validator;

public class NewCategoryActivity extends Activity {
	private DataDaoSqLite dataDaoSqLite = new DataDaoSqLite();
	private Data category;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_category);

		if (getIntent().getExtras() != null && (String) getIntent().getExtras().get("idCategory") != null) {
			category = dataDaoSqLite.find((String) getIntent().getExtras().get("idCategory"));

			((EditText) findViewById(R.id.editNewCategory)).setText(category.getContent());
//			((EditText) findViewById(R.id.editNewCategoryOrdem)).setText(category.getOrdem().toString());
			
		} else {
			category = new Data();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_category, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_new_category_save) {
			validateCategory();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveCategory() {
		category.setName("Category");
		category.setContent(((EditText) findViewById(R.id.editNewCategory)).getText().toString());
//		category.setOrdem(Integer.parseInt(((EditText) findViewById(R.id.editNewCategoryOrdem)).getText().toString()));
		category.setType(((TypeDaoSqLite) dataDaoSqLite.instanceDaoSqLite("Type")).find("name", "Data"));

		Data ordem = dataDaoSqLite.find("ordem", category.getOrdem().toString());

		if ((dataDaoSqLite.findCategory(category.getContent()) == null && ordem == null) || category.getId() != null) {
			try {
				dataDaoSqLite.save(category);

				if (category.getId() == null) {
					Toast.makeText(this, "Nova categoria inserida com sucesso!!!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Categoria alterada com sucesso!!!", Toast.LENGTH_SHORT).show();
				}

				finish();
			} catch (Exception e) {
				Toast.makeText(this, "Erro ao salvar esta categoria!!!", Toast.LENGTH_SHORT).show();
			}
		} else {
			if (ordem != null) {
				Toast.makeText(this, "Esta ordem já existe nas categorias!!!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Já existe uma categoria com este nome!!!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void validateCategory() {
		if (Validator.validateNotNull(findViewById(R.id.editNewCategory), "Preencha no nome da categoria!")
//				&& Validator.validateNotNull(findViewById(R.id.editNewCategoryOrdem), "Preencha a ordem da categoria!")
				) {
			saveCategory();
		} 
	}

	public Data getCategory() {
		return category;
	}

	public void setCategory(Data category) {
		this.category = category;
	}
}
