package decorator.order;

import util.SimpleUtil;

public class OrderFooter extends OrderDecorator {

	public OrderFooter(Order order) {
		super(order);
	}

	public void print() {
		super.order.print();
		this.printFooter();
	}
	
	public void printFooter() {
		System.out.println("==============================================");
		System.out.println("Total\t\t\t\t\t"
				+ SimpleUtil.formatCurrency(super.order.getGrandTotal()));
	}
	
}
