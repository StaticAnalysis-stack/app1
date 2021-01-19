package com.example.medicinal.controller;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.medicinal.dao.MedicineDAO;
import com.example.medicinal.model.Medicine;
import com.example.medicinal.model.Validation;

@Controller
public class HomeController {
	@Autowired
	MedicineDAO med_dao;
	
	@RequestMapping("/")
	public String homePage()
	{
		return "index";
	}
	
	@RequestMapping("/add")
	public String addMed()
	{
		return "addmed";
	}
	
	@RequestMapping("/addmed")
	public String insertItem(@Valid @RequestParam("brandname") String brand_name,@RequestParam("compname") String comp_name, @RequestParam("expiry") String expiry,@RequestParam("batch_no") String batch_no,@RequestParam("Product") String product_type,@RequestParam("amount") double amount, Model model)
	{
		Medicine medicine=new Medicine(brand_name,comp_name,expiry,batch_no,product_type,amount);
		boolean valid=Validation.validDate(expiry);
		if(!valid)
		{
			model.addAttribute("Err", "Invalid Expiry Date");
			return "addmed";
		}
		
		int result=med_dao.insertMed(medicine);
		if(result!=0)
		{
		model.addAttribute("message", "Medicine Stored successfully");
		return "index";
	}
		else
		{
			model.addAttribute("Err", "Failed to insert Medicine");
			return "index";
		}
			
	}
	
	@RequestMapping("/view")
	public String viewMed(Model model)
	{
		List<Medicine> meds=med_dao.listMeds();
		model.addAttribute("meds",meds);
		return "viewmeds";
	}
	
	@RequestMapping("/viewexp")
	public String viewExpMed(Model model)
	{
		List<Medicine> meds=med_dao.listexpMeds();
		model.addAttribute("meds",meds);
		return "viewexpmeds";
	}
	
	@RequestMapping("/delete")
	public String delMed()
	{
		return "deletemed";
	}
	
	@RequestMapping("/deletemed")
	public String delete(@RequestParam("batch_no") String batch_no,Model model)
	{
		Medicine med=new Medicine();
		med.setBatch_no(batch_no);
		int result=med_dao.deleteMed(med);
		if(result!=0)
		{
			model.addAttribute("message", "Medicine Deleted Successfully!");
		return "index";
		}
		else
		{
			model.addAttribute("Err", "Failed to Delete!");
			return "index";
		}
	}
	
	@RequestMapping("/update")
	public String updateAmount()
	{
		return "updatemed";
	}
	
	@RequestMapping("/updateamount")
	public String update(@RequestParam("batch_no") String batch_no,Model model)
	{
		Medicine med=new Medicine();
		double amount=0;
		med.setBatch_no(batch_no);
		List<Medicine> medicine=med_dao.getMed(med);
		for(Iterator<Medicine> it=medicine.iterator();it.hasNext();)
		{
			Medicine m1=(Medicine) it.next();
			amount=m1.getAmount();
		}
		double up_amount=amount+0.1*amount;
		med.setAmount(up_amount);
		int result=med_dao.updateAmount(med);
		if(result!=0)
		{
			model.addAttribute("message", "Medicine Updated Successfully!");
		return "index";
		}
		else
		{
			model.addAttribute("Err", "Failed to Update!");
			return "index";
		}
	
	}
	public  ResponseEntity<Object> validdetails(@Valid @RequestBody Medicine med)
	{
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/batch_no").buildAndExpand(med.getBatch_no()).toUri();
		ResponseEntity<Object> res=ResponseEntity.created(location).build();
		return res;
	}
}
