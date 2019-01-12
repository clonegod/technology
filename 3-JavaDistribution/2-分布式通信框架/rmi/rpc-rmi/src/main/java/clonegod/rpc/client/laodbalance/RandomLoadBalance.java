package clonegod.rpc.client.laodbalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLoadBalance extends AbstractloadBalance {

	@Override
	public String doSelect(List<String> list) {
		int rand = ThreadLocalRandom.current().nextInt(list.size());
		String address = list.get(rand);
		return address;
	}

}
