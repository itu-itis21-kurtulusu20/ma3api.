using System;
using System.Runtime.CompilerServices;
using System.Collections.Generic;
using xml_signature.tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver
{

    using Logger = log4net.ILog;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Config = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.Config;
	using ResolversConfig = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ResolversConfig;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
	using Reference = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;


	/// <summary>
	/// @author ahmety
	/// date: May 15, 2009
	/// </summary>
	public static class Resolver
	{
	    private static IList<IResolver> msResolvers = new SynchronizedList<IResolver>();
		private static bool msInited = false;

		private static Logger logger = log4net.LogManager.GetLogger(typeof(Resolver));

		public static Document resolve(Reference aReference, Context aContext)
		{
			return resolve(aReference.URI, aContext);
		}

		[MethodImpl(MethodImplOptions.Synchronized)]
		private static void init(Config aConfig)
		{
			ResolversConfig rc = aConfig.ResolversConfig;
			IList<Type> classes = rc.Resolvers;
			foreach (Type clazz in classes)
			{
				try
				{
				    IResolver resolver =  (IResolver)Activator.CreateInstance(clazz);
					msResolvers.Add(resolver);
				}
				catch (Exception exc)
				{
					logger.Error("Cant init resolver: " + clazz,exc);
				}
			}
			msInited = true;
		}

        [MethodImpl(MethodImplOptions.Synchronized)]
		public static Document resolve(string aUri, Context aContext)
		{
			logger.Debug("Resolver.resolve: '" + aUri + "'");

			if (!msInited)
			{
					init(aContext.Config);
			}
                       

			lock (msResolvers)
			{
				if (aContext.Resolvers != null)
				{
					((SynchronizedList<IResolver>)msResolvers).AddRange(aContext.Resolvers);
				}

				try
				{
					for (int i = 0; i < msResolvers.Count; i++)
					{
						IResolver resolver = msResolvers[i];
						if (resolver.isResolvable(aUri, aContext))
						{
							logger.Debug("Resolver.resolver: '" + resolver + "'");

							try
							{
								return resolver.resolve(aUri, aContext);
							}
							catch (Exception x)
							{
								logger.Error("Resolver " + resolver.GetType() + " return error while resolving: '" + aUri + "' : " + x.Message);
							}
						}
					}
				}
				finally
				{
					if (aContext.Resolvers != null)
					{
					    List<IResolver> resolvers = aContext.Resolvers;
					    foreach (IResolver resolver in resolvers)
					    {
					        msResolvers.Remove(resolver);
					    }
					}
				}
			}
			logger.Info("Cant resolve reference!");
			throw new XMLSignatureException("resolver.cantFindResolverForUri", aUri);
		}

	}

}