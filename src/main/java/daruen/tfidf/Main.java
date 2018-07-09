package daruen.tfidf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import daruen.tfidf.calculators.TFIDFCalculator;
import daruen.tfidf.comparators.MapValueComparator;

public class Main {
	volatile static Map<String, Double> unorderedResults = Collections.synchronizedMap(new HashMap<>());

	private static Comparator<String> comparator = new MapValueComparator<>(unorderedResults);

	volatile static Map<String, Double> sortedMap = new TreeMap<>(comparator);

	public static void main(String[] args) {
		String d = null;
		String t = null;
		Integer nAux = null;
		Integer p = null;

		for (int i = 0; i < args.length - 1; i++) {
			if (args[i].equals("-D") || args[i].equals("-d")) {
				i++;
				d = args[i];
			}

			if (args[i].equals("-n")) {
				i++;
				nAux = Integer.valueOf(args[i]);
			}
			if (args[i].equals("-p")) {
				i++;
				p = Integer.valueOf(args[i]);
			}
			if (args[i].equals("-t")) {
				i++;
				t = args[i];
			}

		}

		final Integer n = nAux;

		if (d == null || t == null || n == null || p == null) {
			System.out.println("The usage should be like this....");
			System.out.println("./tdIdf -d dir -n 5 -p 300 -t \"password try again\"");
			return;
		}

		File directory = new File(d);
		TFIDFCalculator tfidfCalculator = new TFIDFCalculator();
		if (!directory.isDirectory()) {
			System.out.println("-d should be a directory");
			return;
		}

		for (File f : directory.listFiles()) {
			unorderedResults.put(f.getName(), tfidfCalculator.process(f, t));
		}

		try {

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {

					sortedMap.putAll(unorderedResults);

					Set<String> sortedKeySet = sortedMap.keySet();

					Iterator<String> it = sortedKeySet.iterator();
					Integer count = 0;
					while (it.hasNext() && count != n) {
						String next = it.next();
						count++;
						System.out.println(next + " " + sortedMap.get(next));

					}
					System.out.println();

				}
			}, 0l, p);
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path path = new File(d).toPath();

			path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

			while (true) {
				WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException x) {
					return;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();

					// This key is registered only
					// for ENTRY_CREATE events,
					// but an OVERFLOW event can
					// occur regardless if events
					// are lost or discarded.
					if (kind == StandardWatchEventKinds.OVERFLOW) {
						continue;
					}

					File createdFile = new File(
							((Path) key.watchable()).resolve(((WatchEvent<Path>) event).context()).toString());

					//This is done in order to check the lock of the file, is not the best way but I could not find out a better way
					while (isFileUnlocked(createdFile) == false) {
					}
					directory = new File(d);
					for (File f : directory.listFiles()) {
						unorderedResults.put(f.getName(), tfidfCalculator.process(f, t));
					}

				}

				// Reset the key -- this step is critical if you want to
				// receive further watch events. If the key is no longer valid,
				// the directory is inaccessible so exit the loop.
				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static boolean isFileUnlocked(File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			if (in != null)
				in.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

}
