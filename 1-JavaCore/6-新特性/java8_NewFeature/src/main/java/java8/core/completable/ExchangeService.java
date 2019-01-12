package java8.core.completable;

public class ExchangeService {
	
	public static enum Money {
		EUR, USD
	}
	
	/**
	 * 汇率换算
	 * 
	 */
	public static double getRate(Money one, Money other) {
		return 0.9d;
	}
	
}
