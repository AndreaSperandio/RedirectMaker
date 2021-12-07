package model;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

public class ImportedRow {
	private static final DataFormatter DATA_FORMATTER = new DataFormatter();

	private List<Cell> cells = new ArrayList<>();

	public List<Cell> getCells() {
		return this.cells;
	}

	public void setCells(final List<Cell> cells) {
		this.cells = cells;
	}

	public void addCell(final Cell cell) {
		this.cells.add(cell);
	}

	public String getStringAt(final int index) {
		if (this.cells == null || index < 0 || index >= this.cells.size()) {
			return null;
		}

		final String value = ImportedRow.getValueAsString(this.cells.get(index));
		return !"".equals(value) ? value : null;
	}

	public Integer getIntegerAt(final int index) {
		if (this.cells == null || index < 0 || index >= this.cells.size()) {
			return null;
		}

		try {
			final String value = ImportedRow.getValueAsString(this.cells.get(index));
			return !"".equals(value) ? Integer.parseInt(value) : null;
		} catch (@SuppressWarnings("unused") final NumberFormatException ex) {
			return null;
		}
	}

	public boolean isEmpty() {
		for (final Cell cell : this.cells) {
			if (!"".equals(ImportedRow.getValueAsString(cell))) {
				return false;
			}
		}
		return true;
	}

	private static String getValueAsString(final Cell cell) {
		return cell == null ? "" : ImportedRow.DATA_FORMATTER.formatCellValue(cell).trim();
	}

	@Override
	public String toString() {
		final StringBuilder retValue = new StringBuilder();
		for (final Cell cell : this.cells) {
			retValue.append(ImportedRow.getValueAsString(cell) + "\t");
		}
		return retValue.length() >= 2 ? retValue.substring(0, retValue.length() - 2).toString() : "";
	}
}
