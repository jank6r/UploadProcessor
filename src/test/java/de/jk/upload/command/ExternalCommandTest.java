package de.jk.upload.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.jk.upload.UploadException;

public class ExternalCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ExternalCommandExecuter executer;

    @Before
    public void setupExecuter() {
        executer = new ExternalCommandExecuter();
    }

    @Test
    public void testExecute() {

        Assume.assumeFalse(System.getProperty("os.name").toLowerCase().startsWith("win"));

        final File workingDirectory = new File(".");
        final int id = 12345;
        final String testOutput = "Command Test";
        final String[] commandStr = new String[] { "echo", testOutput };

        ExternalCommand command = new ExternalCommand(commandStr);

        command.setId(id);
        command.setWorkingDirectory(workingDirectory);

        assertThat(command.getStatus(), equalTo(ExternalCommand.Status.PENDING));

        executer.execute(command);

        assertThat(command.getStatus(), equalTo(ExternalCommand.Status.SUCCESS));

        assertEquals(0, command.getExitCode());
        assertEquals(testOutput + "\n", command.getOutput());
        assertEquals(workingDirectory, command.getWorkingDirectory());
        assertEquals(id, command.getId());

    }

    @Test
    public void testExecuteFailed() {

        String[] commandStr = new String[] { "ls", "no_existing_folder" };
        ExternalCommand command = new ExternalCommand(commandStr);

        assertThat(command.getStatus(), equalTo(ExternalCommand.Status.PENDING));

        executer.execute(command);

        assertThat(command.getStatus(), equalTo(ExternalCommand.Status.FAILURE));

        assertEquals(2, command.getExitCode());

    }

    @Test
    public void testUnknownCommand() {

        thrown.expect(UploadException.class);

        String testOutput = "Command Test";
        String[] commandStr = new String[] { "unknownCommand", testOutput };
        ExternalCommand command = new ExternalCommand(commandStr);

        assertThat(command.getStatus(), equalTo(ExternalCommand.Status.PENDING));

        executer.execute(command);

    }
}
