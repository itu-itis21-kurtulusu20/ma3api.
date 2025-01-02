package dev.esya.api.dist.manualExamples;

import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.ArchiveTimeStampAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SigningTimeAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.EST;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.Calendar;
import java.util.List;

public class ImzaZamaniAlma extends TestCase
{
	String BESwithSIGNING_TIME = TestConstants.getDirectory() + "testdata\\support\\manual\\BESwithSigningTime.p7s";
	String ESA = TestConstants.getDirectory() + "testdata\\support\\manual\\ESA1.p7s";
	
	public void testImzaZamaniAlma1() throws Exception
	{
byte[] input = AsnIO.dosyadanOKU(ESA);
BaseSignedData bs = new BaseSignedData(input);
EST estSign = (EST)bs.getSignerList().get(0);
Calendar time = estSign.getTime();
	}
	
	public void testImzaZamaniAlma2() throws Exception
	{
byte[] input = AsnIO.dosyadanOKU(BESwithSIGNING_TIME);
BaseSignedData bs = new BaseSignedData(input);
List<EAttribute>  attrs = bs.getSignerList().get(0).getSignedAttribute(AttributeOIDs.id_signingTime);
Calendar time = SigningTimeAttr.toTime(attrs.get(0));
System.out.println(time.getTime().toString());
	}
	
	public void testImzaZamaniAlma3() throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(ESA);
		BaseSignedData bs = new BaseSignedData(input);
		List<EAttribute>  attrs = bs.getSignerList().get(0).getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestamp);
		List<EAttribute>  attrsV2 = bs.getSignerList().get(0).getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV2);
		attrs.addAll(attrsV2);
		for (EAttribute attribute : attrs) 
		{
			Calendar time = ArchiveTimeStampAttr.toTime(attribute);
			System.out.println(time.getTime().toString());
		}
	}
}
