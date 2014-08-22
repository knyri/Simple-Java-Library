package tests.time;

import java.util.Calendar;
import java.util.TimeZone;

public class CalendarVsManual{

	public static void main(String[] args){
		//System.out.println("(): "+tests+" tests: "+(end-start)+"ms");
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		final int tests=50000;
		long start=System.currentTimeMillis(),end;
		for(int i=0;i<tests;i++){
			//avg: 100ms
			getTimeLong1(System.currentTimeMillis());
		}
		end=System.currentTimeMillis();
		System.out.println("getTimeLong1(): "+tests+" tests: "+(end-start)+"ms");
		System.out.println(getTimeLong1(System.currentTimeMillis()));

		start=System.currentTimeMillis();
		for(int i=0;i<tests;i++){
			//avg: 250ms
			getTimeLong2(System.currentTimeMillis());
		}
		end=System.currentTimeMillis();
		System.out.println("getTimeLong2(): "+tests+" tests: "+(end-start)+"ms");
		System.out.println(getTimeLong2(System.currentTimeMillis()));

		start=System.currentTimeMillis();
		for(int i=0;i<tests;i++){
			//avg: 60ms
			getTimeLong3(System.currentTimeMillis());
		}
		end=System.currentTimeMillis();
		System.out.println("getTimeLong3(): "+tests+" tests: "+(end-start)+"ms");
		System.out.println(getTimeLong3(System.currentTimeMillis()));
	}

	public static final String getTimeLong1(final long milliseconds) {
		if (milliseconds <= 0)
			return "0 seconds";
		final StringBuilder buf = new StringBuilder(32);
		final long totalSeconds	= milliseconds/1000,
		totalMinutes	= totalSeconds/60,
		totalHours		= totalMinutes/60;
		boolean sep=false;
		if (totalHours%24 > 0) {
			buf.append((totalHours % 24)+((totalHours%24 > 1)?" hours":" hour"));
			sep=true;
		}
		if (totalMinutes%60 > 0) {
			buf.append(((sep)?", ":"")+(totalMinutes % 60)+((totalMinutes%60 > 1)?" minutes":" minute"));
			sep=true;
		}
		if (totalSeconds%60 > 0) {
			buf.append(((sep)?", ":"")+(totalSeconds % 60)+((totalSeconds%60 > 1)?" seconds":" second"));
		}

		return buf.toString();
	}
	public static final String getTimeLong2(final long milliseconds) {
		if (milliseconds <= 0)
			return "0 seconds";
		Calendar c=Calendar.getInstance();
		c.setTimeInMillis(milliseconds);
		int hour=c.get(Calendar.HOUR_OF_DAY),
				minute=c.get(Calendar.MINUTE),
				second=c.get(Calendar.SECOND);
		final StringBuilder buf = new StringBuilder(32);
		boolean sep=false;
		if (hour > 0){
			buf.append(hour+((hour > 1)?" hours":" hour"));
			sep=true;
		}

		if (minute > 0){
			buf.append(((sep)?", ":"")+(minute)+((minute > 1)?" minutes":" minute"));
			sep=true;
		}

		if (second > 0)
			buf.append(((sep)?", ":"")+(second)+((second > 1)?" seconds":" second"));

		return buf.toString();
	}
	public static final String getTimeLong3(final long milliseconds) {
		if (milliseconds <= 0)
			return "0 seconds";
		final StringBuilder buf = new StringBuilder(32);
		final long totalSeconds	= milliseconds/1000,
		totalMinutes	= totalSeconds/60,
		totalHours		= totalMinutes/60;
		final int
			hours=(int)(totalHours%24),
			minutes=(int)(totalMinutes % 60),
			seconds=(int)(totalSeconds % 60);
		boolean sep=false;
		if (hours > 0) {
			buf.append((hours)+((hours > 1)?" hours":" hour"));
			sep=true;
		}
		if (minutes > 0) {
			buf.append(((sep)?", ":"")+(minutes)+((minutes > 1)?" minutes":" minute"));
			sep=true;
		}
		if (seconds > 0) {
			buf.append(((sep)?", ":"")+(seconds)+((seconds > 1)?" seconds":" second"));
		}

		return buf.toString();
	}
}
