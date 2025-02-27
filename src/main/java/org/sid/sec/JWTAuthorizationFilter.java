package org.sid.sec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTAuthorizationFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String jwt=request.getHeader(SecurityParams.JWT_HEADER_NAME);
		if(jwt==null || !jwt.startsWith(SecurityParams.HEADER_PREFIX)) {
			filterChain.doFilter(request, response);//on authentifie pas l'utilisateur on passe au prochain filtre qui va rejecter l'acces a l'application 
			return;
		}
		JWTVerifier verifier=JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
		DecodedJWT decodedJWT=verifier.verify(jwt.substring(SecurityParams.HEADER_PREFIX.length()));
		String username=decodedJWT.getSubject();
		List<String> roles=decodedJWT.getClaims().get("roles").asList(String.class);
		Collection<GrantedAuthority> authorities=new ArrayList<>();
		roles.forEach(rn->{
			authorities.add(new SimpleGrantedAuthority(rn));
		});
		UsernamePasswordAuthenticationToken user= new UsernamePasswordAuthenticationToken(username,null,authorities);
		SecurityContextHolder.getContext().setAuthentication(user);
		filterChain.doFilter(request, response);
	}


}
