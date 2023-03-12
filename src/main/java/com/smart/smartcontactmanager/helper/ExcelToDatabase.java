package com.smart.smartcontactmanager.helper;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.repository.UserRepository;

import net.bytebuddy.asm.Advice.This;

public class ExcelToDatabase {
	
	
	
	public static boolean isExcelFormat(MultipartFile file) 
	{
		String contentType = file.getContentType();
		if(contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) 
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public static List<Contact> importExcelFile(InputStream is,User user)
	{
		
		List<Contact> list=new ArrayList<>();
		try {
			XSSFWorkbook workbook=new XSSFWorkbook(is);
			XSSFSheet sheet = workbook.getSheet("Contacts");
			Iterator<Row> iterator = sheet.iterator();
			int rowNumber=0;
			while (iterator.hasNext()) {
				Row row = iterator.next();
				
				if(rowNumber==0) {
					rowNumber++;
					continue;
				}
				
				Iterator<Cell> cells = row.iterator();
				int cId=0;
				
				Contact contact=new Contact();
				contact.setUser(user);
				
				while(cells.hasNext()) 
				{
					Cell cell = cells.next();
					CellType cType = cell.getCellType();
					switch (cId){
					
					case 0:
						if(cType==CellType.STRING) {
							contact.setName(cell.getStringCellValue());
						}
						else {
							contact.setName(""+cell.getNumericCellValue());
						}
						
						break;
					case 1:
						if(cType==CellType.STRING) {
							contact.setNickName(cell.getStringCellValue());
							}
							else {
								contact.setNickName(""+cell.getNumericCellValue());
							}
						
						break;
					case 2:
						if(cType==CellType.STRING) {
							contact.setDescription(cell.getStringCellValue());
							}
							else {
								contact.setDescription(""+cell.getNumericCellValue());
							}
						
						break;
					case 3:
						if(cType==CellType.STRING) {
							contact.setEmail(cell.getStringCellValue());
							}
							else {
								contact.setEmail(""+cell.getNumericCellValue());
							}
						
						break;
					case 4:
						
						if(cType==CellType.STRING) {
						contact.setMob(cell.getStringCellValue());
						}
						else {
							contact.setMob(NumberToTextConverter.toText(cell.getNumericCellValue())); 
						}
						break;
					case 5:
						
						if(cType==CellType.STRING) {
							contact.setWork(cell.getStringCellValue());
						}
						else {
							contact.setWork(""+cell.getNumericCellValue());
						}
						
						break;
					
					default:
						break;
					}
					cId++;
				}
				contact.setImage("default.jpg");
				System.out.println(contact);
				list.add(contact);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
		
	}

}
