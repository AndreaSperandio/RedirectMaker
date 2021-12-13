package model;

import java.util.ArrayList;
import java.util.List;

public class ExportRow {
	public class ExportCell {
		private String value;
		private boolean toColor;
		private boolean isRedirect;

		public ExportCell(final String value, final boolean toColor, final boolean isRedirect) {
			super();
			this.value = value;
			this.toColor = toColor;
			this.isRedirect = isRedirect;
		}

		public String getValue() {
			return !"".equals(this.value) ? this.value : null;
		}

		public void setValue(final String value) {
			this.value = value;
		}

		public boolean isToColor() {
			return this.toColor;
		}

		public void setToColor(final boolean toColor) {
			this.toColor = toColor;
		}

		public boolean isRedirect() {
			return this.isRedirect;
		}

		public void setRedirect(final boolean isRedirect) {
			this.isRedirect = isRedirect;
		}

		@Override
		public String toString() {
			return "ExportCell [value=" + this.value + ", toColor=" + this.toColor + ", isRedirect=" + this.isRedirect
					+ "]";
		}
	}

	private List<ExportCell> exportCells = new ArrayList<>();

	public List<ExportCell> getCells() {
		return this.exportCells;
	}

	public void setCells(final List<ExportCell> exportCells) {
		this.exportCells = exportCells;
	}

	public void addCell(final ExportCell exportCell) {
		this.exportCells.add(exportCell);
	}

	public ExportCell getCellAt(final int index) {
		if (this.exportCells == null || index < 0 || index >= this.exportCells.size()) {
			return null;
		}

		return this.exportCells.get(index);
	}

	public boolean isEmpty() {
		for (final ExportCell exportCell : this.exportCells) {
			if (exportCell.getValue() != null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder retValue = new StringBuilder();
		for (final ExportCell cell : this.exportCells) {
			retValue.append(cell + "\t");
		}
		return retValue.length() >= 2 ? retValue.substring(0, retValue.length() - 2).toString() : "";
	}
}
