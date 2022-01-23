package com.fire.model;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.firestore.QueryDocumentSnapshot;

public class Sample {

	private String document_id;
	private String name;
	private String profession;
	
	public Sample() {}
	
	public Sample(QueryDocumentSnapshot document) {
		this.document_id = document.getId();
		this.name = document.getString("name");
		this.profession = document.getString("profession");
	}
	
	public Sample(String document_id, String name, String profession) {
		super();
		this.document_id = document_id;
		this.name = name;
		this.profession = profession;
	}


	public String getDocument_id() {
		return document_id;
	}

	public void setDocument_id(String document_id) {
		this.document_id = document_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public static List<Sample> getSampleList(List<QueryDocumentSnapshot> documents) {
		List<Sample> list = new ArrayList<>();
		for(QueryDocumentSnapshot document : documents) {
			list.add(new Sample(document));
		}
		
		return list;
	}

	@Override
	public String toString() {
		return "Sample [document_id=" + document_id + ", name=" + name + ", profession=" + profession + "]";
	}
	
}