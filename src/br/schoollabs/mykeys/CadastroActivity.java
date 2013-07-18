package br.schoollabs.mykeys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.schoollabs.mykeys.dao.sqlite.DataDaoSqLite;
import br.schoollabs.mykeys.dao.sqlite.TypeDaoSqLite;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.mykeys.model.Type;
import br.schoollabs.utils.Validator;

public class CadastroActivity extends Activity {
	private TypeDaoSqLite typeDaoSqLite = new TypeDaoSqLite();
	private DataDaoSqLite dataDaoSqLite = new DataDaoSqLite();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro);

		Button btn = (Button) findViewById(R.id.buttonCadastroSalvar);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				validarCampos();
			}
		});

	}

	private void validarCampos() {
		if (Validator.validateNotNull((EditText) findViewById(R.id.editCadastroNome), "Preencha o Nome!")
				&& Validator.validateNotNull((EditText) findViewById(R.id.editCadastroEmail), "Preencha o E-mail!")
				&& Validator.validateNotNull((EditText) findViewById(R.id.editCadastroSenha), "Preencha a Senha!")) {
			if(salvar()){
				finish();
				
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
			}
		}
	}

	private Boolean salvar() {
		Type typeNome = new Type();
		typeNome.setContent("Username");
		Data dataNome = new Data();
		dataNome.setType(typeNome);
		dataNome.setContent(((EditText) findViewById(R.id.editCadastroNome)).getText().toString());
		
		Type typeEmail = new Type();
		typeEmail.setContent("EmailUser");
		Data dataEmail = new Data();
		dataEmail.setType(typeEmail);
		dataEmail.setContent(((EditText) findViewById(R.id.editCadastroEmail)).getText().toString());

		Type typeSenha = new Type();
		typeSenha.setContent("Password");
		Data dataSenha = new Data();
		dataSenha.setType(typeSenha);
		dataSenha.setContent(((EditText) findViewById(R.id.editCadastroSenha)).getText().toString());
		
		try {
			// Salvar objetos
			typeDaoSqLite.save(typeNome);
			typeDaoSqLite.save(typeEmail);
			typeDaoSqLite.save(typeSenha);
			
			dataDaoSqLite.save(dataNome);
			dataDaoSqLite.save(dataEmail);
			dataDaoSqLite.save(dataSenha);

			Toast.makeText(this, "Cadastro de senha realizado com sucesso!", Toast.LENGTH_SHORT).show();
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}
}
