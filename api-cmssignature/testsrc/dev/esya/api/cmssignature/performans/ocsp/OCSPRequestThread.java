package dev.esya.api.cmssignature.performans.ocsp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.ESingleResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.OCSPClient;

public class OCSPRequestThread implements Runnable
{
	
	public static FileWriter logger;

	public void run() 
	{
		ECertificate [] cert = null;
		ECertificate [] issuerCert;
		String address = null;
		
		try 
		{
			logger = new FileWriter("ocsp.log", true);
		} 
		catch (IOException e) 
		{
			throw new RuntimeException(e);
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss,S");
		try 
		{
			cert = new ECertificate[] {new ECertificate((new File(PerformansTest.cert_for_ocsp)))};
			issuerCert = new ECertificate[] {new ECertificate(new File(PerformansTest.issuer_of_ocsp_cert))};
			address = cert[0].getOCSPAdresses().get(0);
		} 
		catch (ESYAException e)
		{
			throw new RuntimeException(e);
		} 
		catch (IOException e) 
		{
			throw new RuntimeException(e);
		}
		
		while(true)
		{
			try
			{
				MessageFormat formatter1 = new MessageFormat("{0} \t OCSP isteği gönderildi.\n");
				Date start = new Date(System.currentTimeMillis());
				logger.write(formatter1.format(new String [] {dateFormat.format(start)}));
				
				
				OCSPClient ocspClient = new OCSPClient(address);
				ocspClient.openConnection();
				ocspClient.queryCertificate(cert, issuerCert);
				ESingleResponse orcunResponse = ocspClient.getSingleResponse(cert[0], issuerCert[0]);
                ocspClient.closeConnection();
				
				Date end = new Date(System.currentTimeMillis());
				Long lasts = end.getTime() - start.getTime();
				MessageFormat formatter2 = new MessageFormat("{0} \t OCSP cevabı alındı. Geçen Süre: \t {1}\n");
				logger.write(formatter2.format(new String [] {dateFormat.format(end), lasts.toString()}));
				
				logger.flush();
				Thread.sleep(PerformansTest.ocsp_interval);
			}
			catch(Exception ex)
			{
				try 
				{
					logger.write(ex.toString() + "\n");
					Thread.sleep(PerformansTest.ocsp_interval);	
				} 
				catch (IOException e1) 
				{
					throw new RuntimeException(e1);
				} 
				catch (InterruptedException e2) 
				{
					e2.printStackTrace();
				}
			}
		}
		
	}
	
}
