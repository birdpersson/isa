package pharmacy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pharmacy.dto.UserDTO;
import pharmacy.exception.ResourceConflictException;
import pharmacy.model.entity.User;
import pharmacy.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/{userId}")
	public User loadById(@PathVariable Long userId) {
		return this.userService.findById(userId);
	}

	@GetMapping("/all")
	public List<User> loadAll() {
		return this.userService.findAll();
	}

	@PostMapping("/create")
	@PreAuthorize("hasRole('SYSADMIN')")
	public ResponseEntity<User> addUser(@RequestBody UserDTO userDTO, UriComponentsBuilder ucBuilder) {

		User existUser = this.userService.findByUsername(userDTO.getUsername());
		if (existUser != null) {
			throw new ResourceConflictException(existUser.getId(), "Username already exists");
		}

		User user = new User();

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/user/{userId}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

}
