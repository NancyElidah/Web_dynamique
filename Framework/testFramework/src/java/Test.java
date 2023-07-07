package test;
import ETU001925.framework.modelView.*;
import ETU001925.framework.annotation.Url;
import java.util.ArrayList;
import java.sql.Date;

public class Test{
    String name ;
    Date age ;
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setAge(Date name){
        this.age = name;
    }
    public Date getAge(){
        return this.age;
    }
    public Test(){}
    public Test(String a,Date ag){
        this.setName(a);
        this.setAge(ag);
    }
    @Url(url="hihi")
    public ModelView hihi()throws Exception{
        ModelView m = new ModelView();
        m.setView("jsp/test.jsp");
        return m;
    }
}