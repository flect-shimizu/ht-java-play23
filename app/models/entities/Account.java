package models.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@NamedQuery(name = "findByEmailAndPass", query = "select a from Account a where a.email = :email and password = :password")
public class Account {

	@Id
	@GeneratedValue
	private int id;

	@Column(nullable = false, length = 256, unique = true)
	private String email;

	@Column(nullable = false, length = 1024)
	private String password;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	@Column(nullable = false, length = 1024)
	private String image_url;

	public void setId(int id) { this.id = id; }
	public int getId() { return id; }

	public void setEmail(String email) { this.email = email; }
	public String getEmail() { return email; }

	public void setPassword(String password) { this.password = password; }
	public String getPassword() { return password; }

	public void setImage_url(String image_url) { this.image_url = image_url; }
	public String getImage_url() { return image_url; }

	public Date getTimestamp() { return timestamp; }
	public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
