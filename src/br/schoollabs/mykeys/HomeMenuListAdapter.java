package br.schoollabs.mykeys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.schoollabs.mykeys.model.Data;

public class HomeMenuListAdapter  extends ArrayAdapter<Data> {
	private Context context;

	public HomeMenuListAdapter(Context context) {
		super(context, R.layout.linear_home);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// View de renderiza��o de cada item
		View rowView = inflater.inflate(R.layout.linear_home, parent, false);

		// TextView para o titulo
		TextView textView = (TextView) rowView.findViewById(R.id.labelLinearHomeCategory);

		// Objeto clicado
		Data data = this.getItem(position);
		// Seta no TextView
		textView.setText(data.getContent());

		return rowView;
	}
}
