package dev.esya.api.cmssignature.codeanalysis;

import org.junit.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoggerController {
    @Test
    public static void loggerClassController(File directory) throws IOException {
        File[] files=directory.listFiles();
        for (int i=0; i<files.length; i++)
        {
            File file = files[i];
            if (file.isDirectory()) {
                loggerClassController(file);
            }
            else {
                if (file.getName().endsWith(".java")) {
                    String fileName = file.getName().substring(0,file.getName().lastIndexOf('.'));
                    //Reading File
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line=reader.readLine();

                  while(line != null) {

                      if(line.contains("LoggerFactory.getLogger")){
                          String getLoggerInside = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
                          getLoggerInside = getLoggerInside.trim();
                          boolean valid = false;

                          if(getLoggerInside.equals(fileName + ".class"))
                              valid = true;

                          if(getLoggerInside.equals("this.getClass()"))
                              valid = true;

                          if(getLoggerInside.equals("getClass()"))
                              valid = true;

                          if(valid == false)
                            System.out.println(fileName);

                      }
                      line = reader.readLine();
                  }
                  reader.close();
                  continue;
                }

            }
        }
    }

    public static void main(String[] args) throws IOException {
        File directory=new File("T:/");
        LoggerController.loggerClassController(directory);
    }
}
