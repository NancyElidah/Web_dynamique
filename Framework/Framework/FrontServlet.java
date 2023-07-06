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
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Collections;
import java.lang.reflect.Parameter;
import javax.servlet.annotation.MultipartConfig;
import com.google.gson.Gson;
import ETU001925.framework.annotation.RestAPI;


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
        PrintWriter out = response.getWriter();
        String url = request.getRequestURI();
        String[] urlSplit= url.split("/");
        int length = urlSplit.length -1;
        url = urlSplit[length];
        if(mappingUrls.containsKey(url)){
            try {
                Object obj = null;
                Class c = Class.forName(mappingUrls.get(url).getClassName());
                if(classSingleton.containsKey(c.getName())){
                    obj = classSingleton.get(c.getName());
                }
                else obj = c.getConstructor().newInstance();
                Method method =Utilitaire.getMethod(c, mappingUrls.get(url).getMethod());
                Method method2 = Utilitaire.getRestApi(c);
                Enumeration<String> parameterNames = request.getParameterNames();
                ArrayList<String> list = Collections.list(parameterNames);
                Parameter[] params = method.getParameters();
                Object[] valueParam = Utilitaire.allValue(method,request);
                Object[] afterCast = Utilitaire.castingValues(valueParam,params);
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

                        if (m.getJson()==true){
                            try{
                                ArrayList<Object> allData = new ArrayList<Object>();
                                for (String cle : m.getData().keySet()) {
                                    allData.add(m.getData().get(cle));
                                }
                                Gson gson = new Gson();
                                String json = gson.toJson(allData);
                                response.getWriter().write(json);
                            }catch(Exception exe){
                                exe.printStackTrace();
                            }
                            
                        }else{
                            for (String cle : m.getData().keySet()){
                                request.setAttribute(cle,m.getData().get(cle));
                            }
                            RequestDispatcher dispat=request.getRequestDispatcher(m.getView());
                            dispat.forward(request,response);
                        }
                    }catch(Exception exe){
                        request.setAttribute(exe.getMessage(),"erreur");
                    }
                }else{
                    if(method2!=null){
                        if(method.getName().equals(method2.getName())){
                            try{
                                if(afterCast.length!=0){
                                    Object objet = method.invoke(obj,afterCast);
                                    Gson json = new Gson();
                                    String jsoString = json.toJson(objet);
                                    out.println(jsoString);
                                }else{
                                    Object objet = method.invoke(obj);
                                    Gson json = new Gson();
                                    String jsoString = json.toJson(objet);
                                    out.println(jsoString);
                                }
                            }catch(Exception exe){
                                exe.printStackTrace();
                            }
                        }
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