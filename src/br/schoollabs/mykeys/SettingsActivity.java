/**
 * 
 */
package br.schoollabs.mykeys;

import org.apache.commons.lang.StringUtils;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import br.schoollabs.mykeys.dao.sqlite.DataDaoSqLite;
import br.schoollabs.mykeys.model.Data;

/**
 * @author WilliamRodrigues
 * 
 */

@SuppressWarnings({ "deprecation" })
public class SettingsActivity extends PreferenceActivity {
	private DataDaoSqLite dataDaoSqLite;
	private Data ownerName;
	private Data email;
	private Data password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		dataDaoSqLite = new DataDaoSqLite();

		ownerName = dataDaoSqLite.findOwnerName();
		email = dataDaoSqLite.findEmail();
		password = dataDaoSqLite.findPWD();

		// Usuario
		EditTextPreference editUser = (EditTextPreference) findPreference("pref_edit_user");
		editUser.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		editUser.setText(ownerName.getContent());

		// Email
		EditTextPreference editEmail = (EditTextPreference) findPreference("pref_edit_email");
		editEmail.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
		editEmail.setText(email.getContent());

		// Senha
		EditTextPreference editSenha = (EditTextPreference) findPreference("pref_edit_password");
		editSenha.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
		int maxLength = 4;
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(maxLength);
		editSenha.getEditText().setFilters(FilterArray);
		editSenha.setText(password.getContent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.preference_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_preference_save) {
			if (concluir()) {
				finish();
				return true;
			} else {
				return false;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private Boolean concluir() {
		try {
			if (StringUtils.isNotEmpty(((EditTextPreference) findPreference("pref_edit_user")).getText().toString())) {
				ownerName.setContent(((EditTextPreference) findPreference("pref_edit_user")).getText().toString());
			}
			if (StringUtils.isNotEmpty(((EditTextPreference) findPreference("pref_edit_email")).getText().toString())) {
				email.setContent(((EditTextPreference) findPreference("pref_edit_email")).getText().toString());
			}
			if (StringUtils.isNotEmpty(((EditTextPreference) findPreference("pref_edit_password")).getText().toString())) {
				password.setContent(((EditTextPreference) findPreference("pref_edit_password")).getText().toString());
			}

			System.out.println("User: " + ownerName.getContent() + " - Email: " + email.getContent() + " - Senha: " + password.getContent());
			dataDaoSqLite.save(ownerName);
			dataDaoSqLite.save(email);
			dataDaoSqLite.save(password);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
