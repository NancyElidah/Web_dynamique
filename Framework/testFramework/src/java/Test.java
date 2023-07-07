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
    @Url(url="haha")
    public ModelView haha()throws Exception{
        ModelView m = new ModelView();
        ArrayList<Test> all = new ArrayList<Test>();
        Date d = Date.valueOf("2004-02-21");
        Test t = new Test("Nivo",d);
        Test a = new Test("Ranto",d);
        all.add(t);
        all.add(a);
        m.addItem("lst",all);
        m.setView("jsp/test.jsp");
        System.out.println(m.getView());
        return m ;
    }
    @Url(url="test")
    public void save()throws Exception{
        System.out.println("Nom : "+this.getName());
         System.out.println("Date : "+this.getAge());
    }
}