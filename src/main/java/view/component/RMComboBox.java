package view.component;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class RMComboBox<T extends RMComboBoxItem<I>, I> extends JComboBox<T> {
	private static final long serialVersionUID = 1105083489692804419L;

	@SuppressWarnings("unchecked")
	private final T nullItem = (T) new RMComboBoxItem<I>(null, " ");
	private boolean nullItemAdded = false;

	public RMComboBox() {
		super();
		this.setup();
	}

	private void setup() {
		this.setBackground(Color.WHITE);

		this.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 2822536811156454461L;

			@Override
			public void paint(final Graphics g) {
				this.setForeground(new JLabel().getForeground());
				super.paint(g);
			}
		});
	}

	public void addItems(final List<T> items) {
		for (final T t : items) {
			this.addItem(t);
		}
	}

	public void addNullItem() {
		this.nullItemAdded = true;
		this.addItem(this.nullItem);
	}

	public void selectNullItem() {
		if (this.nullItemAdded) {
			this.setSelectedItem(this.nullItem);
		}
	}

	public void setSelectedItemByKey(final I key) {
		if (key == null) {
			return;
		}

		T item;
		for (int i = 0; i < this.getItemCount(); i++) {
			item = this.getItemAt(i);
			if (item != null && key.equals(item.getKey())) {
				this.setSelectedIndex(i);
				return;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getSelectedItem() {
		return (T) super.getSelectedItem();
	}

	public T getItemByKey(final I key) {
		if (key == null) {
			return null;
		}

		T item;
		for (int i = 0; i < this.getItemCount(); i++) {
			item = this.getItemAt(i);
			if (item != null && key.equals(item.getKey())) {
				return item;
			}
		}

		return null;
	}

	public I getSelectedItemKey() {
		final T selectedItem = this.getSelectedItem();
		return selectedItem == null ? null : selectedItem.getKey();
	}

	public String getSelectedItemValue() {
		final T selectedItem = this.getSelectedItem();
		return selectedItem == null ? null : selectedItem.getValue();
	}

	@Override
	public void setForeground(final Color color) {
		this.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 2822536811156454461L;

			@Override
			public void paint(final Graphics g) {
				this.setForeground(color);
				super.paint(g);
			}
		});
	}

	@Override
	public void setEditable(final boolean editable) {
		this.setEnabled(editable);
	}
}
