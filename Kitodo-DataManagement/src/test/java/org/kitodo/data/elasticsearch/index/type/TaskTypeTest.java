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

package org.kitodo.data.elasticsearch.index.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.kitodo.data.database.beans.Process;
import org.kitodo.data.database.beans.Role;
import org.kitodo.data.database.beans.Task;
import org.kitodo.data.database.beans.User;
import org.kitodo.data.database.helper.enums.TaskEditType;
import org.kitodo.data.database.helper.enums.TaskStatus;
import org.kitodo.data.elasticsearch.index.type.enums.RoleTypeField;
import org.kitodo.data.elasticsearch.index.type.enums.TaskTypeField;

/**
 * Test class for TaskType.
 */
public class TaskTypeTest {

    private static List<Task> prepareData() {

        List<Task> tasks = new ArrayList<>();
        List<User> users = new ArrayList<>();
        List<Role> roles = new ArrayList<>();

        Process process = new Process();
        process.setTitle("First");
        process.setId(1);

        User firstUser = new User();
        firstUser.setId(1);
        users.add(firstUser);

        User secondUser = new User();
        secondUser.setId(2);
        users.add(secondUser);

        Role firstRole = new Role();
        firstRole.setId(1);
        roles.add(firstRole);

        Role secondRole = new Role();
        secondRole.setId(2);
        roles.add(secondRole);

        Task firstTask = new Task();
        firstTask.setId(1);
        firstTask.setTitle("Testing");
        firstTask.setPriority(1);
        firstTask.setOrdering(1);
        firstTask.setProcessingStatusEnum(TaskStatus.DONE);
        firstTask.setEditTypeEnum(TaskEditType.MANUAL_SINGLE);
        LocalDate localDate = new LocalDate(2017, 2, 17);
        firstTask.setProcessingTime(localDate.toDate());
        localDate = new LocalDate(2017, 2, 1);
        firstTask.setProcessingBegin(localDate.toDate());
        localDate = new LocalDate(2017, 2, 17);
        firstTask.setProcessingEnd(localDate.toDate());
        firstTask.setHomeDirectory((short) 1);
        firstTask.setTypeMetadata(true);
        firstTask.setTypeAutomatic(false);
        firstTask.setBatchStep(true);
        firstTask.setProcessingUser(users.get(0));
        firstTask.setProcess(process);
        firstTask.setRoles(roles);
        tasks.add(firstTask);

        Task secondTask = new Task();
        secondTask.setId(2);
        secondTask.setTitle("Rendering");
        secondTask.setPriority(2);
        secondTask.setOrdering(2);
        secondTask.setProcessingStatusEnum(TaskStatus.INWORK);
        localDate = new LocalDate(2017, 2, 17);
        secondTask.setProcessingTime(localDate.toDate());
        localDate = new LocalDate(2017, 2, 10);
        secondTask.setProcessingBegin(localDate.toDate());
        secondTask.setProcessingUser(users.get(1));
        secondTask.setRoles(roles);
        tasks.add(secondTask);

        Task thirdTask = new Task();
        thirdTask.setId(3);
        thirdTask.setTitle("Incomplete");
        thirdTask.setProcessingStatusEnum(TaskStatus.OPEN);
        tasks.add(thirdTask);

        return tasks;
    }

