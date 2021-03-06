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
package org.jboss.reddeer.eclipse.test.ui.ide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.eclipse.ui.ide.ExtendedFileEditorsPreferencePage;
import org.jboss.reddeer.jface.preference.PreferenceDialog;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * 
 * @author rhopp
 *
 */
@RunWith(RedDeerSuite.class)
public class ExtendedFileEditorsPreferencePageTest {

    private static final String TEST_FILE_TYPE = "myFileType";
    private ExtendedFileEditorsPreferencePage prefPage;
    private PreferenceDialog prefDialog;

    @Before
    public void setup() {
	prefPage = new ExtendedFileEditorsPreferencePage();
	prefDialog = new WorkbenchPreferenceDialog();
	prefDialog.open();
	prefDialog.select(prefPage);
    }

    @After
    public void teardown() {
	prefDialog.cancel();
    }

    @Test
    public void selectFileTypeTest() {
	prefPage.selectFileType("*.html");
	DefaultTable defaultTable = new DefaultTable();
	List<TableItem> selectetItems = defaultTable.getSelectetItems();

	assertEquals("Only one item should be selected", 1, selectetItems.size());
	assertTrue("*.html file type should be selected", selectetItems.get(0).getText().equals("*.html"));
    }

    @Test
    public void addFileTypeTest() {
	prefPage.addFileType(TEST_FILE_TYPE);

	assertTrue(new DefaultTable().containsItem(TEST_FILE_TYPE));

	new DefaultTable().getItem(TEST_FILE_TYPE).select();
	new PushButton("Remove").click();
    }

    @Test
    public void removeFileTypeTest() {
	prefPage.addFileType(TEST_FILE_TYPE);
	prefPage.removeFileType(TEST_FILE_TYPE);

	assertFalse(new DefaultTable().containsItem(TEST_FILE_TYPE));
    }

    @Test
    public void addEditorTest() {
	prefPage.addFileType(TEST_FILE_TYPE);
	prefPage.selectFileType(TEST_FILE_TYPE);

	assertEquals("There should be no editor.", 0, new DefaultTable(1).getItems().size());

	prefPage.addAssociatedEditor("Java Editor");

	assertEquals("There should be one editor.", 1, new DefaultTable(1).getItems().size());
    }
    
    @Test
    public void removeEditorTest() {
	prefPage.addFileType(TEST_FILE_TYPE);
	prefPage.selectFileType(TEST_FILE_TYPE);
	prefPage.addAssociatedEditor("Java Editor");
	
	assertEquals("There should be no editor.", 1, new DefaultTable(1).getItems().size());
	
	prefPage.removeAssociatedEditor("Java Editor");
	
	assertEquals("There should be one editor.", 0, new DefaultTable(1).getItems().size());
    }

    @Test
    public void getAssociatedEditorsTest() {
	prefPage.selectFileType("*.html");
	List<String> associatedEditors = prefPage.getAssociatedEditors();
	assertEquals("There should be 3 associated editors to *.html", 3, associatedEditors.size());
    }

    @Test
    public void getAssociatedEditorsForFileTypeTest() {
	List<String> associatedEditors = prefPage.getAssociatedEditorForFileType("*.html");
	assertEquals("There should be 3 associated editors to *.html", 3, associatedEditors.size());
    }

}
