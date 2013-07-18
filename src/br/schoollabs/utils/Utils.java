package br.schoollabs.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Classe Utilitaria
 * 
 * @author WilliamRodrigues
 * 
 */
public class Utils {
	public static void startActivityOnClickButton(final Activity activity, int buttonId, final Class<? extends Activity> activityClass) {
		Button btn = (Button) activity.findViewById(buttonId);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				activity.startActivity(new Intent(activity, activityClass));
			}
		});
	}

	public static void startActivity(final Activity activity, final Class<? extends Activity> activityClass){
		Intent intent = new Intent(activity, activityClass);
		activity.startActivity(intent);
	}

	public static String getTextFromEditText(Activity activity, int inputTextId) {
		return ((EditText) activity.findViewById(inputTextId)).getText().toString();
	}

	public static void setTextEditText(Activity activity, int inputTextId, String text) {
		((EditText) activity.findViewById(inputTextId)).setText(text);
	}

	public static String streamToString(InputStream is) throws IOException {

		byte[] bytes = new byte[1024];
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		int lidos;
		while ((lidos = is.read(bytes)) > 0) {
			bout.write(bytes, 0, lidos);
		}
		return new String(bout.toByteArray());
	}

	public static boolean hasFroyo() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

}
