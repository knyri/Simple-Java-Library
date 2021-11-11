package simple.util.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
			preader= new Unknown();
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
	public static boolean processExists(Class<?> clazz) throws IOException {
		return preader.processExists(clazz);
	}
	public static void writeProcessId(Class<?> clazz) throws IOException {
		preader.writeProcessId(clazz);
	}
	public static String readProcessId(Class<?> clazz) throws IOException {
		return preader.readProcessId(clazz);
	}

	private ProcessUtil() {}

	private static interface ProcessReader{
		public boolean processExists(int pid) throws IOException;
		public int getProcessId() throws IOException;
		public default boolean processExists(Class<?> clazz) throws IOException {
			String pidStr= readProcessId(clazz);
			if(pidStr != null && !pidStr.trim().isEmpty()) {
				if(processExists(Integer.parseInt(pidStr, 10))) {
					return true;
				}
			}
			return false;
		}
		public default void writeProcessId(Class<?> clazz) throws IOException {
			File pidFile= new File(clazz.getCanonicalName() + ".pid");
			try(FileWriter pidWriter= new FileWriter(pidFile, false)){
				pidWriter.write(Integer.toString(ProcessUtil.getProcessId(), 10));
			}
		}
		public default String readProcessId(Class<?> clazz) throws IOException {
			Path pidFile= Paths.get(clazz.getCanonicalName() + ".pid");
			if(Files.exists(pidFile)){
				return Files.readAllLines(pidFile).get(0);
			}
			return null;
		}
	}
	private static final class Unknown implements ProcessReader{
		@Override
		public boolean processExists(int pid) throws IOException{
			throw new IOException("Unknown system");
		}
		@Override
		public int getProcessId() throws IOException{
			throw new IOException("Unknown system");
		}
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
