package com.tpv.selenium.urtracker;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import pw.agiledev.e2e.ExcelHelper;
import pw.agiledev.e2e.exception.ExcelContentInvalidException;
import pw.agiledev.e2e.exception.ExcelParseException;
import pw.agiledev.e2e.exception.ExcelRegexpValidFailedException;

public class ExcelIssueParser {
	public static List<ExcelIssueRecord> parseIssueRecord(String file){

		List<ExcelIssueRecord> entities  = null;
		ExcelHelper eh = null;
		try {
			eh = ExcelHelper.readExcel(file);
		} catch (IOException | InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
		}

		try{
			entities = eh.toEntitys(ExcelIssueRecord.class);
	    }catch (ExcelParseException e) {
	        System.out.println(e.getMessage());
	    } catch (ExcelContentInvalidException e) {
	        System.out.println(e.getMessage());
	    } catch (ExcelRegexpValidFailedException e) {
	        System.out.println(e.getMessage());
	    }
	    return entities;
	}
}
