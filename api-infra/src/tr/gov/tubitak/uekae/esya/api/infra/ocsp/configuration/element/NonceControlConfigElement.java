package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element;

import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;

public class NonceControlConfigElement extends AbstractConfigElement {
    private OcspConfigTags[] tags = new OcspConfigTags[]{OcspConfigTags.NONCECHECK, OcspConfigTags.NONCECACHETIME};

    public NonceControlConfigElement() {
    }

    public NonceControlConfigElement(boolean nonceCheck, int nonceCacheTime) {
        map.put(OcspConfigTags.NONCECHECK, Boolean.toString(nonceCheck));
        map.put(OcspConfigTags.NONCECACHETIME, Integer.toString(nonceCacheTime));
    }

    @Override
    public OcspConfigTags getRootTag() {
        return OcspConfigTags.NONCECONTROL;
    }

    @Override
    public OcspConfigTags[] getElementNames() {
        return tags;
    }

    @Override
    public boolean isMultiple() {
        return true;
    }

    public boolean isNonceCheck(){
        return Boolean.parseBoolean(map.get(OcspConfigTags.NONCECHECK));
    }

    public int getNonceCacheTime(){
        return Integer.parseInt(map.get(OcspConfigTags.NONCECACHETIME));
    }
}
