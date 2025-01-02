PROJECT(EsyaCMS)

SET(KERMEN_MAIN_SOURCE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../)
include("${CMAKE_CURRENT_SOURCE_DIR}/../cmake/version.cmake") 
include("${CMAKE_CURRENT_SOURCE_DIR}/../cmake/KermenCMakeLib.cmake") 
include("${CMAKE_CURRENT_SOURCE_DIR}/../cmake/includeHeaders.cmake") 
SET(KERMEN_CMAKE_LIB_TANIMLANDI TRUE)


libOlustur(EsyaCMS SHARED
	WINLIBS EsyaOrtakDLL EsyaASN EsyaPKCS11 EsyaKripto asn
	LNXLIBS EsyaOrtakDLL EsyaASN EsyaPKCS11 EsyaKripto asn
	TESTIMYOK
    )
