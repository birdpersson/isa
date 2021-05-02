package pharmacy.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pharmacy.model.auth.Authority;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USERS")
public class User implements UserDetails {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username")
	private String username;

	@JsonIgnore
	@Column(name = "password")
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column
	private String address;

	@Column
	private String city;

	@Column
	private String country;

	@Column
	private String phone;

	@Column
	private String work_role;

	@Column(name = "enabled")
	private boolean enabled;

	@Column(name = "last_password_reset_date")
	private Timestamp lastPasswordResetDate;

	@Column(columnDefinition = "boolean default true")
	private boolean firstTimeLogin = true;

	@ManyToMany
	private List<BusinessHours> working_hours = new ArrayList<>();

	@OneToMany
	private List<Prescription> prescriptions = new ArrayList<>();
	@ManyToMany
	@JoinTable(name = "users_work_in_pharmacies",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "pharmacy_id", referencedColumnName = "id"))
	private List<Pharmacy> pharmacy = new ArrayList<Pharmacy>();

	@JsonIgnore
	@ManyToOne
	private Pharmacy dedicated_pharmacy;

	public User() {
	}

	public List<Pharmacy> getPharmacy() {
		return pharmacy;
	}

	public void setPharmacy(List<Pharmacy> pharmacy) {
		this.pharmacy = pharmacy;
	}


	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_authority",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private List<Authority> authorities = new ArrayList<>();

	public void addAuthority(Authority auth) {
		authorities.add(auth);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		Timestamp now = new Timestamp(new Date().getTime());
		this.setLastPasswordResetDate(now);
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Timestamp getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public String getWork_role() {
		return work_role;
	}

	public void setWork_role(String works_role) {
		this.work_role = works_role;
	}

	public List<BusinessHours> getWorking_hours() {
		return working_hours;
	}

	public void setWorking_hours(List<BusinessHours> working_hours) {
		this.working_hours = working_hours;
	}

	public Pharmacy getDedicated_pharmacy() {
		return dedicated_pharmacy;
	}

	public void setDedicated_pharmacy(Pharmacy dedicated_pharmacy) {
		this.dedicated_pharmacy = dedicated_pharmacy;
	}

	public List<Prescription> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(List<Prescription> prescriptions) {
		this.prescriptions = prescriptions;
	}

	public boolean isFirstTimeLogin() {
		return firstTimeLogin;
	}

	public void setFirstTimeLogin(boolean firstTimeLogin) {
		this.firstTimeLogin = firstTimeLogin;
	}

}
