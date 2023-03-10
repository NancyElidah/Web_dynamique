package ETU001925.framework.utils;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import ETU001925.framework.Mapping;
import ETU001925.framework.annotation.Url;

import java.lang.reflect.Method;
import java.lang.Class;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import  java.lang.reflect.Parameter;
import java.net.http.HttpRequest;
import java.util.Date;
import java.util.Enumeration;


import javax.servlet.http.Part;




public class Utilitaire {
    public static Method getMethod(Class classe , String nomMethod){
        Method method = null ;
        Method[] allMethods = classe.getDeclaredMethods();
        for(Method m : allMethods){
            if (m.getName().equals(nomMethod)){
                method = m ;
            }
        }
        return method;
    }
    public static ArrayList<Class> getAllClass (String path,File packages) throws Exception {
        ArrayList<Class> all = new ArrayList<Class>();
        
        if (path.contains("/")) path = path.replace("/",".");
        if (packages.exists()){
            File[] files = packages.listFiles();
            if (files.length==0) throw new Exception("Package NULL");
            else{
                for (int i = 0 ; i < files.length ; i++) {
                    if (files[i].isDirectory()){
                        System.out.println(files[i].getName());
                        String route = path + files[i].getName()+".";
                        ArrayList<Class> otherClass = getAllClass(route,files[i]);
                        all.addAll(otherClass);
                    }else{
                        String file = files[i].toString().replace("\\","/");
                                if (file.endsWith(".class")){
                                    System.out.println(file);
                                    try {
                                        String f= file.substring(0,file.length()-6);
                                        System.out.println(file);
                                        f = getNameClasse(f);
                                        f = f.replace("/",".");
                                        f = path+"."+f;
                                        System.out.println(f);
                                        all.add(Class.forName(f));
                                    } catch (Exception e) {
                                        throw new Exception(e.getMessage());

                                    }
                                }

                    }
                }
            }
        }else throw new Exception("Package doesn't exist");
        return all;
    }
    public static String splitClass(String classe){
        String[] tab = classe.split(".class");
        return tab[0];
    }
    public static String getNameClasse(String classe){
        String[] tab = classe.split("/");
        return tab[tab.length-1];
    }
    public static HashMap<String,Mapping> completeHashMap(ArrayList<Class> allClass)throws Exception{
        HashMap<String,Mapping> mapping = new HashMap<String,Mapping>();
        for (Class c : allClass){
            Method[] allMethod = c.getDeclaredMethods();
            for (Method m : allMethod){
                if (m.isAnnotationPresent(Url.class)){ 
                    Url annotation = m.getAnnotation(Url.class);
                    if (annotation!=null){
                        Mapping map = new Mapping();
                        map.setClassName(c.getName());
                        System.out.println(c.getName());
                        map.setMethod(m.getName());
                        String urls = annotation.url();
                        mapping.put(urls, map);
                    }else{
                        throw new Exception("Annotation null");
                    }
                }
            }
        } 
        return mapping;
    }
    public static String getValueConfig(InputStream inputFile)throws Exception{
            String path = null;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputFile);
            inputFile.close();
            Element eElement = document.getDocumentElement();
            NodeList nodeList = eElement.getElementsByTagName("packages");
            path = nodeList.item(0).getTextContent();
        return path;
    }
}
