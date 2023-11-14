package com.espub;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.espub.dao.EssayDao;
import com.espub.dao.RoleDao;
import com.espub.dao.UserDao;
import com.espub.model.Essay;
import com.espub.model.Role;
import com.espub.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AuthenticationTest
{
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private EssayDao essayDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	public void expiredTokenRequestShouldReturnForbiden() throws Exception
	{
		User user = setupDatabase();
		String expiredToken = generateExpiredToken(user);
		
		ResultActions response = mockMvc.perform(patch("/essay/0")
				.header("Authorization", "Bearer " + expiredToken));
		
		response.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	public void badTokenRequestShouldReturnForbiden() throws Exception
	{
		ResultActions response = mockMvc.perform(get("/essay/edit/notExistingPage")
				.header("Authorization", "Bearer " + "bad.token"));
		
		response.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	private User setupDatabase()
	{
		String name = "admin";
		String password = "password";
		Role role = Role.builder()
				.name("ADMIN")
				.build();
		roleDao.save(role);
		List<Role> roleList = new ArrayList<>();
		roleList.add(role);
		User user = User.builder()
				.username(name)
				.password(passwordEncoder.encode(password))
				.role(roleList)
				.build();
		userDao.save(user);
		User anotherUser = User.builder()
				.username(name + "dfsiphuh")
				.build();
		userDao.save(anotherUser);
		Essay essay = Essay.builder()
				.user(anotherUser)
				.build();
		essayDao.save(essay);
		return user;
	}
	private String generateExpiredToken(User user)
	{
		return Jwts
				.builder()
				.claims()
					.add(new HashMap<>())
					.subject(user.getUsername())
					.issuedAt(new Date())
					.expiration(new Date(System.currentTimeMillis() - 100_000))
					.and()
				.signWith(
						Keys.hmacShaKeyFor
							(
								Decoders.BASE64.decode("7c8d4b5bb5f4efe8aab6aeb7c301baae27c9c8b05ca7946439b5e21eb2d12750")
							)
						)
				.compact();
	}
}
