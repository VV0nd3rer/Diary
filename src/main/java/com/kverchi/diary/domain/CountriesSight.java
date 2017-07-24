package com.kverchi.diary.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="countries_sights")
public class CountriesSight implements Serializable {
	@Id
	@Column(name="sight_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int sight_id;
	@Column(name="sight_label")
	private String sight_label;
	
	@ManyToOne
    @JoinColumn(name="country_code")
	private Country country;
	
	/*@Column(name="country_code")
	private String country_code;*/
	
	@Column(name="img_url")
	private String img_url;
	@Column(name="description")
	private String sight_description;
	@Column(name="map_coord_x")
	private float map_coord_x;
	@Column(name="map_coord_y")
	private float map_coord_y;
	
	/*@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="sight_id")
	Set<Post> sight_posts;*/
	
	public int getSight_id() {
		return sight_id;
	}
	public void setSight_id(int sight_id) {
		this.sight_id = sight_id;
	}
	public String getSight_label() {
		return sight_label;
	}
	public void setSight_label(String sight_label) {
		this.sight_label = sight_label;
	}
	
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}

	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getSight_description() {
		return sight_description;
	}
	public void setSight_description(String sight_description) {
		this.sight_description = sight_description;
	}

	public float getMap_coord_x() {
		return map_coord_x;
	}
	public void setMap_coord_x(float map_coord_x) {
		this.map_coord_x = map_coord_x;
	}
	public float getMap_coord_y() {
		return map_coord_y;
	}
	public void setMap_coord_y(float map_coord_y) {
		this.map_coord_y = map_coord_y;
	}
   
}
