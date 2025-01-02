package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1Tag;
import com.objsys.asn1j.runtime.Asn1Type;

public class SignatureAndPukRemainder extends Asn1Type
{
	public final static Asn1Tag TAG =
		      new Asn1Tag (Asn1Tag.APPL, Asn1Tag.CONS, 33);

		   static {
		   }

		   public Asn1OctetString signature;
		   public Asn1OctetString puKRemainder; 

		   public SignatureAndPukRemainder () {
		      super();
		      init();
		   }

		   /**
		    * This constructor sets all elements to references to the 
		    * given objects
		    */
		   public SignatureAndPukRemainder (
		      Asn1OctetString signature_,
		      Asn1OctetString puKRemainder_
		   ) {
		      super();
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
		   )
		   {
		      super();
		      signature = new Asn1OctetString (signature_);
		      puKRemainder = new Asn1OctetString (puKRemainder_);
		   }



		   public void init () 
		   {
		      signature = null;
		      puKRemainder = null;
		   }

		   public void decode
		      (Asn1BerDecodeBuffer buffer, boolean explicit, int implicitLength)
		      throws Asn1Exception, java.io.IOException
		   {
			   
			   init ();
			   
			   {
				   Asn1Tag signatureTAG =  new Asn1Tag (Asn1Tag.APPL, Asn1Tag.CONS, 55);
				   int llen =  matchTag (buffer, signatureTAG);
				   int offset = buffer.getByteCount();
				   signature = new Asn1OctetString();
				   signature.decode (buffer, false, llen);
			   }
			   
			   {
				   Asn1Tag pukRemainderTAG =  new Asn1Tag (Asn1Tag.APPL, Asn1Tag.CONS, 56);
				   int llen =  matchTag (buffer, pukRemainderTAG);
				   int offset = buffer.getByteCount();
				   puKRemainder = new Asn1OctetString();
				   puKRemainder.decode (buffer, false, llen);
			   }

		    
		   }

		   public int encode (Asn1BerEncodeBuffer buffer, boolean explicit)
		      throws Asn1Exception
		   {
		      int _aal = 0, len;


		      // encode puKRemainder

		      if (puKRemainder != null) {
		         len = puKRemainder.encode (buffer, false);
		         len += buffer.encodeTagAndLength (Asn1Tag.APPL, Asn1Tag.PRIM, 56, len);
		         _aal += len;
		      }

		      // encode signature

		      len = signature.encode (buffer, false);
		      len += buffer.encodeTagAndLength (Asn1Tag.APPL, Asn1Tag.PRIM, 55, len);
		      _aal += len;


		      return (_aal);
		   }

		  /* public void print (java.io.PrintStream _out, String _varName, int _level)
		   {
		      indent (_out, _level);
		      _out.println (_varName + " {");
		      if (signature != null) signature.print (_out, "signature", _level+1);
		      if (puKRemainder != null) puKRemainder.print (_out, "puKRemainder", _level+1);
		      indent (_out, _level);
		      _out.println ("}");
		   }*/
}