    @Test
    public void shouldCreateFirstDocument() throws Exception {
        TaskType taskType = new TaskType();

        Task task = prepareData().get(0);
        HttpEntity document = taskType.createDocument(task);

        JsonObject actual = Json.createReader(new StringReader(EntityUtils.toString(document))).readObject();

        assertEquals("Key title doesn't match to given value!", "Testing", TaskTypeField.TITLE.getStringValue(actual));
        assertEquals("Key ordering doesn't match to given value!", 1, TaskTypeField.ORDERING.getIntValue(actual));
        assertEquals("Key priority doesn't match to given value!", 1, TaskTypeField.PRIORITY.getIntValue(actual));
        assertEquals("Key editType doesn't match to given value!", 1, TaskTypeField.EDIT_TYPE.getIntValue(actual));
        assertEquals("Key processingStatus doesn't match to given value!", 3,
            TaskTypeField.PROCESSING_STATUS.getIntValue(actual));
        assertEquals("Key processingUser doesn't match to given value!", 1,
            TaskTypeField.PROCESSING_USER.getIntValue(actual));
        assertEquals("Key processingBegin doesn't match to given value!", "2017-02-01 00:00:00",
            TaskTypeField.PROCESSING_BEGIN.getStringValue(actual));
        assertEquals("Key processingEnd doesn't match to given value!", "2017-02-17 00:00:00",
            TaskTypeField.PROCESSING_END.getStringValue(actual));
        assertEquals("Key processingTime doesn't match to given value!", "2017-02-17 00:00:00",
            TaskTypeField.PROCESSING_TIME.getStringValue(actual));
        assertEquals("Key homeDirectory doesn't match to given value!", "1",
            TaskTypeField.HOME_DIRECTORY.getStringValue(actual));
        assertTrue("Key batchStep doesn't match to given value!", TaskTypeField.BATCH_STEP.getBooleanValue(actual));
        assertFalse("Key typeAutomatic doesn't match to given value!",
            TaskTypeField.TYPE_AUTOMATIC.getBooleanValue(actual));
        assertTrue("Key typeMetadata doesn't match to given value!",
            TaskTypeField.TYPE_METADATA.getBooleanValue(actual));
        assertFalse("Key typeImagesWrite doesn't match to given value!",
            TaskTypeField.TYPE_IMAGES_WRITE.getBooleanValue(actual));
        assertFalse("Key typeImagesRead doesn't match to given value!",
            TaskTypeField.TYPE_IMAGES_READ.getBooleanValue(actual));
        assertEquals("Key processForTask.id doesn't match to given value!", 1,
            TaskTypeField.PROCESS_ID.getIntValue(actual));
        assertEquals("Key processForTask.title doesn't match to given value!", "First",
            TaskTypeField.PROCESS_TITLE.getStringValue(actual));

        JsonArray roles = TaskTypeField.ROLES.getJsonArray(actual);
        assertEquals("Size roles doesn't match to given value!", 2, roles.size());

        JsonObject role = roles.getJsonObject(0);
        assertEquals("Key roles.id doesn't match to given value!", 1, RoleTypeField.ID.getIntValue(role));

        role = roles.getJsonObject(1);
        assertEquals("Key roles.id doesn't match to given value!", 2, RoleTypeField.ID.getIntValue(role));
    }

    @Test
    public void shouldCreateSecondDocument() throws Exception {
        TaskType taskType = new TaskType();

        Task task = prepareData().get(1);
        HttpEntity document = taskType.createDocument(task);

        JsonObject actual = Json.createReader(new StringReader(EntityUtils.toString(document))).readObject();

        assertEquals("Key title doesn't match to given value!", "Rendering",
            TaskTypeField.TITLE.getStringValue(actual));
        assertEquals("Key ordering doesn't match to given value!", 2, TaskTypeField.ORDERING.getIntValue(actual));
        assertEquals("Key priority doesn't match to given value!", 2, TaskTypeField.PRIORITY.getIntValue(actual));
        assertEquals("Key editType doesn't match to given value!", 0, TaskTypeField.EDIT_TYPE.getIntValue(actual));
        assertEquals("Key processingStatus doesn't match to given value!", 2,
            TaskTypeField.PROCESSING_STATUS.getIntValue(actual));
        assertEquals("Key processingUser doesn't match to given value!", 2,
            TaskTypeField.PROCESSING_USER.getIntValue(actual));
        assertEquals("Key processingBegin doesn't match to given value!", "2017-02-10 00:00:00",
            TaskTypeField.PROCESSING_BEGIN.getStringValue(actual));
        assertEquals("Key processingEnd doesn't match to given value!", JsonValue.NULL,
            actual.get(TaskTypeField.PROCESSING_END.getKey()));
        assertEquals("Key processingTime doesn't match to given value!", "2017-02-17 00:00:00",
            TaskTypeField.PROCESSING_TIME.getStringValue(actual));
        assertEquals("Key homeDirectory doesn't match to given value!", "0",
            TaskTypeField.HOME_DIRECTORY.getStringValue(actual));
        assertFalse("Key batchStep doesn't match to given value!", TaskTypeField.BATCH_STEP.getBooleanValue(actual));
        assertFalse("Key typeAutomatic doesn't match to given value!",
            TaskTypeField.TYPE_AUTOMATIC.getBooleanValue(actual));
        assertFalse("Key typeMetadata doesn't match to given value!",
            TaskTypeField.TYPE_METADATA.getBooleanValue(actual));
        assertFalse("Key typeImagesWrite doesn't match to given value!",
            TaskTypeField.TYPE_IMAGES_WRITE.getBooleanValue(actual));
        assertFalse("Key typeImagesRead doesn't match to given value!",
            TaskTypeField.TYPE_IMAGES_READ.getBooleanValue(actual));
        assertEquals("Key processForTask.id doesn't match to given value!", 0,
            TaskTypeField.PROCESS_ID.getIntValue(actual));
        assertEquals("Key processForTask.title doesn't match to given value!", "",
            TaskTypeField.PROCESS_TITLE.getStringValue(actual));

        JsonArray roles = TaskTypeField.ROLES.getJsonArray(actual);
        assertEquals("Size roles doesn't match to given value!", 2, roles.size());

        JsonObject role = roles.getJsonObject(0);
        assertEquals("Key roles.id doesn't match to given value!", 1, RoleTypeField.ID.getIntValue(role));

        role = roles.getJsonObject(1);
        assertEquals("Key roles.id doesn't match to given value!", 2, RoleTypeField.ID.getIntValue(role));
    }

