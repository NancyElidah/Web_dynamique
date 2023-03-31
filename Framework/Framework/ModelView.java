/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ETU001925.framework.modelView;
import java.util.HashMap;
/**
 *
 * @author ITU
 */
public class ModelView {
    String view ;
    public String getView() {
        return this.view;
    }
    public void setView(String view) throws Exception{
        if (view.endsWith(".jsp")){
            this.view = view;
        }else{
            throw new Exception("Le fichier doit etre un fichier jsp");
        }
    }
    public ModelView(){
    }
    public ModelView(String b)throws Exception{
        this.setView(b);
    }
}
