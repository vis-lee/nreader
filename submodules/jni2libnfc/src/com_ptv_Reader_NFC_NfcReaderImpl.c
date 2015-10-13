/*
 * reader.c
 *
 *  Created on: 2015¦~8¤ë21¤é
 *      Author: Vis
 */

#include <stdio.h>
#include <signal.h>

#include <nfc/nfc.h>

#include "com_ptv_Reader_NFC_NfcReaderImpl.h"
#include "libnfc_interface.h"


/*
 * Class:     com_ptv_Reader_NFC_NfcReaderImpl
 * Method:    openNfcReader
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_ptv_Reader_NFC_NfcReaderImpl_openNfcDevice
  (JNIEnv *env, jclass cls){

	int ret = open_nfc_reader();

	// we need to flush out the buffer before we return to Java
	fflush(stdout);

	return ret;

}

/*
 * Class:     com_ptv_Reader_NFC_NfcReaderImpl
 * Method:    closeNfcReader
 * Signature: ()I
 */
JNIEXPORT void JNICALL Java_com_ptv_Reader_NFC_NfcReaderImpl_closeNfcDevice
  (JNIEnv *env, jclass cls){

	close_nfc_reader();

	// we need to flush out the buffer before we return to Java
	fflush(stdout);

}



/*
 * Class:     com_ptv_Reader_NFC_NfcReaderImpl
 * Method:    getReaderName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_ptv_Reader_NFC_NfcReaderImpl_getDeviceName
  (JNIEnv *env, jclass cls){

	// no need to release the const char *
	const char * ret = get_reader_name();

	return (ret != NULL) ? (*env)->NewStringUTF(env, ret) : NULL;

}


jint throwReaderRemovedException( JNIEnv *env, char *message )
{
    jclass exClass;
    const char *className = "com/ptv/Reader/ReaderRemovedException" ;

    exClass = (*env)->FindClass( env, className );
    if ( exClass == NULL ) {
        return -109;
    }

    return (*env)->ThrowNew( env, exClass, message );
}


/*
 * Class:     com_ptv_Reader_NFC_NfcReaderImpl
 * Method:    startPolling
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_ptv_Reader_NFC_NfcReaderImpl_startPolling
  (JNIEnv *env, jclass cls){

	char * nfc_target = NULL;
	jstring jnfc_target = NULL;

	nfc_target = start_polling();

	// normal case
	if(nfc_target != NULL){

		jnfc_target = (*env)->NewStringUTF(env, nfc_target);

		nfc_free(nfc_target);

	} else {

		/*
		 * cmd abort or device not-exist,
		 * check the last error in the nfc device
		 */
		int le = get_device_last_error();
		jnfc_target = (*env)->NewStringUTF(env, get_nfc_strerror());

		if( (le == NFC_EIO) || (le == NFC_ENOTSUCHDEV) ){

			// raise exception
			throwReaderRemovedException(env, get_nfc_strerror());

			// release the global context first
			close_nfc_reader();
		}
	}

	// we need to flush out the buffer before we return to Java
	fflush(stdout);

	return jnfc_target;

}


/*
 * Class:     com_ptv_Reader_NFC_NfcReaderImpl
 * Method:    stopPolling
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_ptv_Reader_NFC_NfcReaderImpl_stopPolling
(JNIEnv *env, jclass cls){

	stop_polling(SIGINT);

	// we need to flush out the buffer before we return to Java
	fflush(stdout);


	stop_polling(SIGINT);

	// we need to flush out the buffer before we return to Java
	fflush(stdout);
}



