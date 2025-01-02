package dev.esya.api.cmssignature.jca;

import org.junit.Test;

import java.security.Provider;
import java.security.Security;

public class JavaAlgorithms {

    @Test
    public void listAlgorithms(){
        for (Provider provider : Security.getProviders()) {
            System.out.println("Provider: " + provider.getName());
            for (Provider.Service service : provider.getServices()) {
                String algorithm = service.getAlgorithm();
                System.out.println(algorithm);
            }
        }
    }

}
