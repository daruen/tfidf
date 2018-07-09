package daruen.tfidf.comparators;

import java.util.Comparator;
import java.util.Map;

public class MapValueComparator<T, U extends Comparable<U>> implements Comparator<T> {

	private Map<T, U> map;

	public MapValueComparator(Map<T, U> map) {
		super();
		this.map = map;
	}

	@Override
	public int compare(T o1, T o2) {
		return map.get(o2).compareTo(map.get(o1));
	}

}
