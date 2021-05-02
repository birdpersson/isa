package pharmacy.service;

import pharmacy.model.auth.Authority;

import java.util.List;

public interface AuthorityService {

	List<Authority> findById(Long id);

	List<Authority> findByName(String name);

}
