package models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "findByNameAndPass", query = "select a from Account a where a.name = :name and password = :password")
public class Account {

	@Id
	@GeneratedValue
	private int id;

	@Column(nullable = false, length = 64, unique = true)
	private String name;

	@Column(nullable = false, length = 256, unique = true)
	private String email;

	@Column(nullable = false, length = 1024)
	private String password;

	public void setId(int id) { this.id = id; }
	public int getId() { return id; }

	public void setName(String name) { this.name = name; }
	public String getName() { return name; }

	public void setEmail(String email) { this.email = email; }
	public String getEmail() { return email; }

	public void setPassword(String password) { this.password = password; }
	public String getPassword() { return password; }
}
