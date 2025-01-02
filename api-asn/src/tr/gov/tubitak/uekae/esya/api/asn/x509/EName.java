package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OpenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
import tr.gov.tubitak.uekae.esya.asn.util.OID_tipEslestirmeleri;
import tr.gov.tubitak.uekae.esya.asn.util.UtilEsitlikler;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import tr.gov.tubitak.uekae.esya.asn.x509.AttributeTypeAndValue;
import tr.gov.tubitak.uekae.esya.asn.x509.DirectoryString;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.asn.x509.RDNSequence;
import tr.gov.tubitak.uekae.esya.asn.x509.RelativeDistinguishedName;

import java.util.ArrayList;
import java.util.List;

/**
 @author ahmety
 date: Jan 29, 2010 */
public class EName extends BaseASNWrapper<Name> {

    protected static Logger logger = LoggerFactory.getLogger(EName.class);

    public EName(Name aObject) {
        super(aObject);
    }

    public EName(byte[] nameInBytes) throws ESYAException {
        super(nameInBytes, new Name());
    }

    public EName(String nameInBase64) throws ESYAException {
        super(nameInBase64, new Name());
    }

    public EName(String aDN, boolean aUTF8) throws ESYAException {
        super(null);
        Name internal;
        try {
            internal = UtilName.string2Name(aDN, aUTF8);
        } catch (Asn1Exception x) {
            throw new ESYAException("Invalid name string! " + aDN, x);
        }
        mObject = internal;
    }

    public String stringValue() {
        return LDAPDNUtil.normalize(_bosluklariTemizle(UtilName.name2String(mObject)));
    }

