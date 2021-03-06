package decorator.order;

import util.SimpleUtil;

public class OrderItem {
	
	private String itemName;
	private int units;
	private double unitPrice;

	public void printLine() {
		System.out.println(
				itemName + "\t\t" + units
				+ "\t" + SimpleUtil.formatCurrency(unitPrice)
				+ "\t\t" + SimpleUtil.formatCurrency(getSubTotal()));
	}

	public double getSubTotal() {
		return units * unitPrice;
	}

	public String getItemName() {
		return itemName;
	}

	public OrderItem name(String itemName) {
		this.itemName = itemName;
		return this;
	}

	public int getUnits() {
		return units;
	}

	public OrderItem units(int units) {
		this.units = units;
		return this;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public OrderItem unitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
		return this;
	}
	
	
	

}
