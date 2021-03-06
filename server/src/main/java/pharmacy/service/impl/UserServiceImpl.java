package pharmacy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pharmacy.dto.UserDTO;
import pharmacy.model.auth.AdminRequest;
import pharmacy.model.auth.Authority;
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

		u.setWork_role("PHADMIN");
		u.setEnabled(true); // trebalo bi prvo false zbog sifre		
//		u.addAuthority(authService.findByname("ROLE_USER"));
//		u.addAuthority(authService.findByname("ROLE_DERMATOLOG"));
//		u.addAuthority(authService.findByname("ROLE_FARMACOLOG"));
//		u.addAuthority(authService.findByname("ROLE_PHADMIN"));
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
	public User create(UserDTO userDTO) {
		User u = new User();
		u.setUsername(userDTO.getUsername());
		u.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		u.setFirstName(userDTO.getFirstname());
		u.setLastName(userDTO.getLastname());
		u.setEmail(userDTO.getEmail());
		u.setEnabled(true);
		u.setWork_role(userDTO.getRole());

		if (userDTO.getRole().equals("SYSADMIN"))
			u.setAuthorities(authService.findByName("ROLE_SYSADMIN"));
		else if (userDTO.getRole().equals("SUPPLIER"))
			u.setAuthorities(authService.findByName("ROLE_SUPPLIER"));
		else if (userDTO.getRole().equals("DERMATOLOGIST"))
			u.setAuthorities(authService.findByName("ROLE_DERMATOLOGIST"));

		u = this.userRepository.save(u);
		return u;
	}

	@Override
	public User save(UserDTO userDTO) {
		User u = new User();
		u.setUsername(userDTO.getUsername());
		u.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		u.setFirstName(userDTO.getFirstname());
		u.setLastName(userDTO.getLastname());
		u.setEmail(userDTO.getEmail());
		u.setAddress(userDTO.getAddress());
		u.setCity(userDTO.getCity());
		u.setCountry(userDTO.getCountry());
		u.setPhone(userDTO.getPhone());
		u.setEnabled(false);
		u.setWork_role("PATIENT");

		List<Authority> auth = authService.findByName("ROLE_PATIENT");
		u.setAuthorities(auth);

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

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		User u = new User();
		u.setUsername(user.getUsername());
		u.setPassword(passwordEncoder.encode(user.getPassword()));
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		u.setEmail(user.getEmail());
		u.setAddress(user.getAddress());
		u.setCity(user.getCity());
		u.setCountry(user.getCountry());
		u.setPhone(user.getPhone());
		u.setEnabled(false);
		u.setWork_role("PATIENT");

		List<Authority> auth = authService.findByName("ROLE_PATIENT");
		u.setAuthorities(auth);

		u = this.userRepository.save(u);
		return u;
	}


}
