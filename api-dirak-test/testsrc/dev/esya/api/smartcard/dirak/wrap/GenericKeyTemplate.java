package dev.esya.api.smartcard.dirak.wrap;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;

public class GenericKeyTemplate extends KeyTemplate {

    protected GenericKeyTemplate(String label) {
        super(label);
    }

    @Override
    public String getAlgorithm() {
        return null;
    }
}
