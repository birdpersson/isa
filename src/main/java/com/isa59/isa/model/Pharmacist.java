package com.isa59.isa.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Pharmacist extends User{

		@ManyToOne
		private Pharmacy pharmacy;
	
}
