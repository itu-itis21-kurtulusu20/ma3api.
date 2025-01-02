package tr.gov.tubitak.uekae.esya.api.pades.pdfbox.util;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureUtil;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PAdESUtil
{
    public static Set<byte[]> convert(List<? extends BaseASNWrapper> wrappers){
        Set<byte[]> objects = new LinkedHashSet<byte[]>();
        for (BaseASNWrapper object :  wrappers){
            byte[] bytes = object.getEncoded();
            objects.add(bytes);
        }
        return objects;
    }

    public static List<EOCSPResponse> wrap(List<EBasicOCSPResponse> input){
        List<EOCSPResponse> result = new ArrayList<EOCSPResponse>();
        for (EBasicOCSPResponse basicOCSPResponse : input){
            result.add(CMSSignatureUtil.convertBasicOCSPRespToOCSPResp(basicOCSPResponse));
        }
        return result;
    }

    public static boolean isPre(SignatureType from, SignatureType to){
        return order(from)<order(to);
    }

    public static int order(SignatureType type){
        switch (type){
            case ES_BES:    return 1;
            case ES_EPES:   return 2;
            case ES_T:      return 3;
            case ES_XL :    return 4;
            case ES_A :     return 5;
        }
        return -1;
    }

    public static boolean isSupported(SignatureType type){
        switch (type){
            case ES_BES:
            case ES_EPES:
            case ES_T:
            case ES_XL :
            case ES_A :     return true;
        }
        return false;
    }

    public static <T> List<T> removeDuplicates(List<T> list){

        List<T> newList = new ArrayList<T>();
        for(T element : list){
            if(!newList.contains(element)){
                newList.add(element);
            }
        }
        return newList;
    }

    public static <T> List<T> findDifferentOnes(List<T> list1, List<T> list2 ){

        List<T> newList = new ArrayList<T>();
        for(T element: list2){
            if(!list1.contains(element)){
                newList.add(element);
            }
        }
        return newList;
    }
}
