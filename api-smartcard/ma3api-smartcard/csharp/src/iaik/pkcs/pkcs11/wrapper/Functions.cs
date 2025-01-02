/* Copyright  (c) 2002 Graz University of Technology. All rights reserved.
*
* Redistribution and use in  source and binary forms, with or without 
* modification, are permitted  provided that the following conditions are met:
*
* 1. Redistributions of  source code must retain the above copyright notice,
*    this list of conditions and the following disclaimer.
*
* 2. Redistributions in  binary form must reproduce the above copyright notice,
*    this list of conditions and the following disclaimer in the documentation
*    and/or other materials provided with the distribution.
*  
* 3. The end-user documentation included with the redistribution, if any, must
*    include the following acknowledgment:
* 
*    "This product includes software developed by IAIK of Graz University of
*     Technology."
* 
*    Alternately, this acknowledgment may appear in the software itself, if 
*    and wherever such third-party acknowledgments normally appear.
*  
* 4. The names "Graz University of Technology" and "IAIK of Graz University of
*    Technology" must not be used to endorse or promote products derived from 
*    this software without prior written permission.
*  
* 5. Products derived from this software may not be called 
*    "IAIK PKCS Wrapper", nor may "IAIK" appear in their name, without prior 
*    written permission of Graz University of Technology.
*  
*  THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
*  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
*  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
*  PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE LICENSOR BE
*  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
*  OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
*  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
*  OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
*  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
*  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
*  OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
*  POSSIBILITY  OF SUCH DAMAGE.
*/
using System;
namespace iaik.pkcs.pkcs11.wrapper
{
	
	
	
	/// <summary> This class contains onyl static methods. It is the place for all functions
	/// that are used by several classes in this package.
	/// 
	/// </summary>
	/// <author>  Karl Scheibelhofer <Karl.Scheibelhofer@iaik.at>
	/// </author>
	/// <author>  Martin Schläffer <schlaeff@sbox.tugraz.at>
	/// </author>
	public class Functions
	{
		
		/// <summary> Maps mechanism codes as Long to their names as Strings.</summary>
		protected internal static System.Collections.Hashtable mechansimNames_;
		
		/// <summary> This table contains the mechanisms that are full encrypt/decrypt
		/// mechanisms; i.e. mechanisms that support the update functoins.
		/// The Long values of the mechanisms are the keys, and the mechanism
		/// names are the values.
		/// </summary>
		protected internal static System.Collections.Hashtable fullEncryptDecryptMechanisms_;
		
		/// <summary> This table contains the mechanisms that are single-operation
		/// encrypt/decrypt mechanisms; i.e. mechanisms that do not support the update
		/// functoins.
		/// The Long values of the mechanisms are the keys, and the mechanism
		/// names are the values.
		/// </summary>
		protected internal static System.Collections.Hashtable singleOperationEncryptDecryptMechanisms_;
		
		/// <summary> This table contains the mechanisms that are full sign/verify
		/// mechanisms; i.e. mechanisms that support the update functoins.
		/// The Long values of the mechanisms are the keys, and the mechanism
		/// names are the values.
		/// </summary>
		protected internal static System.Collections.Hashtable fullSignVerifyMechanisms_;
		
		/// <summary> This table contains the mechanisms that are single-operation
		/// sign/verify mechanisms; i.e. mechanisms that do not support the update
		/// functoins.
		/// The Long values of the mechanisms are the keys, and the mechanism
		/// names are the values.
		/// </summary>
		protected internal static System.Collections.Hashtable singleOperationSignVerifyMechanisms_;
		
		/// <summary> This table contains the mechanisms that are sign/verify mechanisms with
		/// message recovery.
		/// The Long values of the mechanisms are the keys, and the mechanism
		/// names are the values.
		/// </summary>
		protected internal static System.Collections.Hashtable signVerifyRecoverMechanisms_;
		
		/// <summary> This table contains the mechanisms that are digest mechanisms.
		/// The Long values of the mechanisms are the keys, and the mechanism
		/// names are the values.
		/// </summary>
		protected internal static System.Collections.Hashtable digestMechanisms_;
		
		/// <summary> This table contains the mechanisms that key generation mechanisms; i.e.
		/// mechanisms for generating symmetric keys.
		/// The Long values of the mechanisms are the keys, and the mechanism
		/// names are the values.
		/// </summary>
		protected internal static System.Collections.Hashtable keyGenerationMechanisms_;
		
		/// <summary> This table contains the mechanisms that key-pair generation mechanisms;
		/// i.e. mechanisms for generating key-pairs.
		/// The Long values of the mechanisms are the keys, and the mechanism
		/// names are the values.
		/// </summary>
		protected internal static System.Collections.Hashtable keyPairGenerationMechanisms_;
		
		/// <summary> This table contains the mechanisms that are wrap/unwrap mechanisms.
		/// The Long values of the mechanisms are the keys, and the mechanism
		/// names are the values.
		/// </summary>
		protected internal static System.Collections.Hashtable wrapUnwrapMechanisms_;
		
		/// <summary> This table contains the mechanisms that are key derivation mechanisms.
		/// The Long values of the mechanisms are the keys, and the mechanism
		/// names are the values.
		/// </summary>
		protected internal static System.Collections.Hashtable keyDerivationMechanisms_;
		
		/// <summary> For converting numbers to their hex presentation.</summary>
		//UPGRADE_NOTE: Final was removed from the declaration of 'HEX_DIGITS'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
		protected internal static readonly char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
		
		/// <summary> Converts a long value to a hexadecimal String of length 16. Includes
		/// leading zeros if necessary.
		/// 
		/// </summary>
		/// <param name="value">The long value to be converted.
		/// </param>
		/// <returns> The hexadecimal string representation of the long value.
		/// </returns>
		public static System.String toFullHexString(long value_Renamed)
		{
			long currentValue = value_Renamed;
			System.Text.StringBuilder stringBuffer = new System.Text.StringBuilder(16);
			for (int j = 0; j < 16; j++)
			{
				int currentDigit = (int) currentValue & 0xf;
				stringBuffer.Append(HEX_DIGITS[currentDigit]);
				currentValue = SupportClass.URShift(currentValue, 4);
			}
			
			return SupportClass.ReverseString(stringBuffer).ToString();
		}
		
		/// <summary> Converts a int value to a hexadecimal String of length 8. Includes
		/// leading zeros if necessary.
		/// 
		/// </summary>
		/// <param name="value">The int value to be converted.
		/// </param>
		/// <returns> The hexadecimal string representation of the int value.
		/// </returns>
		public static System.String toFullHexString(int value_Renamed)
		{
			int currentValue = value_Renamed;
			System.Text.StringBuilder stringBuffer = new System.Text.StringBuilder(8);
			for (int i = 0; i < 8; i++)
			{
				int currentDigit = currentValue & 0xf;
				stringBuffer.Append(HEX_DIGITS[currentDigit]);
				currentValue = SupportClass.URShift(currentValue, 4);
			}
			
			return SupportClass.ReverseString(stringBuffer).ToString();
		}
		
