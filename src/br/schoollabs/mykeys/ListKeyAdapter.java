package br.schoollabs.mykeys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.mykeys.model.Registry;
import br.schoollabs.utils.RSA;

public class ListKeyAdapter extends ArrayAdapter<Data> {
	private Context context;

	public ListKeyAdapter(Context context) {
		super(context, R.layout.linear_list_key);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// View de renderização de cada item
		View rowView = inflater.inflate(R.layout.linear_list_key, parent, false);

		// TextViews da tela
		TextView textViewNomeApp = (TextView) rowView.findViewById(R.id.labelLinearHomeCategory);
		TextView textViewUser = (TextView) rowView.findViewById(R.id.labelListKeyUser);
		TextView textViewPassword = (TextView) rowView.findViewById(R.id.labelListKeyPassword);

		// Objeto clicado
		Data key = this.getItem(position);

		// Seta os valore nos TextView's
		textViewNomeApp.setText(key.getContent());

		for (Registry registry : key.getRegistries()) {
			if ("UserApp".equals(registry.getName())) {
				textViewUser.setText("Usuário: " + registry.getContent());
			} else if ("PasswordApp".equals(registry.getName())) {
				// Mostrando senha descriptografada
				textViewPassword.setText("Senha: " + RSA.decrypter(registry.getContent()));
			}
		}

		return rowView;
	}
}
