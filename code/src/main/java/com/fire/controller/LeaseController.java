package com.fire.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fire.FirestoreselfApplication;
import com.fire.model.InboxModel;
import com.fire.model.InboxModelGeneric;
import com.fire.model.LeaseInfoModel;
import com.fire.model.UserProfileModel;
import com.fire.service.AuthService;
import com.fire.service.FirestoreService;
import com.fire.service.LeaseService;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;

@Controller
public class LeaseController {

	@Autowired
	FirestoreService fireService;
	
	@Autowired
	AuthService authService;
	
	@Autowired
	LeaseService leaseService;
	
	@GetMapping("/lease")
	public String viewMyLease(Model model, HttpServletRequest request) throws InterruptedException, ExecutionException {
		String cookieVal = authService.getCookieValIfExists(FirestoreselfApplication.UserIdCookie, request);
		if(cookieVal!=null) {
			List<LeaseInfoModel> list = LeaseInfoModel.getLeaseList(fireService.getAllDocuments(FirestoreselfApplication.Lease));
			list = LeaseInfoModel.extractMyLeases(list, cookieVal);
			
			model.addAttribute("leaseList",list);
			
			if(list.isEmpty()) {
				model.addAttribute("emptyList",true);
			}
			
			return "yourLeases";
		}		
		
		return "redirect:/login";
	}
	
	@GetMapping("/lease/add")
	public String addLease(Model model, HttpServletRequest request) {
		if(authService.getCookieValIfExists(FirestoreselfApplication.UserIdCookie, request)!=null) {
			LeaseInfoModel lease = new LeaseInfoModel();
			model.addAttribute("lease",lease);
			
			return "addLease";
		}
		
		return "redirect:/login";		
	}
	
	@PostMapping("/lease/add")
	public String uploadLease(@ModelAttribute("lease") LeaseInfoModel lease, Model model, HttpServletRequest request) throws InterruptedException, ExecutionException {
		lease.setLeaseOwnerId(authService.getCookieValIfExists(FirestoreselfApplication.UserIdCookie, request));
		fireService.addObject((Object)lease, FirestoreselfApplication.Lease);
		
		return "redirect:/lease";
	}
	
	@GetMapping("/home")
	public String viewLease(Model model, HttpServletRequest request) throws InterruptedException, ExecutionException {
		String cookieVal = authService.getCookieValIfExists(FirestoreselfApplication.UserIdCookie, request);
		if(cookieVal!=null) {
			List<LeaseInfoModel> list = LeaseInfoModel.getLeaseList(fireService.getAllDocuments(FirestoreselfApplication.Lease));
			model.addAttribute("leaseList", LeaseInfoModel.declineMyLeases(list, cookieVal));
			
			return "home";
		}
		
		return "redirect:/login";
	}
	
	//redirect to same page pass paginate size, and document as params
	
	@GetMapping("/lease/view/{leaseId}")
	public String viewLeaseFull(@PathVariable String leaseId, Model model, HttpServletRequest request) throws InterruptedException, ExecutionException {
		String cookieVal = authService.getCookieValIfExists(FirestoreselfApplication.UserIdCookie, request);
		if(cookieVal!=null) {
			model.addAttribute("lease",new LeaseInfoModel(fireService.getDocument(FirestoreselfApplication.Lease, leaseId)));
			model.addAttribute("isWishlist", leaseService.isWishlist(leaseId, cookieVal, fireService));
			model.addAttribute("leaseId",leaseId);
			
			return "viewLease";
		}
		
		return "redirect:/login";
	}
	
	@GetMapping("/lease/wishlist/{type}/{leaseId}")
	public String addToWishlist(@PathVariable String leaseId, @PathVariable String type, Model model, HttpServletRequest request) throws InterruptedException, ExecutionException {
		leaseService.handleWishlist(authService.getCookieValIfExists(FirestoreselfApplication.UserIdCookie, request),
				type, fireService, leaseId);
		
		return "redirect:/lease/view/"+leaseId;
	}
	
	@GetMapping("/wishlist")
	public String viewWishlist(Model model, HttpServletRequest request) throws InterruptedException, ExecutionException {
		String cookieVal = authService.getCookieValIfExists(FirestoreselfApplication.UserIdCookie, request);
		if(cookieVal!=null) {
			List<LeaseInfoModel> wishlist = leaseService.getWishlist(fireService, cookieVal);
			model.addAttribute("leaseList", wishlist);
			if(wishlist==null) {
				model.addAttribute("emptyList",true);
			}
			
			return "wishlist";
		}
		
		return "redirect:/login";
	}
	
	@GetMapping("/inbox/{type}")
	public String viewInbox(@PathVariable String type, Model model, HttpServletRequest request) throws InterruptedException, ExecutionException {
		String cookieVal = authService.getCookieValIfExists(FirestoreselfApplication.UserIdCookie, request);
		if(cookieVal!=null) {
			if(type.equals("lro")) {
				List<LeaseInfoModel> myList = LeaseInfoModel.getLeaseList(fireService.getAllDocuments(FirestoreselfApplication.Lease));
				myList = LeaseInfoModel.extractMyLeases(myList, cookieVal);		
				
				List<InboxModelGeneric> list = InboxModelGeneric.createLRO(LeaseInfoModel.extractLeaseIds(myList), fireService.getAllDocuments(FirestoreselfApplication.LRO));
				model.addAttribute("leaseList",list);
				if(list.isEmpty()) {
					model.addAttribute("emptyList",true);
				}
				
			}
			else if(type.equals("lru")) {
				List<InboxModelGeneric> list = InboxModelGeneric.createLRU(fireService.getDocument(FirestoreselfApplication.LRU, cookieVal));
				model.addAttribute("leaseList",list);
				if(list.isEmpty()) {
					model.addAttribute("emptyList",true);
				}
			}
			model.addAttribute("type",type);
			
			return "inbox";
		}
		
		return "redirect:/login";
	}
	
