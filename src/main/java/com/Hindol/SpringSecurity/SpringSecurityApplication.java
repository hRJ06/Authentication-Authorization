package com.Hindol.SpringSecurity;

import com.Hindol.SpringSecurity.Configuration.CONSTANT;
import com.Hindol.SpringSecurity.Model.Role;
import com.Hindol.SpringSecurity.Model.User;
import com.Hindol.SpringSecurity.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringSecurityApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}
	public void run(String... args) {
		User Admin = this.userRepository.findByRole(Role.ADMIN);
		if(Admin != null) {
			return;
		}
		else {
			User user = new User();
			user.setEmail(CONSTANT.Email);
			user.setFirstName(CONSTANT.firstName);
			user.setLastName(CONSTANT.lastName);
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode(CONSTANT.Password));
			this.userRepository.save(user);
		}
	}
}
