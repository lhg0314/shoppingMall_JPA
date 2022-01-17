package com.example.demo.domain.Item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@DiscriminatorValue("M")
public class Movie extends Item {

	private String actor;
	private String director;
}
