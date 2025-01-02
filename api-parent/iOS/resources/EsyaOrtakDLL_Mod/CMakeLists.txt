PROJECT(EsyaOrtakDLL)
SET(KERMEN_MAIN_SOURCE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../)
include("${CMAKE_CURRENT_SOURCE_DIR}/../cmake/version.cmake") 
include("${CMAKE_CURRENT_SOURCE_DIR}/../cmake/KermenCMakeLib.cmake") 
include("${CMAKE_CURRENT_SOURCE_DIR}/../cmake/includeHeaders.cmake") 
SET(KERMEN_CMAKE_LIB_TANIMLANDI TRUE)


libOlustur(EsyaOrtakDLL SHARED
	   WINLIBS 
	   LNXLIBS smbclient
	   TESTIMYOK)

#--------------------------------------------SON------------------------------------------------------------








