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
	private Data ownerName;
	private Data email;
	private Data password;
	private Boolean novo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_registering_the_in_system);

		novo = loadInput();
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
				&& Validator.validateLength((EditText) findViewById(R.id.editHomeRegisterSenha), "A Senha deve ter 4 caracteres!", 4)) {
			if (registerSystemUser()) {
				finish();

				if(novo){
					Utils.startActivity(this, MainActivity.class);
					
					Utils.msg(this, "Usuário cadastrado no sistema!");
				}
				else{
					Utils.msg(this, "Dados alterados com sucesso!");
				}
			} else {
				Toast.makeText(this, "Erro ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
			}
		}

	}

	private Boolean registerSystemUser() {
		Type type = ((TypeDaoSqLite) dataDaoSqLite.instanceDaoSqLite("Type")).find("name", "System");

		ownerName.setType(type);
		ownerName.setName("OwnerName");
		ownerName.setContent(((EditText) findViewById(R.id.editHomeRegisterNome)).getText().toString());

		email.setType(type);
		email.setName("Email");
		email.setContent(((EditText) findViewById(R.id.editHomeRegisterEmail)).getText().toString());

		password.setName("Password");
		password.setType(type);
		password.setContent(((EditText) findViewById(R.id.editHomeRegisterSenha)).getText().toString());

		try {
			// Salvar objetos
			dataDaoSqLite.save(ownerName);
			dataDaoSqLite.save(email);
			dataDaoSqLite.save(password);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	private Boolean loadInput() {
		ownerName = dataDaoSqLite.findOwnerName();
		email = dataDaoSqLite.findEmail();
		password = dataDaoSqLite.findPWD();

		if (ownerName == null && email == null && password == null) {
			ownerName = new Data();
			email = new Data();
			password = new Data();
			
			return true;
		} else {
			((EditText) findViewById(R.id.editHomeRegisterNome)).setText(ownerName.getContent());

			((EditText) findViewById(R.id.editHomeRegisterEmail)).setText(email.getContent());

			((EditText) findViewById(R.id.editHomeRegisterSenha)).setText(password.getContent());
			
			return false;
		}
	}
}
