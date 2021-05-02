package pharmacy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pharmacy.dto.PatientDTO;
import pharmacy.exception.ResourceConflictException;
import pharmacy.model.auth.UserTokenState;
import pharmacy.model.entity.User;
import pharmacy.security.TokenUtils;
import pharmacy.security.auth.JwtAuthenticationRequest;
import pharmacy.service.EmailService;
import pharmacy.service.UserService;
import pharmacy.service.impl.CustomUserDetailsServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsServiceImpl userDetailsService;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@PostMapping("/login")
	public ResponseEntity<UserTokenState> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		User user = (User) authentication.getPrincipal();
		String jwt = tokenUtils.generateToken(user.getUsername(), user);
		int expiresIn = tokenUtils.getExpiredIn();

		return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
	}

	@PostMapping("/signup")
	public ResponseEntity<User> addUser(@RequestBody PatientDTO patientDTO, UriComponentsBuilder ucBuilder) {

		User existUser = this.userService.findByUsername(patientDTO.getUsername());
		if (existUser != null) {
			throw new ResourceConflictException(existUser.getId(), "Username already exists");
		}

		User user = this.userService.save(patientDTO);
		try {
			emailService.sendToken(user);
		} catch (MailException e) {
			e.printStackTrace();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/user/{userId}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@GetMapping("/verify")
	public ResponseEntity<User> verifyUser(@RequestParam("id") String id) {
		User existUser = userService.findById(Long.parseLong(id));
		if (existUser == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		userService.enable(existUser);
		return new ResponseEntity<>(existUser, HttpStatus.CREATED);
	}

	@PostMapping(value = "/refresh")
	public ResponseEntity<UserTokenState> refreshAuthenticationToken(HttpServletRequest request) {

		String token = tokenUtils.getToken(request);
		String username = this.tokenUtils.getUsernameFromToken(token);
		User user = (User) this.userDetailsService.loadUserByUsername(username);

		if (this.tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
			String refreshedToken = tokenUtils.refreshToken(token);
			int expiresIn = tokenUtils.getExpiredIn();

			return ResponseEntity.ok(new UserTokenState(refreshedToken, expiresIn));
		} else {
			UserTokenState userTokenState = new UserTokenState();
			return ResponseEntity.badRequest().body(userTokenState);
		}
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
//	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChanger passwordChanger) {
		userDetailsService.changePassword(passwordChanger.oldPassword, passwordChanger.newPassword);

		Map<String, String> result = new HashMap<>();
		result.put("result", "success");
		return ResponseEntity.accepted().body(result);
	}

	static class PasswordChanger {
		public String oldPassword;
		public String newPassword;
	}
}