    private String _bosluklariTemizle(String aStr) {
        if (aStr == null)
            return null;

        String str = null;
        String[] splittedStr = aStr.trim().split(" ");

        for (String part : splittedStr) {
            if (!part.equals("")) {
                if (str == null)
                    str = part.trim();
                else {
                    if (part.charAt(0) == ',' || str.endsWith("=")) {
                        str = str + part.trim();
                    } else {
                        str = str + " " + part.trim();
                    }
                }
            }
        }
        return str;
    }
    /**
   	 * Returns Common Name Attribute of EName
   	 * @return
   	 */
    public String getCommonNameAttribute() {
        List<ERelativeDistinguishedName> mList = getRDNList();

        for (ERelativeDistinguishedName rdn : mList) {
            String name = rdn.getCommonNameAttribute();
            if (name != null)
                return name;
        }

        return null;
    }
    /**
   	 * Returns Email Attribute of EName
   	 * @return
   	 */
    public String getEmailAttribute() {
        return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_emailAddress);
    }
    /**
   	 * Returns Serial Number Attribute of EName
   	 * @return
   	 */
    public String getSerialNumberAttribute() {
        return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_serialNumber);
    }
    /**
   	 * Returns Name Attribute of EName
   	 * @return
   	 */
    public String getNameAttribute() {
        return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_name);
    }
    /**
   	 * Returns Given Name Attribute of EName
   	 * @return
   	 */
    public String getGivenNameAttribute() {
        return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_givenName);
    }
    /**
   	 * Returns Surname Attribute of EName
   	 * @return
   	 */
    public String getSurnameAttribute() {
        return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_surname);
    }
    /**
   	 * Returns Locality Name Attribute of EName
   	 * @return
   	 */
    public String getLocalityNameAttribute() {
        return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_localityName);
    }
    /**
   	 * Returns State Or Province Name Attribute of EName
   	 * @return
   	 */
    public String getStateOrProvinceNameAttribute() {
        return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_stateOrProvinceName);
    }
    /**
   	 * Returns Organizational Unit Name Attribute of EName
   	 * @return
   	 */
    public String getOrganizationNameAttribute() {
        return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_organizationName);
    }
    /**
   	 * Returns Organizational Unit Name Attribute of EName
   	 * @return
   	 */
    public String getOrganizationalUnitNameAttribute() {
        return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_organizationalUnitName);
    }
    /**
   	 * Returns Title Attribute of EName
   	 * @return
   	 */
    public String getTitleAttribute() {
        return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_title);
    }
    /**
   	 * Returns Country Name Attribute of EName
   	 * @return
   	 */
    public String getCountryNameAttribute() {
        return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_countryName);
    }
    /**
	 * Returns Pseudonym Attribute of EName
	 * @return
	 */
    public String getPseudonymAttribute() {
        return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_pseudonym);
    }
    /**
	 * Returns Domain Component Attribute of EName
	 * @return
	 */
    public String getDomainComponentAttribute() {
        return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_domainComponent);
    }
    /**
   	 * Returns RDN String Attribute of EName
   	 * @return
   	 */
    public String getRDNStringAttribute(Asn1ObjectIdentifier oid) {
        List<ERelativeDistinguishedName> mList = getRDNList();
        for (ERelativeDistinguishedName rdn : mList) {
            String name = rdn.getStringAttribute(oid);
            if (name != null)
                return name;
        }
        return null;
    }

    public String getOrganizationIdentifierAttribute() {
        return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_organizationIdentifier);
    }

    /**
   	 * Checks whether EName is Utf8String or not.
   	 * @return True if it is not null and its choiceId is UTF8STRING ,false otherwise
   	 */ 
    public boolean isSubNameOf(EName aName) {
        return isSubNameOf(aName.getObject());
    }

    public boolean isSubNameOf(Name aName) {

        RelativeDistinguishedName[] elemsBase = ((RDNSequence) (mObject.getElement())).elements;

        if (elemsBase == null || elemsBase.length < 1) // Empty Name is sub Name of all names!
            return true;

        RelativeDistinguishedName[] elems = aName.getElement() != null ? ((RDNSequence) (aName.getElement())).elements : null;
        if (elems == null || elems.length < 1 || elems.length > elemsBase.length)
            return false;

        for (int i = 0; i < elems.length; i++) {
            if (!UtilEsitlikler.esitMi(elemsBase[i], elems[i]))
                return false;
        }
        return true;
    }

    /**
   	 * Returns RDN List of EName if its ChoiceID equals to Name._RDNSEQUENCE
   	 * @return
   	 */
    public List<ERelativeDistinguishedName> getRDNList() {
        if (mObject.getChoiceID() == Name._RDNSEQUENCE) {
            RDNSequence rdnSequence = (RDNSequence) mObject.getElement();
            List<ERelativeDistinguishedName> list = new ArrayList<ERelativeDistinguishedName>(rdnSequence.elements.length);
            for (RelativeDistinguishedName rdn : rdnSequence.elements) {
                list.add(new ERelativeDistinguishedName(rdn));
            }
            return list;
        }
        return new ArrayList<ERelativeDistinguishedName>(0);
    }


    public void appendRDN(RelativeDistinguishedName aRDN) {
        RDNSequence rdnSequence = (RDNSequence) mObject.getElement();
        rdnSequence.elements = extendArray(rdnSequence.elements, aRDN);
    }


    public static boolean isSubNameOf(String iName, String iBaseName) {
        return isSubNameOf(iName, iBaseName, ".");
    }

    // static util methods
    public static boolean isSubNameOf(EName iName, EName iBaseName) {

        RelativeDistinguishedName[] elemsBase = ((RDNSequence) (iBaseName.getObject().getElement())).elements;

        if (elemsBase == null || elemsBase.length < 1) // Empty Name is sub Name of all names!
            return true;

        RelativeDistinguishedName[] elems = ((RDNSequence) (iName.getObject().getElement())).elements;
        if (elems == null || elems.length < 1 || elems.length > elemsBase.length)
            return false;

        for (int i = 0; i < elems.length; i++) {
            if (!UtilEsitlikler.esitMi(elemsBase[i], elems[i]))
                return false;
        }
        return true;
    }

    public static boolean isSubNameOf(String iName, String iBaseName, String iSeperator) {
        if (iName.equals(iBaseName))
            return true;

        if (iBaseName.startsWith("."))
            return iName.endsWith(iBaseName);

        int dotPos = iName.indexOf(iSeperator);

        if (dotPos < 0) return (iName.equals(iBaseName));

        String tail = iName.substring(dotPos + 1);

        return (tail.equals(iBaseName));

    }

    public static boolean isSubURINameOf(String iURIName, String iBaseURIName) {
        if (iURIName.equals(iBaseURIName))
            return true;

        if (iBaseURIName.startsWith("."))
            return (iURIName.indexOf(iBaseURIName) >= 0);

        int dotPos = iURIName.indexOf("//");

        if (dotPos < 0) return false;

        String tail = iURIName.substring(dotPos + 2);

        return (tail.startsWith(iBaseURIName));
    }
    /**
	 * Checks whether EName is Utf8String or not.
	 * @return True if it is not null and its choiceId is UTF8STRING ,false otherwise
	 */   
    public boolean isUtf8String() {
        if (mObject == null)
            return false;
        RDNSequence rdnSeq = (RDNSequence) mObject.getElement();
        if (rdnSeq == null)
            return false;
        RelativeDistinguishedName[] rdns = rdnSeq.elements;
        if (rdns == null || rdns.length == 0 || rdns[0] == null)
            return false;

        int i, j;
        AttributeTypeAndValue[] atavs;
        AttributeTypeAndValue atav;

        for (i = 0; i < rdns.length; i++) {
            atavs = rdns[i].elements;
            if (atavs == null || atavs.length == 0)
                continue;
            for (j = 0; j < atavs.length; j++) {
                atav = atavs[j];
                if (atav == null || atav.value == null)
                    continue;
                Asn1OpenType openType = atav.value;
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                Asn1DerDecodeBuffer decBuf;
                DirectoryString dirStr = new DirectoryString();
                try {
                    openType.encode(encBuf);
                    decBuf = new Asn1DerDecodeBuffer(encBuf.getMsgCopy());
                    dirStr.decode(decBuf);
                } catch (Exception e) {
                    logger.warn("Warning in EName", e);
                    continue;
                }

                if (dirStr.getChoiceID() == DirectoryString._UTF8STRING)
                    return true;

            }
        }

        return false;
    }


    @Override
    public EName clone() {
        try {
            Name cloned = (Name) mObject.clone();
            if (cloned.getChoiceID() == Name._RDNSEQUENCE) {
                RDNSequence rdns = (RDNSequence) cloned.getElement().clone();
                cloned.setElement(cloned.getChoiceID(), rdns);
                rdns.elements = rdns.elements.clone();
            }
            return new EName(cloned);
        } catch (CloneNotSupportedException x) {
            throw new ESYARuntimeException("Cannot clone name : " + stringValue(), x);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof EName) {
            return stringValue().equalsIgnoreCase(((EName) obj).stringValue());
        }
        return super.equals(obj);
    }
    @Override
    public int hashCode(){
        return super.hashCode();
    }

    public String toShortTitle(){
            String retStr="";
            String cn = null;
        cn = getCommonNameAttribute();
        if(cn != null &&  !cn.isEmpty())
        {
            retStr = cn;
            String sn =getSurnameAttribute();
            if(sn != null && !sn.isEmpty() )
            {
                retStr+=" "+sn;
            }
        }
        else
        {
            String ou = getOrganizationalUnitNameAttribute();
            if(ou != null && !ou.isEmpty())
            {
                retStr = ou;
            }
            else
            {
                String o = getOrganizationNameAttribute();
                if(o!=null && !o.isEmpty())
                {
                    retStr = o;
                }
            }
        }
        if(retStr.isEmpty())
            retStr=stringValue();
        return retStr;
    }
}
