package com.db.mongo.ga;

//Java program to find last index 
//of character x in given string. 
import java.io.*; 

public class GFG { 

//Returns last index of x if 
//it is present Else returns -1. 
public static int findLastIndex(String str, Character x) 
{ 
 int index = -1; 
 try {
	for (int i = 0; i < str.length(); i++) 
	     if (str.charAt(i) == x) 
	         index = i;
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} 
 return index; 
} 

public static String replaceLastChar(String str, Character x) {

	try {
		int index = findLastIndex(str, x); 
		 if (index == -1) {
		     System.out.println("Character not found"); 
		 }else {
		    
		     str = str.substring(0, index) + "@"
		    		 + str.substring(index+1,str.length());
		 }
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	return str;
}

//Driver code 
public static void main(String[] args) 
{ 
 // String in which char is to be found 
 String str = "viki_tripathi_gmail.com"; 

 // char whose index is to be found 
 Character x = '_'; 

 str= replaceLastChar(str,x);
 System.out.println("String is "+str); 
} 
} 