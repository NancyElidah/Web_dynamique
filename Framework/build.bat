set PATH_FRAM="V:\Fianarana\L2\S4\Mr Naina\Projet\backup Fram\Sprint7\Framework"
set JAR="V:\Fianarana\L2\S4\Mr Naina\Projet\backup Fram\Sprint7\framework.jar"
set V="V:\framework.jar"
set SRC="V:\Fianarana\L2\S4\Mr Naina\Projet\backup Fram\Sprint7"
set PATH_TEST="V:\Fianarana\L2\S4\Mr Naina\Projet\backup Fram\Sprint7\testFramework"
set TOMCAT="V:\Util2.0\apache-tomcat-8.5.75"

@REM redirection 
cd %PATH_FRAM%

@REM compiler Framework
javac -d . Url.java
javac -d . Mapping.java
javac -d . Utilitaire.java
javac -d . ModelView.java
javac -d . FrontServlet.java
@REM creation du .jar (projet framework)
jar cf framework.jar ETU001925
@REM copier .jar dans le dossier Sprint7
copy framework.jar %JAR%
@REM remplacer .jar dans le variable d'environnement
if exist %V% (
    del %V%
)
copy framework.jar %V%
cd ../
@REM redirection vers le dossier Sprint7 
cd %SRC%
@REM creation du projet pour les jsp
mkdir  %SRC%\temporary
mkdir %SRC%\temporary\WEB-INF
mkdir %SRC%\temporary\WEB-INF\classes
mkdir  lib
xcopy %SRC%\lib %SRC%\temporary\WEB-INF\lib /E /I
copy framework.jar %SRC%\temporary\WEB-INF\lib\
rmdir /S /Q %SRC%\lib

@REM compiler les .class  du testFramework et les copier dans temporary
cd %PATH_TEST%\src\java 
javac -d . -parameters Test.java
cd %PATH_TEST%
@REM copier les .class vers le projet temporary
xcopy %PATH_TEST%\src\java\test %SRC%\temporary\WEB-INF\classes\test\
@REM copier les .jsp dans le projet temporary
xcopy %PATH_TEST%\web\jsp  %SRC%\temporary\jsp\
copy %PATH_TEST%\web.xml %SRC%\temporary\WEB-INF\web.xml
cd ../
@REM creation de .war du projet temporary
cd %SRC%\temporary
jar -cvf "temporary.war" *

@REM copier dans le dossier de tomCat
copy temporary.war %TOMCAT%\webapps\

@REM supprimer le projet temporary
cd %SRC%
rmdir /s /q %SRC%\temporary

@REM demarrer tomcat
cd %TOMCAT%\bin
startup.bat
