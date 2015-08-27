/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_ptv_Reader_NFC_NfcReaderImpl */

#ifndef _Included_com_ptv_Reader_NFC_NfcReaderImpl
#define _Included_com_ptv_Reader_NFC_NfcReaderImpl
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_ptv_Reader_NFC_NfcReaderImpl
 * Method:    openNfcReader
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_ptv_Reader_NFC_NfcReaderImpl_openNfcReader
  (JNIEnv *, jclass);

/*
 * Class:     com_ptv_Reader_NFC_NfcReaderImpl
 * Method:    closeNfcReader
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_ptv_Reader_NFC_NfcReaderImpl_closeNfcReader
  (JNIEnv *, jclass);

/*
 * Class:     com_ptv_Reader_NFC_NfcReaderImpl
 * Method:    getReaderName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_ptv_Reader_NFC_NfcReaderImpl_getReaderName
  (JNIEnv *, jclass);

/*
 * Class:     com_ptv_Reader_NFC_NfcReaderImpl
 * Method:    startPolling
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_ptv_Reader_NFC_NfcReaderImpl_startPolling
  (JNIEnv *, jclass);

/*
 * Class:     com_ptv_Reader_NFC_NfcReaderImpl
 * Method:    stopPolling
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_ptv_Reader_NFC_NfcReaderImpl_stopPolling
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
