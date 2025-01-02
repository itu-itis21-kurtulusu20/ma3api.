package dev.esya.api.cmssignature.performans.ocsp;

import tr.gov.tubitak.uekae.esya.api.common.tools.IniFile;

public class PerformansTest 
{
	public static long ocsp_interval;
	public static String	cert_for_ocsp;
	public static String	issuer_of_ocsp_cert;
	
	public static long ts_interval;
	public static String ts_address;
	public static int	ts_user_id;
	public static String ts_password;

	
	public static void main(String[] args) throws Exception
	{
		
		IniFile settings = new IniFile("settings.ini");
		settings.loadIni();
		
		ocsp_interval = Long.parseLong(settings.getValue("OCSP", "ocsp_interval").trim());
		cert_for_ocsp = settings.getValue("OCSP", "cert_for_ocsp").trim();
		issuer_of_ocsp_cert = settings.getValue("OCSP", "issuer_of_ocsp_cert").trim();
		
		ts_interval = Long.parseLong(settings.getValue("TS", "ts_interval").trim());
		try
		{
			ts_address = settings.getValue("TS", "ts_address").trim();
			if(ts_address.length() <= 0)
				throw new Exception("Zaman damgasi adresi bos olamaz.");
			ts_user_id = Integer.parseInt(settings.getValue("TS", "ts_user_id").trim());
			ts_password = settings.getValue("TS", "ts_password").trim();
			
				
		}
		catch(Exception ex)
		{
			throw new RuntimeException("Zaman Damgasi Ayarlarini veriniz" ,ex);
		}
		
		
		Thread zdThread = new Thread(new TSRequestThread(), "Zaman Damgası Treadi");
		zdThread.start();
				
		Thread ocspThread = new Thread(new OCSPRequestThread(), "OCSP Thread");
		ocspThread.start();
		
		zdThread.join();
		ocspThread.join();
	}
}