		/// <summary> Converts a long value to a hexadecimal String.
		/// 
		/// </summary>
		/// <param name="value">The long value to be converted.
		/// </param>
		/// <returns> The hexadecimal string representation of the long value.
		/// </returns>
		public static System.String toHexString(long value_Renamed)
		{
			return System.Convert.ToString(value_Renamed, 16);
		}
		
		/// <summary> Converts a byte array to a hexadecimal String. Each byte is presented by
		/// its two digit hex-code; 0x0A -> "0a", 0x00 -> "00". No leading "0x" is
		/// included in the result.
		/// 
		/// </summary>
		/// <param name="value">the byte array to be converted
		/// </param>
		/// <returns> the hexadecimal string representation of the byte array
		/// </returns>
		public static System.String toHexString(byte[] aValue)
		{
			if (aValue == null)
			{
				return null;
			}
			
			System.Text.StringBuilder buffer = new System.Text.StringBuilder(2 * aValue.Length);
			int single;
			
			for (int i = 0; i < aValue.Length; i++)
			{
				single = aValue[i] & 0xFF;
				
				if (single < 0x10)
				{
					buffer.Append('0');
				}
				
				buffer.Append(System.Convert.ToString(single, 16));
			}
			
			return buffer.ToString();
		}
		
		/// <summary> Converts a long value to a binary String.
		/// 
		/// </summary>
		/// <param name="value">the long value to be converted.
		/// </param>
		/// <returns> the binary string representation of the long value.
		/// </returns>
		public static System.String toBinaryString(long value_Renamed)
		{
			return System.Convert.ToString(value_Renamed, 2);
		}
		
		/// <summary> Converts a byte array to a binary String.
		/// 
		/// </summary>
		/// <param name="value">The byte array to be converted.
		/// </param>
		/// <returns> The binary string representation of the byte array.
		/// </returns>
		public static System.String toBinaryString(byte[] value_Renamed)
		{
            
			//UPGRADE_TODO: Class 'java.math.BigInteger' was converted to 'System.Decimal' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javamathBigInteger'"
			//UPGRADE_ISSUE: Constructor 'java.math.BigInteger.BigInteger' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javamathBigIntegerBigInteger_int_byte[]'"
			//BigInteger helpBigInteger = new BigInteger(1, value_Renamed);

            String binaryString = "";
            for (int index = value_Renamed.Length - 1; index >= 0; index--)   //big/little endian sorunu nedeniyle .net platformunda dizinin son elemanýndan baþlanýyor
            {
                byte b = value_Renamed[index];
                String eachByte = Convert.ToString(b, 2);
                binaryString = binaryString.Insert(0, eachByte);
                if (index != value_Renamed.Length - 1)
                    for (int i = eachByte.Length; i < 8; i++)
                        binaryString = binaryString.Insert(0, "0");

            }
            return binaryString;
			//UPGRADE_ISSUE: Method 'java.math.BigInteger.toString' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javamathBigIntegertoString_int'"
			//return helpBigInteger.toString(2);
           
		}
      
