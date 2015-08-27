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


/*
 * Class:     com_ptv_Reader_NFC_NfcReaderImpl
 * Method:    openNfcReader
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_ptv_Reader_NFC_NfcReaderImpl_openNfcReader
  (JNIEnv *env, jclass cls){

	return open_nfc_reader();

}

/*
 * Class:     com_ptv_Reader_NFC_NfcReaderImpl
 * Method:    closeNfcReader
 * Signature: ()I
 */
JNIEXPORT void JNICALL Java_com_ptv_Reader_NFC_NfcReaderImpl_closeNfcReader
  (JNIEnv *env, jclass cls){

	close_nfc_reader();

}



/*
 * Class:     com_ptv_Reader_NFC_NfcReaderImpl
 * Method:    getReaderName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_ptv_Reader_NFC_NfcReaderImpl_getReaderName
  (JNIEnv *env, jclass cls){

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

}
