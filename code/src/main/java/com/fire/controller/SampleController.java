package com.fire.controller;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fire.model.Sample;
import com.fire.service.SampleService;
import com.google.cloud.firestore.QueryDocumentSnapshot;

@RestController
public class SampleController {

	@Autowired
	SampleService serv;
	
	@GetMapping("/get")
	public List<Sample> get() throws InterruptedException, ExecutionException {
		return serv.getAllSample();
	}
	
	@PostMapping("/add")
	public String add() throws InterruptedException, ExecutionException {		
		for(int i=1;i<=20;i++) {
			serv.addSample((Object)new Sample("user_"+Integer.toString(i),"U","test"), "user_"+Integer.toString(i));
		}
		
		return "added";
	}
	
	@DeleteMapping("/delete/{docId}")
	public String delete(@PathVariable String docId) throws InterruptedException, ExecutionException {
		return serv.deleteSample(docId);
	}
}
