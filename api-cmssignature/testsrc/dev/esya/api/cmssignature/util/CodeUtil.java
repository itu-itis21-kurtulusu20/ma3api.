/**
 * Created by orcun.ertugrul on 18-Dec-17.
 */

package dev.esya.api.cmssignature.util;


import org.junit.Assert;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeUtil
{
    //@Test
    public void testMyCodes()throws Exception
    {
        String referenceFile= "SmartOpOld.txt";
        String newFile= "SmartOpNew.java";

        boolean hasAllFunctions = hasAllFunctions(referenceFile, newFile);

        Assert.assertEquals(true, hasAllFunctions);
    }

    /**
     *  Fonksiyon bir sınıfı değiştirdiğimizde eski fonksiyon imzalarının hepsinin değişmiş sınıfta varlığını kontrol ediyor.
     *  Bulamadığı fonksiyonları
     *
     * */
    public boolean hasAllFunctions(String referenceFile, String newFile) throws Exception
    {
        boolean hasAllFunctions = true;
        Pattern functionPattern = Pattern.compile("public .*\\(.*\\)");
        List<String> functionList = new ArrayList<String>();


        byte [] newFileBytes = AsnIO.dosyadanOKU(newFile);
        String newFileStr = new String(newFileBytes, "UTF8");
        Matcher newFileMatcher = functionPattern.matcher(newFileStr);


        while (newFileMatcher.find())
        {
            String functionSignature = newFileMatcher.group();
            String functionID = getFunctionID(functionSignature);

            functionList.add(functionID);
        }


        byte [] refFileBytes = AsnIO.dosyadanOKU(referenceFile);
        String refFileStr = new String(refFileBytes);
        Matcher refFileMatcher = functionPattern.matcher(refFileStr);

        while (refFileMatcher.find())
        {
            String functionSignature = refFileMatcher.group();
            String functionID = getFunctionID(functionSignature);

            boolean result = functionList.contains(functionID);
            if(result == false) {
                System.out.println("Not Found: " + functionID);
                hasAllFunctions = false;
            }
        }

        return hasAllFunctions;
    }

    private String getFunctionID(String functionSignature)
    {
        int paranthezStartIndex = functionSignature.indexOf("(");
        int paranthezEndIndex = functionSignature.indexOf(")");

        String parameterStr = functionSignature.substring(paranthezStartIndex+1, paranthezEndIndex);
        String parameters = getParameterTypes(parameterStr);

        String functionName = functionSignature.substring(0, paranthezStartIndex).trim();

        String functionID = functionName + parameters;

        return functionID;
    }

    private String getParameterTypes(String parameterStr)
    {
        String parametersTypes = "";
        String [] parameters = parameterStr.split(",");
        for (String aParameter: parameters)
        {
            String aParameterType = aParameter.trim().split(" ")[0];
            parametersTypes = parametersTypes + "-" + aParameterType;
        }

        return parametersTypes;


    }

}
