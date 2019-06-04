package com.project.services;

import com.project.entities.User;
import com.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository usersRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = usersRepository.findByEmail(email);
		if(user==null)//user not found, wrong credentials
			throw new UsernameNotFoundException("That user doesn't exist in the system");
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

		grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session=attr.getRequest().getSession(true);
		session.setAttribute("user",user);

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				grantedAuthorities);
	}
}