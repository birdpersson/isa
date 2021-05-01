package pharmacy.service;

import pharmacy.model.auth.AdminRequest;
import pharmacy.model.auth.UserRequest;
import pharmacy.model.entity.User;

import java.util.List;

public interface UserService {

	User findById(Long id);

	User findByUsername(String username);

	List<User> findAll();

	User save(UserRequest userRequest);

	User enable(User user);

	String getWorkRoleByUsername(String username);

	User saveAdmin(AdminRequest userRequest);
}
