package view.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class RMTextField extends JTextField {
	private static final long serialVersionUID = -321195476023329926L;
	private final StyleContext sc = StyleContext.getDefaultStyleContext();
	private AttributeSet aset = this.sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);

	private FocusAdapter focusAdapter = null;

	public RMTextField() {
		super();
		this.setup();
	}

	private void setup() {
		//((AbstractDocument) this.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());

		this.aset = this.sc.addAttribute(this.aset, StyleConstants.FontFamily, "Lucida Console");
		this.aset = this.sc.addAttribute(this.aset, StyleConstants.FontSize, 14);
		this.aset = this.sc.addAttribute(this.aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

		this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null); // Restore TAB behaviour
		this.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null); // Restore SHIFT+TAB behaviour

		this.focusAdapter = new FocusAdapter() {
			@Override
			public void focusGained(final FocusEvent e) {
				SwingUtilities.invokeLater(RMTextField.this::selectAll); // Select all on focus gained
			}
		};

		this.addFocusListener(this.focusAdapter);
	}

	public void allowLowerCase() {
		((AbstractDocument) this.getDocument()).setDocumentFilter(new DocumentFilter());
	}

	public void setBold(final boolean bold) {
		final Font font = this.getFont();
		this.setFont(new Font(font.getFontName(), bold ? Font.BOLD : Font.PLAIN, font.getSize()));
	}

	public void clear() {
		this.setText(null);
	}

	@Override
	public void setEditable(final boolean editable) {
		super.setEditable(editable);

		final Dimension d = this.getSize();
		this.setSize((int) d.getWidth(), (int) d.getHeight() + (editable ? 1 : -1));
	}

	public synchronized void append(final String txt, final Color c) {
		this.aset = this.sc.addAttribute(this.aset, StyleConstants.Foreground, c);

		final Document doc = this.getDocument();
		if (doc != null) {
			try {
				doc.insertString(doc.getLength(), txt, this.aset);
			} catch (final BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isEmpty() {
		final String text = this.getText();
		return text == null || "".equals(text);
	}

	@Override
	public String getText() {
		try {
			final Document doc = this.getDocument();
			return doc != null ? doc.getText(0, doc.getLength()) : null;
		} catch (final BadLocationException e) {
			e.printStackTrace();
		}

		// Workaround cause doc.getText (0 -> length) doesn't work and getText inserts \n
		final int caretPosition = this.getCaretPosition();
		this.selectAll();
		final String text = this.getSelectedText();
		this.select(0, 0);
		this.setCaretPosition(caretPosition);
		return text;
	}

	/*private class UppercaseDocumentFilter extends DocumentFilter {
		public UppercaseDocumentFilter() {
			super();
		}
	
		@Override
		public void insertString(final DocumentFilter.FilterBypass fb, final int offset, final String text,
				final AttributeSet attr) throws BadLocationException {
			fb.insertString(offset, text != null ? text.toUpperCase() : null, attr);
		}
	
		@Override
		public void replace(final DocumentFilter.FilterBypass fb, final int offset, final int length, final String text,
				final AttributeSet attrs) throws BadLocationException {
			fb.replace(offset, length, text != null ? text.toUpperCase() : null, attrs);
		}
	}*/

	public void removeSelectAllOnFocusGained() {
		this.removeFocusListener(this.focusAdapter);
	}
}