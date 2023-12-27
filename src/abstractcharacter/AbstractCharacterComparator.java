package abstractcharacter;

import java.util.Comparator;

public class AbstractCharacterComparator<T extends AbstractCharacter> implements Comparator<T>{

	@Override
	public int compare(T o1, T o2) {
		// if o1 > o2 it is negative, so o1 has a priority.
		return o2.getSpeed()-o1.getSpeed();
	}

}
