package main.java;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;


public class Memory_Check implements Runnable{

	public Memory_Check() {
	}
	@Override
	public void run() {
		while(true){
			long heapsize=Runtime.getRuntime().totalMemory();
			long freememory=Runtime.getRuntime().freeMemory();
			System.out.println("Memory is::"+freememory + "/" + heapsize);	
			
			//Check for thread deadlock
			ThreadMXBean bean = ManagementFactory.getThreadMXBean();
			long[] threadIds = bean.findDeadlockedThreads(); // Returns null if no threads are deadlocked.

			if (threadIds != null) {
			    ThreadInfo[] infos = bean.getThreadInfo(threadIds);

			    for (ThreadInfo info : infos) {
			        StackTraceElement[] stack = info.getStackTrace();
			        // Log or store stack trace information.
			    }
			    break;
			}
		}
	}

}
