//package com.Testing;
//
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.TimeZone;
//
//import org.json.JSONObject;
//
//import com.db.mongo.ga.GetTokenForVerifiedWebsites;
//import com.leadconverter.ga.GoogleAnalyticsReportingSample;
//
//public class Testing {
//
//	public static void main(String args[]) {
//		Integer value = 19000101;
//		SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
////		Date date = originalFormat.parse(value.toString());
//		
////		String campaignaddapiurlparameters = "subject=" + subject + "&fromfield=" + fromfield + "&message=" + body + "&textmessage=hii&footer=" + footer
////				+ "&status=draft&sendformat=html&template=&embargo=" + embargo
////				+ "&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
////	
////		
////		String campaignresponse = this.sendHttpPostData(campaignaddurl,
////				campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""),
////				response).replace("<pre>", "");
////
////		JSONObject campaignjson = new JSONObject(campaignresponse);
//		
//		Date d12=new Date();  
//        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
//       StringBuilder selday=new StringBuilder(simpleDateformat.format(d12));
//        System.out.println("selday = "+selday.toString());
//        String format1 = new SimpleDateFormat("HH:mm").format(d12);
//        System.out.println("format1 = "+format1);
//		String aa="SalesconvertURLTest / email";
//		 aa.replace("\\", "").replace("email",
//					"").replaceAll("/", "\\\\/");
//		 System.out.println("a = "+aa);
//		getweeknumber("2019-08-24 23:00:46");
//		String urlstr="http://prod.bizlem.io:8085/NewMailDev/getFileAttachServlet";
//		String ob=" {\"cc\":[],\"bcc\":[],\"attachments\":[],\"fromPass\":\"doctiger@123\",\"subject\":\"Testing12 Send Mail From MailTemlate\",\"to\":[\"tejal.jabade@bizlem.com\"],\"body\":\"<p>Hello  Tejal ,<\\/p>\\n\\n<p>How are you?<\\/p>\\n\\n <p><strong>This is test mail sent from DocTiger.<\\/strong><\\/p>\\n\\n <p><u>hiiiiiiiiii<\\/u<\\/p>\\n\\n <p> 1 <\\/p>\\n\\n <p>Thanks<\\/p>\\n\\n<p>&nbsp;<\\/p>\\n\",\"fromId\":\"doctigertest@gmail.com\",\"attachmentPath\":\"\"}\r\n" + 
//				"";
//		String oj="{\"cc\":[],\"bcc\":[],\"attachments\":[],\"fromPass\":\"doctiger@123\",\"subject\":\"LeadAutoConverter Mail Alert for Website Verification\",\"to\":[],\"body\":\"Your following websiteswww.bizlem.comwww.bizlem.comwww.bizlem.comare configured\",\"fromId\":\"doctigertest@gmail.com\",\"attachmentPath\":\"\"}";
//		JSONObject sendobj1 = new JSONObject(oj);
//		System.out.println("sendobj1; "+sendobj1);
////		GetTokenForVerifiedWebsites.callPostJSonModified(urlstr,sendobj1);
////		String accessToken="ya29.Gl1JByekbC_KOEQrT9Rh74P4fMlU08mH3_GOFdjJ9c_IsYKZGANJJnjZvs7hyel-97ByRUAQnzNWs2I9mcO5SHIpCcj7zExwPOHADmHkU8jrlvcsURtqX9w-pXdvF24";
////		String google_analytics_response = GoogleAnalyticsReportingSample
////				.getGAReport("input", accessToken, "2019-07-18", "", "").toString();
//	Date d=new Date();
//	System.out.println("date= "+d);
//	Date set_date = new Date();
//	System.out.println("set_date= "+set_date);
//		 String myString =  "2019-08-24 23:00:46";
//		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		    Date myDateTime = null;
//		    Date date=new Date();
//		    //Parse your string to SimpleDateFormat
//		    try
//		      {
//		        myDateTime = simpleDateFormat.parse(myString);
//		      }
//		    catch (ParseException e)
//		      {
//		         e.printStackTrace();
//		      }
//		    
//		    System.out.println("This is the Actual Date:"+myDateTime+"== date= "+date);
//		    Calendar cal = new GregorianCalendar();
//		    cal.setTime(myDateTime);
//
//		    //Adding 21 Hours to your Date
//		    cal.add(Calendar.HOUR_OF_DAY, 11);
//		    System.out.println("set_date= "+set_date);
//		    System.out.println("This is Hours Added Date:"+simpleDateFormat.format(cal.getTime()));
//		 // Conversion to ISO
//		    SimpleDateFormat sdf;
//		    sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//		    sdf.setTimeZone(TimeZone.getTimeZone("CET"));
//		    String text = sdf.format(cal.getTime());
//		    System.out.println("This is the Actual Date:"+myDateTime+"== date= "+text);
//			
//	
//	}
//	
//	public static void getweeknumber(String text) {
//		try {
//		Calendar calendar = Calendar.getInstance();
//		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date = formatter.parse(text);
//		calendar.setTime(date); 
//		System.out.println("Week number:" + 
//		calendar.get(Calendar.WEEK_OF_MONTH));
//		} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		}	
//		}
//}
