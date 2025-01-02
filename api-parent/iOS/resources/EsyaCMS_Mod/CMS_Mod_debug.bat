cleartool update
cd..
cd Kripto_Mod
	mkdir built
	cd built
	cmake .. -G "NMake Makefiles" -DCMAKE_BUILT_TYPE:STRING=Debug
	nmake
	nmake
	cd..
	cd..
cd CMS_Mod
	mkdir built
	cd built 
	cmake .. -G "NMake Makefiles" -DCMAKE_BUILT_TYPE:STRING=Debug
	nmake
	nmake