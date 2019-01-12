package part1_basic

class Money {
	private int amount
	private String currency
	
	Money(amountValue, currencyValue) {
		this.amount = amountValue
		this.currency = currencyValue
	}
	
	// 重写 == 操作符
	boolean equals(Object other) {
		if(null == other) return false
		if (! (other instanceof Money)) return false
		if(currency != other.currency) return false
		if(amount != other.amount) return false
		return true
	}
	
	int hashCode() {
		amount.hashCode() + currency.hashCode();
	}
	
	// 实现 + 操作符
	Money plus(Money other) {
		if (null == other) return null
		if (other.currency != this.currency) {
			throw new IllegalArgumentException("cannot add $other.currency to $currency");
		}
		return new Money(this.amount + other.amount, currency)
	}
	
	// 为Money的plus操作符进行了重载
	Money plus(Integer more) {
		return new Money(this.amount + more, currency)
	}
	
}
