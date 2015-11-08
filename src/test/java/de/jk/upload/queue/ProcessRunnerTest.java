package de.jk.upload.queue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import de.jk.upload.command.ExternalCommandExecuter;
import de.jk.upload.command.ExternalCommand;
import de.jk.upload.command.ExternalCommandFactory;

public class ProcessRunnerTest {

    private ProcessRunner runner;

    @Before
    public void setupRunner() {
        runner = new ProcessRunner();
        runner.setExecuter(new ExternalCommandExecuter());
    }

    @Test
    public void testExecute() {

        Assume.assumeFalse(System.getProperty("os.name").toLowerCase().startsWith("win"));

        ExternalCommand command = new ExternalCommandFactory().create(new String[] { "echo", "test" });

        runner.runCommand(command);

        assertThat("Command was executed.", command.getStatus(), equalTo(ExternalCommand.Status.SUCCESS));
    }
}
