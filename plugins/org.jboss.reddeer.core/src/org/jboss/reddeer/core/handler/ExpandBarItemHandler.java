/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.reddeer.core.handler;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.core.handler.WidgetHandler;

/**
 * Contains methods for handling UI operations on {@link ExpandBarItem} widgets.
 * 
 * @author Vlado Pakan
 *
 */
public class ExpandBarItemHandler {

	private static final Logger logger = Logger
			.getLogger(ExpandBarItemHandler.class);

	private static ExpandBarItemHandler instance;

	private ExpandBarItemHandler() {

	}

	/**
	 * Gets instance of ExpandBarItemHandler.
	 * 
	 * @return instance of ExpandBarItemHandler
	 */
	public static ExpandBarItemHandler getInstance() {
		if (instance == null) {
			instance = new ExpandBarItemHandler();
		}
		return instance;
	}

	/**
	 * Expands specified {@link ExpandItem} and wait for specified time period.
	 * 
	 * @param timePeriod time period to wait for after expanding
	 * @param expandItem expand item to handle
	 */
	public static void expand(TimePeriod timePeriod,
			final ExpandItem expandItem) {
		logger.debug("Expand Expand Bar Item " + WidgetHandler.getInstance().getText(expandItem));
		if (!ExpandBarItemHandler.getInstance().isExpanded(expandItem)) {
			ExpandBarItemHandler.notifyExpandBar(ExpandBarItemHandler
					.createEventForExpandBar(SWT.Expand, expandItem),
					expandItem);
			Display.syncExec(new Runnable() {
				@Override
				public void run() {
					expandItem.setExpanded(true);
				}
			});
			AbstractWait.sleep(timePeriod);
			logger.info("Expand Bar Item " + WidgetHandler.getInstance().getText(expandItem)
					+ " has been expanded");
		} else {
			logger.debug("Expand Bar Item " + WidgetHandler.getInstance().getText(expandItem)
					+ " is already expanded. No action performed");
		}
	}

	/**
	 * Collapses specified {@link ExpandItem}.
	 * 
	 * @param expandItem expand item to handle
	 */
	public static void collapse(final ExpandItem expandItem) {
		logger.debug("Collapse Expand Bar Item " + WidgetHandler.getInstance().getText(expandItem));
		if (ExpandBarItemHandler.getInstance().isExpanded(expandItem)) {
			Display.syncExec(new Runnable() {
				@Override
				public void run() {
					expandItem.setExpanded(false);
				}
			});
			ExpandBarItemHandler.notifyExpandBar(ExpandBarItemHandler
					.createEventForExpandBar(SWT.Collapse, expandItem),
					expandItem);
			logger.info("Expand Bar Item " + WidgetHandler.getInstance().getText(expandItem)
					+ " has been collapsed");
		} else {
			logger.debug("Expand Bar Item " + WidgetHandler.getInstance().getText(expandItem)
					+ " is already collapsed. No action performed");
		}
	}

	/**
	 * Notifies listeners of specified {@link ExpandItem} about specified event.
	 * Field for event type in specified event has to be set properly.
	 * 
	 * @param event event to notify about
	 * @param expandBarItem expand bar item to handle
	 */
	private static void notifyExpandBar(final Event event,
			final ExpandItem expandItem) {
		Display.syncExec(new Runnable() {
			public void run() {
				expandItem.getParent().notifyListeners(event.type, event);
			}
		});
	}
	
	/**
	 * Creates the event for specified {@link ExpandItem} with specified type
	 * and empty details.
	 * 
	 * @param type type of the event
	 * @param expandBarItem expand bar item to handle
	 * @return event for specified expand bar
	 */
	private static Event createEventForExpandBar(int type,
			ExpandItem expandItem) {
		return createEventForExpandBar(type, SWT.NONE, expandItem);
	}

	/**
	 * Creates the event for specified {@link ExpandItem} with specified type
	 * and details.
	 * 
	 * @param type type of the event
	 * @param detail details of the event
	 * @param expandBarItem expand bar item to handle
	 * @return event for specified expand bar
	 */
	private static Event createEventForExpandBar(int type, int detail,
			ExpandItem expandItem) {
		Event event = new Event();
		event.type = type;
		event.display = Display.getDisplay();
		event.time = (int) System.currentTimeMillis();
		event.item = expandItem;
		event.widget = ExpandBarItemHandler.getInstance().getParent(expandItem);
		event.detail = detail;
		return event;
	}

	/**
	 * Expands specified {@link ExpandItem} under specified {@link ExpandBar}.
	 * 
	 * @param expandItem expand item to handle
	 * @param expandBar parent expand bar
	 */
	public void expand(final ExpandItem expandItem, final ExpandBar expandBar) {
		notifyExpandBar(createEventForExpandBar(SWT.Expand, expandItem, expandBar),
				expandBar);
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				expandItem.setExpanded(true);
			}
		});
	}

	/**
	 * Collapses specified {@link ExpandItem} under specified {@link ExpandBar}.
	 * 
	 * @param expandItem expand item to handle
	 * @param expandBar parent expand bar
	 */
	public void collapse(final ExpandItem expandItem, final ExpandBar expandBar) {
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				expandItem.setExpanded(false);
			}
		});
		notifyExpandBar(
				createEventForExpandBar(SWT.Collapse, expandItem, expandBar),
				expandBar);
	}

	private void notifyExpandBar(final Event event, final ExpandBar expandBar) {
		Display.syncExec(new Runnable() {
			public void run() {
				expandBar.notifyListeners(event.type, event);
			}
		});
	}

	private Event createEventForExpandBar(int type, ExpandItem expandItem,
			ExpandBar expandBar) {
		return createEventForExpandBar(type, SWT.NONE, expandItem, expandBar);
	}
	
	private Event createEventForExpandBar(int type, int detail,
			ExpandItem expandItem, ExpandBar expandBar) {
		Event event = new Event();
		event.type = type;
		event.display = Display.getDisplay();
		event.time = (int) System.currentTimeMillis();
		event.item = expandItem;
		event.widget = expandBar;
		event.detail = detail;
		return event;
	}

	/**
	 * Gets tool tip text of specified expand item.
	 * 
	 * @param item expand item to get tool tip
	 * @return tool tip text of specified item
	 */
	public String getToolTipText(final ExpandItem item) {
		String text = Display.syncExec(new ResultRunnable<String>() {
			@Override
			public String run() {
				return item.getParent().getToolTipText();
			}
		});
		return text;
	}

	/**
	 * Checks whether specified {@link ExpandItem} is expanded or not.
	 * 
	 * @param item item to handle
	 * @return true if expand item is expanded, false otherwise
	 */
	public boolean isExpanded(final ExpandItem item) {
		return Display.syncExec(new ResultRunnable<Boolean>() {

			@Override
			public Boolean run() {
				return item.getExpanded();
			}
		});
	}

	/**
	 * Gets parent {@link ExpandBar} of specified {@link ExpandItem}.
	 * 
	 * @param item to handle
	 * @return parent expand bar of specified item
	 */
	public ExpandBar getParent(final ExpandItem item) {
		return Display.syncExec(new ResultRunnable<ExpandBar>() {
			@Override
			public ExpandBar run() {
				return item.getParent();
			}
		});
	}
}
