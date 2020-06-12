package tests;

import org.testng.annotations.Factory;

public class TestFactoryRun {

	@Factory
	public Object[] factoryMethod() {
		return new Object[] { new TestClass(), new TestClass() };
	}

}
