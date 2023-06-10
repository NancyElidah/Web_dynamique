package test;
import ETU001925.framework.modelView.*;
import ETU001925.framework.utils.FileUpload;
import ETU001925.framework.annotation.Url;
import java.util.ArrayList;
import java.sql.Date;
import ETU001925.framework.annotation.RestAPI;
import ETU001925.framework.annotation.Scope;

@Scope
public class Test{
    String name ;
    Date age ;
    int i =1;
    FileUpload file;
    public FileUpload getFile() {
        return file;
    }
    public void setFile(FileUpload file) {
        this.file = file;
    }
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
    @Url(url="haha")
    public ModelView haha()throws Exception{
        ModelView m = new ModelView();
        ArrayList<Test> all = new ArrayList<Test>();
        Date d = Date.valueOf("2004-02-21");
        Test t = new Test("Maimbo",d);
        Test a = new Test("Ranto",d);
        all.add(t);
        all.add(a);
        m.addItem("lst",all);
        m.setView("jsp/test.jsp");
        System.out.println(m.getView());
        System.out.println(this.getI());
        this.setI(i);
        return m ;
    }
    @Url(url = "hehe")
    @RestAPI
    public ArrayList<Test> hehe()throws Exception{
        ArrayList<Test> all = new ArrayList<Test>();
        Date d = Date.valueOf("2004-02-21");
        Test t = new Test("Maimbo",d);
        Test a = new Test("Ranto",d);
        all.add(t);
        all.add(a);
        System.out.println(this.getI());
        this.setI(i);
        return all;
    }
    @Url(url="huhu")
    public ModelView huhu()throws Exception{
        ModelView m = new ModelView();
        ArrayList<Test> all = new ArrayList<Test>();
        Date d = Date.valueOf("2004-02-21");
        Test t = new Test("Maimbo",d);
        Test a = new Test("Ranto",d);
        all.add(t);
        all.add(a);
        m.setJson(true);
        m.addItem("liste", all);
        System.out.println(this.getI());
        this.setI(i);
        return m ;
    }
    @Url(url="test")
    public void save()throws Exception{
        System.out.println("Nom : "+this.getName());
         System.out.println("Date : "+this.getAge());
    }
    @Url(url="emp")
    public ModelView test(Double id)throws Exception{
        ModelView m = new ModelView();
        ArrayList<Test> all = new ArrayList<Test>();
        Date d = Date.valueOf("2004-02-21");
        Test t = new Test("Maimbo",d);
        all.add(t);
        m.addItem("lst",all);
        m.setView("test.jsp");
        System.out.println(m.getView());
        return m ;
    }
    public void setI(int i){
        this.i = i +1 ;
        System.out.println(i);
    }
    public int getI(){
        return this.i; 
    }
    @Url(url = "hoho")
    public void hoho()throws Exception{
        System.out.println(this.getFile().getName());
        System.out.println(this.getFile().getPath());
    }
}