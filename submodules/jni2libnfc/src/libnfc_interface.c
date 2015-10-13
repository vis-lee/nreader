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

// TODO we should create a container for the devices after POC ( return an index to upper layer and call the function with the index )
static nfc_device *g_pnd = NULL;
static nfc_context *g_context = NULL;

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

/*
 * we want to detect all kinds of NFC card
 */
static nfc_modulation nmModulations[5] = {
		{ .nmt = NMT_ISO14443A, .nbr =	NBR_106 },
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
void stop_polling(int sig) {
	(void) sig;
	if (g_pnd != NULL) {
		int retcode = nfc_abort_command(g_pnd);
		printf("received interrupt signal! terminate nfc polling... the retcode = %d ", retcode);
		WARN("received interrupt signal! terminate nfc polling...");
	} else {
		ERR("received interrupt signal! BUT pnd is NULL!");
	}

	printf("%s, %d, signal is triggered!\n", __func__, __LINE__);
}

/*
 * detected nfc target, prepare detected target data.
 * @RETVAL s a string contains the target info
 */
char * show_nfc_target(const nfc_target *pnt, bool verbose) {
	char *s = NULL;
	str_nfc_target(&s, pnt, verbose);
	DBG("%s", s);
	return s;
}


int get_device_last_error(){

	int ret_code = 0;

	if(g_pnd != NULL){

		ret_code = nfc_device_get_last_error(g_pnd);
		nfc_perror(g_pnd, "get device error: ");
		printf("nfc_device_get_last_error = %d", ret_code);

	} else {

		printf("get_device_last_error fail!!...");
	}

	return ret_code;
}

const char * get_nfc_strerror(){

	return (g_pnd != NULL) ? nfc_strerror(g_pnd) : "UNKNOWN";
}

/*
 * init nfc context and open device
 * @retval NFC_SUCCESS if nfc reader open successfully.
 */
int open_nfc_reader(void) {

	// Display libnfc version
	const char *acLibnfcVersion = nfc_version();
	printf("%s uses libnfc %s\n", __func__, acLibnfcVersion);

	//init context, only one context
	if (g_context == NULL) {

		nfc_init(&g_context);

		if (g_context == NULL) {
			ERR("Unable to init libnfc (malloc)");
			return NFC_ESOFT;
		}

	} else {

		printf("g_context exist at = %p \n", (void *) g_context);
	}

	// FIXME to allow multiple devices
	if (g_pnd == NULL) {

		// open device
		g_pnd = nfc_open(g_context, NULL);

		if (g_pnd == NULL) {
			ERR("%s", "Unable to open NFC device.");
			// we don't need to release context if there is no nfc devices
			// nfc_exit(g_context);
			// g_context = NULL;
			return NFC_ENOTSUCHDEV;
		}

		// set as a reader (initiator)
		if (nfc_initiator_init(g_pnd) < 0) {
			nfc_perror(g_pnd, "nfc_initiator_init failed!");
			nfc_close(g_pnd);
			g_pnd = NULL;
			nfc_exit(g_context);
			g_context = NULL;
			return NFC_EIO;
		}

	} else {

		// shouldn't happened
		printf("g_pnd isn't NULL = %p", (void *) g_pnd);
	}

	printf("NFC reader: %s opened\n", nfc_device_get_name(g_pnd));

	return NFC_SUCCESS;

}

/*
 * close nfc reader device and nfc context
 * @RETVAL NFC_SUCCESS if nfc reader closed smoothly.
 */
void close_nfc_reader(void) {

	if (g_pnd != NULL) {
		nfc_abort_command(g_pnd);
		nfc_close(g_pnd);
		g_pnd = NULL;
		printf("close the device and ");
	}

	if (g_context != NULL) {
		nfc_exit(g_context);
		g_context = NULL;
		printf("close the nfc context success. \n");
	}

}

const char * get_reader_name(void) {

	if (g_pnd != NULL) {
		return nfc_device_get_name(g_pnd);
	}

	return NULL;
}

char * start_polling(void) {
	bool verbose = false;

	signal(SIGINT, stop_polling);

	nfc_target nt;
	int res = 0;
	char *target = NULL;

	if ((res = nfc_initiator_poll_target(g_pnd, nmModulations, szModulations, uiPollNr, uiPeriod, &nt)) < 0) {
		nfc_perror(g_pnd, "nfc_initiator_poll_target");
		printf("%s, %d, return code = %d", __func__, __LINE__, res);
	}

	if (res > 0) {
		target = show_nfc_target(&nt, verbose);

		printf("Waiting for card removing...");
		while (0 == nfc_initiator_target_is_present(g_pnd, NULL)) {}

	} else {
		printf("No target found.\n");
	}

	printf(g_pnd, "nfc_initiator_target_is_present");

	return target;

}



