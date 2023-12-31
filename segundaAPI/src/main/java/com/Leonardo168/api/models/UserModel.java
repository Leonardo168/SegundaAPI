package com.Leonardo168.api.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_USER")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel implements UserDetails, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID userId;
	@Column(nullable = false, length = 70)
	private String name;
	@Column(nullable = false, unique = true, length = 20)
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private boolean enable;
	@ManyToMany
	@JoinTable(name = "TB_USERS_ROLES",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<RoleModel> roles;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(this.enable == true) {
			return this.roles;
		}
		return null;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return this.enable;
	}

}
