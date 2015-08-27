/*-
 * Free/Libre Near Field Communication (NFC) library
 *
 * Libnfc historical contributors:
 * Copyright (C) 2009      Roel Verdult
 * Copyright (C) 2009-2013 Romuald Conty
 * Copyright (C) 2010-2012 Romain Tarti癡re
 * Copyright (C) 2010-2013 Philippe Teuwen
 * Copyright (C) 2012-2013 Ludovic Rousseau
 * See AUTHORS file for a more comprehensive list of contributors.
 * Additional contributors of this file:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1) Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  2 )Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Note that this license only applies on the examples, NFC library itself is under LGPL
 *
 */

/*
 * libnfc_interface.c
 *
 *  Created on: 2015年8月21日
 *      Author: Vis.Lee
 */

/**
 * @file libnfc_interface.c
 * @brief Polling example
 */



#include <err.h>
#include <inttypes.h>
#include <signal.h>
#include <stdio.h>
#include <stddef.h>
#include <stdlib.h>
#include <string.h>

#include <nfc/nfc.h>
#include <nfc/nfc-types.h>

#include "nfc-utils.h"


/******************************************************************************/
/*                                                                            */
/*  define static variables to keep the info until terminate the nfc reader   */
/*                                                                            */
/******************************************************************************/


#define MAX_DEVICE_COUNT 16

static nfc_device *pnd = NULL;
static nfc_context *context = NULL;


/*
 * NFC device will poll in total (uiPollNr * szModulations * uiPeriod * 150) ms
 * more detail: uiPollNr pollings of (uiPeriod * 150) ms for szModulations modulations)
 *
 * @param uiPollNr specifies the number of polling (0x01 to 0xFE: 1 up to 254 polling, 0xFF: Endless polling)
 * @note one polling is a polling for each desired target type
 * @param uiPeriod indicates the polling period in units of 150 ms
 * @note e.g. if uiPeriod=10, it will poll each desired target type during 1.5s
 */
static uint8_t uiPollNr = 0xFF;
static uint8_t uiPeriod = 2;

static nfc_modulation nmModulations[5] = {
  { .nmt = NMT_ISO14443A, .nbr = NBR_106 },
  { .nmt = NMT_ISO14443B, .nbr = NBR_106 },
  { .nmt = NMT_FELICA, .nbr = NBR_212 },
  { .nmt = NMT_FELICA, .nbr = NBR_424 },
  { .nmt = NMT_JEWEL, .nbr = NBR_106 },
};

#define NUM_MOD (sizeof(nfc_modulation) / sizeof(nmModulations[0]))

static size_t szModulations = NUM_MOD;

/******************************************************************************/
/*                                                                            */
/*                         functions implementation                           */
/*                                                                            */
/******************************************************************************/

/*
 * signal action, which could terminate the polling if ctrl-c pressed
 */
void stop_polling(int sig)
{
  (void) sig;
  if (pnd != NULL){
    nfc_abort_command(pnd);
    WARN("received interrupt signal! terminate nfc polling...");
  } else {
	ERR("received interrupt signal! BUT pnd is NULL!");
  }
}

/*
 * detected nfc target, prepare detected target data.
 * @RETVAL s a string contains the target info
 */
char * show_nfc_target(const nfc_target *pnt, bool verbose)
{
  char *s = NULL;
  str_nfc_target(&s, pnt, verbose);
  DBG("%s", s);
  return s;
}

/*
 * init nfc context and open device
 * @retval NFC_SUCCESS if nfc reader open successfully.
 */
int open_nfc_reader(void){

  // Display libnfc version
  const char *acLibnfcVersion = nfc_version();

  printf("%s uses libnfc %s\n", __func__, acLibnfcVersion);

  // init context
  nfc_init(&context);
  if (context == NULL) {
    ERR("Unable to init libnfc (malloc)");
    return NFC_ESOFT;
  }

  // open device
  pnd = nfc_open(context, NULL);

  if (pnd == NULL) {
    ERR("%s", "Unable to open NFC device.");
    nfc_exit(context);
    return NFC_ENOTSUCHDEV;
  }

  // set as a reader (initiator)
  if (nfc_initiator_init(pnd) < 0) {
    nfc_perror(pnd, "nfc_initiator_init");
    nfc_close(pnd);
    nfc_exit(context);
    return NFC_EIO;
  }

  printf("NFC reader: %s opened\n", nfc_device_get_name(pnd));

  return NFC_SUCCESS;

}


/*
 * close nfc reader device and nfc context
 * @RETVAL NFC_SUCCESS if nfc reader closed smoothly.
 */
void close_nfc_reader(void){


  if (pnd != NULL){
	nfc_abort_command(pnd);
	nfc_close(pnd);
	printf("close the device and ");
  }

  if(context != NULL){
	nfc_exit(context);
	printf("close the nfc context success. \n");
  }


}


char * get_reader_name(void){

	if(pnd != NULL){
		return nfc_device_get_name(pnd);
	}

	return NULL;
}

char * start_polling(void)
{
  bool verbose = false;

  signal(SIGINT, stop_polling);

  nfc_target nt;
  int res = 0;
  char *target = NULL;

  if ((res = nfc_initiator_poll_target(pnd, nmModulations, szModulations, uiPollNr, uiPeriod, &nt))  < 0) {
    nfc_perror(pnd, "nfc_initiator_poll_target");
  }

  if (res > 0) {
	target = show_nfc_target(&nt, verbose);
  } else {
    printf("No target found.\n");
  }

  //printf("Waiting for card removing...");
  while (0 == nfc_initiator_target_is_present(pnd, NULL)) {}
  nfc_perror(pnd, "nfc_initiator_target_is_present");

  return target;

}