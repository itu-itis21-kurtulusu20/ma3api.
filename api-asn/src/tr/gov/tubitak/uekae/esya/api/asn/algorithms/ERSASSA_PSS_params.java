/**
 * 
 */
package tr.gov.tubitak.uekae.esya.api.asn.algorithms;

import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;

import com.objsys.asn1j.runtime.Asn1Integer;

import tr.gov.tubitak.uekae.esya.api.asn.sun.security.x509.AlgorithmId;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.algorithms.RSASSA_PSS_params;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;

/**
 * @author aslihan.kubilay
 *
 */
public class ERSASSA_PSS_params extends BaseASNWrapper<RSASSA_PSS_params>{
	
	public ERSASSA_PSS_params(RSASSA_PSS_params aObject)
	{
		super(aObject);
	}
	
	public ERSASSA_PSS_params(PSSParameterSpec aSpec)
	throws ESYAException
	{
		
		super(new RSASSA_PSS_params());
		
		try
		{
			String digestAlg = aSpec.getDigestAlgorithm();
			if(digestAlg!=null)
			{
				AlgorithmId digestAlgId = AlgorithmId.get(digestAlg);
				byte[] encodedDigestAlg = digestAlgId.encode();
				mObject.hashAlgorithm = new EAlgorithmIdentifier(encodedDigestAlg).getObject();
			}
			
			if((aSpec.getMGFParameters() != null) && (aSpec.getMGFParameters() instanceof MGF1ParameterSpec))
			{
				MGF1ParameterSpec mgfspec = (MGF1ParameterSpec) aSpec.getMGFParameters();
				byte[] encodedMGFDigestAlg = AlgorithmId.get(mgfspec.getDigestAlgorithm()).encode();
				mObject.maskGenAlgorithm = new EAlgorithmIdentifier(_algorithmsValues.id_mgf1, encodedMGFDigestAlg).getObject();
			}
			
			int saltL = aSpec.getSaltLength();
			int trailer = aSpec.getTrailerField();
			
			mObject.saltLength = new Asn1Integer(saltL);
			mObject.trailerField = new Asn1Integer(trailer);
			
		}
		catch(Exception aEx)
		{
			throw new ESYAException("Error in conversion to asn object",aEx);
		}
	}

}
