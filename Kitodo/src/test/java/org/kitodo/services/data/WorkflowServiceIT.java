/*
 * (c) Kitodo. Key to digital objects e. V. <contact@kitodo.org>
 *
 * This file is part of the Kitodo project.
 *
 * It is licensed under GNU General Public License version 3 or later.
 *
 * For the full copyright and license information, please read the
 * GPL3-License.txt file that was distributed with this source code.
 */

package org.kitodo.services.data;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kitodo.MockDatabase;
import org.kitodo.SecurityTestUtils;
import org.kitodo.data.database.beans.Workflow;
import org.kitodo.services.ServiceManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WorkflowServiceIT {

    private ServiceManager serviceManager = new ServiceManager();
    private WorkflowService workflowService = serviceManager.getWorkflowService();

    @BeforeClass
    public static void prepareDatabase() throws Exception {
        MockDatabase.startNode();
        MockDatabase.insertRolesFull();
        MockDatabase.insertWorkflows();
    }

    @AfterClass
    public static void cleanDatabase() throws Exception {
        MockDatabase.stopNode();
        MockDatabase.cleanDatabase();
    }

    @Test
    public void shouldGetWorkflow() throws Exception {
        Workflow workflow = workflowService.getById(1);
        boolean condition = workflow.getTitle().equals("say-hello") && workflow.getFileName().equals("test");
        assertTrue("Workflow was not found in database!", condition);
    }

    @Test
    public void shouldGetAllWorkflows() throws Exception {
        List<Workflow> workflows = workflowService.getAll();
        assertEquals("Workflows were not found in database!", 2, workflows.size());
    }

    @Test
    public void shouldGetWorkflowsForTitleAndFile() {
        List<Workflow> workflows = workflowService.getWorkflowsForTitleAndFile("say-hello", "test");
        assertEquals("Workflows were not found in database!", 1, workflows.size());
    }

    @Test
    public void shouldGetAvailableWorkflows() throws Exception {
        SecurityTestUtils.addUserDataToSecurityContext(serviceManager.getUserService().getById(1), 1);

        List<Workflow> workflows = workflowService.getAvailableWorkflows();
        assertEquals("Workflows were not found in database!", 1, workflows.size());

        SecurityTestUtils.cleanSecurityContext();
    }
}
