import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Logger {

	private FileWriter fw;
	private BufferedWriter bw;

	public Logger(File logFile) throws Exception {
		fw = new FileWriter(logFile);
		bw = new BufferedWriter(fw);
	}

	public void print(String text) throws Exception {
		bw.write(text);
	}

	public void println(String text) throws Exception {
		bw.write(text);
		bw.newLine();
	}

	public void printKeyValue(String key, String value) throws Exception {
		bw.write(key + ": " + value);
		bw.newLine();
	}

	public void printKeyValue(String key, int value) throws Exception {
		printKeyValue(key, Integer.toString(value));
	}
	
	public void printKeyValue(String key, double value) throws Exception{
		printKeyValue(key, Double.toString(value));
	}

	public void flush() throws Exception {
		bw.flush();
	}

	public void close() throws Exception {
		bw.close();
	}

}