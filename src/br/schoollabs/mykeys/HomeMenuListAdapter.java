package br.schoollabs.mykeys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.schoollabs.mykeys.dao.sqlite.RegistryDaoSqLite;
import br.schoollabs.mykeys.model.Data;

public class HomeMenuListAdapter  extends ArrayAdapter<Data> {
	private Context context;
	private RegistryDaoSqLite registryDaoSqLite = new RegistryDaoSqLite();

	public HomeMenuListAdapter(Context context) {
		super(context, R.layout.linear_home);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// View de renderização de cada item
		View rowView = inflater.inflate(R.layout.linear_home, parent, false);

		// TextView para o titulo
		TextView textView = (TextView) rowView.findViewById(R.id.labelListKeyNomeApp);

		// Objeto clicado
		Data data = this.getItem(position);
		// Seta no TextView
		textView.setText(data.getContent());
		

		// Baixando imagem remota
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imageHomeLinear);
		imageView.setImageResource(Integer.parseInt(registryDaoSqLite.findByDataWithImage(data).getContent()));

		return rowView;
	}
}
