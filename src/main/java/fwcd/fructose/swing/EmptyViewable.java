package fwcd.fructose.swing;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class EmptyViewable implements Viewable {
	private final JPanel view = new JPanel();
	
	@Override
	public JComponent getComponent() {
		return view;
	}
}
