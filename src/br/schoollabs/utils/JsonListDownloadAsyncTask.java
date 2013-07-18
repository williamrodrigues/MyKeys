package br.schoollabs.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/**
 * 
 * @author WilliamRodrigues
 * 
 */
public class JsonListDownloadAsyncTask<E> extends
		AsyncTask<String, Void, List<E>> {
	private ListActivity listActivity;
	private ArrayAdapter<E> adapter;
	private JsonParse<E> jsonParse;
	private ProgressDialog dialog;
	private String jsonListName;

	public JsonListDownloadAsyncTask(ListActivity listActivity,
			ArrayAdapter<E> adapter, JsonParse<E> jsonParse, String jsonListName) {
		super();
		this.listActivity = listActivity;
		this.adapter = adapter;
		this.jsonParse = jsonParse;
		this.jsonListName = jsonListName;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = ProgressDialog.show(listActivity, "Aguarde",
				"Baixando dados...");
	}

	@Override
	protected List<E> doInBackground(String... params) {
		String urlString = params[0];

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(urlString);

		try {
			HttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();

				String json = Utils.streamToString(instream);
				instream.close();

				List<E> models = getModels(json);

				return models;
			}
		} catch (Exception e) {
			dialog.hide();
			Toast.makeText(listActivity, "Nao foi possivel acessar o servidor",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return null;
	}

	private List<E> getModels(String jsonString) throws JSONException {

		List<E> models = new ArrayList<E>();

		JSONObject json = new JSONObject(jsonString);
		JSONArray jsonArray = json.getJSONArray(jsonListName);

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonItem = new JSONObject(jsonArray.getString(i));
			models.add(jsonParse.parse(jsonItem));
		}

		return models;
	}

	@Override
	protected void onPostExecute(List<E> result) {
		super.onPostExecute(result);
		dialog.dismiss();
		adapter.clear();
		for (E not : result) {
			adapter.add(not);
		}
		listActivity.setListAdapter(adapter);
	}

}
