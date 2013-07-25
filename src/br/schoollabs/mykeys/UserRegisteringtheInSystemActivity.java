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
import br.schoollabs.mykeys.model.Type;
import br.schoollabs.utils.Utils;
import br.schoollabs.utils.Validator;

public class UserRegisteringTheInSystemActivity extends Activity {
	private DataDaoSqLite dataDaoSqLite = new DataDaoSqLite();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_registering_the_in_system);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_registering_the_in_system, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_new_user_system_save) {
			validateFields();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void validateFields() {
		if (Validator.validateNotNull((EditText) findViewById(R.id.editHomeRegisterNome), "Preencha o Nome!")
				&& Validator.validateNotNull((EditText) findViewById(R.id.editHomeRegisterEmail), "Preencha o E-mail!")
				&& Validator.validateNotNull((EditText) findViewById(R.id.editHomeRegisterSenha), "Preencha a Senha!")) {
			if (registerSystemUser()) {
				finish();
				
				Utils.startActivity(this, MainActivity.class);
				
				Toast.makeText(this, "Usuário cadastrado no sistema!", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(this, "Erro ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
			}
		}

	}

	private Boolean registerSystemUser() {
		Type type = ((TypeDaoSqLite) dataDaoSqLite.instanceDaoSqLite("Type")).find("name", "System");

		Data dataNome = new Data();
		dataNome.setType(type);
		dataNome.setName("OwnerName");
		dataNome.setContent(((EditText) findViewById(R.id.editHomeRegisterNome)).getText().toString());

		Data dataEmail = new Data();
		dataEmail.setType(type);
		dataEmail.setName("Email");
		dataEmail.setContent(((EditText) findViewById(R.id.editHomeRegisterEmail)).getText().toString());

		Data dataSenha = new Data();
		dataSenha.setName("Password");
		dataSenha.setType(type);
		dataSenha.setContent(((EditText) findViewById(R.id.editHomeRegisterSenha)).getText().toString());

		try {
			// Salvar objetos
			dataDaoSqLite.save(dataNome);
			dataDaoSqLite.save(dataEmail);
			dataDaoSqLite.save(dataSenha);
			return true;

		} catch (Exception e) {
			return false;
		}
	}
}
