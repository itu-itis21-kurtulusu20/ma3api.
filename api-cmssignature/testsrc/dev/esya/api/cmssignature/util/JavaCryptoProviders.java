package dev.esya.api.cmssignature.util;

import org.junit.Test;

import java.security.Provider;
import java.security.Security;
import java.util.Set;

/**
 * Created by sura.emanet on 12.02.2018.
 */
public class JavaCryptoProviders
{
    @Test
    public void listJavaCryptoProviders()
    {
        Provider[] providers = Security.getProviders();
        for (Provider aProvider: providers ) {
            Set<Provider.Service> services = aProvider.getServices();
            System.out.println("******Provider: " + aProvider.getName());
            for(Provider.Service aService: services){
                System.out.println(aService.getAlgorithm());
            }
        }
    }
}
