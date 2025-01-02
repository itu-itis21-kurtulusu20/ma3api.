using System;
using System.Collections.Generic;
using System.Reflection;
using System.Runtime.CompilerServices;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;
using tr.gov.tubitak.uekae.esya.api.smartcard.config;
using log4net;


//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11
{
    public class CardType
    {
        protected static readonly IDictionary<String, CardType> cards = new SortedDictionary<String, CardType>();
        protected static readonly Dictionary<String, List<String>> cardATRs = new Dictionary<String, List<String>>();

        private static readonly Boolean isPlatform32Bit = IntPtr.Size.Equals(4);

        public static readonly CardType AKIS = new CardType("akisp11", typeof(AkisTemplate), "AKIS", Application.ESIGNATURE);
        public static readonly CardType AKIS_KK = new CardType("tckkp11", typeof(AkisTemplate), "AKIS_KK", Application.TCKK);

        public static readonly CardType CARDOS = new CardType("cmp11", typeof(CardosTemplate), "CARDOS", Application.ESIGNATURE);
        public static readonly CardType ALADDIN = new CardType("eTPkcs11", typeof(AladdinTemplate), "ALADDIN", Application.ESIGNATURE);
        public static readonly CardType DATAKEY = new CardType("dkck201", typeof(DataKeyTemplate), "DATAKEY", Application.ESIGNATURE);
        public static readonly CardType GEMPLUS = new CardType("gclib", typeof(GemPlusTemplate), "GEMPLUS", Application.ESIGNATURE);
        public static readonly CardType KEYCORP = new CardType("spmp1132", typeof(KeyCorpTemplate), "KEYCORP", Application.ESIGNATURE);
        public static readonly CardType NCIPHER = new CardType(IntPtr.Size.Equals(4) ? "cknfast" : "cknfast-64", typeof(NCipherTemplate), "NCIPHER", Application.ESIGNATURE);
        public static readonly CardType SAFESIGN = new CardType("aetpkss1", typeof(SafeSignTemplate), "SAFESIGN", Application.ESIGNATURE);
        public static readonly CardType SEFIROT = new CardType("pkcs11sfr", typeof(SefirotTemplate), "SEFIROT", Application.ESIGNATURE);
        public static readonly CardType AEPKEYPER = new CardType("bp201w32HSM", typeof(AEPTemplate), "AEPKEYPER", Application.ESIGNATURE);
        public static readonly CardType UTIMACO = new CardType("cs2_pkcs11", typeof(UtimacoTemplate), "UTIMACO", Application.ESIGNATURE);
        public static readonly CardType TKART = new CardType("siecap11", typeof(TKartTemplate), "TKART", Application.ESIGNATURE);
        public static readonly CardType NETID = new CardType("iidp11", typeof(DefaultCardTemplate), "Net-ID", Application.ESIGNATURE);
        public static readonly CardType SAFENET = new CardType("cryptoki", typeof(SafenetTemplate), "SAFENET", Application.ESIGNATURE);
        public static readonly CardType DIRAKHSM = new CardType(IntPtr.Size.Equals(8) ? "dirakp11-64" : "dirakp11-32", typeof(DirakHSMTemplate), "DiRAK_HSM", Application.ESIGNATURE);
        public static readonly CardType UNKNOWN = new CardType("", typeof(DefaultCardTemplate), "UNKNOWN", Application.ESIGNATURE);

        public static readonly CardType OPENDNSSOFTHSM = new CardType(IntPtr.Size.Equals(8) ? "softhsm2-x64" : "softhsm2", typeof(DefaultCardTemplate), "OPENDNSSOFTHSM", Application.ESIGNATURE);
        public static readonly CardType PROCENNEHSM = new CardType("procryptoki", typeof(DefaultCardTemplate), "PROCENNEHSM", Application.ESIGNATURE);


        protected static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);



        protected String name;
        protected ICardTemplate template;
        protected String libName;
        protected Type/*<? extends CardTemplate>*/ templateClass;
        protected Application app;

        /**
         * Defines a card
         * @param aLibName library name of card. For akisp11.dll it is akisp11
         * @param aName name of the card. it is used for information.
         */
        public CardType(String aLibName, String aName)
        {
            libName = aLibName;
            name = aName;
            template = new DefaultCardTemplate(this);
            cards.Add(name, this);
        }

        /**
         * Defines a card
         * @param aLibName library name of card. For akisp11.dll it is akisp11
         * @param aTemplateClass 
         * @param aName name of the card. it is used for information.
         */
        public CardType(String aLibName, /*Class<? extends CardTemplate>*/Type aTemplateClass, String aName, Application aApplication)
        {
            libName = aLibName;
            templateClass = aTemplateClass;
            name = aName;
            app = aApplication;
            cards.Add(name, this);
        }

        /// @see applyCardTypes
        private CardType(CardTypeConfig config)
        {

            setLibName(config.getLib(), config.getLib32(), config.getLib64());
            template = new DefaultCardTemplate(this);
            name = config.getName();
            app = Application.ESIGNATURE;
            cards.Add(name, this);
        }

        private void setLibName(String aLibName, String aLib32Name, String aLib64Name)
        {
            libName = aLibName;
            if (aLib32Name != null && isPlatform32Bit)
                libName = aLib32Name;
            if (aLib64Name != null && !isPlatform32Bit)
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
         * Returns application
         * @return
         */
        public Application getApplication()
        {
            return app;
        }
        /**
         * Returns template of card
         * @return
         */
        [MethodImpl(MethodImplOptions.Synchronized)]
        public ICardTemplate getCardTemplate()
        {
            if (template == null)
            {
                ConstructorInfo cons = templateClass.GetConstructor(new Type[0]);
                if (cons != null)
                {
                    template = cons.Invoke(null) as ICardTemplate;
                }
                else
                {
                    cons = templateClass.GetConstructor(new Type[] { typeof(CardType) });
                    template = cons.Invoke(new object[] { this }) as ICardTemplate;
                }
            }
            return template;
        }
        /**
         * Returns all card types
         * @return
         */
        public static CardType[] getCardTypes()
        {
            CardType[] types = new CardType[cards.Count];
            cards.Values.CopyTo(types, 0);
            return types;
        }
        /**
         * Returns card type which recognized from atr value and application
         * @param aATRHex
         * @param aApp
         * @return
         */
        public static CardType getCardTypeFromATR(String aATRHex, Application aApplication)
        {
            foreach (CardType card in cards.Values)
            {
                if (aApplication == card.getApplication())
                {
                    List<String> configurationATRs;
                    cardATRs.TryGetValue(card.getName(), out configurationATRs);

                    if (configurationATRs != null)
                    {
                        foreach (String s in configurationATRs)
                        {
                            if (s.Equals(aATRHex, StringComparison.OrdinalIgnoreCase))
                                return card;
                        }
                    }

                    String[] hashes = null;
                    try
                    {
                        hashes = card.getCardTemplate().getATRHashes();
                    }
                    catch (SystemException e)
                    {
                        logger.Error("Can not create card template", e);
                    }

                    if (hashes == null) continue;
                    foreach (String s in hashes)
                    {
                        if (s.Equals(aATRHex, StringComparison.OrdinalIgnoreCase))
                            return card;
                    }
                }
            }

            if (AkisTemplate.isAkisATR(aATRHex))
            {
                if (aApplication == Application.TCKK)
                    return CardType.AKIS_KK;
                else
                    return CardType.AKIS;

            }

            return CardType.UNKNOWN;
        }
        /**
         * Returns card type which recognized from lib name
         * @param aLibName
         * @return
         */
        public static CardType getCardType(String aLibName)
        {
            foreach (CardType card in cards.Values)
            {
                if (card.getLibName().Equals(aLibName))
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
        public static void applyCardTypeConfig(List<CardTypeConfig> config)
        {
            foreach (CardTypeConfig cardTypeConfig in config)
            {
                if (cards.ContainsKey(cardTypeConfig.getName()))
                {
                    // update known card
                    CardType cardType;
                    cards.TryGetValue(cardTypeConfig.getName(), out cardType);
                    cardType.setLibName(cardTypeConfig.getLib(), cardTypeConfig.getLib32(), cardTypeConfig.getLib64());
                    cardType.getCardTemplate().getATRHashes();
                    cardATRs.Add(cardType.getName(), cardTypeConfig.getAtrs());

                }
                else
                {
                    // add card type to known cards
                    new CardType(cardTypeConfig);
                }
            }
        }
        /**
         * Returns name of card as string
         * @return
         */

        public override String ToString()
        {
            return getName();
        }

        public enum Application
        {
            TCKK,
            ESIGNATURE,
            EPASSPORT
        }


        /*
        public static readonly CardType AKIS = new CardType(_enum.AKIS);
        public static readonly CardType CARDOS = new CardType(_enum.CARDOS);
        public static readonly CardType DATAKEY = new CardType(_enum.DATAKEY);
        public static readonly CardType GEMPLUS = new CardType(_enum.GEMPLUS);
        public static readonly CardType KEYCORP = new CardType(_enum.KEYCORP);
        public static readonly CardType NCIPHER = new CardType(_enum.NCIPHER);

        public static readonly CardType SAFESIGN = new CardType(_enum.SAFESIGN);
        public static readonly CardType SEFIROT = new CardType(_enum.SEFIROT);
        public static readonly CardType AEPKEYPER = new CardType(_enum.AEPKEYPER);
        public static readonly CardType UTIMACO = new CardType(_enum.UTIMACO);

        public static readonly CardType TKART = new CardType(_enum.TKART);
        public static readonly CardType UNKNOWN = new CardType(_enum.UNKNOWN);
        enum _enum
        {
            AKIS,
            CARDOS,
            DATAKEY,
            GEMPLUS,
            KEYCORP,
            NCIPHER,
            SAFESIGN,
            SEFIROT,
            AEPKEYPER,
            UTIMACO,
            TKART,
            UNKNOWN
        }

        private _enum mValue;
        private CardType(_enum aValue)
        {
            mValue = aValue;
        }
        public static CardType getCardType(String aLibName)
        {
            CardType kt = CardType.UNKNOWN;

            if (aLibName.Equals("gclib", StringComparison.OrdinalIgnoreCase))
                kt = CardType.GEMPLUS;
            else if (aLibName.Equals("dkck201", StringComparison.OrdinalIgnoreCase))
                kt = CardType.DATAKEY;
            else if (aLibName.Equals("akisp11", StringComparison.OrdinalIgnoreCase))
                kt = CardType.AKIS;
            else if (aLibName.Equals("pkcs11sfr", StringComparison.OrdinalIgnoreCase))
                kt = CardType.SEFIROT;
            else if (aLibName.Equals("aetpkss1", StringComparison.OrdinalIgnoreCase))
                kt = CardType.SAFESIGN;
            else if (aLibName.Equals("spmp1132", StringComparison.OrdinalIgnoreCase))
                kt = CardType.KEYCORP;
            else if (aLibName.Equals("cmp11", StringComparison.OrdinalIgnoreCase))
                kt = CardType.CARDOS;
            else if (aLibName.Equals("cknfast", StringComparison.OrdinalIgnoreCase))
                kt = CardType.NCIPHER;
            else if (aLibName.Equals("bp201w32HSM", StringComparison.OrdinalIgnoreCase))
                kt = CardType.AEPKEYPER;
            else if (aLibName.Equals("cs2_pkcs11", StringComparison.OrdinalIgnoreCase))
                kt = CardType.UTIMACO;
            else if (aLibName.Equals("siecap11", StringComparison.OrdinalIgnoreCase))
                kt = CardType.TKART;
            return kt;
        }
        public static String getCardDll(CardType aCardType)
        {
            String dll;

            switch (aCardType.mValue)
            {
                case _enum.AKIS: dll = "akisp11"; break;
                case _enum.CARDOS: dll = "cmp11"; break;
                case _enum.DATAKEY: dll = "dkck201"; break;
                case _enum.GEMPLUS: dll = "gclib"; break;
                case _enum.KEYCORP: dll = "spmp1132"; break;
                case _enum.NCIPHER: dll = "cknfast"; break;
                case _enum.SAFESIGN: dll = "aetpkss1"; break;
                case _enum.SEFIROT: dll = "pkcs11sfr"; break;
                case _enum.AEPKEYPER: dll = "bp201w32HSM"; break;
                case _enum.UTIMACO: dll = "cs2_pkcs11"; break;
                case _enum.TKART: dll = "siecap11"; break;
                default: dll = ""; break;
            }

            return dll;
        }*/

    };


}
