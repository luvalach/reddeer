/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.eclipse.reddeer.swt.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.core.lookup.ShellLookup;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.swt.widgets.Shell;

public class NewShellIsOpenedOrIsNotAvailable extends AbstractWaitCondition {

	private List<Shell> previousShells;
	private Shell newShell;
	private Shell oldShell;

	public NewShellIsOpenedOrIsNotAvailable(Shell oldShell, final Shell[] previousShells) {
		this.previousShells = new ArrayList<Shell>(Arrays.asList(previousShells));
		this.oldShell = oldShell;
	}

	@Override
	public boolean test() {
		List<Shell> s2List = new ArrayList<Shell>(Arrays.asList(ShellLookup.getInstance().getShells()));
		if (!s2List.contains(oldShell)) {
			return true;
		}
		s2List.removeAll(previousShells);
		if (s2List.size() == 1) {
			newShell = s2List.get(0);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public org.eclipse.reddeer.swt.api.Shell getResult() {
		if (newShell != null) {
			return new DefaultShell(newShell);
		}
		return null;
	}

}
