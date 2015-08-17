/*
 *
 *  Created on: 2015¦~8¤ë14¤é
 *      Author: Vis
 */

#include "com_ptv_JNI_Cpp_example.h"

#include <stdlib.h>

#include "vis_JNI_impl.h"


JNIEXPORT jstring JNICALL Java_com_ptv_JNI_Cpp_1example_helloVis
  (JNIEnv *env, jclass clazz, jstring str){


	// get native string type
	const char *_extract_str = env->GetStringUTFChars(str, 0);

	// call to whatever you want to do
	char * ret_str = get_test_str((char *)_extract_str);

	env->ReleaseStringUTFChars(str, _extract_str);

	// copy to JVM
	jstring jstr = env->NewStringUTF(ret_str);

	// do I need to release the buf I allocate from c?
	// Ans: Yes, I need to do that.
	put_test_str(ret_str);

	return jstr;


}

