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
import javax.servlet.annotation.MultipartConfig;
import com.google.gson.Gson;
import ETU001925.framework.annotation.RestAPI;
import javax.servlet.http.Part;

@MultipartConfig
public class FrontServlet extends HttpServlet {
    HashMap<String , Mapping> mappingUrls;
    HashMap<String , Object> classSingleton;

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
                        classSingleton = Utilitaire.getClassSingleton(allClass);
                        for(Class a : allClass){
                            if(classSingleton.containsKey(a.getName())){
                                classSingleton.replace(a.getName(), a.getConstructor().newInstance());
                            }
                        }
                   }else{
                       throw new Exception("C'est null");
                   }
                }
            }
        } catch (Exception exe) {
            // throw new ServletException(exe.getMessage());
        }
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {  
        String header=request.getHeader("multipart/form-data");
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
                if(classSingleton.containsKey(c.getName())){
                    obj = classSingleton.get(c.getName());
                }
                else obj = c.getConstructor().newInstance();
                Method method =Utilitaire.getMethod(c, mappingUrls.get(url).getMethod());
                Enumeration<String> parameterNames = request.getParameterNames();
                ArrayList<String> list = Collections.list(parameterNames);
                Parameter[] params = method.getParameters();
                Object[] valueParam = Utilitaire.allValue(method,request);
                Object[] afterCast = Utilitaire.castingValues(valueParam,params);
                String destination = getInitParameter("destinationFile");
                out.print(destination);
                Field[] field = c.getDeclaredFields();
                try{
                    String contentType = request.getContentType();
                    out.println(contentType);
                    if(contentType != null && contentType.startsWith("multipart/form-data")){
                    for(Part part:request.getParts()){
                        out.println(part);
                        for(Field f : field){
                                if(f.getName().equals(part.getName()) && f.getType().equals(ETU001925.framework.utils.FileUpload.class)){
                                    String fileName = part.getSubmittedFileName();
                                    InputStream fileInputStream = part.getInputStream();
                                    byte[] fileBytes = fileInputStream.readAllBytes();
                                    File fileUpload = new File(destination, fileName);
                                    fileInputStream.close();
                                    // OutputStream outputStream = new FileOutputStream(fileUpload);
                                    ETU001925.framework.utils.FileUpload file = new ETU001925.framework.utils.FileUpload(fileName,fileUpload.getName(),fileBytes);
                                    out.println(file.getName());
                                    out.println(file.getByteFile());
                                    out.println(file.getPath());
                                    Utilitaire.setOfClass(obj, f.getName(), file);
                                }

                            }
                        }
                    }
                }catch(Exception exe){
                    exe.printStackTrace();
                }
                if (parameterNames.hasMoreElements() && !method.getReturnType().equals(ETU001925.framework.modelView.ModelView.class)){
                        Object ob = Utilitaire.getVerifyObject(list,obj);
                        if (ob!=null){
                            Object finaly = Utilitaire.castingValue(ob,list,request);
                            method.invoke(finaly);
                        }
                }
                else if (method.getReturnType().equals(ETU001925.framework.modelView.ModelView.class)){
                    try {
                        ETU001925.framework.modelView.ModelView m = new ETU001925.framework.modelView.ModelView();
                        if (afterCast.length !=0){
                            m =(ETU001925.framework.modelView.ModelView)method.invoke(obj,afterCast);
                        }else{
                            try{
                                m = ( ETU001925.framework.modelView.ModelView )method.invoke(obj);
                            }catch(Exception exception){
                                exception.printStackTrace();
                            }
                            
                        }
                            for (String cle : m.getData().keySet()){
                                request.setAttribute(cle,m.getData().get(cle));
                            }
                            RequestDispatcher dispat=request.getRequestDispatcher(m.getView());
                            dispat.forward(request,response);
                    }catch(Exception exe){
                        request.setAttribute(exe.getMessage(),"erreur");
                    }
                }
            } catch (ClassNotFoundException e) {
                request.setAttribute(e.getMessage(),"erreur");
            }
        }else{
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