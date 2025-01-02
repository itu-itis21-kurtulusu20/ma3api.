package tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

public class DepoSistemParametresi
{
	public static final String PARAM_DEPO_PAROLA = "DepoParolasiDogrulama";
	
	private String mParamAdi;
	private Object mParamDegeri;
	
	public String getParamAdi()
	{
		return mParamAdi;
	}
	
	public void setParamAdi(String aParamAdi)
	{
		mParamAdi = aParamAdi;
	}
	
	public Object getParamDegeri()
	{
		return mParamDegeri;
	}
	
	public void setParamDegeri(Object aParamDegeri)
	{
		mParamDegeri = aParamDegeri;
	}
}
