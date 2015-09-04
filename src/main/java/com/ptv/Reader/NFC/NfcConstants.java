package com.ptv.Reader.NFC;

public interface NfcConstants {

	
	
	//******************************************************************************************//
	//																							//
	//								constants and error code									//
	//																							//
	//******************************************************************************************//
	
	// N.B. the error codes are copied from "libnfc"
	
	
	/** @ingroup error
	 * @hideinitializer
	 * Success (no error)
	 */
	final static int NFC_SUCCESS = 0;
	
	/** @ingroup error
	 * @hideinitializer
	 * Input / output error, device may not be usable anymore without re-open it
	 */
	final static int NFC_EIO = -1;
	
	/** @ingroup error
	 * @hideinitializer
	 * Invalid argument(s)
	 */
	final static int NFC_EINVARG = -2;
	
	/** @ingroup error
	 * @hideinitializer
	 *  Operation not supported by device
	 */
	final static int NFC_EDEVNOTSUPP = -3;
	
	/** @ingroup error
	 * @hideinitializer
	 * No such device
	 */
	final static int NFC_ENOTSUCHDEV = -4;
	
	/** @ingroup error
	 * @hideinitializer
	 * Buffer overflow
	 */
	final static int NFC_EOVFLOW = -5;
	
	/** @ingroup error
	 * @hideinitializer
	 * Operation timed out
	 */
	final static int NFC_ETIMEOUT = -6;
	
	/** @ingroup error
	 * @hideinitializer
	 * Operation aborted (by user)
	 */
	final static int NFC_EOPABORTED = -7;
	
	/** @ingroup error
	 * @hideinitializer
	 * Not (yet) implemented
	 */
	final static int NFC_ENOTIMPL = -8;
	
	/** @ingroup error
	 * @hideinitializer
	 * Target released
	 */
	final static int NFC_ETGRELEASED = -10;
	
	/** @ingroup error
	 * @hideinitializer
	 * Error while RF transmission
	 */
	final static int NFC_ERFTRANS = -20;
	
	/** @ingroup error
	 * @hideinitializer
	 * MIFARE Classic: authentication failed
	 */
	final static int NFC_EMFCAUTHFAIL = -30;
	
	/** @ingroup error
	 * @hideinitializer
	 * Software error (allocation, file/pipe creation, etc.)
	 */
	final static int NFC_ESOFT = -80;
	
	/** @ingroup error
	 * @hideinitializer
	 * Device's internal chip error
	 */
	final static int NFC_ECHIP = -90;
	
	
}
