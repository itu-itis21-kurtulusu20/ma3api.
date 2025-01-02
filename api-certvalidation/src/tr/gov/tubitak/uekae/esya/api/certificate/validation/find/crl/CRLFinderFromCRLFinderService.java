package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.service.CrlFinderClient;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CRLFinderFromCRLFinderService extends CRLFinder
{
	private static Logger logger = LoggerFactory.getLogger(CRLFinderFromCRLFinderService.class);
	
	@Override
	protected List<ECRL> _findCRL(ECertificate aCertificate)
			throws ESYAException 
	{
		List<ECRL> crls = new ArrayList<ECRL>();
		
		aCertificate.getCRLIssuer();
		
		Calendar baseValidationTime = mParentSystem.getBaseValidationTime();
		
		String serviceAddress = getCRLServiceAddress();
		try
		{
			CrlFinderClient finderClient = null;
			String timeout = mParameters.getParameterAsString(PARAM_TIMEOUT);
	    	if(timeout != null)
	    	{
	    		finderClient = new CrlFinderClient(new URL(serviceAddress), Integer.parseInt(timeout)/1000);
	    	}
	    	else
	    	{
	    		finderClient = new CrlFinderClient(new URL(serviceAddress));
	    	}
			 
			List<String> addresses =  finderClient.silSorgulaTarihindenSonraki(aCertificate.getCRLIssuer(), baseValidationTime);
			//  kok sili icin onceki siller de kullanilabilir
			if(!mParentSystem.isDoNotUsePastRevocationInfo()){
			List<String> addressesOnceki =  finderClient.silSorgulaTarihindenOnceki(aCertificate.getCRLIssuer(), baseValidationTime);
			addresses.addAll(addressesOnceki);
			}
			
			for (String aCrlAddress : addresses) 
			{
				try
				{
                    // dirty url fix hack
					int breakingBad = aCrlAddress.lastIndexOf('/');
					String last = aCrlAddress.substring(breakingBad+1);
					// URL encoder does not work because server do not accept
					// '+' for spaces and expects "%20" instead.
                    String encoded = last.replaceAll(" ", "%20");
					URL url = new URL(aCrlAddress.substring(0, breakingBad+1)+encoded);
					URLConnection conn = url.openConnection();
					setTimeOut(conn);
					InputStream crlStream = conn.getInputStream();
					ECRL crl = new ECRL(crlStream);
					crls.add(crl);
				}
				catch(IOException ex)
				{
					logger.error("Can not read CRL from the address: "+ aCrlAddress ,ex);
				}
			}
		} 
		catch(MalformedURLException e)
		{
			logger.error("Malformed URL address for crl service address: "+ serviceAddress ,e);
		}
		catch (Exception e) 
		{
			logger.error("Can not query crl from service at "+ serviceAddress ,e);
		}
		return crls;
	}
}
