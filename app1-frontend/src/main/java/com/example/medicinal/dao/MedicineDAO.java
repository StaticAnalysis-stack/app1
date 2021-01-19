package com.example.medicinal.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.medicinal.model.Medicine;

@Repository
public class MedicineDAO {
	@Autowired
	JdbcTemplate jdbc;
	public int insertMed(Medicine med)
	{
		String insert="insert into Medicine values(default,'"+med.getBrand_name()+"','"+med.getCompany_name()+"','"+med.getExpiry()+"','"+med.getBatch_no()+"','"+med.getProduct_type()+"','"+med.getAmount()+"')";
		return jdbc.update(insert);
	}
	public int deleteMed(Medicine med)
	{
		String delete_item="delete from Medicine where batch_no=?";
		return jdbc.update(delete_item,med.getBatch_no());
	}
	public List<Medicine> listMeds()
	{
		String list_meds="select * from Medicine";
		List<Medicine> meds=jdbc.query(list_meds, (result,rowNum)->new Medicine(result.getInt("No"),result.getString("brand_name"),result.getString("company_name"),result.getString("expiry"),result.getString("batch_no"),result.getString("product_type"),result.getDouble("amount")));
		return meds;
		
	}
	public List<Medicine> listexpMeds()
	{
		String list_meds="select * from Medicine where DATEDIFF(expiry,CURDATE())<100";
		List<Medicine> meds=jdbc.query(list_meds, (result,rowNum)->new Medicine(result.getInt("No"),result.getString("brand_name"),result.getString("company_name"),result.getString("expiry"),result.getString("batch_no"),result.getString("product_type"),result.getDouble("amount")));
		return meds;
		
	}
	public List<Medicine> getMed(Medicine med)
	{
		String list_meds="select * from Medicine where batch_no='"+med.getBatch_no()+"'";
		List<Medicine> meds=jdbc.query(list_meds, (result,rowNum)->new Medicine(result.getInt("No"),result.getString("brand_name"),result.getString("company_name"),result.getString("expiry"),result.getString("batch_no"),result.getString("product_type"),result.getDouble("amount")));
		return meds;
		
	}
	public int updateAmount(Medicine med)
	{
		String update_amount="update Medicine set amount='"+med.getAmount()+"' where batch_no='"+med.getBatch_no()+"'";
		return jdbc.update(update_amount);
	}
	/*public List<Item> getItem(Medicine med)
	{
		String get_item="select * from Item where id='"+item.getId()+"'";
		List<Item> getitem=jdbc.query(get_item,new BeanPropertyRowMapper<>(Item.class));
		return getitem;
	}*/
}
