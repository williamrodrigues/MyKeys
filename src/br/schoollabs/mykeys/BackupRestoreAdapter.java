package br.schoollabs.mykeys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.utils.DataSistema;

public class BackupRestoreAdapter extends ArrayAdapter<Data> {
	private Context context;

	public BackupRestoreAdapter(Context context) {
		super(context, R.layout.linear_backup);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// View de renderização de cada item
		View rowView = inflater.inflate(R.layout.linear_backup, parent, false);

		// TextView para o titulo
		TextView textView = (TextView) rowView.findViewById(R.id.labelSettings);

		// TextView para a data
		TextView textViewData = (TextView) rowView.findViewById(R.id.labelSettingsContent);

		// Objeto clicado
		Data data = this.getItem(position);
		// Seta no TextView do Titulo
		textView.setText(data.getContent());
		// Seta no TextView da Data
		textViewData.setText(context.getString(R.string.labelBackupDataCompl) + " " + DataSistema.parseDateString(data.getDate()));

		return rowView;
	}
}
