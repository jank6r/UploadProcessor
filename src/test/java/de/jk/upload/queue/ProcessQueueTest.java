package de.jk.upload.queue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.jk.upload.command.ExternalCommand;
import de.jk.upload.command.ExternalCommandFactory;

@RunWith(MockitoJUnitRunner.class)
public class ProcessQueueTest {

    private ProcessQueue queue;

    private final ExternalCommandFactory externalCommandFactory = new ExternalCommandFactory();

    @Mock
    private ProcessRunner runner;

    @Before
    public void setupQueue() {
        queue = new ProcessQueue();

        ((ProcessQueue) queue).setRunner(runner);
    }

    @Test
    public void testQueue() {

        ExternalCommand command01 = externalCommandFactory.create("test", "command");
        queue.add(command01);

        ExternalCommand command02 = externalCommandFactory.create("test", "command");
        queue.add(command02);

        assertThat(queue.getById(command01.getId()), sameInstance(command01));

        assertThat(queue.getAll().size(), equalTo(2));

    }

}
