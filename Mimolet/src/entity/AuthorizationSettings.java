package entity;

import java.io.Serializable;

public class AuthorizationSettings implements Serializable {
	private static final long serialVersionUID = 7565075647325306829L;
	
	private AuthorizationType authorizationType = AuthorizationType.UNCHECKED;
	private String email;
	private String password;
	private String facebookValidationID;
	private String googlePlusValdationID;
	public AuthorizationType getAuthorizationType() {
		return authorizationType;
	}
	public void setAuthorizationType(AuthorizationType authorizationType) {
		this.authorizationType = authorizationType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFacebookValidationID() {
		return facebookValidationID;
	}
	public void setFacebookValidationID(String facebookValidationID) {
		this.facebookValidationID = facebookValidationID;
	}
	public String getGooglePlusValdationID() {
		return googlePlusValdationID;
	}
	public void setGooglePlusValdationID(String googlePlusValdationID) {
		this.googlePlusValdationID = googlePlusValdationID;
	}
}
