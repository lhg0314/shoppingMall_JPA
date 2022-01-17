package com.example.demo.domain.Item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@DiscriminatorValue("A")
public class Album extends Item {

	private String artist;
	private String etc;
}
