package ETU001925.framework.servlet;

import java.io.PrintWriter;
import ETU001925.framework.Mapping;
import ETU001925.framework.annotation.Url;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;
import ETU001925.framework.modelView.*;
import ETU001925.framework.utils.*;
import javax.servlet.ServletContext;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Collections;
import java.lang.reflect.Parameter;

public class FrontServlet extends HttpServlet {
    HashMap<String , Mapping> mappingUrls;

    public void init() throws ServletException{
        super.init();
        File packages = null;
        try{
            ClassLoader oneClass = Thread.currentThread().getContextClassLoader();
            if (oneClass == null) throw new Exception("class vide");
            else{
                ServletContext servletContext = getServletContext();
                InputStream in = servletContext.getResourceAsStream("/WEB-INF/web.xml");
                String path = Utilitaire.getValueConfig(in);
                URL fileUrl = oneClass.getResource(path);
                if (fileUrl==null) throw new Exception("url file vide");
                else{
                    packages = new File(fileUrl.getFile());
                    ArrayList<Class> allClass = Utilitaire.getAllClass(path,packages);
                   if (allClass.size()!=0 || allClass!=null){
                        mappingUrls = Utilitaire.completeHashMap(allClass);
                   }else{
                       throw new Exception("C'est null");
                   }
                }
            }
        } catch (Exception exe) {
            // throw new ServletException(exe);
        }
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {  
        PrintWriter out = response.getWriter();
        String url = request.getRequestURI();
        String[] urlSplit= url.split("/");
        int length = urlSplit.length -1;
        url = urlSplit[length];
        if(mappingUrls.containsKey(url)){
            out.println(url);
            try {
                Object obj = null;
                Class c = Class.forName(mappingUrls.get(url).getClassName());
                obj = c.getConstructor().newInstance();
                Method method =Utilitaire.getMethod(c, mappingUrls.get(url).getMethod());
                out.println(method.getName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                request.setAttribute(e.toString(),"erreur");
            }   
    }else{
        out.print("Diso");
        request.setAttribute("votre url n'existe pas","erreur");
    }
}
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        processRequest(request, response);
    }catch(Exception exe){}
}
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        processRequest(request, response);
    }catch(Exception exe){}
} 
}