package com.fire.model;

import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.QueryDocumentSnapshot;

public class InboxModel {

	public List<InboxModelGeneric> lease;

	public InboxModel() {}
	
	public InboxModel(List<InboxModelGeneric> list) {
		this.lease = list;
	}

	public List<InboxModelGeneric> getLease() {
		return lease;
	}

	public void setLease(List<InboxModelGeneric> lease) {
		this.lease = lease;
	}

	@Override
	public String toString() {
		for(InboxModelGeneric inbox: lease) {
			System.out.println(inbox);
		}
		System.out.println("===================================");
		
		return "done";
	}
}