		/// <summary> Converts the long value flags to a SlotInfoFlag string.
		/// 
		/// </summary>
		/// <param name="flags">The flags to be converted.
		/// </param>
		/// <returns> The SlotInfoFlag string representation of the flags.
		/// </returns>
		public static System.String slotInfoFlagsToString(long flags)
		{
			System.Text.StringBuilder buffer = new System.Text.StringBuilder();
			bool notFirst = false;
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_TOKEN_PRESENT) != 0L)
			{
				buffer.Append("CKF_TOKEN_PRESENT");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_REMOVABLE_DEVICE) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_TOKEN_PRESENT");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_HW_SLOT) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_HW_SLOT");
			}
			
			return buffer.ToString();
		}
		
		/// <summary> Converts long value flags to a TokenInfoFlag string.
		/// 
		/// </summary>
		/// <param name="flags">The flags to be converted.
		/// </param>
		/// <returns> The TokenInfoFlag string representation of the flags.
		/// </returns>
		public static System.String tokenInfoFlagsToString(long flags)
		{
			System.Text.StringBuilder buffer = new System.Text.StringBuilder();
			bool notFirst = false;
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_RNG) != 0L)
			{
				buffer.Append("CKF_RNG");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_WRITE_PROTECTED) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_WRITE_PROTECTED");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_LOGIN_REQUIRED) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_LOGIN_REQUIRED");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_USER_PIN_INITIALIZED) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_USER_PIN_INITIALIZED");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_RESTORE_KEY_NOT_NEEDED) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_RESTORE_KEY_NOT_NEEDED");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_CLOCK_ON_TOKEN) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_CLOCK_ON_TOKEN");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_PROTECTED_AUTHENTICATION_PATH) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_PROTECTED_AUTHENTICATION_PATH");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_DUAL_CRYPTO_OPERATIONS) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_DUAL_CRYPTO_OPERATIONS");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_TOKEN_INITIALIZED) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_TOKEN_INITIALIZED");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_SECONDARY_AUTHENTICATION) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_SECONDARY_AUTHENTICATION");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_USER_PIN_COUNT_LOW) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_USER_PIN_COUNT_LOW");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_USER_PIN_FINAL_TRY) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_USER_PIN_FINAL_TRY");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_USER_PIN_LOCKED) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_USER_PIN_LOCKED");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_USER_PIN_TO_BE_CHANGED) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_USER_PIN_TO_BE_CHANGED");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_SO_PIN_COUNT_LOW) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_SO_PIN_COUNT_LOW");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_SO_PIN_FINAL_TRY) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_SO_PIN_FINAL_TRY");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_SO_PIN_LOCKED) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_USER_PIN_FINAL_TRY");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_SO_PIN_TO_BE_CHANGED) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_USER_PIN_LOCKED");
				
				notFirst = true;
			}
			
			return buffer.ToString();
		}
		
		/// <summary> Converts the long value flags to a SessionInfoFlag string.
		/// 
		/// </summary>
		/// <param name="flags">The flags to be converted.
		/// </param>
		/// <returns> The SessionInfoFlag string representation of the flags.
		/// </returns>
		public static System.String sessionInfoFlagsToString(long flags)
		{
			System.Text.StringBuilder buffer = new System.Text.StringBuilder();
			bool notFirst = false;
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_RW_SESSION) != 0L)
			{
				buffer.Append("CKF_RW_SESSION");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_SERIAL_SESSION) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_SERIAL_SESSION");
			}
			
			return buffer.ToString();
		}
		
		/// <summary> Converts the long value state to a SessionState string.
		/// 
		/// </summary>
		/// <param name="state">The state to be converted.
		/// </param>
		/// <returns> The SessionState string representation of the state.
		/// </returns>
		public static System.String sessionStateToString(long state)
		{
			System.String name;
			
			if (state == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKS_RO_PUBLIC_SESSION)
			{
				name = "CKS_RO_PUBLIC_SESSION";
			}
			else if (state == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKS_RO_USER_FUNCTIONS)
			{
				name = "CKS_RO_USER_FUNCTIONS";
			}
			else if (state == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKS_RW_PUBLIC_SESSION)
			{
				name = "CKS_RW_PUBLIC_SESSION";
			}
			else if (state == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKS_RW_USER_FUNCTIONS)
			{
				name = "CKS_RW_USER_FUNCTIONS";
			}
			else if (state == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKS_RW_SO_FUNCTIONS)
			{
				name = "CKS_RW_SO_FUNCTIONS";
			}
			else
			{
				name = "ERROR: unknown session state 0x" + toFullHexString(state);
			}
			
			return name;
		}
		
		/// <summary> Converts the long value flags to a MechanismInfoFlag string.
		/// 
		/// </summary>
		/// <param name="flags">The flags to be converted to a string representation.
		/// </param>
		/// <returns> The MechanismInfoFlag string representation of the flags.
		/// </returns>
		public static System.String mechanismInfoFlagsToString(long flags)
		{
			System.Text.StringBuilder buffer = new System.Text.StringBuilder();
			bool notFirst = false;
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_HW) != 0L)
			{
				buffer.Append("CKF_HW");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_ENCRYPT) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_ENCRYPT");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_DECRYPT) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_DECRYPT");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_DIGEST) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_DIGEST");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_SIGN) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_SIGN");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_SIGN_RECOVER) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_SIGN_RECOVER");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_VERIFY) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_VERIFY");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_VERIFY_RECOVER) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_VERIFY_RECOVER");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_GENERATE) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_GENERATE");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_GENERATE_KEY_PAIR) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_GENERATE_KEY_PAIR");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_WRAP) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_WRAP");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_UNWRAP) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_UNWRAP");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_DERIVE) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_DERIVE");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_EC_F_P) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_EC_F_P");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_EC_F_2M) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_EC_F_2M");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_EC_ECPARAMETERS) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_EC_ECPARAMETERS");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_EC_NAMEDCURVE) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_EC_NAMEDCURVE");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_EC_UNCOMPRESS) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_EC_UNCOMPRESS");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_EC_COMPRESS) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_EC_COMPRESS");
				
				notFirst = true;
			}
			
			if ((flags & iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKF_EXTENSION) != 0L)
			{
				if (notFirst)
				{
					buffer.Append(" | ");
				}
				
				buffer.Append("CKF_EXTENSION");
				
				notFirst = true;
			}
			
			return buffer.ToString();
		}
		
		/// <summary> Converts the long value code of a mechanism to a name.
		/// 
		/// </summary>
		/// <param name="mechansimCode">The code of the mechanism to be converted to a string.
		/// </param>
		/// <returns> The string representation of the mechanism.
		/// </returns>
		public static System.String mechanismCodeToString(long mechansimCode)
		{
			if (mechansimNames_ == null)
			{
				System.Collections.Hashtable mechansimNames = System.Collections.Hashtable.Synchronized(new System.Collections.Hashtable(200));
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_PKCS_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_PKCS_KEY_PAIR_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_PKCS;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_9796] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_9796;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_X_509] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_X_509;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD2_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD2_RSA_PKCS;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD5_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD5_RSA_PKCS;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA1_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA1_RSA_PKCS;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD128_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD128_RSA_PKCS;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD160_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD160_RSA_PKCS;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_PKCS_OAEP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_PKCS_OAEP;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_X9_31_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_X9_31_KEY_PAIR_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_X9_31] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_X9_31;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA1_RSA_X9_31] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA1_RSA_X9_31;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_PKCS_PSS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_PKCS_PSS;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA1_RSA_PKCS_PSS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA1_RSA_PKCS_PSS;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DSA_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DSA_KEY_PAIR_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DSA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DSA;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DSA_SHA1] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DSA_SHA1;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DH_PKCS_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DH_PKCS_KEY_PAIR_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DH_PKCS_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DH_PKCS_DERIVE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_X9_42_DH_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_X9_42_DH_KEY_PAIR_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_X9_42_DH_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_X9_42_DH_DERIVE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_X9_42_DH_HYBRID_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_X9_42_DH_HYBRID_DERIVE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_X9_42_MQV_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_X9_42_MQV_DERIVE;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA256_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA256_RSA_PKCS;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA384_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA384_RSA_PKCS;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA512_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA512_RSA_PKCS;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA256_RSA_PKCS_PSS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA256_RSA_PKCS_PSS;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA384_RSA_PKCS_PSS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA384_RSA_PKCS_PSS;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA512_RSA_PKCS_PSS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA512_RSA_PKCS_PSS;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_ECB;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_MAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_MAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_CBC_PAD;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC4_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC4_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC4] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC4;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_ECB;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_MAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_MAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_CBC_PAD;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES2_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES2_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_ECB;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_MAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_MAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_CBC_PAD;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_ECB;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_MAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_MAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_CBC_PAD;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_OFB64] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_OFB64;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_OFB8] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_OFB8;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_CFB64] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_CFB64;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_CFB8] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_CFB8;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD2] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD2;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD2_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD2_HMAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD2_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD2_HMAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD5] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD5;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD5_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD5_HMAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD5_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD5_HMAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA_1] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA_1;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA_1_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA_1_HMAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA_1_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA_1_HMAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD128] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD128;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD128_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD128_HMAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD128_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD128_HMAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD160] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD160;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD160_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD160_HMAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD160_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD160_HMAC_GENERAL;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA256] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA256;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA256_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA256_HMAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA256_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA256_HMAC_GENERAL;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA384] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA384;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA384_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA384_HMAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA384_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA384_HMAC_GENERAL;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA512] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA512;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA512_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA512_HMAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA512_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA512_HMAC_GENERAL;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_ECB;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_MAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_MAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_CBC_PAD;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_ECB;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_MAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_MAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_CBC_PAD;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_ECB;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_ECB;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_MAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_MAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_MAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_MAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_CBC_PAD;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_CBC_PAD;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_ECB;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_MAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_MAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_CBC_PAD;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_ECB;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_MAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_MAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_CBC_PAD;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_GENERIC_SECRET_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_GENERIC_SECRET_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CONCATENATE_BASE_AND_KEY] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CONCATENATE_BASE_AND_KEY;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CONCATENATE_BASE_AND_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CONCATENATE_BASE_AND_DATA;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CONCATENATE_DATA_AND_BASE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CONCATENATE_DATA_AND_BASE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_XOR_BASE_AND_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_XOR_BASE_AND_DATA;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_EXTRACT_KEY_FROM_KEY] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_EXTRACT_KEY_FROM_KEY;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SSL3_PRE_MASTER_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SSL3_PRE_MASTER_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SSL3_MASTER_KEY_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SSL3_MASTER_KEY_DERIVE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SSL3_KEY_AND_MAC_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SSL3_KEY_AND_MAC_DERIVE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SSL3_MASTER_KEY_DERIVE_DH] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SSL3_MASTER_KEY_DERIVE_DH;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_TLS_PRE_MASTER_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TLS_PRE_MASTER_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_TLS_MASTER_KEY_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TLS_MASTER_KEY_DERIVE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_TLS_KEY_AND_MAC_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TLS_KEY_AND_MAC_DERIVE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_TLS_MASTER_KEY_DERIVE_DH] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TLS_MASTER_KEY_DERIVE_DH;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SSL3_MD5_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SSL3_MD5_MAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SSL3_SHA1_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SSL3_SHA1_MAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD5_KEY_DERIVATION] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD5_KEY_DERIVATION;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD2_KEY_DERIVATION] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD2_KEY_DERIVATION;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA1_KEY_DERIVATION] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA1_KEY_DERIVATION;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA256_KEY_DERIVATION] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA256_KEY_DERIVATION;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA384_KEY_DERIVATION] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA384_KEY_DERIVATION;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA512_KEY_DERIVATION] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA512_KEY_DERIVATION;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_MD2_DES_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_MD2_DES_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_MD5_DES_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_MD5_DES_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_MD5_CAST_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_MD5_CAST_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_MD5_CAST3_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_MD5_CAST3_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_MD5_CAST5_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_MD5_CAST5_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_MD5_CAST128_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_MD5_CAST128_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_CAST5_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_CAST5_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_CAST128_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_CAST128_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_RC4_128] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_RC4_128;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_RC4_40] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_RC4_40;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_DES3_EDE_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_DES3_EDE_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_DES2_EDE_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_DES2_EDE_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_RC2_128_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_RC2_128_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_RC2_40_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_RC2_40_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PKCS5_PBKD2] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PKCS5_PBKD2;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBA_SHA1_WITH_SHA1_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBA_SHA1_WITH_SHA1_HMAC;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_WTLS_PRE_MASTER_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_WTLS_PRE_MASTER_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_WTLS_MASTER_KEY_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_WTLS_MASTER_KEY_DERIVE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_WTLS_MASTER_KEY_DERVIE_DH_ECC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_WTLS_MASTER_KEY_DERIVE_DH_ECC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_WTLS_PRF] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_WTLS_PRF;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_WTLS_SERVER_KEY_AND_MAC_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_WTLS_SERVER_KEY_AND_MAC_DERIVE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_WTLS_CLIENT_KEY_AND_MAC_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_WTLS_CLIENT_KEY_AND_MAC_DERIVE;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_KEY_WRAP_LYNKS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_KEY_WRAP_LYNKS;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_KEY_WRAP_SET_OAEP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_KEY_WRAP_SET_OAEP;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CMS_SIG] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CMS_SIG;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_ECB64] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_ECB64;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_CBC64] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_CBC64;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_OFB64] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_OFB64;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_CFB64] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_CFB64;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_CFB32] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_CFB32;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_CFB16] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_CFB16;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_CFB8] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_CFB8;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_WRAP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_WRAP;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_PRIVATE_WRAP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_PRIVATE_WRAP;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_RELAYX] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_RELAYX;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_KEA_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_KEA_KEY_PAIR_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_KEA_KEY_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_KEA_KEY_DERIVE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_FORTEZZA_TIMESTAMP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_FORTEZZA_TIMESTAMP;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BATON_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BATON_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BATON_ECB128] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BATON_ECB128;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BATON_ECB96] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BATON_ECB96;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BATON_CBC128] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BATON_CBC128;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BATON_COUNTER] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BATON_COUNTER;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BATON_SHUFFLE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BATON_SHUFFLE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BATON_WRAP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BATON_WRAP;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_ECDSA_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_ECDSA_KEY_PAIR_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_EC_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_EC_KEY_PAIR_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_ECDSA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_ECDSA;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_ECDSA_SHA1] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_ECDSA_SHA1;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_ECDH1_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_ECDH1_DERIVE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_ECDH1_COFACTOR_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_ECDH1_COFACTOR_DERIVE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_ECMQV_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_ECMQV_DERIVE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_JUNIPER_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_JUNIPER_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_JUNIPER_ECB128] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_JUNIPER_ECB128;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_JUNIPER_CBC128] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_JUNIPER_CBC128;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_JUNIPER_COUNTER] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_JUNIPER_COUNTER;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_JUNIPER_SHUFFLE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_JUNIPER_SHUFFLE;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_JUNIPER_WRAP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_JUNIPER_WRAP;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_FASTHASH] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_FASTHASH;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_ECB;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_CBC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_MAC;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_MAC_GENERAL;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_CBC_PAD;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BLOWFISH_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BLOWFISH_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BLOWFISH_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BLOWFISH_CBC;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_TWOFISH_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TWOFISH_KEY_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_TWOFISH_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TWOFISH_CBC;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_ECB_ENCRYPT_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_ECB_ENCRYPT_DATA;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_CBC_ENCRYPT_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_CBC_ENCRYPT_DATA;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_ECB_ENCRYPT_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_ECB_ENCRYPT_DATA;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_CBC_ENCRYPT_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_CBC_ENCRYPT_DATA;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_ECB_ENCRYPT_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_ECB_ENCRYPT_DATA;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_CBC_ENCRYPT_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_CBC_ENCRYPT_DATA;
				
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DSA_PARAMETER_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DSA_PARAMETER_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DH_PKCS_PARAMETER_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DH_PKCS_PARAMETER_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_X9_42_DH_PARAMETER_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_X9_42_DH_PARAMETER_GEN;
				mechansimNames[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_VENDOR_DEFINED] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_VENDOR_DEFINED;
				mechansimNames_ = mechansimNames;
			}
			
			System.Int64 mechansimCodeObject = (long) mechansimCode;
			System.Object entry = mechansimNames_[mechansimCodeObject];
			
			//UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Object.toString' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
			System.String mechanismName = (entry != null)?entry.ToString():"Unknwon mechanism with code: 0x" + toFullHexString(mechansimCode);
			
			return mechanismName;
		}
		
		/// <summary> Converts the long value classType to a string representation of it.
		/// 
		/// </summary>
		/// <param name="classType">The classType to be converted.
		/// </param>
		/// <returns> The string representation of the classType.
		/// </returns>
		public static System.String classTypeToString(long classType)
		{
			System.String name;
			
			if (classType == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKO_DATA)
			{
				name = "CKO_DATA";
			}
			else if (classType == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKO_CERTIFICATE)
			{
				name = "CKO_CERTIFICATE";
			}
			else if (classType == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKO_PUBLIC_KEY)
			{
				name = "CKO_PUBLIC_KEY";
			}
			else if (classType == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKO_PRIVATE_KEY)
			{
				name = "CKO_PRIVATE_KEY";
			}
			else if (classType == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKO_SECRET_KEY)
			{
				name = "CKO_SECRET_KEY";
			}
			else if (classType == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKO_HW_FEATURE)
			{
				name = "CKO_HW_FEATURE";
			}
			else if (classType == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKO_DOMAIN_PARAMETERS)
			{
				name = "CKO_DOMAIN_PARAMETERS";
			}
			else if (classType == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKO_VENDOR_DEFINED)
			{
				name = "CKO_VENDOR_DEFINED";
			}
			else
			{
				name = "ERROR: unknown classType with code: 0x" + toFullHexString(classType);
			}
			
			return name;
		}
		
		/// <summary> Check the given arrays for equalitiy. This method considers both arrays as
		/// equal, if both are <code>null</code> or both have the same length and
		/// contain exactly the same byte values.
		/// 
		/// </summary>
		/// <param name="array1">The first array.
		/// </param>
		/// <param name="array2">The second array.
		/// </param>
		/// <returns> True, if both arrays are <code>null</code> or both have the same
		/// length and contain exactly the same byte values. False, otherwise.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static bool equals(byte[] array1, byte[] array2)
		{
			bool equal = false;
			
			if (array1 == array2)
			{
				equal = true;
			}
			else if ((array1 != null) && (array2 != null))
			{
				int length = array1.Length;
				if (length == array2.Length)
				{
					equal = true;
					for (int i = 0; i < length; i++)
					{
						if (array1[i] != array2[i])
						{
							equal = false;
							break;
						}
					}
				}
				else
				{
					equal = false;
				}
			}
			else
			{
				equal = false;
			}
			
			return equal;
		}
		
		/// <summary> Check the given arrays for equalitiy. This method considers both arrays as
		/// equal, if both are <code>null</code> or both have the same length and
		/// contain exactly the same char values.
		/// 
		/// </summary>
		/// <param name="array1">The first array.
		/// </param>
		/// <param name="array2">The second array.
		/// </param>
		/// <returns> True, if both arrays are <code>null</code> or both have the same
		/// length and contain exactly the same char values. False, otherwise.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static bool equals(char[] array1, char[] array2)
		{
			bool equal = false;
			
			if (array1 == array2)
			{
				equal = true;
			}
			else if ((array1 != null) && (array2 != null))
			{
				int length = array1.Length;
				if (length == array2.Length)
				{
					equal = true;
					for (int i = 0; i < length; i++)
					{
						if (array1[i] != array2[i])
						{
							equal = false;
							break;
						}
					}
				}
				else
				{
					equal = false;
				}
			}
			else
			{
				equal = false;
			}
			
			return equal;
		}
		
		/// <summary> Check the given arrays for equality. This method considers both arrays as
		/// equal, if both are <code>null</code> or both have the same length and
		/// contain exactly the same byte values.
		/// 
		/// </summary>
		/// <param name="array1">The first array.
		/// </param>
		/// <param name="array2">The second array.
		/// </param>
		/// <returns> True, if both arrays are <code>null</code> or both have the same
		/// length and contain exactly the same byte values. False, otherwise.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static bool equals(long[] array1, long[] array2)
		{
			bool equal = false;
			
			if (array1 == array2)
			{
				equal = true;
			}
			else if ((array1 != null) && (array2 != null))
			{
				int length = array1.Length;
				if (length == array2.Length)
				{
					equal = true;
					for (int i = 0; i < length; i++)
					{
						if (array1[i] != array2[i])
						{
							equal = false;
							break;
						}
					}
				}
				else
				{
					equal = false;
				}
			}
			else
			{
				equal = false;
			}
			
			return equal;
		}
		
		/// <summary> Check the given dates for equalitiy. This method considers both dates as
		/// equal, if both are <code>null</code> or both contain exactly the same char
		/// values.
		/// 
		/// </summary>
		/// <param name="date1">The first date.
		/// </param>
		/// <param name="date2">The second date.
		/// </param>
		/// <returns> True, if both dates are <code>null</code> or both contain the same
		/// char values. False, otherwise.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static bool equals(CK_DATE date1, CK_DATE date2)
		{
			bool equal = false;
			
			if (date1 == date2)
			{
				equal = true;
			}
			else if ((date1 != null) && (date2 != null))
			{
				equal = equals(date1.year, date2.year) && equals(date1.month, date2.month) && equals(date1.day, date2.day);
			}
			else
			{
				equal = false;
			}
			
			return equal;
		}
		
		/// <summary> Calculate a hash code for the given byte array.
		/// 
		/// </summary>
		/// <param name="array">The byte array.
		/// </param>
		/// <returns> A hash code for the given array.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static int hashCode(byte[] array)
		{
			int hash = 0;
			
			if (array != null)
			{
				for (int i = 0; (i < 4) && (i < array.Length); i++)
				{
					hash ^= (0xFF & array[i]) << ((i % 4) << 3);
				}
			}
			
			return hash;
		}
		
		/// <summary> Calculate a hash code for the given char array.
		/// 
		/// </summary>
		/// <param name="array">The char array.
		/// </param>
		/// <returns> A hash code for the given array.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static int hashCode(char[] array)
		{
			int hash = 0;
			
			if (array != null)
			{
				for (int i = 0; (i < 4) && (i < array.Length); i++)
				{
					hash ^= (unchecked((int) 0xFFFFFFFF) & (array[i] >> 32));
					hash ^= (unchecked((int) 0xFFFFFFFF) & array[i]);
				}
			}
			
			return hash;
		}
		
		/// <summary> Calculate a hash code for the given long array.
		/// 
		/// </summary>
		/// <param name="array">The long array.
		/// </param>
		/// <returns> A hash code for the given array.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static int hashCode(long[] array)
		{
			int hash = 0;
			
			if (array != null)
			{
				for (int i = 0; (i < 4) && (i < array.Length); i++)
				{
					hash ^= (int) ((unchecked((int) 0xFFFFFFFF) & (array[i] >> 4)));
					hash ^= (int) ((unchecked((int) 0xFFFFFFFF) & array[i]));
				}
			}
			
			return hash;
		}
		
		/// <summary> Calculate a hash code for the given date object.
		/// 
		/// </summary>
		/// <param name="date">The date object.
		/// </param>
		/// <returns> A hash code for the given date.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static int hashCode(CK_DATE date)
		{
			int hash = 0;
			
			if (date != null)
			{
				if (date.year.Length == 4)
				{
					hash ^= (0xFFFF & date.year[0]) << 16;
					hash ^= 0xFFFF & date.year[1];
					hash ^= (0xFFFF & date.year[2]) << 16;
					hash ^= 0xFFFF & date.year[3];
				}
				if (date.month.Length == 2)
				{
					hash ^= (0xFFFF & date.month[0]) << 16;
					hash ^= 0xFFFF & date.month[1];
				}
				if (date.day.Length == 2)
				{
					hash ^= (0xFFFF & date.day[0]) << 16;
					hash ^= 0xFFFF & date.day[1];
				}
			}
			
			return hash;
		}
		
		/// <summary> This method checks, if the mechanism with the given code is a full
		/// encrypt/decrypt mechanism; i.e. it supports the encryptUpdate()
		/// and decryptUpdate() functions.
		/// This is the information as provided by the table on page 229
		/// of the PKCS#11 v2.11 standard.
		/// If this method returns true, the mechanism can be used with the encrypt and
		/// decrypt functions including encryptUpdate and decryptUpdate.
		/// 
		/// </summary>
		/// <param name="mechanismCode">The code of the mechanism to check.
		/// </param>
		/// <returns> True, if the provided mechanism is a full encrypt/decrypt
		/// mechanism. False, otherwise.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static bool isFullEncryptDecryptMechanism(long mechanismCode)
		{
			// build the hashtable on demand (=first use)
			if (fullEncryptDecryptMechanisms_ == null)
			{
				System.Collections.Hashtable fullEncryptDecryptMechanisms = System.Collections.Hashtable.Synchronized(new System.Collections.Hashtable());
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_ECB;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_CBC;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_CBC_PAD;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC4] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC4;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_ECB;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_CBC;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_CBC_PAD;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_ECB;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_CBC;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_CBC_PAD;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_ECB;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_CBC;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_CBC_PAD;
				
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_OFB64] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_OFB64;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_OFB8] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_OFB8;
				
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_CFB64] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_CFB64;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_CFB8] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_CFB8;
				
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_ECB;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_CBC;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_CBC_PAD;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_ECB;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_CBC;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_CBC_PAD;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_ECB;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_ECB;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_CBC;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_CBC;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_CBC_PAD;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_CBC_PAD;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_ECB;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_CBC;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_CBC_PAD;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_ECB;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_CBC;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_CBC_PAD;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BLOWFISH_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BLOWFISH_CBC;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_TWOFISH_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TWOFISH_CBC;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_ECB;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_CBC;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_CBC_PAD;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_ECB64] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_ECB64;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_CBC64] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_CBC64;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_OFB64] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_OFB64;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_CFB64] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_CFB64;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_CFB32] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_CFB32;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_CFB16] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_CFB16;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_CFB8] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_CFB8;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BATON_ECB128] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BATON_ECB128;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BATON_ECB96] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BATON_ECB96;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BATON_CBC128] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BATON_CBC128;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BATON_COUNTER] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BATON_COUNTER;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BATON_SHUFFLE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BATON_SHUFFLE;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_JUNIPER_ECB128] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_JUNIPER_ECB128;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_JUNIPER_CBC128] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_JUNIPER_CBC128;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_JUNIPER_COUNTER] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_JUNIPER_COUNTER;
				fullEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_JUNIPER_SHUFFLE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_JUNIPER_SHUFFLE;
				fullEncryptDecryptMechanisms_ = fullEncryptDecryptMechanisms;
			}
			
			return fullEncryptDecryptMechanisms_.ContainsKey((long) mechanismCode);
		}
		
		/// <summary> This method checks, if the mechanism with the given code is a
		/// single-operation encrypt/decrypt mechanism; i.e. it does not support the
		/// encryptUpdate() and decryptUpdate() functions.
		/// This is the information as provided by the table on page 229
		/// of the PKCS#11 v2.11 standard.
		/// If this method returns true, the mechanism can be used with the encrypt and
		/// decrypt functions excluding encryptUpdate and decryptUpdate.
		/// 
		/// </summary>
		/// <param name="mechanismCode">The code of the mechanism to check.
		/// </param>
		/// <returns> True, if the provided mechanism is a single-operation
		/// encrypt/decrypt mechanism. False, otherwise.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static bool isSingleOperationEncryptDecryptMechanism(long mechanismCode)
		{
			// build the hashtable on demand (=first use)
			if (singleOperationEncryptDecryptMechanisms_ == null)
			{
				System.Collections.Hashtable singleOperationEncryptDecryptMechanisms = System.Collections.Hashtable.Synchronized(new System.Collections.Hashtable());
				singleOperationEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_PKCS;
				singleOperationEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_PKCS_OAEP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_PKCS_OAEP;
				singleOperationEncryptDecryptMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_X_509] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_X_509;
				singleOperationEncryptDecryptMechanisms_ = singleOperationEncryptDecryptMechanisms;
			}
			
			return singleOperationEncryptDecryptMechanisms_.ContainsKey((long) mechanismCode);
		}
		
		/// <summary> This method checks, if the mechanism with the given code is a full
		/// sign/verify mechanism; i.e. it supports the signUpdate()
		/// and verifyUpdate() functions.
		/// This is the information as provided by the table on page 229
		/// of the PKCS#11 v2.11 standard.
		/// If this method returns true, the mechanism can be used with the sign and
		/// verify functions including signUpdate and verifyUpdate.
		/// 
		/// </summary>
		/// <param name="mechanismCode">The code of the mechanism to check.
		/// </param>
		/// <returns> True, if the provided mechanism is a full sign/verify
		/// mechanism. False, otherwise.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static bool isFullSignVerifyMechanism(long mechanismCode)
		{
			// build the hashtable on demand (=first use)
			if (fullSignVerifyMechanisms_ == null)
			{
				System.Collections.Hashtable fullSignVerifyMechanisms = System.Collections.Hashtable.Synchronized(new System.Collections.Hashtable());
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD2_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD2_RSA_PKCS;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD5_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD5_RSA_PKCS;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA1_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA1_RSA_PKCS;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD128_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD128_RSA_PKCS;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD160_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD160_RSA_PKCS;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA1_RSA_PKCS_PSS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA1_RSA_PKCS_PSS;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA1_RSA_X9_31] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA1_RSA_X9_31;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DSA_SHA1] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DSA_SHA1;
				
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA256_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA256_RSA_PKCS;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA384_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA384_RSA_PKCS;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA512_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA512_RSA_PKCS;
				
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA256_RSA_PKCS_PSS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA256_RSA_PKCS_PSS;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA384_RSA_PKCS_PSS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA384_RSA_PKCS_PSS;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA512_RSA_PKCS_PSS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA512_RSA_PKCS_PSS;
				
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_MAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_MAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_MAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_MAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_MAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_MAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_MAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_MAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD2_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD2_HMAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD2_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD2_HMAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD5_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD5_HMAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD5_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD5_HMAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA_1_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA_1_HMAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA_1_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA_1_HMAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD128_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD128_HMAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD128_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD128_HMAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD160_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD160_HMAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD160_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD160_HMAC_GENERAL;
				
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA256_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA256_HMAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA256_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA256_HMAC_GENERAL;
				
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA384_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA384_HMAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA384_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA384_HMAC_GENERAL;
				
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA512_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA512_HMAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA512_HMAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA512_HMAC_GENERAL;
				
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_MAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_MAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_MAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_MAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_MAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_MAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_MAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_MAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_MAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_MAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_MAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_MAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_MAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_MAC_GENERAL] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_MAC_GENERAL;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SSL3_MD5_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SSL3_MD5_MAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SSL3_SHA1_MAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SSL3_SHA1_MAC;
				fullSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_ECDSA_SHA1] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_ECDSA_SHA1;
				fullSignVerifyMechanisms_ = fullSignVerifyMechanisms;
			}
			
			return fullSignVerifyMechanisms_.ContainsKey((long) mechanismCode);
		}
		
		/// <summary> This method checks, if the mechanism with the given code is a
		/// single-operation sign/verify mechanism; i.e. it does not support the
		/// signUpdate() and encryptUpdate() functions.
		/// This is the information as provided by the table on page 229
		/// of the PKCS#11 v2.11 standard.
		/// If this method returns true, the mechanism can be used with the sign and
		/// verify functions excluding signUpdate and encryptUpdate.
		/// 
		/// </summary>
		/// <param name="mechanismCode">The code of the mechanism to check.
		/// </param>
		/// <returns> True, if the provided mechanism is a single-operation
		/// sign/verify mechanism. False, otherwise.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static bool isSingleOperationSignVerifyMechanism(long mechanismCode)
		{
			// build the hashtable on demand (=first use)
			if (singleOperationSignVerifyMechanisms_ == null)
			{
				System.Collections.Hashtable singleOperationSignVerifyMechanisms = System.Collections.Hashtable.Synchronized(new System.Collections.Hashtable());
				singleOperationSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_PKCS;
				singleOperationSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_PKCS_PSS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_PKCS_PSS;
				singleOperationSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_9796] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_9796;
				singleOperationSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_X_509] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_X_509;
				singleOperationSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_X9_31] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_X9_31;
				singleOperationSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DSA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DSA;
				singleOperationSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_FORTEZZA_TIMESTAMP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_FORTEZZA_TIMESTAMP;
				singleOperationSignVerifyMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_ECDSA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_ECDSA;
				singleOperationSignVerifyMechanisms_ = singleOperationSignVerifyMechanisms;
			}
			
			return singleOperationSignVerifyMechanisms_.ContainsKey((long) mechanismCode);
		}
		
		/// <summary> This method checks, if the mechanism with the given code is a sign/verify
		/// mechanism with message recovery.
		/// This is the information as provided by the table on page 229
		/// of the PKCS#11 v2.11 standard.
		/// If this method returns true, the mechanism can be used with the signRecover
		/// and verifyRecover functions.
		/// 
		/// </summary>
		/// <param name="mechanismCode">The code of the mechanism to check.
		/// </param>
		/// <returns> True, if the provided mechanism is a sign/verify mechanism with
		/// message recovery. False, otherwise.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static bool isSignVerifyRecoverMechanism(long mechanismCode)
		{
			// build the hashtable on demand (=first use)
			if (signVerifyRecoverMechanisms_ == null)
			{
				System.Collections.Hashtable signVerifyRecoverMechanisms = System.Collections.Hashtable.Synchronized(new System.Collections.Hashtable());
				signVerifyRecoverMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_PKCS;
				signVerifyRecoverMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_9796] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_9796;
				signVerifyRecoverMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_X_509] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_X_509;
				signVerifyRecoverMechanisms_ = signVerifyRecoverMechanisms;
			}
			
			return signVerifyRecoverMechanisms_.ContainsKey((long) mechanismCode);
		}
		
		/// <summary> This method checks, if the mechanism with the given code is a digest
		/// mechanism.
		/// This is the information as provided by the table on page 229
		/// of the PKCS#11 v2.11 standard.
		/// If this method returns true, the mechanism can be used with the digest
		/// functions.
		/// 
		/// </summary>
		/// <param name="mechanismCode">The code of the mechanism to check.
		/// </param>
		/// <returns> True, if the provided mechanism is a digest mechanism. False,
		/// otherwise.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static bool isDigestMechanism(long mechanismCode)
		{
			// build the hashtable on demand (=first use)
			if (digestMechanisms_ == null)
			{
				System.Collections.Hashtable digestMechanisms = System.Collections.Hashtable.Synchronized(new System.Collections.Hashtable());
				digestMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD2] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD2;
				digestMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD5] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD5;
				digestMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA_1] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA_1;
				digestMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD128] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD128;
				digestMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RIPEMD160] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RIPEMD160;
				digestMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA256] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA256;
				digestMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA384] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA384;
				digestMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA512] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA512;
				digestMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_FASTHASH] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_FASTHASH;
				digestMechanisms_ = digestMechanisms;
			}
			
			return digestMechanisms_.ContainsKey((long) mechanismCode);
		}
		
		/// <summary> This method checks, if the mechanism with the given code is a key
		/// generation mechanism for generating symmetric keys.
		/// This is the information as provided by the table on page 229
		/// of the PKCS#11 v2.11 standard.
		/// If this method returns true, the mechanism can be used with the generateKey
		/// function.
		/// 
		/// </summary>
		/// <param name="mechanismCode">The code of the mechanism to check.
		/// </param>
		/// <returns> True, if the provided mechanism is a key generation mechanism.
		/// False, otherwise.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static bool isKeyGenerationMechanism(long mechanismCode)
		{
			// build the hashtable on demand (=first use)
			if (keyGenerationMechanisms_ == null)
			{
				System.Collections.Hashtable keyGenerationMechanisms = System.Collections.Hashtable.Synchronized(new System.Collections.Hashtable());
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DSA_PARAMETER_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DSA_PARAMETER_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DH_PKCS_PARAMETER_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DH_PKCS_PARAMETER_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_X9_42_DH_PARAMETER_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_X9_42_DH_PARAMETER_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC4_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC4_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES2_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES2_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_KEY_GEN;
				
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BLOWFISH_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BLOWFISH_KEY_GEN;
				
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_TWOFISH_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TWOFISH_KEY_GEN;
				
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_GENERIC_SECRET_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_GENERIC_SECRET_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SSL3_PRE_MASTER_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SSL3_PRE_MASTER_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_TLS_PRE_MASTER_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TLS_PRE_MASTER_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_MD2_DES_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_MD2_DES_CBC;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_MD5_DES_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_MD5_DES_CBC;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_MD5_CAST_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_MD5_CAST_CBC;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_MD5_CAST3_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_MD5_CAST3_CBC;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_MD5_CAST5_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_MD5_CAST5_CBC;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_MD5_CAST128_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_MD5_CAST128_CBC;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_CAST5_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_CAST5_CBC;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_CAST128_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_CAST128_CBC;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_RC4_128] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_RC4_128;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_RC4_40] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_RC4_40;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_DES3_EDE_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_DES3_EDE_CBC;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_DES2_EDE_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_DES2_EDE_CBC;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_RC2_128_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_RC2_128_CBC;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBE_SHA1_RC2_40_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBE_SHA1_RC2_40_CBC;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PKCS5_PBKD2] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PKCS5_PBKD2;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_PBA_SHA1_WITH_SHA1_HMAC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_PBA_SHA1_WITH_SHA1_HMAC;
				
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_WTLS_PRE_MASTER_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_WTLS_PRE_MASTER_KEY_GEN;
				
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BATON_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BATON_KEY_GEN;
				keyGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_JUNIPER_KEY_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_JUNIPER_KEY_GEN;
				keyGenerationMechanisms_ = keyGenerationMechanisms;
			}
			
			return keyGenerationMechanisms_.ContainsKey((long) mechanismCode);
		}
		
		/// <summary> This method checks, if the mechanism with the given code is a key-pair
		/// generation mechanism for generating key-pairs.
		/// This is the information as provided by the table on page 229
		/// of the PKCS#11 v2.11 standard.
		/// If this method returns true, the mechanism can be used with the generateKeyPair
		/// function.
		/// 
		/// </summary>
		/// <param name="mechanismCode">The code of the mechanism to check.
		/// </param>
		/// <returns> True, if the provided mechanism is a key-pair generation mechanism.
		/// False, otherwise.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static bool isKeyPairGenerationMechanism(long mechanismCode)
		{
			// build the hashtable on demand (=first use)
			if (keyPairGenerationMechanisms_ == null)
			{
				System.Collections.Hashtable keyPairGenerationMechanisms = System.Collections.Hashtable.Synchronized(new System.Collections.Hashtable());
				keyPairGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_PKCS_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_PKCS_KEY_PAIR_GEN;
				keyPairGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_X9_31_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_X9_31_KEY_PAIR_GEN;
				keyPairGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DSA_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DSA_KEY_PAIR_GEN;
				keyPairGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DH_PKCS_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DH_PKCS_KEY_PAIR_GEN;
				keyPairGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_KEA_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_KEA_KEY_PAIR_GEN;
				keyPairGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_ECDSA_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_ECDSA_KEY_PAIR_GEN;
				keyPairGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_EC_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_EC_KEY_PAIR_GEN;
				keyPairGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DH_PKCS_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DH_PKCS_KEY_PAIR_GEN;
				keyPairGenerationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_X9_42_DH_KEY_PAIR_GEN] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_X9_42_DH_KEY_PAIR_GEN;
				keyPairGenerationMechanisms_ = keyPairGenerationMechanisms;
			}
			
			return keyPairGenerationMechanisms_.ContainsKey((long) mechanismCode);
		}
		
		/// <summary> This method checks, if the mechanism with the given code is a
		/// wrap/unwrap mechanism; i.e. it supports the wrapKey()
		/// and unwrapKey() functions.
		/// This is the information as provided by the table on page 229
		/// of the PKCS#11 v2.11 standard.
		/// If this method returns true, the mechanism can be used with the wrapKey
		/// and unwrapKey functions.
		/// 
		/// </summary>
		/// <param name="mechanismCode">The code of the mechanism to check.
		/// </param>
		/// <returns> True, if the provided mechanism is a wrap/unwrap mechanism.
		/// False, otherwise.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static bool isWrapUnwrapMechanism(long mechanismCode)
		{
			// build the hashtable on demand (=first use)
			if (wrapUnwrapMechanisms_ == null)
			{
				System.Collections.Hashtable wrapUnwrapMechanisms = System.Collections.Hashtable.Synchronized(new System.Collections.Hashtable());
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_PKCS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_PKCS;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_X_509] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_X_509;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RSA_PKCS_OAEP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RSA_PKCS_OAEP;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_ECB;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_CBC;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC2_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC2_CBC_PAD;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_ECB;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_CBC;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_CBC_PAD;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_ECB;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_CBC;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_CBC_PAD;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_ECB;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_CBC;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CDMF_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CDMF_CBC_PAD;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_ECB;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_CBC;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST_CBC_PAD;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_ECB;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_CBC;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST3_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST3_CBC_PAD;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_ECB;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_ECB;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_CBC;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_CBC;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST5_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST5_CBC_PAD;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CAST128_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CAST128_CBC_PAD;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_ECB;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_CBC;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_RC5_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_RC5_CBC_PAD;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_ECB;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_CBC;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_IDEA_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_IDEA_CBC_PAD;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_KEY_WRAP_LYNKS] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_KEY_WRAP_LYNKS;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_KEY_WRAP_SET_OAEP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_KEY_WRAP_SET_OAEP;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_WRAP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_WRAP;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_PRIVATE_WRAP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_PRIVATE_WRAP;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SKIPJACK_RELAYX] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SKIPJACK_RELAYX;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BATON_WRAP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BATON_WRAP;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_JUNIPER_WRAP] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_JUNIPER_WRAP;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_ECB] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_ECB;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_CBC;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_CBC_PAD] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_CBC_PAD;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_BLOWFISH_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_BLOWFISH_CBC;
				wrapUnwrapMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_TWOFISH_CBC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TWOFISH_CBC;
				wrapUnwrapMechanisms_ = wrapUnwrapMechanisms;
			}
			
			return wrapUnwrapMechanisms_.ContainsKey((long) mechanismCode);
		}
		
		/// <summary> This method checks, if the mechanism with the given code is a
		/// key derivation mechanism.
		/// This is the information as provided by the table on page 229
		/// of the PKCS#11 v2.11 standard.
		/// If this method returns true, the mechanism can be used with the deriveKey
		/// function.
		/// 
		/// </summary>
		/// <param name="mechanismCode">The code of the mechanism to check.
		/// </param>
		/// <returns> True, if the provided mechanism is a key derivation mechanism.
		/// False, otherwise.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public static bool isKeyDerivationMechanism(long mechanismCode)
		{
			// build the hashtable on demand (=first use)
			if (keyDerivationMechanisms_ == null)
			{
				System.Collections.Hashtable keyDerivationMechanisms = System.Collections.Hashtable.Synchronized(new System.Collections.Hashtable());
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DH_PKCS_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DH_PKCS_DERIVE;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CONCATENATE_BASE_AND_KEY] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CONCATENATE_BASE_AND_KEY;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CONCATENATE_BASE_AND_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CONCATENATE_BASE_AND_DATA;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_CONCATENATE_DATA_AND_BASE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_CONCATENATE_DATA_AND_BASE;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_XOR_BASE_AND_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_XOR_BASE_AND_DATA;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_EXTRACT_KEY_FROM_KEY] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_EXTRACT_KEY_FROM_KEY;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SSL3_MASTER_KEY_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SSL3_MASTER_KEY_DERIVE;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SSL3_MASTER_KEY_DERIVE_DH] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SSL3_MASTER_KEY_DERIVE_DH;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SSL3_KEY_AND_MAC_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SSL3_KEY_AND_MAC_DERIVE;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_TLS_MASTER_KEY_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TLS_MASTER_KEY_DERIVE;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_TLS_MASTER_KEY_DERIVE_DH] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TLS_MASTER_KEY_DERIVE_DH;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_TLS_KEY_AND_MAC_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TLS_KEY_AND_MAC_DERIVE;
				
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_TLS_PRF] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TLS_PRF;
				
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD5_KEY_DERIVATION] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD5_KEY_DERIVATION;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_MD2_KEY_DERIVATION] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_MD2_KEY_DERIVATION;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA1_KEY_DERIVATION] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA1_KEY_DERIVATION;
				
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA256_KEY_DERIVATION] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA256_KEY_DERIVATION;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA384_KEY_DERIVATION] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA384_KEY_DERIVATION;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_SHA512_KEY_DERIVATION] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_SHA512_KEY_DERIVATION;
				
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_WTLS_MASTER_KEY_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_TLS_MASTER_KEY_DERIVE;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_WTLS_MASTER_KEY_DERIVE_DH_ECC] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_WTLS_MASTER_KEY_DERIVE_DH_ECC;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_WTLS_SERVER_KEY_AND_MAC_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_WTLS_SERVER_KEY_AND_MAC_DERIVE;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_WTLS_CLIENT_KEY_AND_MAC_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_WTLS_CLIENT_KEY_AND_MAC_DERIVE;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_WTLS_PRF] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_WTLS_PRF;
				
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_KEA_KEY_DERIVE] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_KEA_KEY_DERIVE;
				
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_ECB_ENCRYPT_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_ECB_ENCRYPT_DATA;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES_CBC_ENCRYPT_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES_CBC_ENCRYPT_DATA;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_ECB_ENCRYPT_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_ECB_ENCRYPT_DATA;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_DES3_CBC_ENCRYPT_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_DES3_CBC_ENCRYPT_DATA;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_ECB_ENCRYPT_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_ECB_ENCRYPT_DATA;
				keyDerivationMechanisms[(long) iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CKM_AES_CBC_ENCRYPT_DATA] = iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.NAME_CKM_AES_CBC_ENCRYPT_DATA;
				
				keyDerivationMechanisms_ = keyDerivationMechanisms;
			}
			
			return keyDerivationMechanisms_.ContainsKey((long) mechanismCode);
		}
	}
}