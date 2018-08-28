# How to change JVM arguments at runtime to avoid application restart

---
## Problem

In order for JVM arguments to become effective, application administrators need to modify configuration files and restart the application, so that the new settings are picked up. In production environments this causes distruption in the service, which is undesirable.


## Workaround

Java SDK comes with an utility that can alter some of these arguments while the java process is running, making them effective without a restart. You can learn more about this tool on Oracle's tech note: jinfo - [Oracle Help Center](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jinfo.html).

Not all of the available parameters can be changed, for a complete list of settings that can be modified, you can use the command below.

	java -XX:+PrintFlagsFinal -version|grep manageable
 
     intx CMSAbortablePrecleanWaitMillis            = 100                                 {manageable}
     intx CMSTriggerInterval                        = -1                                  {manageable}
     intx CMSWaitDuration                           = 2000                                {manageable}
     bool HeapDumpAfterFullGC                       = false                               {manageable}
     bool HeapDumpBeforeFullGC                      = false                               {manageable}
     bool HeapDumpOnOutOfMemoryError                = false                               {manageable}
    ccstr HeapDumpPath                              =                                     {manageable}
    uintx MaxHeapFreeRatio                          = 100                                 {manageable}
    uintx MinHeapFreeRatio                          = 0                                   {manageable}
     bool PrintClassHistogram                       = false                               {manageable}
     bool PrintClassHistogramAfterFullGC            = false                               {manageable}
     bool PrintClassHistogramBeforeFullGC           = false                               {manageable}
     bool PrintConcurrentLocks                      = false                               {manageable}
     bool PrintGC                                   = false                               {manageable}
     bool PrintGCDateStamps                         = false                               {manageable}
     bool PrintGCDetails                            = false                               {manageable}
     bool PrintGCID                                 = false                               {manageable}
     bool PrintGCTimeStamps                         = false     

To change a specific parameter of the above, the command can be used as you can see in the following example (<PID> represents the process id for the corresponding Java process):

	jinfo -flag +PrintGCDetails <PID>


##### Please note that the changes made via jinfo are not persistent, meaning if you restart the application they will revert back to their default value, set by your startup scripts. If you want the changes to be effective after a restart, you will need to modify your startup scripts accordingly.