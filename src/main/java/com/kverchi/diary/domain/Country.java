package com.kverchi.diary.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="countries")
public class Country {
	@Id
	@Column(name="country_code")
	private String country_code;
	@Column(name="country_name")
	private String country_name;
	@Column(name="img_path")
	private String img_path;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="country_code")
	private Set<CountriesSight> countriesSight;
	
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getCountry_name() {
		return country_name;
	}
	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
	public String getImg_path() {
		return img_path;
	}
	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}
	
	public Set<CountriesSight> getCountriesSight() {
		return countriesSight;
	}
	public void setCountriesSight(Set<CountriesSight> countriesSight) {
		this.countriesSight = countriesSight;
	}
	

}
