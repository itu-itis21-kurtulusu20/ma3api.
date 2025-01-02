package tr.gov.tubitak.uekae.esya.api.common.tools;

import java.io.*;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * <p>Title: IniFile</p>
 * <p>Description: Bu class KSM ve SM'nin ini dosyalarını olusturur ve okur </p>
 * <p>Copyright: Copyright (c) 2001-2004</p>
 * <p>Company: UEKAE</p>
 * @author MA3
 * @version 1.0
 * @deprecated use Ini
 * @see Ini
 */

@Deprecated
public class IniFile
{
     protected static Logger logger = LoggerFactory.getLogger(IniFile.class);
     private static final String CHARSET = "UTF-8";
//   private static final String CHARSET = "ISO8859_9";
//   private static final String CHARSET = "Cp1254";
     
     String mInifileName = "";
     byte[] mIniBytes = null;
     Vector<oberBegriff> mIniVector = new Vector<oberBegriff>();
     
     
     String bis(String s,String w) {
          if (s.indexOf(w)!=-1) {
               return s.substring(0,s.indexOf(w));
          } return "";
     }
     
     String hinter(String s,String w) {
          if (s.indexOf(w)!=-1) {
               return s.substring(s.indexOf(w)+w.length());
          } else return "";
     }
     public IniFile()
     {
          //do nothing
     }
    
     public void loadIni(String f)
     throws FileNotFoundException, IOException
     {
          mInifileName = f;
          mIniBytes = null;
          loadIni();
     }
     public void loadIni(byte[] bytes) 
     throws FileNotFoundException, IOException
     {
          mInifileName = "";
          mIniBytes = bytes;
          loadIni();
     }
     
     public void loadIni()
     throws FileNotFoundException, IOException
     {
          try {
               if(mIniBytes == null)
                    loadIni(new InputStreamReader(new FileInputStream(mInifileName),CHARSET));
               else
                    loadIni(new InputStreamReader(new ByteArrayInputStream (mIniBytes),CHARSET));
          }
          catch (UnsupportedEncodingException ex)
          {
               if(mIniBytes == null)
                    loadIni(new InputStreamReader(new FileInputStream(mInifileName),"UTF-8"));
               else
                    loadIni(new InputStreamReader(new ByteArrayInputStream (mIniBytes),"UTF-8"));

               logger.warn("Warning in IniFile", ex);

          }
     }
     
     private void loadIni(InputStreamReader instreamR) 
     throws IOException
     {
          mIniVector = new Vector<oberBegriff>();
          String L = "";
          BufferedReader f = new BufferedReader(instreamR);
          do {
               L = f.readLine();
               if (L!=null) {
                    L=L.trim();
                    if (!L.equals("") && !L.startsWith(";")) {
                         //koksermak.kontmer.Context.EkranaYaz(L);
                         if (L.startsWith("[")) {    // OberBegriff !
                              L=hinter(L,"[");
                              L=bis(L,"]");
                              oberBegriff ob = new oberBegriff(L);
                              mIniVector.addElement(ob);
                         } else {    // UnterBegriff !
                              String B = bis(L,"=");
                              String E = hinter(L,"=");
                              unterBegriff ub = new unterBegriff(B,E);
                              mIniVector.lastElement().UB.addElement(ub);
                         }
                    }
               }
          } while (L!=null);
          f.close();
     }
     
     
     
