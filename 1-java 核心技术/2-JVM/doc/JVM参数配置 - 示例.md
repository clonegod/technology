	

	-server 
	-Xmx2g -Xms2g -Xmn512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -Xss256k 
	-XX:+DisableExplicitGC 
	-XX:+UseConcMarkSweepGC 
	-XX:+CMSParallelRemarkEnabled 
	-XX:+UseCMSCompactAtFullCollection
	-XX:+UseCMSInitiatingOccupancyOnly 
	-XX:CMSInitiatingOccupancyFraction=70 
	-XX:LargePageSizeInBytes=128m 
	-XX:+UseFastAccessorMethods 
	-XX:+PrintGCDetails
	-XX:+PrintGCTimeStamps
	-Xloggc:/tmp/logs/gc.log
	-XX:+HeapDumpBeforeFullGC 
	-XX:+HeapDumpAfterFullGC  
	-XX:HeapDumpPath=/tmp/logs/fullgc.hprof
	-XX:+PrintFlagsFinal
 