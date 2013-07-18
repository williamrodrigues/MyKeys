package br.schoollabs.mykeys.model;

import java.io.Serializable;

public interface Model extends Serializable, Cloneable {
	public Long getId();

	public void setId(Long id);

	public Object clone() throws CloneNotSupportedException;

	public String asString();
}
