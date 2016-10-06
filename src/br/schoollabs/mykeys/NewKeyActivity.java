package br.schoollabs.mykeys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import br.schoollabs.mykeys.dao.sqlite.DataDaoSqLite;
import br.schoollabs.mykeys.dao.sqlite.RegistryDaoSqLite;
import br.schoollabs.mykeys.dao.sqlite.TypeDaoSqLite;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.mykeys.model.Registry;
import br.schoollabs.utils.RSA;
import br.schoollabs.utils.Utils;
import br.schoollabs.utils.Validator;

public class NewKeyActivity extends Activity {
	private DataDaoSqLite dataDaoSqLite = new DataDaoSqLite();
	private Data key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_key);

		String nameCategory = "";

		if (getIntent().getExtras() != null && (String) getIntent().getExtras().get("idKey") != null) {
			key = dataDaoSqLite.find((String) getIntent().getExtras().get("idKey"));

			nameCategory = key.getCategory().getContent();

			((EditText) findViewById(R.id.editNewKeyApp)).setText(key.getContent());
			((EditText) findViewById(R.id.editNewKeyUser)).setText((((RegistryDaoSqLite) dataDaoSqLite.instanceDaoSqLite("Registry")).findUserAppByData(key)).getContent());
			((EditText) findViewById(R.id.editNewKeyPassword)).setText((((RegistryDaoSqLite) dataDaoSqLite.instanceDaoSqLite("Registry")).findPasswordAppByData(key)).getContent());

		} else {
			key = new Data();

			nameCategory = dataDaoSqLite.find((String) getIntent().getExtras().get("idCategory")).getContent();
		}

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

		for (int i = 0; i < adp.getCount(); i++) {
			if (adp.getItem(i).equals(nameCategory)) {
				combo.setSelection(i);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_key, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_new_key_save) {
			validateKey();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveKey() {
		Registry userApp = null;
		Registry passwordApp = null;
		String msg = "";

		if (key.getId() != null) {
			userApp = ((RegistryDaoSqLite) dataDaoSqLite.instanceDaoSqLite("Registry")).findUserAppByData(key);
			passwordApp = ((RegistryDaoSqLite) dataDaoSqLite.instanceDaoSqLite("Registry")).findPasswordAppByData(key);
			msg = "Senha alterada com sucesso!!!";
		} else {
			userApp = new Registry();
			passwordApp = new Registry();
			msg = "Nova senha inserida com sucesso!!!";
		}

		/* App */
		key.setType(((TypeDaoSqLite) dataDaoSqLite.instanceDaoSqLite("Type")).find("name", "Data"));
		key.setCategory(dataDaoSqLite.findCategory(((Spinner) findViewById(R.id.spinnerNewKey)).getSelectedItem().toString()));
		key.setName("NameApp");
		key.setContent(((EditText) findViewById(R.id.editNewKeyApp)).getText().toString());

		/* Usuario */
		userApp.setName("UserApp");
		userApp.setContent(((EditText) findViewById(R.id.editNewKeyUser)).getText().toString());
		/* Senha */
		passwordApp.setName("PasswordApp");
		// CRIPTOGRAFA A SENHA
		passwordApp.setContent(RSA.encrypt(((EditText) findViewById(R.id.editNewKeyPassword)).getText().toString()));
		System.out.println("####### ENCRYPT: " + passwordApp.getContent());
		
		//
		key.getRegistries().add(userApp);
		key.getRegistries().add(passwordApp);

		if (dataDaoSqLite.saveKey(key)) {
			Utils.msg(this, msg);
			
			finish();
		} else {
			Toast.makeText(this, "Já existe uma cadastro de senha este Nome!!!", Toast.LENGTH_SHORT).show();
		}
	}

	private void validateKey() {
		if (Validator.validateNotNull(findViewById(R.id.editNewKeyApp), "Preencha no nome da app!") && Validator.validateNotNull(findViewById(R.id.editNewKeyUser), "Preencha o usuário!")
				&& Validator.validateNotNull(findViewById(R.id.editNewKeyPassword), "Preencha a senha!")) {
			saveKey();
		}
	}

	public Data getKey() {
		return key;
	}

	public void setKey(Data key) {
		this.key = key;
	}

}
