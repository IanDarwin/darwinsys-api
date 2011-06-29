package com.darwinsys.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/** 
 * A subset of a Cookbook Recipe object, for demos that need it
 */
@Entity
public class MiniRecipe {
	@Id
	int id;
	String title;
	@ManyToOne(fetch = FetchType.LAZY)
	//@NotNull
	@JoinColumn(name = "owner")
	MiniPerson owner;
	Date creationDate;
}
