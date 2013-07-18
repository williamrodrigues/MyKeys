package br.schoollabs.utils;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * 
 * @author WilliamRodrigues
 * 
 */
public class JsonSingleDownloadAsyncTask<E> extends AsyncTask<String, Void, E> {
	private JsonSingleResult<E> jsonSingleResult;
	private JsonParse<E> jsonParse;
	private ProgressDialog dialog;
	private Activity detailActivity;

	public JsonSingleDownloadAsyncTask(Activity detailActivity, JsonSingleResult<E> jsonSingleResult, JsonParse<E> jsonParse) {
		super();
		this.detailActivity = detailActivity;
		this.jsonSingleResult = jsonSingleResult;
		this.jsonParse = jsonParse;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = ProgressDialog.show(detailActivity, "Aguarde", "Baixando dados...");
	}

	@Override
	protected E doInBackground(String... params) {
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

				return getModel(json);
			}
		} catch (Exception e) {
			throw new RuntimeException("Falha ao acessar Web service", e);
		}
		return null;
	}

	private E getModel(String jsonString) throws JSONException {

		JSONObject json = new JSONObject(jsonString);

		return jsonParse.parse(json);
	}

	@Override
	protected void onPostExecute(E model) {
		super.onPostExecute(model);
		dialog.dismiss();
		jsonSingleResult.onJsonResult(model);
	}

}
