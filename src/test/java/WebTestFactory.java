package test.java;

import org.testng.annotations.Factory;

public class WebTestFactory {
	  /*
	  @Factory
	  public static Object[] createInstances() {
	   Object[] result = new Object[10]; 
	   for (int i = 0; i < 10; i++) {
	      result[i] = new TestTest(i * 10);
	    }
	    return result;
	  }
	 */

	@Factory
	public static Object[] createInstances1() {
	 Object[] result = new Object[10]; 
	 for (int i = 0; i < 10; i++) {
	    result[i] = new TestTest2(i * 10);
	  }
	  return result;
	}
}


