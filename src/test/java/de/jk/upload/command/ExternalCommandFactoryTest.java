package de.jk.upload.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ExternalCommandFactoryTest {

    @Test
    public void testCreate() {

        ExternalCommandFactory factory = new ExternalCommandFactory();

        final String[] commandStr = new String[] { "echo", "test" };

        ExternalCommand command = factory.create(commandStr);
        assertThat(command.getId(), equalTo(0));
        assertThat(command.getCreationDate(), notNullValue());
        assertThat(command.getStatus(), equalTo(ExternalCommand.Status.PENDING));

        ExternalCommand command2 = factory.create(commandStr);

        assertThat(command2.getId(), equalTo(1));
        assertThat(command2.getCreationDate(), notNullValue());
        assertThat(command2.getStatus(), equalTo(ExternalCommand.Status.PENDING));

    }

}
