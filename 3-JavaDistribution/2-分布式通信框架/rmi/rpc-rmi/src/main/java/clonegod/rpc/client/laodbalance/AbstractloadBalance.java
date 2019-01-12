package clonegod.rpc.client.laodbalance;

import java.util.List;

public abstract class AbstractloadBalance implements LoadBalance {

	@Override
	public String select(List<String> list) {
		if(list==null || list.isEmpty()) {
			return null;
		}
		if(list.size() == 1) {
			return list.get(0);
		}
		return doSelect(list);
	}

	protected abstract String doSelect(List<String> list);

}
