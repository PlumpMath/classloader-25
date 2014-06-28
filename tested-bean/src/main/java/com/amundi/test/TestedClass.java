package com.amundi.test;

import com.amundi.test.api.IBaseInterface;

public class TestedClass implements IBaseInterface {

	public String getVersion() {		
		return this.getClass().getCanonicalName() + " version 1.0";
	}

}
