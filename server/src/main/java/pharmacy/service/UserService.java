package pharmacy.service;

import pharmacy.dto.PatientDTO;
import pharmacy.model.auth.AdminRequest;
import pharmacy.model.entity.User;

import java.util.List;

public interface UserService {

	User findById(Long id);

	User findByUsername(String username);

	List<User> findAll();

	User save(PatientDTO patientDTO);

	User enable(User user);

	String getWorkRoleByUsername(String username);

	User saveAdmin(AdminRequest userRequest);

}
