package br.schoollabs.mykeys.dao;

import br.schoollabs.mykeys.model.Data;
import br.schoollabs.mykeys.model.Registry;

public interface RegistryDao extends Dao<Registry>{
	
	public Registry findByDataWithImage(Data data);
}
