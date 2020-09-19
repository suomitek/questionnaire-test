package cn.csl.basics.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ReadTxt {
	public static List<String> read(String fileName){
		List<String> contents = new ArrayList<>();
		BufferedReader reader=null;  
        try{  
        	FileInputStream fis = new FileInputStream(fileName); 
        	InputStreamReader isr = new InputStreamReader(fis, "UTF-8");   
        	BufferedReader br = new BufferedReader(isr); 
        	String temp=null;  
        	String line = null;   
        	while ((line = br.readLine()) != null) {   
        		contents.add(line);
        	}   
        }  
        catch(Exception e){  
            e.printStackTrace();  
        }  
        finally{  
            if(reader!=null){  
                try{  
                    reader.close();  
                }  
                catch(Exception e){  
                    e.printStackTrace();  
                }
            }
        }
		return contents;  
	}
}