     public void saveIni(String file) 
     throws FileNotFoundException
     {
          mInifileName = file;
          saveIni();
     }
     public void saveIni()
     throws FileNotFoundException
     {
          PrintStream f;
          try
          {
               f = new PrintStream(new FileOutputStream(mInifileName), true, CHARSET);
          } 
//          catch (FileNotFoundException ex)
//          {
//               return; /* * @ todo Burada bir seyler yapmali... */
//          } 
          catch (UnsupportedEncodingException ex)
          {
               try
               {
                    f = new PrintStream(new FileOutputStream(mInifileName), true, "UTF-8");
               } 
//               catch (FileNotFoundException ex1)
//               {
//                    return; /* * @ todo Burada bir seyler yapmali... */
//               } 
               catch (UnsupportedEncodingException ex1)
               {
                    throw new RuntimeException("Zaten buraya hic giremez. Cunki UTF-8 tum VM'ler tarafindan desteklenmeli.",ex1);
                    //return; //Zaten buraya hic giremez. Cunki UTF-8 tum VM'ler tarafindan desteklenmeli.
               }
          }
          
          
          for (int i=0;i<mIniVector.size();i++) {
               oberBegriff ob = mIniVector.elementAt(i);
               f.println("["+ob.Begriff+"]");
               for (int j=0;j<ob.UB.size();j++) {
                    unterBegriff ub = ob.UB.elementAt(j);
                    f.println(ub.Begriff+"="+ub.Eintrag);
               }
               f.println("");
          }
          f.close();
     }
     
     public void setValue(String OB,String Begriff,boolean Eintrag) {
          String tf = "FALSE";
          if (Eintrag) tf = "TRUE";
          setValue(OB,Begriff,tf);
     }
     public void setValue(String OB,String Begriff,int Eintrag) {
          setValue(OB,Begriff,""+Eintrag);
     }
     public void setValue(String OB,String Begriff,long Eintrag) {
          setValue(OB,Begriff,""+Eintrag);
     }
     public void setValue(String OB,String Begriff,String Eintrag) {
          
          for (int i=0;i<mIniVector.size();i++) {
               oberBegriff ob = mIniVector.elementAt(i);
               if (ob.Begriff.equals(OB)) {
                    for (int j=0;j<ob.UB.size();j++) {
                         unterBegriff ub = ob.UB.elementAt(j);
                         if (ub.Begriff.equals(Begriff)) {
                              ub.Eintrag = Eintrag;
                              return;
                         }
                    }
                    unterBegriff ub = new unterBegriff(Begriff,Eintrag);
                    ob.UB.addElement(ub);
                    return;
               }
          }
          oberBegriff ob = new oberBegriff(OB);
          unterBegriff ub = new unterBegriff(Begriff,Eintrag);
          ob.UB.addElement(ub);
          mIniVector.addElement(ob);
          
          
     }
     
     public boolean getBoolValue(String OB,String Begriff) {
          String E = getValue(OB,Begriff);
          if (E.equals("TRUE")) return true; else return false;
     }
     public int getIntValue(String OB,String Begriff) {
          String E = getValue(OB,Begriff);
          if (E.equals("")) E="-1";
          return (new Integer(E)).intValue();
     }
     public long getLongValue(String OB,String Begriff) {
          String E = getValue(OB,Begriff);
          if (E.equals("")) E="-1";
          return (new Long(E)).longValue();
     }
     public String getValue(String OB,String Begriff) {
          for (int i=0;i<mIniVector.size();i++) {
               oberBegriff ob = mIniVector.elementAt(i);
               if (ob.Begriff.equals(OB)) {
                    for (int j=0;j<ob.UB.size();j++) {
                         unterBegriff ub = ob.UB.elementAt(j);
                         if (ub.Begriff.equals(Begriff)) {
                              return ub.Eintrag;
                         }
                    }
                    return "";
               }
          }
          return "";
     }
     
     
     public IniFile(String aInifile){
          this.mInifileName = aInifile;
          mIniBytes = null;
     }
     
     public IniFile(byte[] aInibytes){
          this.mIniBytes = aInibytes;
          mInifileName = "";
     }

    // internal
    private class unterBegriff {
         String Begriff = "";
         String Eintrag = "";
         public unterBegriff(String B,String E) {
              Begriff = B;
              Eintrag = E;
         }
    }

    private class oberBegriff {
         String Begriff = "";
         Vector<unterBegriff> UB = new Vector<unterBegriff>();

         public oberBegriff(String B) {
              Begriff = B;
         }
    }

}
