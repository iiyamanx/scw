cd "C:\Users\iiyama\Documents\init\02_管理\03_製品管理\Jtest\parasoft_jtest_2022.1.0_win32_x86_64\jtest"

SET WORK="C:\Users\iiyama\Documents\init\04_環境\Dev\workflow\actions-runner\_work\demo\demo\End2EndSOAP"
SET REP="C:\Users\iiyama\Documents\init\04_環境\Dev\workflow\actions-runner\_work\demo\demo"

jtestcli -J-Dfile.encoding=UTF-8 -data %WORK%\jtest.data.json -config "builtin://CWE Top 25 2021" -report %REP%
