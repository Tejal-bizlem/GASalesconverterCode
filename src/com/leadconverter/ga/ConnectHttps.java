package com.leadconverter.ga;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.api.AccesAndRefreshToken;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ResourceBundle;

public class ConnectHttps {
	final static Logger logger = Logger.getLogger(ConnectHttps.class);
  public static void main(String[] args)  {
	  
		try {
			JSONObject respose_json_object=getTokenByRefreshToken("1/xlZy_MAfdJ3G-Tg6KmPbJU8_zMT-5Z_qaVg2v98QCIPN8b0hqVCMDgXJezplU4M8");
			System.out.println("respose_json_object "+respose_json_object.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
		/**
     *  fix for
     *    Exception in thread "main" javax.net.ssl.SSLHandshakeException:
     *       sun.security.validator.ValidatorException:
     *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
     *               unable to find valid certification path to requested target
     
    TrustManager[] trustAllCerts = new TrustManager[] {
       new X509TrustManager() {
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
          }

          public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

          public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

       }
    };

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    // Create all-trusting host name verifier
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
    };
    // Install the all-trusting host verifier
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    
     * end of the fix
     

    URL url = new URL("https://securewebsite.com");
    URLConnection con = url.openConnection();
    Reader reader = new InputStreamReader(con.getInputStream());
    
    
    while (true) {
      int ch = reader.read();
      if (ch==-1) {
        break;
      }
      System.out.print((char)ch);
    }
  */}
  
  public static JSONObject getTokenByRefreshToken(String refresh_token)  {
	   	 JSONObject respose_json_object=null;
	   	 HttpURLConnection conn=null;
	 	 String response_code=null;
	 	 String res="10";
	 	logger.info("refresh_token=  "+refresh_token);
	    TrustManager[] trustAllCerts = new TrustManager[] {
	    	       new X509TrustManager() {
	    	          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	    	            return null;
	    	          }

	    	          public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

	    	          public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

	    	       }
	    	    };

	    	    
	    	    try {
	    	    	SSLContext sc = SSLContext.getInstance("SSL");
					sc.init(null, trustAllCerts, new java.security.SecureRandom());
					HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	    
	    	    // Create all-trusting host name verifier
	    	    HostnameVerifier allHostsValid = new HostnameVerifier() {
	    	        public boolean verify(String hostname, SSLSession session) {
	    	          return true;
	    	        }
	    	    };
	    	    // Install the all-trusting host verifier
	    	    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    	    /*
	    	     * end of the fix
	    	     */

	    	  
	   	       	try{
		   			String client_id=ResourceBundle.getBundle("config").getString("client_id");
		   			res=res+"client_id:: "+client_id;
		   			String client_secret=ResourceBundle.getBundle("config").getString("client_secret");
		   			//String redirect_uri="http://localhost:9999/GALeadConverter/googleanalytics.html";
		   			/*
			        String client_id=ResourceBundle.getBundle("config").getString("client_id");
		            String client_secret=ResourceBundle.getBundle("config").getString("client_secret");
		            String redirect_uri=ResourceBundle.getBundle("config").getString("redirect_uri");
	  			 	*/	
		   			res=res+"1";
	  			    String post_data_content = "grant_type=refresh_token&refresh_token="+refresh_token+"&client_id="+client_id+"&client_secret="+client_secret;      
	  			    String url_str=ResourceBundle.getBundle("config").getString("oauth2_token_url");
	  			  res=res+"2";
		   			URL url = new URL(url_str);
		   			logger.info("url_str = "+url_str);
		   			logger.info("post_data_content = "+post_data_content);
		   		        conn = (HttpURLConnection) url.openConnection();
		   		        conn.setRequestMethod("POST");
		   		        conn.setDoOutput(true);
		   		        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.0.5) Gecko/2008120122 Firefox/3.0.5");
//				   	System.out.println("conn.getResponseCode()= "+conn.getResponseCode());
		   		        conn.setRequestProperty("Content-Length", String.valueOf(post_data_content.length()));
				   		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				   		conn.setRequestProperty("Accept", "*/*");
				   		conn.setRequestMethod("POST");
				   		conn.setInstanceFollowRedirects(false); // very important line :)
				   		res=res+"3";
		   	        PrintWriter pw = new PrintWriter(new OutputStreamWriter(conn.getOutputStream()), true);
		   	     res=res+"4";
		   	            pw.print(post_data_content);
		   	            pw.flush();
		   	            pw.close();
		   		        
		   	            response_code=String.valueOf(conn.getResponseCode());
		   	         res=res+"5";
		   		        if (conn.getResponseCode() != 200) {
		   		        	res=res+"6";
		   		        	respose_json_object=new JSONObject();
		   		        	respose_json_object.put("response_code", response_code);
		   		        }else{
		   		        	res=res+"7";
		   		        	String newLine = System.getProperty("line.separator");
			   		        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			   		        StringBuilder result = new StringBuilder();
			   		                String line;
			   		                boolean flag = false;
			   		                while ((line = reader.readLine()) != null) {
			   		                    result.append(flag ? newLine : "").append(line);
			   		                   
			   		                    flag = true;
			   		                }
			   		             //respose_json_object
			   		             res=res+"result.toString()"+result.toString();
			   		          logger.info("result.toString = "+result.toString());
			   		        respose_json_object= (JSONObject) new JSONParser().parse(result.toString());
			   		        
			   		        respose_json_object.put("response_code", response_code);
			   		     logger.info("result.respose_json_object = "+respose_json_object);
		   		        }
		   		        conn.disconnect();
		   		       System.out.println("res== "+res);
		   		    }
		   		    catch (Exception e ) {
		   		            e.printStackTrace();
		   		    	respose_json_object=new JSONObject();
		   		     respose_json_object.put("res", res);
		   		     respose_json_object.put("response_exc", e.toString());
		   		        }finally {
		   		        	conn.disconnect();
						}
		       	return respose_json_object;
	   }
  
  //

}