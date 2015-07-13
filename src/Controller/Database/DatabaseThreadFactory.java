package Controller.Database;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseThreadFactory implements ThreadFactory 
{
	static final AtomicInteger poolNumber = new AtomicInteger(1);
   
	@Override 
	public Thread newThread(Runnable runnable) 
	{
		Thread thread = new Thread(runnable, "Database-Connection-" + poolNumber.getAndIncrement() + "-thread");
		thread.setDaemon(true);

		return thread;
	}
}