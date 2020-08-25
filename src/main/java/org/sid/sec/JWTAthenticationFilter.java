package org.sid.sec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sid.entities.AppUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAthenticationFilter extends UsernamePasswordAuthenticationFilter {
private AuthenticationManager authenticationManager;

	public JWTAthenticationFilter(AuthenticationManager authenticationManager) {
	this.authenticationManager = authenticationManager;
}

public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException
{
/*Si les données sont envoyées au format WWW-urlEnconder
 * String username=request.getParameter("username");
String password=request.getParameter("password");*/
	//Cas si les données sont envoyées au formats Json
	
	try {
		AppUser appUser= new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
		System.out.println(appUser);
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword()));

	} catch (JsonParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("appUser");
		throw new RuntimeException(e);
	} catch (JsonMappingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("appUser");
		throw new RuntimeException(e);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("appUser");
		throw new RuntimeException(e);
	}
}
protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		FilterChain chain,Authentication authResult) throws IOException, ServletException
{
	User springUser=(User)authResult.getPrincipal();
	
	List<String> roles= new ArrayList<>();
	authResult.getAuthorities().forEach(a->{
		roles.add(a.getAuthority());
	});
	String jwt=JWT.create()
			.withIssuer(request.getRequestURI())
			.withSubject(springUser.getUsername())
			.withArrayClaim("roles", roles.toArray(new String[roles.size()]))
			.withExpiresAt(new Date(System.currentTimeMillis()+SecurityParams.EXPIRATION))
			.sign(Algorithm.HMAC256(SecurityParams.SECRET));
	response.addHeader(SecurityParams.JWT_HEADER_NAME,jwt);
	super.successfulAuthentication(request, response, chain, authResult);
}
}
