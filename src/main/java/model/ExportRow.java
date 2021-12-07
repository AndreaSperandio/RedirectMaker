package model;

import java.util.ArrayList;
import java.util.List;

public class ExportRow {
	private List<String> cells = new ArrayList<>();

	public List<String> getCells() {
		return this.cells;
	}

	public void setCells(final List<String> cells) {
		this.cells = cells;
	}

	public void addCell(final String cell) {
		this.cells.add(cell);
	}

	public String getCellAt(final int index) {
		if (this.cells == null || index < 0 || index >= this.cells.size()) {
			return null;
		}

		final String value = this.cells.get(index);
		return !"".equals(value) ? value : null;
	}

	public boolean isEmpty() {
		for (final String cell : this.cells) {
			if (!"".equals(cell)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder retValue = new StringBuilder();
		for (final String cell : this.cells) {
			retValue.append(cell + "\t");
		}
		return retValue.length() >= 2 ? retValue.substring(0, retValue.length() - 2).toString() : "";
	}
}
