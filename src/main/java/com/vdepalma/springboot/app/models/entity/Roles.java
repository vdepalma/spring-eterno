package com.vdepalma.springboot.app.models.entity;

public enum Roles {
	USER("USER"), ADMIN("ADMIN");

	private String rol;

	public String getRol() {
		return rol;
	}

	private Roles(String rol) {
		this.rol = rol;
	}

}
