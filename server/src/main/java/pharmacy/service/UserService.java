package pharmacy.service;

import pharmacy.dto.UserDTO;
import pharmacy.model.auth.AdminRequest;
import pharmacy.model.entity.User;

import java.util.List;

public interface UserService {

	User findById(Long id);

	User findByUsername(String username);

	List<User> findAll();

	User create(UserDTO userDTO);

	User save(UserDTO userDTO);
	
	User save(User user);

	User enable(User user);

	String getWorkRoleByUsername(String username);

	User saveAdmin(AdminRequest userRequest);


}
