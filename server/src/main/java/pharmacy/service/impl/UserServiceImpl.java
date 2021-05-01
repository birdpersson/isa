package pharmacy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pharmacy.model.auth.AdminRequest;
import pharmacy.model.auth.UserRequest;
import pharmacy.model.entity.Pharmacy;
import pharmacy.model.entity.User;
import pharmacy.repository.UserRepository;
import pharmacy.service.AuthorityService;
import pharmacy.service.PharmacyService;
import pharmacy.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthorityService authService;

	@Autowired
	private PharmacyService pharmacyService;

	@Override
	public User findByUsername(String username) throws UsernameNotFoundException {
		User u = userRepository.findByUsername(username);
		return u;
	}

	public User findById(Long id) throws AccessDeniedException {
		User u = userRepository.findById(id).orElseGet(null);
		return u;
	}

	public List<User> findAll() throws AccessDeniedException {
		List<User> result = userRepository.findAll();
		return result;
	}

	@Override
	public User saveAdmin(AdminRequest userRequest) {
		User u = new User();
		u.setUsername(userRequest.getUsername());
		u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		u.setFirstName(userRequest.getFirstname());
		u.setLastName(userRequest.getLastname());

		u.setWork_role("Admin");
		u.setEnabled(true); // trebalo bi prvo false zbog sifre		
		u.addAuthority(authService.findByname("ROLE_USER"));
		u.addAuthority(authService.findByname("ROLE_DERMATOLOG"));
		u.addAuthority(authService.findByname("ROLE_FARMACOLOG"));
		u.addAuthority(authService.findByname("ROLE_ADMIN"));
		u = this.userRepository.save(u);
		if (userRequest.getPharmacyId() != null) {
			Pharmacy p = pharmacyService.getById(userRequest.getPharmacyId());
			p.getAdmins().add(u);
			this.pharmacyService.savePharmacy(p);
			u.setDedicated_pharmacy(p);
		} else {
			u.setDedicated_pharmacy(null);
		}
		return u;
	}

	@Override
	public User save(UserRequest userRequest) {
		User u = new User();
		u.setUsername(userRequest.getUsername());
		u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		u.setFirstName(userRequest.getFirstname());
		u.setLastName(userRequest.getLastname());
		u.setEnabled(false);
		u.setWork_role("Pacient");
		//TODO: expand user model (addr, city, token, role)

		u.addAuthority(authService.findByname("ROLE_USER"));

		u = this.userRepository.save(u);
		return u;
	}

	@Override
	public User enable(User user) {
		user.setEnabled(true);
		userRepository.save(user);
		return user;
	}

	@Override
	public String getWorkRoleByUsername(String username) {
		return this.userRepository.getWorkRoleByUsername(username);
	}

}
