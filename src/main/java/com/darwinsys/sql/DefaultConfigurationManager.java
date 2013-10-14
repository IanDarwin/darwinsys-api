package com.darwinsys.sql;

import java.sql.Connection;
import java.util.List;

import com.darwinsys.database.DataBaseException;

public class DefaultConfigurationManager implements ConfigurationManager {

	public List<Configuration> getConfigurations() {
		return ConnectionUtil.getConfigurations();
	}

	public Connection getConnection(Configuration config) {
		try {
			return ConnectionUtil.getConnection(config);
		} catch (Exception e) {
			throw new DataBaseException("Failed to get connection to " + config.getName(), e);
		}
	}

}
