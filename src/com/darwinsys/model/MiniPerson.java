package com.darwinsys.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/** 
 * A subset of a Person object, for demos that need it
 */
@Entity
public class MiniPerson {
	@Id
	int id;
	String username;
	String firstname;
	String lastname;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "owner")
	@OrderBy("creationDate DESC")
	Set<MiniRecipe> miniRecipes = new HashSet<MiniRecipe>();
}
