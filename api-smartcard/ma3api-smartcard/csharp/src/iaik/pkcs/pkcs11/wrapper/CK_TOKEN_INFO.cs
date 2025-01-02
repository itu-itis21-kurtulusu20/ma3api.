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
	
	
	
	/// <summary> class CK_TOKEN_INFO provides information about a token.<p>
	/// <B>PKCS#11 structure:</B>
	/// <PRE>
	/// typedef struct CK_TOKEN_INFO {&nbsp;&nbsp;
	/// CK_UTF8CHAR label[32];&nbsp;&nbsp;
	/// CK_UTF8CHAR manufacturerID[32];&nbsp;&nbsp;
	/// CK_UTF8CHAR model[16];&nbsp;&nbsp;
	/// CK_CHAR serialNumber[16];&nbsp;&nbsp;
	/// CK_FLAGS flags;&nbsp;&nbsp;
	/// CK_ULONG ulMaxSessionCount;&nbsp;&nbsp;
	/// CK_ULONG ulSessionCount;&nbsp;&nbsp;
	/// CK_ULONG ulMaxRwSessionCount;&nbsp;&nbsp;
	/// CK_ULONG ulRwSessionCount;&nbsp;&nbsp;
	/// CK_ULONG ulMaxPinLen;&nbsp;&nbsp;
	/// CK_ULONG ulMinPinLen;&nbsp;&nbsp;
	/// CK_ULONG ulTotalPublicMemory;&nbsp;&nbsp;
	/// CK_ULONG ulFreePublicMemory;&nbsp;&nbsp;
	/// CK_ULONG ulTotalPrivateMemory;&nbsp;&nbsp;
	/// CK_ULONG ulFreePrivateMemory;&nbsp;&nbsp;
	/// CK_VERSION hardwareVersion;&nbsp;&nbsp;
	/// CK_VERSION firmwareVersion;&nbsp;&nbsp;
	/// CK_CHAR utcTime[16];&nbsp;&nbsp;
	/// } CK_TOKEN_INFO;
	/// &nbsp;&nbsp;
	/// </PRE>
	/// 
	/// </summary>
	/// <author>  Karl Scheibelhofer <Karl.Scheibelhofer@iaik.at>
	/// </author>
	/// <author>  Martin Schläffer <schlaeff@sbox.tugraz.at>
	/// </author>
	public class CK_TOKEN_INFO
	{
		
		/* label, manufacturerID, and model have been changed from
		* CK_CHAR to CK_UTF8CHAR for v2.11. */
		/// <summary> must be blank padded and only the first 32 chars will be used<p>
		/// <B>PKCS#11:</B>
		/// <PRE>
		/// CK_UTF8CHAR label[32];
		/// </PRE>
		/// </summary>
		public char[] label; /* blank padded */
		
		/// <summary> must be blank padded and only the first 32 chars will be used<p>
		/// <B>PKCS#11:</B>
		/// <PRE>
		/// CK_UTF8CHAR manufacturerID[32];
		/// </PRE>
		/// </summary>
		public char[] manufacturerID; /* blank padded */
		
		/// <summary> must be blank padded and only the first 16 chars will be used<p>
		/// <B>PKCS#11:</B>
		/// <PRE>
		/// CK_UTF8CHAR model[16];
		/// </PRE>
		/// </summary>
		public char[] model; /* blank padded */
		
		/// <summary> must be blank padded and only the first 16 chars will be used<p>
		/// <B>PKCS#11:</B>
		/// <PRE>
		/// CK_CHAR serialNumber[16];
		/// </PRE>
		/// </summary>
		public char[] serialNumber; /* blank padded */
		
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_FLAGS flags;
		/// </PRE>
		/// </summary>
		public long flags; /* see below */
		
		/* ulMaxSessionCount, ulSessionCount, ulMaxRwSessionCount,
		* ulRwSessionCount, ulMaxPinLen, and ulMinPinLen have all been
		* changed from CK_USHORT to CK_ULONG for v2.0 */
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_ULONG ulMaxSessionCount;
		/// </PRE>
		/// </summary>
		public long ulMaxSessionCount; /* max open sessions */
		
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_ULONG ulSessionCount;
		/// </PRE>
		/// </summary>
		public long ulSessionCount; /* sess. now open */
		
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_ULONG ulMaxRwSessionCount;
		/// </PRE>
		/// </summary>
		public long ulMaxRwSessionCount; /* max R/W sessions */
		
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_ULONG ulRwSessionCount;
		/// </PRE>
		/// </summary>
		public long ulRwSessionCount; /* R/W sess. now open */
		
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_ULONG ulMaxPinLen;
		/// </PRE>
		/// </summary>
		public long ulMaxPinLen; /* in bytes */
		
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_ULONG ulMinPinLen;
		/// </PRE>
		/// </summary>
		public long ulMinPinLen; /* in bytes */
		
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_ULONG ulTotalPublicMemory;
		/// </PRE>
		/// </summary>
		public long ulTotalPublicMemory; /* in bytes */
		
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_ULONG ulFreePublicMemory;
		/// </PRE>
		/// </summary>
		public long ulFreePublicMemory; /* in bytes */
		
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_ULONG ulTotalPrivateMemory;
		/// </PRE>
		/// </summary>
		public long ulTotalPrivateMemory; /* in bytes */
		
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_ULONG ulFreePrivateMemory;
		/// </PRE>
		/// </summary>
		public long ulFreePrivateMemory; /* in bytes */
		
		/* hardwareVersion, firmwareVersion, and time are new for
		* v2.0 */
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_VERSION hardwareVersion;
		/// </PRE>
		/// </summary>
		public CK_VERSION hardwareVersion; /* version of hardware */
		
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_VERSION firmwareVersion;
		/// </PRE>
		/// </summary>
		public CK_VERSION firmwareVersion; /* version of firmware */
		
		/// <summary> only the first 16 chars will be used
		/// <B>PKCS#11:</B>
		/// <PRE>
		/// CK_CHAR utcTime[16];
		/// </PRE>
		/// </summary>
		public char[] utcTime; /* time */
		
		/// <summary> Returns the string representation of CK_TOKEN_INFO.
		/// 
		/// </summary>
		/// <returns> the string representation of CK_TOKEN_INFO
		/// </returns>
		public override System.String ToString()
		{
			System.Text.StringBuilder buffer = new System.Text.StringBuilder();
			
			buffer.Append(Constants.INDENT);
			buffer.Append("label: ");
			buffer.Append(new System.String(label));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("manufacturerID: ");
			buffer.Append(new System.String(manufacturerID));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("model: ");
			buffer.Append(new System.String(model));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("serialNumber: ");
			buffer.Append(new System.String(serialNumber));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("flags: ");
			buffer.Append(Functions.tokenInfoFlagsToString(flags));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("ulMaxSessionCount: ");
			buffer.Append((ulMaxSessionCount == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CK_EFFECTIVELY_INFINITE)?"CK_EFFECTIVELY_INFINITE":((ulMaxSessionCount == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CK_UNAVAILABLE_INFORMATION)?"CK_UNAVAILABLE_INFORMATION":System.Convert.ToString(ulMaxSessionCount)));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("ulSessionCount: ");
			buffer.Append((ulSessionCount == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CK_UNAVAILABLE_INFORMATION)?"CK_EFFECTIVELY_INFINITE":System.Convert.ToString(ulSessionCount));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("ulMaxRwSessionCount: ");
			buffer.Append((ulMaxRwSessionCount == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CK_EFFECTIVELY_INFINITE)?"CK_EFFECTIVELY_INFINITE":((ulMaxRwSessionCount == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CK_UNAVAILABLE_INFORMATION)?"CK_UNAVAILABLE_INFORMATION":System.Convert.ToString(ulMaxRwSessionCount)));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("ulRwSessionCount: ");
			buffer.Append((ulRwSessionCount == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CK_UNAVAILABLE_INFORMATION)?"CK_EFFECTIVELY_INFINITE":System.Convert.ToString(ulRwSessionCount));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("ulMaxPinLen: ");
			buffer.Append(System.Convert.ToString(ulMaxPinLen));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("ulMinPinLen: ");
			buffer.Append(System.Convert.ToString(ulMinPinLen));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("ulTotalPublicMemory: ");
			buffer.Append((ulTotalPublicMemory == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CK_UNAVAILABLE_INFORMATION)?"CK_UNAVAILABLE_INFORMATION":System.Convert.ToString(ulTotalPublicMemory));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("ulFreePublicMemory: ");
			buffer.Append((ulFreePublicMemory == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CK_UNAVAILABLE_INFORMATION)?"CK_UNAVAILABLE_INFORMATION":System.Convert.ToString(ulFreePublicMemory));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("ulTotalPrivateMemory: ");
			buffer.Append((ulTotalPrivateMemory == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CK_UNAVAILABLE_INFORMATION)?"CK_UNAVAILABLE_INFORMATION":System.Convert.ToString(ulTotalPrivateMemory));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("ulFreePrivateMemory: ");
			buffer.Append((ulFreePrivateMemory == iaik.pkcs.pkcs11.wrapper.PKCS11Constants_Fields.CK_UNAVAILABLE_INFORMATION)?"CK_UNAVAILABLE_INFORMATION":System.Convert.ToString(ulFreePrivateMemory));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("hardwareVersion: ");
			buffer.Append(hardwareVersion.ToString());
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("firmwareVersion: ");
			buffer.Append(firmwareVersion.ToString());
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("utcTime: ");
			buffer.Append(new System.String(utcTime));
			//buffer.append(Constants.NEWLINE);
			
			return buffer.ToString();
		}
	}
}