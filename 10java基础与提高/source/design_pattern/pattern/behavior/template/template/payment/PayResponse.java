package template.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayResponse {
	
	String channel;
	
	String message;

	public PayResponse(String channel, String message) {
		super();
		this.channel = channel;
		this.message = message;
	}

	
}
