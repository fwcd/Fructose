package fwcd.fructose.swing;

import javax.swing.JPanel;

public class EmptyView implements Viewable {
	private JPanel view;
	
	public EmptyView() {
		view = new JPanel();
	}
	
	@Override
	public JPanel getComponent() {
		return view;
	}
}
