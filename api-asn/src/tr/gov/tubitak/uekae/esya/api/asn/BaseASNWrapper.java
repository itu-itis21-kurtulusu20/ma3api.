package tr.gov.tubitak.uekae.esya.api.asn;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.util.UtilEsitlikler;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;

/**
 * @author ahmety
 *         date: Jan 29, 2010
 */
public class BaseASNWrapper<T extends Asn1Type> implements Serializable, Cloneable {

    protected static Logger logger = LoggerFactory.getLogger(BaseASNWrapper.class);

    protected T mObject;

    public BaseASNWrapper(T aObject) {
        mObject = aObject;
    }

    public BaseASNWrapper(byte[] aBytes, T aObject) throws ESYAException
    {
        mObject = aObject;
        try {
        	mObject = (T)  AsnIO.arraydenOku(mObject, aBytes);
        }
        catch (Exception x) {
            throw new ESYAException(x);
        }
    }

    public BaseASNWrapper(String aBase64Encoded, T aObject) throws ESYAException {
        mObject = aObject;
        try {
        	mObject = (T) AsnIO.arraydenBase64Oku(mObject, aBase64Encoded.getBytes(StandardCharsets.US_ASCII));
        }
        catch (Exception x) {
            throw new ESYAException(x);
        }
    }

    public BaseASNWrapper(InputStream aStream, T aObject) throws ESYAException {
        try 
        {
            mObject = aObject;
            Asn1DerDecodeBuffer b = new Asn1DerDecodeBuffer(aStream);
            mObject = (T) AsnIO.derOku(mObject, b);
        } 
        catch (Exception x) 
        {
            throw new ESYAException(x);
        }
    }


    /**
     * @return AsnObject as der encoded byte[].
     */
    public synchronized byte[] getEncoded() {
        Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
        try {
            mObject.encode(enc);
        }
        catch (Exception aEx) {
            logger.error("Encode edilirken hata oluştu", aEx);
            throw new ESYARuntimeException("Cannot encode "+mObject, aEx);
        }
        return enc.getMsgCopy();
    }

    public String getBase64Encoded(){
        byte [] encodedBytes = getEncoded();
        StringBuilder sb = new StringBuilder();
        sb.append(getBase64EncodeHeader());
        sb.append("\n");
        sb.append(Base64.encode(encodedBytes));
        sb.append("\n");
        sb.append(getBase64EncodeFooter());
        return sb.toString();
    }

    public byte [] getEncoded(EncodeMethod method) {
        if(method == EncodeMethod.ASN1)
            return getEncoded();
        else if(method == EncodeMethod.BASE64)
            return getBase64Encoded().getBytes(StandardCharsets.US_ASCII);
        else
            throw new ESYARuntimeException("Unknown Encoded Method." + method.name());
    }

    public String getBase64EncodeHeader() {
        return "";
    }

    public String getBase64EncodeFooter() {
        return "";
    }

    public T getObject() {
        return mObject;
    }

    @Override
    public int hashCode()
    {
        return mObject.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseASNWrapper) {
            return UtilEsitlikler.esitMi(mObject, ((BaseASNWrapper) obj).getObject());
        }
        return super.equals(obj);
    }

    // util methods

    @SuppressWarnings("unchecked")
    protected static <V> V[] extendArray(V[] aArray, V aNewMember)
    {
        int oldSize = (aArray==null) ? 0 : aArray.length;
        Class clazz = (aArray==null) ? aNewMember.getClass() : aArray.getClass().getComponentType();

        // create new array
        V[] newArray = (V[])Array.newInstance(clazz, oldSize+1);

        // copy old members to new one
        if (aArray!=null)
            System.arraycopy(aArray, 0, newArray, 0, oldSize);

        //set the new member
        newArray[oldSize] = aNewMember;

        return newArray;
    }

    @SuppressWarnings("unchecked")
    protected static <V> V[] extendArray(V[] aArray, V[] aNewMembers)
    {
        if (aNewMembers==null)
            throw new ESYARuntimeException("Extend array expected array, but got null ");

        int oldSize = (aArray==null) ? 0 : aArray.length;
        Class clazz = (aArray==null) ? aNewMembers.getClass().getComponentType() : aArray.getClass().getComponentType();

        // create new array
        V[] newArray = (V[])Array.newInstance(clazz, oldSize+aNewMembers.length);

        // copy old members to new one
        if (aArray!=null)
            System.arraycopy(aArray, 0, newArray, 0, oldSize);

        //set the new member
        System.arraycopy(aNewMembers, 0, newArray, oldSize, aNewMembers.length);

        return newArray;
    }
    
    protected static <V> V[] removeFromArray(V [] aArray, V object) throws ESYAException
    {
    	if(aArray == null || aArray.length == 0)
    		throw new ESYAException("Array is null or empty");
    	
    	Class clazz = aArray.getClass().getComponentType();
    	int newSize = aArray.length - 1;
    	V[] newArray = (V[])Array.newInstance(clazz, newSize);

    	int i = 0;
    	for (V v : aArray) 
    	{
    		if(!v.equals(object))
    		{
    			if(i >= newSize)
        			throw new ESYAException("Object can not find in the array");
    			newArray[i] = v;
    			i++;
    		}
		}
    	
    	
    	return newArray;
    }

    @SuppressWarnings("unchecked")
    protected static <U extends Asn1Type, W extends BaseASNWrapper<U>> U[] unwrapArray(W[] aArray){
        if (aArray==null)
            return null;

//        Class clazz = aArray.getClass().getTypeParameters()[0].getClass();
        ParameterizedType genericBaseAsnWrapper = (ParameterizedType) aArray.getClass().getComponentType().getGenericSuperclass();
        Class clazz = (Class) genericBaseAsnWrapper.getActualTypeArguments()[0];  // Arrays has only 1 argument and its safe because W extends BaseASNWrapper<U>
        U[] newArray = (U[])Array.newInstance(clazz, aArray.length);
        for (int i=0;i<aArray.length; i++){
            newArray[i]=aArray[i].getObject();
        }
        return newArray;
    }

    @SuppressWarnings("unchecked")
    protected static <U extends Asn1Type, W extends BaseASNWrapper<U>> W[] wrapArray(U[] aArray, Class<W> aClazz){
        if (aArray==null)
            return null;

        W[] newArray = (W[])Array.newInstance(aClazz, aArray.length);
        try {
            Constructor c = aClazz.getConstructor(aArray.getClass().getComponentType());
            for (int i=0;i<aArray.length; i++){
                newArray[i]= (W)c.newInstance(aArray[i]);// new W(U);
            }
        } catch (Exception x){
            throw new ESYARuntimeException("Wrap error!", x);
        }

        return newArray;
    }

    @Override
    protected Object clone()  {
        try {
            BaseASNWrapper clone = (BaseASNWrapper)super.clone();
            if(mObject != null) {
                clone.mObject = mObject.getClass().newInstance();
                clone.mObject = AsnIO.arraydenOku(clone.mObject,getEncoded());
            }
            return clone;
        } catch (Exception e) {
            throw new ESYARuntimeException(e);
        }
    }
}
