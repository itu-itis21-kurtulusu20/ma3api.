#PROJECT(CMS_TEST)

ENABLE_TESTING()
include("${CMAKE_CURRENT_SOURCE_DIR}/../../cmake/includeHeaders.cmake") 
#---------------------------------------------------------------------------------------------------
FILE(GLOB_RECURSE CMS_TEST_SOURCES ${CMAKE_CURRENT_SOURCE_DIR}/*.cpp)
FILE(GLOB_RECURSE CMS_TEST_MOC_HEADERS ${CMAKE_CURRENT_SOURCE_DIR}/*.h)
#------------------------------------------SON-------------------------------------------------------

QT4_WRAP_CPP( CMS_TEST_MOC_SOURCES ${CMS_TEST_MOC_HEADERS} )
    
#-------------------------------------------------------------------------------------------------------
# K�t�phaneyi olu�turup di�er k�t�phanelerle ba�lant�lar�n� sa�l�yoruz.
#-------------------------------------------BAS---------------------------------------------------------
IF(UNIX)
        MESSAGE(STATUS "Linux-Test")
	ADD_EXECUTABLE(CMS_TEST ${CMS_TEST_SOURCES} ${CMS_TEST_MOC_SOURCES})
	TARGET_LINK_LIBRARIES(CMS_TEST ASNLib_l asn1rt_l asn1ber_l qtkripto_l epkcs11_l CmsLibrary_l)
	TARGET_LINK_LIBRARIES(CMS_TEST ${QT_LIBRARIES} ${QT_QTTEST_LIBRARY})
	ADD_TEST(testCmsLibrary CMS_TEST)
ELSE(WIN32)
	MESSAGE(STATUS "Windows-Test")
	ADD_EXECUTABLE(CMS_TEST ${CMS_TEST_SOURCES} ${CMS_TEST_MOC_SOURCES})
	TARGET_LINK_LIBRARIES(CMS_TEST ASNLib qtkripto epkcs11 CmsLibrary)
	TARGET_LINK_LIBRARIES(CMS_TEST ${QT_LIBRARIES} ${QT_QTTEST_LIBRARY})
	ADD_TEST(testCmsLibrary CMS_TEST)

ENDIF(UNIX)

#--------------------------------------------SON------------------------------------------------------------







