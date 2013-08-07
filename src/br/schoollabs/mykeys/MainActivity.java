package br.schoollabs.mykeys;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import br.schoollabs.mykeys.dao.sqlite.DataDaoSqLite;
import br.schoollabs.mykeys.dao.sqlite.DatabaseFactory;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.utils.Utils;

public class MainActivity extends FragmentActivity {

	// ############################################################################################################################################################################
	// ATRIBUTOS
	// ############################################################################################################################################################################

	private DataDaoSqLite dataDaoSqLite;

	private Integer valueButton;
	private Integer cont = 0;
	private Data pwd;

	// ############################################################################################################################################################################
	// GETS E SETS
	// ############################################################################################################################################################################
	public Integer getValueButton() {
		return valueButton;
	}

	public void setValueButton(Integer valueButton) {
		if (cont != null && cont != 0) {
			if (cont == 1) {
				((EditText) findViewById(R.id.editMainDig1)).setText(valueButton.toString());
				findViewById(R.id.editMainDig2).setFocusableInTouchMode(true);
				findViewById(R.id.editMainDig2).requestFocus();
				findViewById(R.id.editMainDig1).clearFocus();
			} else if (cont == 2) {
				((EditText) findViewById(R.id.editMainDig2)).setText(valueButton.toString());
				findViewById(R.id.editMainDig3).setFocusableInTouchMode(true);
				findViewById(R.id.editMainDig3).requestFocus();
				findViewById(R.id.editMainDig2).clearFocus();
			} else if (cont == 3) {
				((EditText) findViewById(R.id.editMainDig3)).setText(valueButton.toString());
				findViewById(R.id.editMainDig4).setFocusableInTouchMode(true);
				findViewById(R.id.editMainDig4).requestFocus();
				findViewById(R.id.editMainDig3).clearFocus();
			} else if (cont == 4) {
				((EditText) findViewById(R.id.editMainDig4)).setText(valueButton.toString());

				// Entrar no sistema
				entrar(pwd);
			}
		}
	}

	public Integer getCont() {
		return cont;
	}

	public void setCont(Integer cont) {
		this.cont += cont;
	}

	// ############################################################################################################################################################################
	// METODOS
	// ############################################################################################################################################################################
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * Iniciando o Bando de Dados SqLite
		 */
		DatabaseFactory.initDatabaseConnection(this);

		// Instaciando o DAO da tabela dos Dados
		dataDaoSqLite = new DataDaoSqLite();
		// Pega a senha cadastrada no sitema
		pwd = dataDaoSqLite.findPWD();
		// Verifica se exite alguma senha cadastra caso nao abre a tela de cadastro de senha

		setContentView(R.layout.activity_main);
		clickButton();

		if (pwd == null) {
			finish();

			Utils.startActivity(this, UserRegisteringTheInSystemActivity.class);
		}
	}

	/**
	 * Click's dos botoes, a cada click seta o valor devido
	 */
	private void clickButton() {
		ImageButton btn0 = (ImageButton) findViewById(R.id.buttonMainPin0);
		btn0.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setCont(1);
				setValueButton(0);
			}
		});

		ImageButton btn1 = (ImageButton) findViewById(R.id.buttonMainPin1);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setCont(1);
				setValueButton(1);
			}
		});

		ImageButton btn2 = (ImageButton) findViewById(R.id.buttonMainPin2);
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setCont(1);
				setValueButton(2);
			}
		});

		ImageButton btn3 = (ImageButton) findViewById(R.id.buttonMainPin3);
		btn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setCont(1);
				setValueButton(3);
			}
		});

		ImageButton btn4 = (ImageButton) findViewById(R.id.buttonMainPin4);
		btn4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setCont(1);
				setValueButton(4);
			}
		});

		ImageButton btn5 = (ImageButton) findViewById(R.id.buttonMainPin5);
		btn5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setCont(1);
				setValueButton(5);
			}
		});

		ImageButton btn6 = (ImageButton) findViewById(R.id.buttonMainPin6);
		btn6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setCont(1);
				setValueButton(6);
			}
		});

		ImageButton btn7 = (ImageButton) findViewById(R.id.buttonMainPin7);
		btn7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setCont(1);
				setValueButton(7);
			}
		});

		ImageButton btn8 = (ImageButton) findViewById(R.id.buttonMainPin8);
		btn8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setCont(1);
				setValueButton(8);
			}
		});

		ImageButton btn9 = (ImageButton) findViewById(R.id.buttonMainPin9);
		btn9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setCont(1);
				setValueButton(9);
			}
		});

		ImageButton btnClear = (ImageButton) findViewById(R.id.buttonMainPin_Del);
		btnClear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clearEdits();
			}
		});
	}

	/**
	 * Entrar no sistema apos validar a senha
	 */
	private void entrar(Data pwd) {
		String pwdSenha = ((EditText) findViewById(R.id.editMainDig1)).getText().toString() + ((EditText) findViewById(R.id.editMainDig2)).getText().toString()
				+ ((EditText) findViewById(R.id.editMainDig3)).getText().toString() + ((EditText) findViewById(R.id.editMainDig4)).getText().toString();

		if (pwd != null && pwd.getContent().equals(pwdSenha)) {
			finish();

			 Utils.startActivity(this, HomeActivity.class);
		} else {
			Toast.makeText(this, "Senha Invalida!", Toast.LENGTH_SHORT).show();
			clearEdits();
		}
	}

	/**
	 * Limpa todos os campos digitados
	 */
	private void clearEdits() {
		((EditText) findViewById(R.id.editMainDig1)).setText("");
		((EditText) findViewById(R.id.editMainDig2)).setText("");
		((EditText) findViewById(R.id.editMainDig3)).setText("");
		((EditText) findViewById(R.id.editMainDig4)).setText("");

		findViewById(R.id.editMainDig1).setFocusableInTouchMode(true);
		findViewById(R.id.editMainDig1).requestFocus();
		findViewById(R.id.editMainDig4).clearFocus();

		cont = 0;
	}
}
