package com.fire.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.fire.model.Sample;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;

@Service
public class SampleService {

	public List<Sample> getAllSample() throws InterruptedException, ExecutionException {
		Firestore dbf = FirestoreClient.getFirestore();
		CollectionReference collection = dbf.collection("crud_user");
		Query page = collection.limit(5);
		
		ApiFuture<QuerySnapshot> future = page.get();
		List<QueryDocumentSnapshot> documents = future.get().getDocuments();
		List<Sample> list = Sample.getSampleList(documents);
		
		//all docs
		ApiFuture<QuerySnapshot> all = dbf.collection("crud_user").get();
		// future.get() blocks on response
		List<QueryDocumentSnapshot> allDocuments = all.get().getDocuments();
		for(Sample smp: Sample.getSampleList(allDocuments)) {
			System.out.println(smp);
		}

		return list;
	}

	public String addSample(Sample sample) throws InterruptedException, ExecutionException {
		Firestore dbf = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> write = dbf.collection("crud_user").document(sample.getDocument_id()).set(sample);
			
		return write.get().getUpdateTime().toString();
	}
	
	public String addSample(Object sample, String docId) throws InterruptedException, ExecutionException {
		Firestore dbf = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> write = dbf.collection("crud_user").document(docId).set(sample);
			
		return write.get().getUpdateTime().toString();
	}

	public String deleteSample(String docId) throws InterruptedException, ExecutionException {
		Firestore dbf = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> delete = dbf.collection("crud_user").document(docId).delete();
		
		return delete.get().getUpdateTime().toString();
	}
}
