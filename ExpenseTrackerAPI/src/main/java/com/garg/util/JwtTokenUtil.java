package com.garg.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/* As JWT structure has 3 parts - encode & decode generated jwt token on jwt official website
 * 	1. Header: Algorithm & Token Type 
 *  2. Payload: Data(username & password)
 *  3. Signature - hard to generate(secret key is used to sign first two parts) 
*/

@Component
public class JwtTokenUtil {

	private static final long JWT_TOKEN_VALIDITY= 5*60*60;
	
	@Value("${jwt.secret.key}")
	private String secretKey;
	
	public String generateJwtToken(UserDetails authenticatedUserDetails) {
		Map<String, Object> map=new HashMap<>();
		
		String jwtToken=Jwts.builder()
							.setClaims(map)
							.setSubject(authenticatedUserDetails.getUsername())
							.setIssuedAt(new Date(System.currentTimeMillis()) )
							.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000) )
							.signWith(SignatureAlgorithm.HS512, secretKey)
							.compact();		// to convert to String
	
		return jwtToken;
	}
	
	// JwtToken 2nd part structure contains data/username 
	public String getUsernameFromJwtToken(String jwtToken) {
		return getClaimFromJwtToken(jwtToken, Claims::getSubject);
	}
	
	// didn't get Java8 Functional Programming - Function passed as an method argument & return as a function
	private <T> T getClaimFromJwtToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims=Jwts.parser()
								.setSigningKey(secretKey)
								.parseClaimsJws(token)
								.getBody();
		
		return claimsResolver.apply(claims);
	}

	public boolean validateJwtToken(String jwtToken, UserDetails userDetails) {
		// TODO Auto-generated method stub
		
		final String username=getUsernameFromJwtToken(jwtToken);
	
		return username.equals(userDetails.getUsername()) && !isJwtTokenExpired(jwtToken);
	}

	private boolean isJwtTokenExpired(String jwtToken) {
		// TODO Auto-generated method stub
		
		final Date expirationDate=getExpirationDateFromJwtToken(jwtToken);
		
		return expirationDate.before(new Date());
	}

	private Date getExpirationDateFromJwtToken(String jwtToken) {
		// TODO Auto-generated method stub
		
		return getClaimFromJwtToken(jwtToken, Claims::getExpiration);
	}
	
}
