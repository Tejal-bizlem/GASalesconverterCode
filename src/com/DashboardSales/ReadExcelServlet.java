package com.DashboardSales;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;



/**
 * Servlet implementation class ReadExcelServlet
 */
@WebServlet("/ReadExcelServlet")
public class ReadExcelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReadExcelServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at ReadExcelServlet : ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		PrintWriter out=response.getWriter();
		
		try {
			
			StringBuilder builder = new StringBuilder();
			BufferedReader bufferedReaderCampaign = request.getReader();
			String line;
			while ((line = bufferedReaderCampaign.readLine()) != null) {
			builder.append(line + "\n");
			}
			String base64excel=builder.toString();
			out.print(ReadExcel_OutCome.readexcel(base64excel));
			}catch (Exception e) {
				e.printStackTrace();
		}
			
		
		
	}

}
