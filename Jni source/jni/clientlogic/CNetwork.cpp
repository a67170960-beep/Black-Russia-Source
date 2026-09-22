#include "CNetwork.h"
#include <stdint.h>

#include "util/CJavaWrapper.h"

const char* g_szServerNames[MAX_SERVERS] = {
	"LUX RUSSIA | RED",
	"LUX RUSSIA | TEST"
};

const CSetServer::CServerInstanceEncrypted g_sEncryptedAddresses[MAX_SERVERS] = {
	CSetServer::create("127.0.0.1", 128, 9, 7777, false),
	CSetServer::create("127.0.0.1", 128, 9, 7777, false)	
};