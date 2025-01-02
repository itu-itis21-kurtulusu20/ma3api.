package tr.gov.tubitak.uekae.esya.api.asic.util;

import tr.gov.tubitak.uekae.esya.api.asic.core.PackageContentResolver;
import tr.gov.tubitak.uekae.esya.api.asic.model.PackageContents;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignedDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.FileResolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.HttpResolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.IResolver;

import java.math.BigInteger;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ayetgin
 */
public class ASiCUtil
{
    // instead of very long unique ids, generate short almost unique id
    public static String id(){
        try {
            byte[] bytes = DigestUtil.digest(DigestAlg.SHA1, new UID().toString().getBytes());
            String s = new BigInteger(bytes).abs().toString(16);
            return s.substring(s.length()-8, s.length());
        } catch (Exception x){
            throw new ESYARuntimeException(x);
        }
    }

    public static void fixResolversConfig(SignatureContainer sc, PackageContents contents){
        Object impl = sc.getUnderlyingObject();

        tr.gov.tubitak.uekae.esya.api.xmlsignature.Context xc;

        if (impl instanceof SignedDocument){
            xc = ((SignedDocument)impl).getContext();
        } else {
            xc = ((XMLSignature)impl).getContext();
        }
        fixResolversConfig(xc, contents);
    }

    public static void fixResolversConfig(Context xc, PackageContents contents){
        List<Class<? extends IResolver>> resolvers = xc.getConfig().getResolversConfig().getResolvers();
        List<Class<? extends IResolver>> remove = new ArrayList<Class<? extends IResolver>>();

        for (Class<? extends IResolver> c : resolvers){
            if (c.equals(FileResolver.class) || c.equals(HttpResolver.class)){
                remove.add(c);
            }
        }
        resolvers.removeAll(remove);
        xc.getResolvers().add(new PackageContentResolver(contents));
    }


    public static void main(String[] args)
    {
        System.out.println(id());
        System.out.println(id());
        System.out.println(id());
        System.out.println(id());
        System.out.println(id());
        System.out.println(id());
        System.out.println(id());
        System.out.println(id());
        System.out.println(id());
    }
}
