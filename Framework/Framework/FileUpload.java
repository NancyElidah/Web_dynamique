package ETU001925.framework.utils;
import java.nio.file.Files;
import java.nio.file.Path;
public class FileUpload{
    String name ;
    String path;
    byte[] byteFile;

    public void setName(String n)throws Exception{
        if (n==null || n.equals("")) throw new Exception("nom du fichier invalid");
        else this.name = n ;
    }
    public void setByteFile(byte[] b)throws Exception{
        if (b.length==0) throw new Exception("byte vide");
        else this.byteFile = b ;
    }
    public void setPath(String path){
        this.path = path;
    }
    public String getName(){
        return this.name;
    }
    public byte[] getByteFile(){
        return this.byteFile;
    }
    public String getPath(){
        return this.path;
    }
    public FileUpload(){}
    public FileUpload(String name ,String path, byte[] bytes)throws Exception{
        setName(name);
        setPath(path);
        setByteFile(bytes);
    }
    public void setByteFile()throws Exception{
        byte[] fileBytes = Files.readAllBytes(Path.of(this.getPath()+this.getName()));
        setByteFile(fileBytes);
    }
}