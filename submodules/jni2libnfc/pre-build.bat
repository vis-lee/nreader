set JAVA_HOME="C:\Program Files\Java\jdk1.8.0_51"
set PTV_DEM_HOME=C:\Users\Redmine\Documents\workspace\git\ptv-daemon
set JNI2LIBNFC_HOME="%PTV_DEM_HOME%\submodules\jni2libnfc"

%JAVA_HOME%\bin\javac %PTV_DEM_HOME%\src\main\java\com\ptv\Reader\NFC\NfcReaderImpl.java
%JAVA_HOME%\bin\javah -cp %PTV_DEM_HOME%\src\main\java com.ptv.Reader.NFC.NfcReaderImpl
xcopy *.h %JNI2LIBNFC_HOME%\include\ /r /y 
pause