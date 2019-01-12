package decorator.order;

import util.SimpleUtil;

public class OrderFooterV2 extends OrderDecorator {

	public OrderFooterV2(Order order) {
		super(order);
	}

	public void print() {
		super.order.print();
		this.printFooter();
	}
	
	public void printFooter() {
		System.out.println("==============================================");
		System.out.println("总价\t\t\t\t\t"
				+ SimpleUtil.formatCurrency(super.order.getGrandTotal()));
	}
	
}
