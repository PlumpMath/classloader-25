package com.amundi.test;

import java.util.ResourceBundle;

import com.amundi.test.api.IBaseInterface;

public class TestedClass implements IBaseInterface {

	public String getVersion() {
		return this.getClass().getCanonicalName() + " version 2.0";
	}

	public String getVersionFromProperty() {
		return ResourceBundle.getBundle("test").getString("version");
	}

}
