package com.fire.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;

public class InboxModelGeneric {

	public String leaseId;
	public String aptName;
	public String userName;
	public String userId;
	public String status;
	
	public InboxModelGeneric() {}
	
	public InboxModelGeneric(String leaseId, String aptName, String userName, String userId, String status) {
		super();
		this.leaseId = leaseId;
		this.aptName = aptName;
		this.userName = userName;
		this.userId = userId;
		this.status = status;
	}
	
	public InboxModelGeneric(String leaseId, String aptName, Map<String, String> request) {
		super();
		this.leaseId = leaseId;
		this.aptName = aptName;
		if(request!=null) {
			this.userName = request.get("userName");
			this.userId = request.get("userId");
			this.status = request.get("status");
		}
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLeaseId() {
		return leaseId;
	}
	public void setLeaseId(String leaseId) {
		this.leaseId = leaseId;
	}
	public String getAptName() {
		return aptName;
	}
	public void setAptName(String aptName) {
		this.aptName = aptName;
	}
	public String getUserName() {
		return userName;
	}
	public void setFullName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public static List<InboxModelGeneric> createLRO(Map<String, LeaseInfoModel> leaseIdMappings, List<QueryDocumentSnapshot> documents) {
		List<InboxModelGeneric> list = new ArrayList<>();
		for(QueryDocumentSnapshot document : documents) {
			if(leaseIdMappings.containsKey(document.getId())) {
				LeaseInfoModel lease = leaseIdMappings.get(document.getId());
				List<Map<String, String>> ll = (List<Map<String, String>>) document.get("lease");
				for(Map<String, String> request: ll) {
					//if(request!=null) {
						list.add(new InboxModelGeneric(document.getId(), lease.getAptName(), request));
					//}
				}
			}
		}
		
		return list;
	}
	
	public static List<InboxModelGeneric> createLRO(DocumentSnapshot document) {
		List<InboxModelGeneric> lroList = new ArrayList<>();
		if(document.exists()) {
			List<Map<String, String>> list = (List<Map<String, String>>) document.get("lease");
			for(Map<String, String> map: list) {
				lroList.add(new InboxModelGeneric(map.get("leaseId"), map.get("aptName"), map.get("userName"), map.get("userId"), map.get("status")));
			}
		}
		
		return lroList;
	}
	
	public static List<InboxModelGeneric> createLRU(DocumentSnapshot document) {
		List<InboxModelGeneric> lruList = new ArrayList<>();
		if(document.exists()) {
			List<Map<String, String>> list = (List<Map<String, String>>) document.get("lease");
			for(Map<String, String> map: list) {
				lruList.add(new InboxModelGeneric(map.get("leaseId"), map.get("aptName"), map.get("userName"), map.get("userId"), map.get("status")));
			}
		}		
		
		return lruList;
	}
	
	@Override
	public String toString() {
		return "InboxModelGeneric [leaseId=" + leaseId + ", aptName=" + aptName + ", userName=" + userName
				+ ", userId=" + userId + ", status=" + status + "]";
	}
	
	
}
