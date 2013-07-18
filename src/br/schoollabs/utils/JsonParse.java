package br.schoollabs.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author WilliamRodrigues
 * 
 * @param <E>
 */
public interface JsonParse<E> {
	public E parse(JSONObject json) throws JSONException;
}
