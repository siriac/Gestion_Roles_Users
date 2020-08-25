package org.sid.sec;

import java.util.ArrayList;
import java.util.Collection;

import org.sid.entities.AppUser;
import org.sid.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service//ça veut dire cette classe doit etre instanciée au demarrage
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
private AccountService accountService;
	@Override
	/*
	 * ici on dit a spring security que si tu savoir si un utilisateur existe ou pas appele la methode loadUserByUsername
	 * maintenant c'est a nous de savoir ou est ce que on va aller recuperer l'utilisateur
	 * */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		System.out.println(username);
		AppUser appUser=accountService.loadUserByUsername(username);
		if(appUser==null) throw new UsernameNotFoundException("invalid user");
		Collection<GrantedAuthority> authorities=new ArrayList<>();
		appUser.getRoles().forEach(r->{
			authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
		});
		System.out.println(appUser.getPassword());
		return new User(appUser.getUsername(), appUser.getPassword(), authorities);
	}

}
