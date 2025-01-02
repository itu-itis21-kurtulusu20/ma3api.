@echo off
setlocal
echo ^<?xml version=^"1.0^" encoding=^"UTF-8^"?^> > .\target\.classpath
echo ^<classpath^> >> .\target\.classpath
echo 	^<classpathentry kind=^"src^" path=^"ornekler/src^"/^> >> .\target\.classpath
echo 	^<classpathentry kind=^"con^" path=^"org.eclipse.jdt.launching.JRE_CONTAINER^"/^> >> .\target\.classpath
for /r .\target\lib %%g in (*.jar) do (
  echo 	^<classpathentry kind=^"lib^" path=^"lib^/%%~nxg^"^/^> >> .\target\.classpath
)
for /r .\target\zkm %%g in (ma3api-xmlsignature*.jar) do (
  echo 	^<classpathentry kind=^"lib^" path=^"lib^/%%~nxg^"^/^> >> .\target\.classpath
)
echo 	^<classpathentry kind=^"output^" path=^"bin^"^/^> >> .\target\.classpath
echo ^</classpath^> >> .\target\.classpath
