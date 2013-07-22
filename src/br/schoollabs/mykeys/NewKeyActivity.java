package br.schoollabs.mykeys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import br.schoollabs.mykeys.dao.sqlite.DataDaoSqLite;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.mykeys.model.Registry;

public class NewKeyActivity extends Activity {
	private DataDaoSqLite dataDaoSqLite = new DataDaoSqLite();
	private Data key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_key);
		
		key = new Data();

		// Criando uma lista de String para popular o Spinner de Categorias
		List<String> categories = new ArrayList<String>();
		for (Data category : dataDaoSqLite.findCategories()) {
			categories.add(category.getContent());
		}

		/* Carrega o Spinner de Categorias */
		Spinner combo = (Spinner) findViewById(R.id.spinnerNewKey);
		ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
		adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		combo.setAdapter(adp);
		combo.setFocusable(true);
		combo.setFocusableInTouchMode(true);
		combo.requestFocus();
		
		/* Butão para salvar a Senha */
		Button btn = (Button) findViewById(R.id.buttonNewKeySave);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				saveKey();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_key, menu);
		return true;
	}

	private void saveKey() {
		key.setCategory(dataDaoSqLite.findCategory(((Spinner) findViewById(R.id.spinnerNewKey)).getSelectedItem().toString()));
		key.setName("NameApp");
		key.setContent(((EditText) findViewById(R.id.editNewKeyApp)).toString());
		
		Registry userApp = new Registry();
		userApp.setName("UserApp");
		userApp.setName(((EditText) findViewById(R.id.editNewKeyUser)).toString());
		
		Registry passwordApp = new Registry();
		passwordApp.setName("PasswordApp");
		passwordApp.setName(((EditText) findViewById(R.id.editNewKeyPassword)).toString());
	}

	public Data getKey() {
		return key;
	}

	public void setKey(Data key) {
		this.key = key;
	}

}
