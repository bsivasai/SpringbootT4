package com.example.t4.t41.Configuration;
import com.example.t4.t41.Services.MyUserDetailsService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;


@Component
public class JwtUtil {

    @Autowired
    MyUserDetailsService myUserDetailsService;

	private String secretKey="ieirhihskf";
	private Integer tokenValidity=1000*60*60*10;

	public String extractUsername(final String token) {
		Claims claims = parseClaims(token);
		String subject = (String) claims.get(Claims.SUBJECT);
        UserDetails userDetails=myUserDetailsService.loadUserByUsername(subject);
		return userDetails.getUsername();
	}

	public String generateToken(UserDetails user) {
		return Jwts.builder()
				.setSubject(user.getUsername())
				.claim("roles","")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}

	public boolean validateToken(final String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException ex) {
			System.out.println("JWT expired"+ ex.getMessage());
		} catch (IllegalArgumentException ex) {
			System.out.println("Token is null, empty or only whitespace"+ex.getMessage());
		} catch (MalformedJwtException ex) {
			System.out.println("JWT is invalid"+ex);
		} catch (UnsupportedJwtException ex) {
			System.out.println("JWT is not supported"+ ex);
		} catch (SignatureException ex) {
			System.out.println("Signature validation failed");
		}

		return false;
	}

	public Claims parseClaims(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();
	}
}
