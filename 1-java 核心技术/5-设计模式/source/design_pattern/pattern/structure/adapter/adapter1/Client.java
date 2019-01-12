package adapter1;

public class Client {

	public static void main(String[] args) {
		
		VIP vip = new VipCustomer();
		vip.showVip();
		
		vip = new VIPAdapter(new Customer());
		vip.showVip();
		
	}

}
