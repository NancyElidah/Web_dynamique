package ETU001925.framework.utils;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import ETU001925.framework.Mapping;
import ETU001925.framework.annotation.Scope;
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
import java.sql.Date;
import java.util.Date;
import javax.servlet.http.Part;



public class Utilitaire {
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
    public static HashMap<String,Object> getClassSingleton(ArrayList<Class> allClass)throws Exception{
        HashMap<String,Object> mapping = new HashMap<String,Object>();
        for (Class c : allClass){
                if (c.isAnnotationPresent(Scope.class)){ 
                        mapping.put(c.getName(), null);
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
    public static String[] getNameofFields(Object obj){
       Field[] fields=obj.getClass().getDeclaredFields();
        String[] noms=new String[fields.length];
        for(int i=0 ; i<fields.length ; i++){
            noms[i]=fields[i].getName();
        }
        return noms;
    }
    public static Object getVerifyObject(ArrayList<String> names,Object objet)throws Exception{ 
        Object ob = null;
            String[] allFields = getNameofFields(objet);
            int length = names.size();
            int count = 0;
            for(String n:names){
                for (String s : allFields){
                    if (s.equals(n)){
                        count++;
                    }
                }
            }
            if (length==count) ob = objet;
        return ob;
    }
    public static void setOfClass(Object obj , String attribute , Object value)throws Exception{
            String s = attribute.substring(0,1).toUpperCase()+attribute.substring(1);
            String att = "set"+s;
            Method m =obj.getClass().getMethod( att, value.getClass());
            m.invoke(obj,value);
    }
    public static void reset(Object obj , Field f)throws Exception{
        if (f.getType().getName().equals("java.lang.Integer")) {
            Integer a = 0;
            setOfClass(obj,f.getName(),a);
        }
        else if (f.getType().getName().equals("java.lang.Double")){
            Double a = 0.0;
            setOfClass(obj,f.getName(),a);
        }
        else if (f.getType().getName().equals("java.util.Date")){
            java.util.Date a = new java.util.Date();
            setOfClass(obj,f.getName(),a);
        }
        else if (f.getType().getName().equals("java.sql.Date")){
            java.sql.Date a=new java.sql.Date(0);
            setOfClass(obj,f.getName(),a);
        }
        else {
            String a = "";
            setOfClass(obj,f.getName(),a);
        }
    }
    public static Object castingValue(Object objet,ArrayList<String> params,HttpServletRequest request)throws Exception{
            for (String oneP : params){
                String values = request.getParameter(oneP);
                Field field = objet.getClass().getDeclaredField(oneP);
                System.out.println(field.getType().getName()+""+field.getName());
                    reset(objet,field);
                    if(field.getType().getName().equals("java.lang.Integer")){
                        int number = Integer.valueOf(values);
                        setOfClass(objet,oneP,number);
                    }else if(field.getType().getName().equals("java.lang.Double")){
                        double decimal = Double.parseDouble(values);
                        setOfClass(objet,oneP,decimal);
                    }else if (field.getType().getName().equals("java.util.Date")){
                        java.util.Date date = new java.util.Date(values);
                        setOfClass(objet,oneP,date);
                    }else if (field.getType().getName().equals("java.sql.Date")){
                        java.sql.Date date = java.sql.Date.valueOf(values);
                        setOfClass(objet,oneP,date);
                    }else setOfClass(objet,oneP,values);
            }
        return objet;
    }
    public static Method getMethods(ArrayList<String>param , Method m)throws Exception{
        Parameter[] allParam = m.getParameters();
        int length_p = allParam.length;
        Method method = null ; 
        int count = 0 ; 
        for (String p : param){
            if (allParam.length!=0){
                for(Parameter params : allParam){
                    if (p.equals(params.getName())) count++;
                }
            }else{
                return m ;
            }

        }
        if(length_p == count) method = m ;
        return method ; 
    }
    public static Object[] allValue(Method m , HttpServletRequest request)throws Exception{
        Parameter[] allP = m.getParameters();
        for(Parameter pp : allP){
            System.out.println(pp.getName());
        }
        Object[] allO = new Object[allP.length];
            int value = 0 ; 
            for (Parameter p : allP){
                allO[value] = request.getParameter("id");
                System.out.println(allO[value]);
                value++;
            }
        
        return allO;
    }
    public static Object[] castingValues(Object[] allObject, Parameter[] param)throws Exception{
        Object[] finaly = new Object[allObject.length];
        int v = 0 ; 
        for (Parameter p : param){
            if (p.getType().getName().equals("java.lang.Integer")){
                finaly[v]=Integer.valueOf(allObject[v].toString());
            }else if (p.getType().getName().equals("java.lang.Double")){
                finaly[v]=Double.parseDouble(allObject[v].toString());
            }else if (p.getType().getName().equals("java.util.Date")){
                java.util.Date date = new java.util.Date(allObject[v].toString());
                finaly[v]= date;
            }else if (p.getType().getName().equals("java.sql.Date")){
                finaly[v]= java.sql.Date.valueOf(allObject[v].toString());
            }else{
                finaly[v]=allObject[v];
            }
            v++;
        }
        return finaly;
    }
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
    
    
}
