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
	
	
	
	/// <summary> class CK_SLOT_INFO provides information about a slot.<p>
	/// <B>PKCS#11 structure:</B>
	/// <PRE>
	/// typedef struct CK_SLOT_INFO {&nbsp;&nbsp;
	/// CK_UTF8CHAR slotDescription[64];&nbsp;&nbsp;
	/// CK_UTF8CHAR manufacturerID[32];&nbsp;&nbsp;
	/// CK_FLAGS flags;&nbsp;&nbsp;
	/// CK_VERSION hardwareVersion;&nbsp;&nbsp;
	/// CK_VERSION firmwareVersion;&nbsp;&nbsp;
	/// } CK_SLOT_INFO;
	/// </PRE>
	/// 
	/// </summary>
	/// <author>  Karl Scheibelhofer <Karl.Scheibelhofer@iaik.at>
	/// </author>
	/// <author>  Martin Schläffer <schlaeff@sbox.tugraz.at>
	/// </author>
	public class CK_SLOT_INFO
	{
		
		/* slotDescription and manufacturerID have been changed from
		* CK_CHAR to CK_UTF8CHAR for v2.11. */
		/// <summary> must be blank padded and only the first 64 chars will be used<p>
		/// <B>PKCS#11:</B>
		/// <PRE>
		/// CK_UTF8CHAR slotDescription[64];
		/// </PRE>
		/// </summary>
		public char[] slotDescription; /* blank padded */
		
		/// <summary> must be blank padded and only the first 32 chars will be used<p>
		/// <B>PKCS#11:</B>
		/// <PRE>
		/// CK_UTF8CHAR manufacturerID[32];
		/// </PRE>
		/// </summary>
		public char[] manufacturerID; /* blank padded */
		
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_FLAGS flags;
		/// </PRE>
		/// </summary>
		public long flags;
		
		/* hardwareVersion and firmwareVersion are new for v2.0 */
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
		
		/// <summary> Returns the string representation of CK_SLOT_INFO.
		/// 
		/// </summary>
		/// <returns> the string representation of CK_SLOT_INFO
		/// </returns>
		public override System.String ToString()
		{
			System.Text.StringBuilder buffer = new System.Text.StringBuilder();
			
			buffer.Append(Constants.INDENT);
			buffer.Append("slotDescription: ");
			buffer.Append(new System.String(slotDescription));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("manufacturerID: ");
			buffer.Append(new System.String(manufacturerID));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("flags: ");
			buffer.Append(Functions.slotInfoFlagsToString(flags));
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("hardwareVersion: ");
			buffer.Append(hardwareVersion.ToString());
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("firmwareVersion: ");
			buffer.Append(firmwareVersion.ToString());
			//buffer.append(Constants.NEWLINE);
			
			return buffer.ToString();
		}
	}
}