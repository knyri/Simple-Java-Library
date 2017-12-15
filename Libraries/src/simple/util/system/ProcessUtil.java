package simple.util.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.List;

import simple.io.FileUtil;
import simple.io.ParseException;
import simple.io.StreamEater;
import simple.parser.csv.CsvReader;

public final class ProcessUtil {

	private static final ProcessReader preader;
	static {
		String os= System.getProperty("os.name").toLowerCase();
		if(os.contains("windows")){
			preader= new Windows();
		}else if(os.contains("linux") || os.contains("unix") || os.contains("mac")){
			preader= new Unix();
		}else{
			preader= null;
		}
	}

	/**
	 * Creates, starts, and returns a ProcessEater
	 * @param proc
	 * @return
	 */
	public static ProcessEater discardProcess(final Process proc){
		ProcessEater eater= new ProcessEater(proc);
		new Thread(eater).start();
		return eater;
	}
	public static int getProcessId() throws IOException{
		return preader.getProcessId();
	}
	public static boolean processExists(int pid) throws IOException{
		return preader.processExists(pid);
	}

	private ProcessUtil() {}

	private static interface ProcessReader{
		public boolean processExists(int pid) throws IOException;
		public int getProcessId() throws IOException;
	}
	private static final class Unix implements ProcessReader{
		private static final String[] getProcesses= new String[]{"ps", "-eo", "pid"};
		@Override
		public boolean processExists(int pid) throws IOException {
			Process processes= Runtime.getRuntime().exec(getProcesses);
			new Thread(new StreamEater(processes.getErrorStream())).start();
			String sPid= Integer.toString(pid, 10);
			BufferedReader procReader= new BufferedReader(new InputStreamReader(processes.getInputStream()));
			String cPid;
			while((cPid= procReader.readLine()) != null){
				if(cPid.trim().equals(sPid)){
					FileUtil.discard(procReader);
					return true;
				}
			}
			return false;
		}

		@Override
		public int getProcessId() throws NumberFormatException, IOException {
			return Integer.parseInt(new File("/proc/self").getCanonicalFile().getName());
		}
	}
	private static final class Windows implements ProcessReader{
		private static final String[] getProcesses= new String[]{System.getenv("windir") + "\\system32\\tasklist.exe", "/PO", "CSV"};
		@Override
		public boolean processExists(int pid) throws IOException {
			Process processes= Runtime.getRuntime().exec(getProcesses);
			new Thread(new StreamEater(processes.getErrorStream())).start();
			String sPid= Integer.toString(pid, 10);
			try(CsvReader procReader= new CsvReader(new BufferedReader(new InputStreamReader(processes.getInputStream())))){
				List<String> process;
				// discard the headers
				procReader.readRow();
				while((process= procReader.readRow()) != null){
					// image name; pid; session name; session#; mem usage
					if(process.get(1).trim().equals(sPid)){
						FileUtil.discard(processes.getInputStream());
						return true;
					}
				}
			} catch (ParseException e) {
				throw new IOException("Error reading the output of the process", e);
			}
			return false;
		}

		@Override
		public int getProcessId() {
			String name= ManagementFactory.getRuntimeMXBean().getName();
			return Integer.parseInt(name.substring(0, name.indexOf('@')), 10);
		}

	}
}
