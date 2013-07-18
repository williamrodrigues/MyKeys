package br.schoollabs.mykeys.model;

import java.io.Serializable;

/**
 * Classe base para modelos de negocio Implementa Mapeamento para Id utilizando a estrategia Identity Implementa os metodos toString, clone e equals.
 * 
 * @author WilliamRodrigues
 * 
 */
public abstract class ModelEntity implements Model, Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		final ModelEntity other = (ModelEntity) obj;
		if (getId() == null || other.getId() == null)
			return super.equals(obj);

		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	public String toString() {
		if (id != null)
			return id.toString() + ":" + getClass().getName();
		return super.toString();
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String asString() {
		return "";
	}
}