    @Test
    public void shouldCreateThirdDocument() throws Exception {
        TaskType taskType = new TaskType();

        Task task = prepareData().get(2);
        HttpEntity document = taskType.createDocument(task);

        JsonObject actual = Json.createReader(new StringReader(EntityUtils.toString(document))).readObject();

        assertEquals("Key title doesn't match to given value!", "Incomplete",
            TaskTypeField.TITLE.getStringValue(actual));
        assertEquals("Key ordering doesn't match to given value!", 0, TaskTypeField.ORDERING.getIntValue(actual));
        assertEquals("Key priority doesn't match to given value!", 0, TaskTypeField.PRIORITY.getIntValue(actual));
        assertEquals("Key editType doesn't match to given value!", 0, TaskTypeField.EDIT_TYPE.getIntValue(actual));
        assertEquals("Key processingStatus doesn't match to given value!", 1,
            TaskTypeField.PROCESSING_STATUS.getIntValue(actual));
        assertEquals("Key processingUser doesn't match to given value!", 0,
            TaskTypeField.PROCESSING_USER.getIntValue(actual));
        assertEquals("Key processingBegin doesn't match to given value!", JsonValue.NULL,
            actual.get(TaskTypeField.PROCESSING_BEGIN.getKey()));
        assertEquals("Key processingEnd doesn't match to given value!", JsonValue.NULL,
            actual.get(TaskTypeField.PROCESSING_END.getKey()));
        assertEquals("Key processingTime doesn't match to given value!", JsonValue.NULL,
            actual.get(TaskTypeField.PROCESSING_TIME.getKey()));
        assertEquals("Key homeDirectory doesn't match to given value!", "0",
            TaskTypeField.HOME_DIRECTORY.getStringValue(actual));
        assertFalse("Key batchStep doesn't match to given value!", TaskTypeField.BATCH_STEP.getBooleanValue(actual));
        assertFalse("Key typeAutomatic doesn't match to given value!",
            TaskTypeField.TYPE_AUTOMATIC.getBooleanValue(actual));
        assertFalse("Key typeMetadata doesn't match to given value!",
            TaskTypeField.TYPE_METADATA.getBooleanValue(actual));
        assertFalse("Key typeImagesWrite doesn't match to given value!",
            TaskTypeField.TYPE_IMAGES_WRITE.getBooleanValue(actual));
        assertFalse("Key typeImagesRead doesn't match to given value!",
            TaskTypeField.TYPE_IMAGES_READ.getBooleanValue(actual));
        assertEquals("Key processForTask.id doesn't match to given value!", 0,
            TaskTypeField.PROCESS_ID.getIntValue(actual));
        assertEquals("Key processForTask.title doesn't match to given value!", "",
            TaskTypeField.PROCESS_TITLE.getStringValue(actual));

        JsonArray roles = TaskTypeField.ROLES.getJsonArray(actual);
        assertEquals("Size roles doesn't match to given value!", 0, roles.size());
    }

    @Test
    public void shouldCreateDocumentWithCorrectAmountOfKeys() throws Exception {
        TaskType taskType = new TaskType();

        Task task = prepareData().get(0);
        HttpEntity document = taskType.createDocument(task);

        JsonObject actual = Json.createReader(new StringReader(EntityUtils.toString(document))).readObject();
        assertEquals("Amount of keys is incorrect!", 20, actual.keySet().size());

        JsonArray roles = TaskTypeField.ROLES.getJsonArray(actual);
        JsonObject role = roles.getJsonObject(0);
        assertEquals("Amount of keys in roles is incorrect!", 1, role.keySet().size());
    }

    @Test
    public void shouldCreateDocuments() {
        TaskType taskType = new TaskType();

        List<Task> tasks = prepareData();
        Map<Integer, HttpEntity> documents = taskType.createDocuments(tasks);
        assertEquals("HashMap of documents doesn't contain given amount of elements!", 3, documents.size());
    }
}
