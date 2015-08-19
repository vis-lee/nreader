/*
 * vis_JNI_impl.cpp
 *
 *  Created on: 2015¦~8¤ë14¤é
 *      Author: vis
 */

#include "vis_JNI_impl.h"

#include <stdio.h>
#include <stdlib.h>



char * get_test_str(char * str){

	char * ret = (char*)malloc(sizeof(char)*128);

	sprintf(ret, "Hello Vis, this msg comes from c lib which is called from JNI with arg: %s", str);

	return ret;

}

int put_test_str(char* buf){

	free(buf);

	return 0;
}

