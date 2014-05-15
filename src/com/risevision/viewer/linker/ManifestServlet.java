package com.risevision.viewer.linker;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ManifestServlet  extends HttpServlet { 
    @Override 
    protected void service(HttpServletRequest req, HttpServletResponse resp) 
                    throws ServletException, IOException { 
		resp.setContentType("text/cache-manifest"); 
		RequestDispatcher disp = req.getRequestDispatcher("/viewer/offline.manifest"); 
		disp.forward(req, resp); 
    } 

}
