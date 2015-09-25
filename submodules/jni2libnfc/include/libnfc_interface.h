/*
 * libnfc_interface.h
 *
 *  Created on: 2015¦~8¤ë26¤é
 *      Author: Vis.Lee
 */

#ifndef INCLUDE_LIBNFC_INTERFACE_H_
#define INCLUDE_LIBNFC_INTERFACE_H_


/******************************************************************************/
/*                                                                            */
/*  funtion declarations                                                      */
/*                                                                            */
/******************************************************************************/

int open_nfc_reader(void);
void close_nfc_reader(void);

char * get_reader_name(void);
int get_device_last_error();
const char * get_nfc_strerror();

char * start_polling(void);
void stop_polling(int sig);

#endif /* INCLUDE_LIBNFC_INTERFACE_H_ */
