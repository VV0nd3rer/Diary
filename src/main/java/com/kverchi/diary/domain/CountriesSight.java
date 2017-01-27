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
@Table(name="Countries_sights")
public class CountriesSight implements Serializable {
	@Id
	@Column(name="sight_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int sight_id;
	@Column(name="sight_label")
	private String sight_label;
	@Column(name="country_code")
	private String country_code;
	@Column(name="img_url")
	private String img_url;
	@Column(name="description")
	private String sight_description;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="sight_id")
	Set<Post> sight_posts;
	
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
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
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
	public Set<Post> getSight_posts() {
		return sight_posts;
	}
	public void setSight_posts(Set<Post> sight_posts) {
		this.sight_posts = sight_posts;
	}
	/*@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		if(!(obj instanceof CountriesSight))
			return false;
		CountriesSight c_s = (CountriesSight)obj;
		return c_s.getSight_id()==this.getSight_id();
	}
	@Override
    public int hashCode() { 
      return Objects.hash(this.getSight_id());
    }*/
   
}
