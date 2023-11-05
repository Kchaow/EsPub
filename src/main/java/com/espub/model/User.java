package com.espub.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Component
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"user\"")
@PrimaryKeyJoinColumn(name="\"user\"")
@Builder
public class User implements UserDetails
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String password;
	private String salt;
	private String username;
	private String profileImage;
	private String status;
	private String description;
	@Getter(value = AccessLevel.NONE)
	@Builder.Default
	private boolean isAccountNonExpired = true;
	@Getter(value = AccessLevel.NONE)
	@Builder.Default
	private boolean isAccountNonLocked = true;
	@Getter(value = AccessLevel.NONE)
	@Builder.Default
	private boolean isCredentialsNonExpired = true;
	@Getter(value = AccessLevel.NONE)
	@Builder.Default
	private boolean isEnabled = true;
	@ManyToMany(fetch = FetchType.EAGER) //чзх
	private List<Role> role;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return role;
	}
	@Override
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}
	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}
	@Override
	public boolean isEnabled() {
		return isEnabled;
	}
}
