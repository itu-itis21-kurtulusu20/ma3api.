package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.smartcard.config.CardTypeConfig;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template.*;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class CardType implements Serializable
{
    protected static Map<String, CardType> cards = new TreeMap<String, CardType>();
    protected static Map<String, List<String>> cardATRs = new HashMap<String, List<String>>();

	public static final String JVM_BITSIZE= System.getProperty("sun.arch.data.model");
	
	public static final CardType AKIS = new CardType("akisp11", AkisTemplate.class, "AKIS", Application.ESIGNATURE);
	public static final CardType AKIS_KK = new CardType("tckkp11", AkisTemplate.class, "AKIS_KK", Application.TCKK);
	
	public static final CardType CARDOS = new CardType("cmp11", CardosTemplate.class, "CARDOS", Application.ESIGNATURE);
	public static final CardType ACS = new CardType("acospkcs11", AcsTemplate.class, "ACS", Application.ESIGNATURE);
	public static final CardType ALADDIN = new CardType("eTPkcs11", AladdinTemplate.class, "ALADDIN", Application.ESIGNATURE);
	public static final CardType DATAKEY = new CardType("dkck201", DataKeyTemplate.class, "DATAKEY", Application.ESIGNATURE);
	public static final CardType GEMPLUS = new CardType("gclib", GemPlusTemplate.class, "GEMPLUS", Application.ESIGNATURE);
	public static final CardType KEYCORP = new CardType("spmp1132", KeyCorpTemplate.class, "KEYCORP", Application.ESIGNATURE);
	public static final CardType NCIPHER = new CardType("cknfast","cknfast-64", NCipherTemplate.class, "NCIPHER", Application.ESIGNATURE,true);
	public static final CardType SAFESIGN = new CardType("aetpkss1", SafeSignTemplate.class, "SAFESIGN", Application.ESIGNATURE,true);
	public static final CardType SEFIROT = new CardType("pkcs11sfr", SefirotTemplate.class, "SEFIROT", Application.ESIGNATURE);
	public static final CardType AEPKEYPER = new CardType("bp201w32HSM", AEPTemplate.class, "AEPKEYPER", Application.ESIGNATURE);
	public static final CardType UTIMACO = new CardType("cs2_pkcs11", UtimacoTemplate.class, "UTIMACO", Application.ESIGNATURE,true);
	public static final CardType UTIMACO_R2 = new CardType("cs_pkcs11_R2", UtimacoTemplateR2.class, "UTIMACO_R2", Application.ESIGNATURE,true);
	public static final CardType TKART = new CardType("siecap11", TKartTemplate.class, "TKART", Application.ESIGNATURE);
	public static final CardType ATIKKG = new CardType("atikKg_jni",  AtikKGTemplate.class, "ATIKKG", Application.ESIGNATURE);
	public static final CardType SAFENET = new CardType("cryptoki", SafenetTemplate.class,"SAFENET", Application.ESIGNATURE,true);
	public static final CardType ATIKHSM = new CardType("uhsmpkcs11","uhsmpkcs11x64", ATIKHSMTemplate.class,"ATIKHSM", Application.ESIGNATURE);
	public static final CardType DIRAKHSM = new CardType("dirakp11-32","dirakp11-64", DirakHSMTemplate.class, "DiRAK_HSM", Application.ESIGNATURE, true);
	public static final CardType OPENDNSSOFTHSM= new CardType("softhsm2","softhsm2-x64", DefaultCardTemplate.class, "OPENDNSSEC SOFTHSMv2", Application.ESIGNATURE);
	public static final CardType PROCENNEHSM= new CardType("procryptoki", DefaultCardTemplate.class, "PROCENNE_HSM", Application.ESIGNATURE);
	public static final CardType NETID = new CardType("iidp11", DefaultCardTemplate.class, "Net-ID", Application.ESIGNATURE);
	public static final CardType UNKNOWN = new CardType("",  DefaultCardTemplate.class, "UNKNOWN", Application.ESIGNATURE);
	public static final CardType GEMALTO = new CardType("idprimepkcs11","idprimepkcs1164", DefaultCardTemplate.class, "GEMALTO IDPRIME", Application.ESIGNATURE);

	//public static final CardType MULTOS = new CardType("TSMP_PKCS11",  DefaultCardTemplate.class, "MULTOS", Application.ESIGNATURE);
	
	private static Logger logger = LoggerFactory.getLogger(CardType.class);

	protected String name;
	protected ICardTemplate template;
	protected String libName;
	protected String libNamex32;
	protected String libNamex64;
	protected boolean supportsWrapUnwrap;
	protected Class<? extends CardTemplate> templateClass;
	protected Application app;
	/**
	 * Defines a card
	 * @param aLibName library name of card. For akisp11.dll it is akisp11
	 * @param aName name of the card. it is used for information.
	 */
	public CardType(String aLibName, String aName)
	{
		libName = aLibName;
		libNamex32 = aLibName;
		libNamex64=aLibName;
		name = aName;
		template = new DefaultCardTemplate(this);
		cards.put(name, this);
	}
	
	/**
	 * Defines a card
	 * @param aLibName library name of card. For akisp11.dll it is akisp11
	 * @param aTemplateClass 
	 * @param aName name of the card. it is used for information.
	 */
	public CardType(String aLibName, Class<? extends CardTemplate> aTemplateClass, String aName, Application aApp)
	{
		libNamex32 = aLibName;
		libNamex64=aLibName;
		libName = aLibName;
		templateClass = aTemplateClass;
		name = aName;
		app = aApp;
		cards.put(name, this);
	}

	/**
	 * Defines a card
	 * @param aTemplateClass
	 * @param aName name of the card. it is used for information.
	 */
	public CardType(String a32BitLibName,String a64BitLibName, Class<? extends CardTemplate> aTemplateClass, String aName, Application aApp)
	{
		libNamex32 = a32BitLibName;
		libNamex64=a64BitLibName;
		libName = "32".equals(JVM_BITSIZE) ? a32BitLibName : a64BitLibName;
		templateClass = aTemplateClass;
		name = aName;
		app = aApp;
		cards.put(name, this);
	}

	/**
	 * Defines a card
	 * @param aTemplateClass
	 * @param aName name of the card. it is used for information.
	 */
	public CardType(String a32BitLibName,String a64BitLibName, Class<? extends CardTemplate> aTemplateClass, String aName, Application aApp,boolean supportsWrapUnwrap)
	{
		libNamex32 = a32BitLibName;
		libNamex64=a64BitLibName;
		libName = "32".equals(JVM_BITSIZE) ? a32BitLibName : a64BitLibName;
		templateClass = aTemplateClass;
		name = aName;
		app = aApp;
		this.supportsWrapUnwrap = supportsWrapUnwrap;
		cards.put(name, this);
	}

	public CardType(String aLibName, Class<? extends CardTemplate> aTemplateClass, String aName, Application aApp,boolean supportsWrapUnwrap)
	{
		libNamex32 = aLibName;
		libNamex64=aLibName;
		libName = aLibName;
		templateClass = aTemplateClass;
		name = aName;
		app = aApp;
		this.supportsWrapUnwrap = supportsWrapUnwrap;
		cards.put(name, this);
	}
	public String getLibNamex64() {
		return libNamex64;
	}

	/// @see applyCardTypes
    private CardType(CardTypeConfig config){

        setLibName(config.getLib(), config.getLib32(), config.getLib64());
        templateClass = DefaultCardTemplate.class;
        name = config.getName();
        app = Application.ESIGNATURE;
        cards.put(name, this);
    }

    private void setLibName(String aLibName, String aLib32Name, String aLib64Name){
        libName = aLibName;
		libNamex32 = aLib32Name;
		libNamex64 = aLib64Name;
		if (aLib32Name != null && "32".equals(JVM_BITSIZE))
            libName = aLib32Name;
		if (aLib64Name != null && "64".equals(JVM_BITSIZE))
            libName = aLib64Name;
    }

    /**
	 * Returns name of card
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * Returns library name of card
	 * @return
	 */
	public String getLibName()
	{
		return libName;
	}
	/**
	 * Returns template of card
	 * @return
	 */
	public boolean isSupportsWrapUnwrap() {
		return supportsWrapUnwrap;
	}

	public void setSupportsWrapUnwrap(boolean supportsWrapUnwrap) {
		this.supportsWrapUnwrap = supportsWrapUnwrap;
	}

	public synchronized ICardTemplate getCardTemplate()
	{
		if(template == null)
		{
			try 
			{
				if(AkisTemplate.class.equals(templateClass) || DefaultCardTemplate.class.equals(templateClass)){
					Constructor<? extends CardTemplate> cons = templateClass.getConstructor(CardType.class);
					template = cons.newInstance(this);
				} else {
					Constructor<? extends CardTemplate> cons = templateClass.getConstructor(null);
					template = cons.newInstance(null);
				}
			} 
			catch (Exception e) 
			{
				throw new ESYARuntimeException(e);
			}
		}
		return template;
	}
	/**
	 * Returns all card types
	 * @return
	 */
	public static CardType [] getCardTypes()
	{
		return cards.values().toArray(new CardType[cards.size()]);
	}
	/**
	 * Returns card type which recognized from atr value and application
	 * @param aATRHex
	 * @param aApp
	 * @return
	 */
	public static CardType getCardTypeFromATR(String aATRHex, Application aApp)
	{
		for(CardType card : cards.values())
		{
			if(card.getApplication().equals(aApp))
			{
                List<String> configurationATRs = cardATRs.get(card.getName());
                if (configurationATRs!=null){
                    for(String s:configurationATRs) {
                        if(s.equalsIgnoreCase(aATRHex))
                            return card;
                    }
                }

				String[] hashes = null;
				try {
					hashes = card.getCardTemplate().getATRHashes();
				} 
				catch (RuntimeException e) {
					logger.error("Can not create card template", e);
				}
				if(hashes==null) continue;
				for(String s : hashes) {
					if(s.equalsIgnoreCase(aATRHex))
						return card;
				}
			}
		}

		if(AkisTemplate.isAkisATR(aATRHex)){
			if(aApp == Application.TCKK)
				return CardType.AKIS_KK;
			else
				return CardType.AKIS;
		}
		
		return CardType.UNKNOWN;
	}

	/**
	 * Returns card type which recognized from lib name
	 *
	 * @param aLibName
	 * @return
	 */
	public static CardType getCardType(String aLibName) {
		for (CardType card : cards.values()) {
			if (card.getLibName()!=null && card.getLibName().equals(aLibName))
				return card;
			if (card.getLibNamex64()!=null && card.getLibNamex64().equals(aLibName))
				return card;
			if (card.getLibNamex32()!=null && card.getLibNamex32().equals(aLibName))
				return card;
		}

		return CardType.UNKNOWN;
	}

    /**
     * Returns card type which recognized from lib name
     * @param aCardName
     * @return
     */
    public static CardType getCardTypeFromName(String aCardName)
    {
        for (CardType card : cards.values())
        {
            if(card.getName().equals(aCardName))
                return card;
        }

        return CardType.UNKNOWN;
    }


    /**
     * This method either adds card type to known types
     * or updates current one according to CardTypeConfig.name
     *
     * @see tr.gov.tubitak.uekae.esya.api.smartcard.config.SmartCardConfigParser
     * @param config
     */
    public static void applyCardTypeConfig(List<CardTypeConfig> config){
        for (CardTypeConfig cardTypeConfig : config){
            if (cards.containsKey(cardTypeConfig.getName())){
                // update known card
                CardType cardType = cards.get(cardTypeConfig.getName());
                cardType.setLibName(cardTypeConfig.getLib(), cardTypeConfig.getLib32(), cardTypeConfig.getLib64());
                cardType.getCardTemplate().getATRHashes();
                cardATRs.put(cardType.getName(), cardTypeConfig.getAtrs());

            } else {
                // add card type to known cards
                new CardType(cardTypeConfig);
            }
        }
    }


    /**
	 * Returns name of card as string
	 * @return
	 */
	public String toString() 
	{
		return getName();
	}
	/**
	 * Returns application
	 * @return
	 */
	public Application getApplication()
	{
		return app;	
	}

    public static void main(String[] args) {
        CardType[] arr = getCardTypes();
        for (CardType ct : arr){
            //System.out.println(ct.getName());
        }
        System.out.println(getCardType("cs2_pkcs11"));
    }


	public String getLibNamex32() {
		return libNamex32;
	}
}
