package com.Testing;

import org.json.JSONObject;

import com.db.mongo.ga.GetTokenForVerifiedWebsites;
import com.leadconverter.ga.GoogleAnalyticsReportingSample;

public class Testing {

	public static void main(String args[]) {
		String urlstr="http://prod.bizlem.io:8085/NewMailDev/getFileAttachServlet";
		String ob=" {\"cc\":[],\"bcc\":[],\"attachments\":[],\"fromPass\":\"doctiger@123\",\"subject\":\"Testing12 Send Mail From MailTemlate\",\"to\":[\"tejal.jabade@bizlem.com\"],\"body\":\"<p>Hello  Tejal ,<\\/p>\\n\\n<p>How are you?<\\/p>\\n\\n <p><strong>This is test mail sent from DocTiger.<\\/strong><\\/p>\\n\\n <p><u>hiiiiiiiiii<\\/u<\\/p>\\n\\n <p> 1 <\\/p>\\n\\n <p>Thanks<\\/p>\\n\\n<p>&nbsp;<\\/p>\\n\",\"fromId\":\"doctigertest@gmail.com\",\"attachmentPath\":\"\"}\r\n" + 
				"";
		String oj="{\"cc\":[],\"bcc\":[],\"attachments\":[],\"fromPass\":\"doctiger@123\",\"subject\":\"LeadAutoConverter Mail Alert for Website Verification\",\"to\":[],\"body\":\"Your following websiteswww.bizlem.comwww.bizlem.comwww.bizlem.comare configured\",\"fromId\":\"doctigertest@gmail.com\",\"attachmentPath\":\"\"}";
		JSONObject sendobj1 = new JSONObject(oj);
		System.out.println("sendobj1; "+sendobj1);
		GetTokenForVerifiedWebsites.callPostJSonModified(urlstr,sendobj1);
//		String accessToken="ya29.Gl1JByekbC_KOEQrT9Rh74P4fMlU08mH3_GOFdjJ9c_IsYKZGANJJnjZvs7hyel-97ByRUAQnzNWs2I9mcO5SHIpCcj7zExwPOHADmHkU8jrlvcsURtqX9w-pXdvF24";
//		String google_analytics_response = GoogleAnalyticsReportingSample
//				.getGAReport("input", accessToken, "2019-07-18", "", "").toString();
	
	
	}
}
