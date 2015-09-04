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

/*
 * Class:     com_ptv_Reader_NFC_NfcReaderImpl
 * Method:    startPolling
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_ptv_Reader_NFC_NfcReaderImpl_startPolling
  (JNIEnv *env, jclass cls){

	char * nfc_target = NULL;

	nfc_target = start_polling();

	jstring jnfc_target = (*env)->NewStringUTF(env, nfc_target);

	nfc_free(nfc_target);

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

}
