package clonegod.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping("/login")
	public Object login(HttpServletRequest request, HttpServletResponse response) {
		// if login success, set token on response header
		String token = JwtUtils.createToken("100"); 
		response.setHeader("jwt", token);
		return Math.random();
	}
	
	@GetMapping("/api/data")
	public Object data() {
		return "this is the data...";
	}
	
}
