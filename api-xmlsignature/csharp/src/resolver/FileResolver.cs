using System;
using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver
{

	using Logger = log4net.ILog;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
	using FileDocument = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;


	/// <summary>
	/// @author ahmety
	/// date: May 14, 2009
	/// </summary>
	public class FileResolver : IResolver
	{

		private static Logger logger = log4net.LogManager.GetLogger(typeof(FileResolver));

		public virtual bool isResolvable(string aURI, Context aBaglam)
		{
			if ((aURI == null) || aURI.Equals("") || (aURI[0] == '#') || aURI.StartsWith("http:"))
			{
				return false;
			}

			string baseURI = aBaglam.BaseURIStr;
			if (logger.IsDebugEnabled)
			{
				logger.Debug("Base uri is '" + baseURI + "'");
			}

            /*if (baseURI == null)
            {
                baseURI = Directory.GetCurrentDirectory();
            }*/

			try
			{
				if (logger.IsDebugEnabled)
				{
					logger.Debug("I was asked whether I can resolve " + aURI);
				}

                if (aURI.StartsWith("file:") || (baseURI!=null && baseURI.StartsWith("file:")))
				{
					if (logger.IsDebugEnabled)
					{
						logger.Debug("I state that I can resolve " + aURI);
					}

					return true;
				}

                //TODO Bu kod ne i�e yar�yor anlamad�m.
               
                FileInfo testFile = new FileInfo(aURI);
				if (testFile.Exists)
				{
					return true;
				}

                testFile = new FileInfo(baseURI + Path.DirectorySeparatorChar + aURI);
				if (testFile.Exists)
				{
					return true;
				}

				Uri birlesikURI = new Uri(aBaglam.BaseURI, aURI);
                testFile = new FileInfo(birlesikURI.ToString());
				if (testFile.Exists)
				{
					return true;
				}

			}
			catch (Exception exc)
			{
                Console.WriteLine(exc.Message);
			}

			logger.Debug("But I can't");

			return false;
		}

		public virtual Document resolve(string aURI, Context aContext)
		{
			try
			{
                FileInfo testFile = new FileInfo(aURI);
				if (!testFile.Exists)
				{
				    String uri = aContext.BaseURIStr + Path.DirectorySeparatorChar + aURI;
				    uri = uri.Substring(8);
                    testFile = new FileInfo(uri);
				}
				if (!testFile.Exists)
				{
                    Uri birlesikURI = new Uri(aContext.BaseURI, aURI);
                    testFile = new FileInfo(birlesikURI.ToString());
					if (!testFile.Exists)
					{
                        // get the scheme-specific part which is everything after colon
                        // ps: scheme name comes before colon
                        testFile = new FileInfo(birlesikURI.ToString().Substring(birlesikURI.ToString().IndexOf(":")+1));
                        if(!testFile.Exists)
						    throw new IOException(I18n.translate("resolver.cantResolveUri", aURI));
					}
				}

				return new FileDocument(testFile, null);
			}
			catch (Exception e)
			{
				logger.Error(I18n.translate("resolver.cantResolveUri", aURI), e);
				throw new IOException(I18n.translate("resolver.cantResolveUri", aURI));
			}
		}

        /*
		static void Main(string[] args)
		{
			
			FileResolver bulucu = new FileResolver();
			Context baglam = new Context();
			baglam.setBaseURI(new URI("C://ahmet/prj/Sunucu/XMLSIG/xml-security-1_4_2/data/at/iaik/ixsil/coreFeatures/signatures/manifestSignature.xml"));
	
			bulucu.resolve("../samples/sampleXMLData.xml", baglam);
			
		}*/
	}

}