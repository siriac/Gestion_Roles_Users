package org.sid.service;

import org.sid.dao.AppRoleRepository;
import org.sid.dao.AppUserRepository;
import org.sid.entities.AppRole;
import org.sid.entities.AppUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service//pouur dire que c'est ma couche service
@Transactional//ça veut dire toutes mes methodes sont transactionnelles
public class AccountServiceImpl implements AccountService{
	private AppUserRepository appUserRepository;
	private AppRoleRepository appRoleRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
 public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
	// TODO Auto-generated constructor stub
	 this.appUserRepository=appUserRepository;
	 this.appRoleRepository=appRepository;
	 this.bCryptPasswordEncoder=bCryptPasswordEncoder;
}
	@Override
	public AppUser saveUser(String username, String password, String confirmedPassword) {
		AppUser user=appUserRepository.findByUsername(username);
		if(user!=null) throw new RuntimeException("User already exist");
		if(!password.equals(confirmedPassword))throw new RuntimeException("Please confirm you password");
		AppUser appUser= new AppUser();
		appUser.setUsername(username);
		appUser.setActived(true);
		appUser.setPassword(bCryptPasswordEncoder.encode(password));
		appUserRepository.save(appUser);
		addRoleToUser(username, "USER");
		return appUser;
	}

	@Override
	public AppRole save(AppRole role) {
		// TODO Auto-generated method stub
		return appRoleRepository.save(role);
	}

	@Override
	public AppUser loadUserByUsername(String username) {
		// TODO Auto-generated method stub
		return appUserRepository.findByUsername(username);
	}

	@Override
	public void addRoleToUser(String username, String rolename) {
		// TODO Auto-generated method stub
		AppUser appUser=appUserRepository.findByUsername(username);
		AppRole appRole=appRoleRepository.findByRoleName(rolename);
		appUser.getRoles().add(appRole);
	}

}