	@GetMapping("/inbox/lease/lro")
	public String requestDecision(@RequestParam(required=true,name="req") String userId, @RequestParam(required=true,name="lease") String leaseId
			, Model model) throws InterruptedException, ExecutionException {
		model.addAttribute("profile", new UserProfileModel(fireService.getDocument(FirestoreselfApplication.UserProfile, userId)));
		model.addAttribute("userId", userId);
		model.addAttribute("leaseId", leaseId);
		
		return "decision";
	}
	
	@GetMapping("/inbox/lease/lro/{decision}")
	public String publishDecision(@RequestParam(required=true,name="req") String userId, @RequestParam(required=true,name="lease") String leaseId
			, @PathVariable String decision, Model model, HttpServletRequest request) throws InterruptedException, ExecutionException {
		String decisionCustom;
		String cookieVal = authService.getCookieValIfExists(FirestoreselfApplication.UserIdCookie, request);
		UserProfileModel profile = new UserProfileModel(fireService.getDocument(FirestoreselfApplication.UserProfile, cookieVal));
		
		List<InboxModelGeneric> list = InboxModelGeneric.createLRO(fireService.getDocument(FirestoreselfApplication.LRO, leaseId));
		for(InboxModelGeneric inbox: list) {
			if(inbox.getLeaseId().equals(leaseId) && inbox.getUserId().equals(userId)) {
				inbox.setStatus(decision.toUpperCase());
				if(decision.equals("accepted")) {
					decisionCustom = decision.toUpperCase()+" (contact: "+profile.getEmail()+" / "+profile.getPhone()+")";
					inbox.setStatus(decisionCustom);
				}
			}
		}
		
		DocumentSnapshot doc = fireService.getDocument(FirestoreselfApplication.LRU, userId);
		List<InboxModelGeneric> listLru = new ArrayList<>();
		if(doc.exists()) {
			listLru = InboxModelGeneric.createLRU(doc);
			boolean exists = false;
			for(InboxModelGeneric inbox: listLru) {
				if(inbox.getLeaseId().equals(leaseId)) {
					inbox.setStatus(decision.toUpperCase());
					
					if(decision.equals("accepted")) {
						decisionCustom = decision.toUpperCase()+" (contact: "+profile.getEmail()+" / "+profile.getPhone()+")";
						inbox.setStatus(decisionCustom);
					}
					exists = true;
					break;
				}
			}
			if(!exists) {
				
			}
		}

		fireService.addObject(new InboxModel(list), FirestoreselfApplication.LRO, leaseId);
		fireService.addObject(new InboxModel(listLru), FirestoreselfApplication.LRU, userId);
		
		return "redirect:/inbox/lro";
	}
	
	@GetMapping("/connect")
	public String connect(Model model, HttpServletRequest request,
			@RequestParam(required=true,name="lease") String leaseId, @RequestParam(required=true,name="apt") String aptName) throws InterruptedException, ExecutionException {
		String cookieVal = authService.getCookieValIfExists(FirestoreselfApplication.UserIdCookie, request);
		if(cookieVal!=null) {
			DocumentSnapshot doc = fireService.getDocument(FirestoreselfApplication.LRU, cookieVal);
			UserProfileModel profile = new UserProfileModel(fireService.getDocument(FirestoreselfApplication.UserProfile, cookieVal));
			InboxModelGeneric inboxObj = null;
			
			if(doc.exists()) {
				List<InboxModelGeneric> list = InboxModelGeneric.createLRU(doc);
				boolean exists = false;
				for(InboxModelGeneric inbox: list) {
					inbox.setUserId(cookieVal);
					inbox.setFullName((profile.getfName()+" "+profile.getlName()));
					if(inbox.getLeaseId().equals(leaseId)) {
						exists = true;
						break;
					}
				}
				if(!exists) {
					inboxObj = new InboxModelGeneric(leaseId, aptName, (profile.getfName()+" "+profile.getlName()), cookieVal, "PENDING");
					list.add(inboxObj);
				}				
				
				for(InboxModelGeneric inbox: list) {
					//System.out.println(inbox);
				}
				
				fireService.addObject((Object)new InboxModel(list), FirestoreselfApplication.LRU, cookieVal);
			}
			else { //new
				List<InboxModelGeneric> list = new ArrayList<>();
				list.add(new InboxModelGeneric(leaseId, aptName, (profile.getfName()+" "+profile.getlName()), cookieVal, "PENDING"));
				fireService.addObject((Object)new InboxModel(list), FirestoreselfApplication.LRU, cookieVal);
			}
			
			DocumentSnapshot lroDoc = fireService.getDocument(FirestoreselfApplication.LRO, leaseId);
			List<InboxModelGeneric> lroList = new ArrayList<>();
			if(lroDoc.exists()) {
				lroList = InboxModelGeneric.createLRO(lroDoc); 
				boolean exists = false;
				for(InboxModelGeneric gen: lroList) {
					if(gen.getUserId().equals(cookieVal)) {
						exists = true;
					}
				}
				if(!exists) {
					lroList.add(inboxObj);
				}
			}
			else {
				lroList.add(new InboxModelGeneric(leaseId, aptName, (profile.getfName()+" "+profile.getlName()), cookieVal, "PENDING"));
			}
			
			fireService.addObject((Object)new InboxModel(lroList), FirestoreselfApplication.LRO, leaseId);
		}
		
		return "redirect:/inbox/lru";
	}
}
