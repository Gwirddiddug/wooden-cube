package org.kaa.gui.main;

import org.kaa.gui.BasePanel;
import org.kaa.model.Figure;
import org.kaa.model.RealSpace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by Gwirggiddug on 02.06.2016.
 */
public class MainForm extends JFrame {

	private RealSpace space;
	private MainFormListener listener;
	private Figure figure;
	private PuzzleInfoPanel infoPanel;

	public MainForm(MainFormListener listener) throws HeadlessException {
		this.listener = listener;
	}

	void init() {
		setTitle("Wooden cube");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(400, 500));
		setLayout(new BorderLayout(1, 1));
		setLocation(400, 200);
		this.setResizable(false);

		add(new ControlPanel(), BorderLayout.SOUTH);

		InfoPanelListener listener = new InfoPanelListener() {
			public void selectSpace(RealSpace space) {
				MainForm.this.space = space;
			}

			public void selectFigure(Figure figure) {
				MainForm.this.figure = figure;
			}
		};
		infoPanel = new PuzzleInfoPanel(listener);
		add(infoPanel, BorderLayout.CENTER);
	}

	public RealSpace getSpace() {
		return infoPanel.getSpace();
	}

	public Figure getFigure() {
		return infoPanel.getFigure();
	}

	/**
	 * Панель с кнопками управления формой
	 */
	private class ControlPanel extends BasePanel {
		public ControlPanel() {
			setLayout(new GridLayout(1, 2));
			add(getRunButton());
			add(getCloseButton());
		}

		/**
		 * @return кнопку закрытия формы
		 */
		private JButton getCloseButton() {
			JButton closeButton = new JButton("Close");
			closeButton.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					listener.close();
				}
			});
			return closeButton;
		}

		/**
		 * возвращает кнопку закрытия формы
		 *
		 * @return
		 */
		private JButton getRunButton() {
			JButton runButon = new JButton("Run");
			runButon.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					listener.run();
				}
			});
			return runButon;
		}

	}


}
