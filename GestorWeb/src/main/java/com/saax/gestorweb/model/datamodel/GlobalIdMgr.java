/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.model.datamodel;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Singleton class para os IDs global
 * @author rodrigo
 */
public class GlobalIdMgr {
    
    private final Map<String, String> mapaClasseID;
    //private final Map<String, Class> mapaIDClasse;
    
// singleton instance
    private static GlobalIdMgr instance;

    private GlobalIdMgr() {
        mapaClasseID = new HashMap<>();
      //  mapaIDClasse = new HashMap<>();

        mapaClasseID.put(Task.class.getCanonicalName(), "TSK");

        mapaClasseID.put(Meta.class.getCanonicalName(), "TGT");
        //mapaIDClasse.put("T",Task.class);
                
        mapaClasseID.put(Usuario.class.getCanonicalName(), "USR");
        //mapaIDClasse.put("U",Usuario.class);
        
        mapaClasseID.put(FilialEmpresa.class.getCanonicalName(), "F");
       // mapaIDClasse.put("F",FilialEmpresa.class);

    }

    public static GlobalIdMgr instance() {
        if (instance==null){
            instance = new GlobalIdMgr();
        }
        return instance;
    }
    
    
    
    
    /**
     * Formata um código ID para exibição ao usuário
     * @param id
     * @param type
     * @return código formatado
     */
    public String getID(Integer id, Class type){

        DecimalFormat decimalFormatterID = new DecimalFormat("0000");
    
        String identificador = mapaClasseID.get(type.getCanonicalName());
        
        StringBuilder idFormatado = new StringBuilder();
        idFormatado.append(identificador);
        idFormatado.append(decimalFormatterID.format(id == null ? 0 : id));
        
        return idFormatado.toString();
    }
    
    /**
     * obtém um objeto dado seu id global
     * @param globalID
     * @return Objeto
     */
    /**public Object getEntityBean(String globalID){

        DecimalFormat decimalFormatterID = new DecimalFormat("0000");
        
        
    
        String identificador = parseIdentificador(globalID);
        Integer id = parseID(globalID);
        
        String className = null;
        
        for (String classeElement : mapaClasseID.keySet()) {
            
            String identificadorElement = mapaClasseID.get(classeElement);
            
            if (identificador.equals(identificadorElement)){
               className = classeElement;
            }
        }
        try {
            Class classe = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GlobalIdMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        StringBuilder idFormatado = new StringBuilder();
        idFormatado.append(identificador);
        idFormatado.append(SEPARADOR);
        idFormatado.append(decimalFormatterID.format(id));
        
        return idFormatado.toString();
    }
    private static final String SEPARADOR = ".";
    
    private String parseIdentificador(String globalID){
        return globalID.split(SEPARADOR)[0];
    }
    private Integer parseID(String globalID){
        return Integer.parseInt(globalID.split(SEPARADOR)[1]);
    }
*/
}
