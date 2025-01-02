using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Com.Objsys.Asn1.Runtime;

namespace tr.gov.tubitak.uekae.esya.api.asn.cvc
{
    public class SignatureAndPukRemainder : Asn1Type
    {
        public static readonly Asn1Tag TAG =
		      new Asn1Tag (Asn1Tag.APPL, Asn1Tag.CONS, 33);
        
        public Asn1OctetString signature;
		public Asn1OctetString puKRemainder; 

		public SignatureAndPukRemainder () : base() {
            Init();
		}

		/**
		* This constructor sets all elements to references to the 
		* given objects
		*/
		public SignatureAndPukRemainder (
		    Asn1OctetString signature_,
		    Asn1OctetString puKRemainder_
		) : base() {
		    
		    signature = signature_;
		    puKRemainder = puKRemainder_;
		}
		  

		/**
		* This constructor allows primitive data to be passed for all 
		* primitive elements.  It will create new object wrappers for 
		* the primitive data and set other elements to references to 
		* the given objects 
		*/
		public SignatureAndPukRemainder (
				byte[] signature_,
		    byte[] puKRemainder_
		): base()
		{		   
		    signature = new Asn1OctetString (signature_);
		    puKRemainder = new Asn1OctetString (puKRemainder_);
		}


        public void Init() 
		{
		    signature = null;
		    puKRemainder = null;
		}

		public override void Decode(Asn1BerDecodeBuffer buffer, bool explicitTagging, int implicitLength)		      
		{

            Init();
			   
		    {
			    Asn1Tag signatureTAG =  new Asn1Tag (Asn1Tag.APPL, Asn1Tag.CONS, 55);
                int llen = MatchTag(buffer, signatureTAG);
		        int offset = buffer.ByteCount;
			    signature = new Asn1OctetString();
			    signature.Decode(buffer, false, llen);
		    }
			   
		    {
			    Asn1Tag pukRemainderTAG =  new Asn1Tag (Asn1Tag.APPL, Asn1Tag.CONS, 56);
                int llen = MatchTag(buffer, pukRemainderTAG);
		        int offset = buffer.ByteCount;
			    puKRemainder = new Asn1OctetString();
                puKRemainder.Decode(buffer, false, llen);
		    }
		    
	    }

        public override int Encode(Asn1BerEncodeBuffer buffer, bool explicitTagging)		     
		{
		    int _aal = 0, len;


		    // encode puKRemainder

		    if (puKRemainder != null) {
		        len = puKRemainder.Encode (buffer, false);
		        len += buffer.EncodeTagAndLength (Asn1Tag.APPL, Asn1Tag.PRIM, 56, len);
		        _aal += len;
		    }

		    // encode signature

		    len = signature.Encode (buffer, false);
		    len += buffer.EncodeTagAndLength (Asn1Tag.APPL, Asn1Tag.PRIM, 55, len);
		    _aal += len;


		    return (_aal);
		}

        public override void Print(System.IO.TextWriter _out,
                                  string _varName, int _level)
        {            
            Indent(_out, _level);
            _out.WriteLine(_varName + " {");
            if (signature != null) signature.Print(_out, "signature", _level + 1);
            if (puKRemainder != null) puKRemainder.Print(_out, "puKRemainder", _level + 1);
            Indent(_out, _level);
            _out.WriteLine("}");
        }
		  
    }
}
