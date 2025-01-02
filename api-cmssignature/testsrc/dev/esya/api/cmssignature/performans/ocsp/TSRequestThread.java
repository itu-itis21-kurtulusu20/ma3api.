package dev.esya.api.cmssignature.performans.ocsp;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;

public class TSRequestThread implements Runnable
{
	
	public static FileWriter logger;

	public void run() 
	{
		Random rand = new Random();
		byte [] digestForTS = new byte[20];
		TSSettings tsSettings = new TSSettings(PerformansTest.ts_address, PerformansTest.ts_user_id, PerformansTest.ts_password.toCharArray());
		
		try 
		{
			logger = new FileWriter("ts.log", true);
		} 
		catch (IOException e) 
		{
			throw new RuntimeException(e);
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss,S");
		while(true)
		{
			rand.nextBytes(digestForTS);
			TSClient tsClient = new TSClient();
			try 
			{
				MessageFormat formatter1 = new MessageFormat("{0} \t ZD isteği gönderildi.\n");
				Date start = new Date(System.currentTimeMillis());
				logger.write(formatter1.format(new String [] {dateFormat.format(start)}));
				
				tsClient.timestamp(digestForTS, tsSettings);
				
				Date end = new Date(System.currentTimeMillis());
				Long lasts = end.getTime() - start.getTime();
				MessageFormat formatter2 = new MessageFormat("{0} \t ZD cevabı alındı. Geçen Süre: \t {1}\n");
				logger.write(formatter2.format(new String [] {dateFormat.format(end), lasts.toString()}));
				
				logger.flush();
				Thread.sleep(PerformansTest.ts_interval);
			} 
			catch (ESYAException e) 
			{
				try 
				{
					logger.write(e.toString());
					Thread.sleep(PerformansTest.ts_interval);
				} 
				catch (IOException e1) 
				{
					throw new RuntimeException(e1);
				} 
				catch (InterruptedException e2) 
				{
					throw new RuntimeException(e2);
				}
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
		
	}
	
	
}
