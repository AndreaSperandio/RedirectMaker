package model;

import java.util.ArrayList;
import java.util.List;

import view.component.RMComboBoxItem;

public enum QueryParam {
	EXACT(0), IGNORE(1), PASS(2);

	private int value;

	private QueryParam(final int value) {
		this.value = value;
	}

	public static QueryParam toQueryParam(final int queryParam) {
		if (queryParam == EXACT.value) {
			return EXACT;
		}
		if (queryParam == IGNORE.value) {
			return IGNORE;
		}
		if (queryParam == PASS.value) {
			return PASS;
		}
		return EXACT;
	}

	private static String toString(final QueryParam queryParam) {
		switch (queryParam) {
		case IGNORE:
			return "Ignora tutti i parametri";
		case PASS:
			return "Ignora e passa i parametri alla destinazione";
		case EXACT:
		default:
			return "Exact match in any order";
		}
	}

	public static List<RMComboBoxItem<QueryParam>> getComboItems() {
		final List<RMComboBoxItem<QueryParam>> l = new ArrayList<>();
		l.add(new RMComboBoxItem<>(EXACT, QueryParam.toString(EXACT)));
		l.add(new RMComboBoxItem<>(IGNORE, QueryParam.toString(IGNORE)));
		l.add(new RMComboBoxItem<>(PASS, QueryParam.toString(PASS)));
		return l;
	}
}
