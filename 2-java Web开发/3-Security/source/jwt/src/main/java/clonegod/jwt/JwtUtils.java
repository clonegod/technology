package clonegod.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUtils {
	
	private static final String JWT_SECRET = "myjwtprivatekey";
	
	public static String createToken(String userId) {
		// 签发日期
		Date iatDate = new Date();
		
		// 过期时间
		LocalDateTime ldt = LocalDateTime.now();
		ldt = ldt.plusSeconds(5);
		Date expireDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		
		Map<String,Object> headerClaims = new HashMap<>();
		headerClaims.put("alg", "HS256");
		headerClaims.put("typ", "JWT");
		
		String token = JWT.create()
				.withHeader(headerClaims)
				.withClaim("userId", userId)
				.withExpiresAt(expireDate)
				.withIssuedAt(iatDate)
				.sign(Algorithm.HMAC256(JWT_SECRET));
		return token;
	}
	
	public static DecodedJWT verifyToken(String token) {
		DecodedJWT jwt = null;
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET)).build();
		try {
			jwt = verifier.verify(token);
		} catch(Exception e) {
			throw new RuntimeException("token失效，请重新登陆", e);
		}
		return jwt;
	}
	
}
