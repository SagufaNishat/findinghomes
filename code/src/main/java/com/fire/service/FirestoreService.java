package com.fire.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class FirestoreService {	
	
	Firestore dbf = FirestoreClient.getFirestore();
	
	public boolean addObject(Object obj, String collectionId, String docId) throws InterruptedException, ExecutionException {
		ApiFuture<WriteResult> write = dbf.collection(collectionId).document(docId).set(obj);
		
		while(!write.isDone());
		
		return write.isDone();
	}
	
	public boolean addObject(Object obj, String collectionId) throws InterruptedException, ExecutionException {
		String docId = dbf.collection(collectionId).document().getId();
		
		ApiFuture<WriteResult> write = dbf.collection(collectionId).document(docId).set(obj);
		while(!write.isDone());
		
		return write.isDone();
	}
	
	public List<QueryDocumentSnapshot> getAllDocuments(String collectionId) throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> collection = dbf.collection(collectionId).get();
		
		return collection.get().getDocuments();
	}
	
	public DocumentSnapshot getDocument(String collectionId, String docId) throws InterruptedException, ExecutionException {
		DocumentSnapshot document = dbf.collection(collectionId).document(docId).get().get();
		
		return document;
	}
	
	public void deleteDoc(String collectionId, String docId) {
		ApiFuture<WriteResult> delete = dbf.collection(collectionId).document(docId).delete();
		while(!delete.isDone());
	}
	
	/*public boolean ifDocExists(String collectionId, String docId) throws InterruptedException, ExecutionException {
		DocumentReference docRef = dbf.collection(collectionId).document(docId);
		DocumentSnapshot document = docRef.get().get();
		
		return document.exists();
	}*/
}
