package de.jk.upload;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.util.NestedServletException;

import de.jk.upload.command.ExternalCommand;
import de.jk.upload.command.ExternalCommandFactory;
import de.jk.upload.queue.ProcessQueue;

@RunWith(MockitoJUnitRunner.class)
public class UploadApiControllerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Mock
    private ExternalCommandFactory factory;
    @Mock
    private ProcessQueue queue;
    @InjectMocks
    private UploadApiController uploadApiController;

    private String jsonCommand01;

    private String jsonCommand02;

    private String jsonCommandList;

    private MockMvc mockMvc;

    @Before
    public void setupCommandQueueMock() throws Exception {

        ExternalCommandFactory externalCommandFactory = new ExternalCommandFactory();

        ExternalCommand command01 = externalCommandFactory.create("run", "command01");
        ExternalCommand command02 = externalCommandFactory.create("run", "command02");
        List<ExternalCommand> commandList = Arrays.asList(command01, command02);

        when(queue.getById(0)).thenReturn(command01);
        when(queue.getById(1)).thenReturn(command02);
        when(queue.getAll()).thenReturn(commandList);

        ObjectMapper mapper = new ObjectMapper();
        jsonCommand01 = mapper.writeValueAsString(command01);
        jsonCommand02 = mapper.writeValueAsString(command02);
        jsonCommandList = mapper.writeValueAsString(commandList);

        when(factory.create(Mockito.<String> anyVararg())).thenReturn(command02);

    }

    @Before
    public void setupMockMvc() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(uploadApiController).build();
    }

    @Before
    public void setupUploadPath() throws Exception {
        Field field = ReflectionUtils.findField(UploadApiController.class, "uploadPath");
        field.setAccessible(true);
        String uploadPath = testFolder.getRoot().getAbsolutePath();
        ReflectionUtils.setField(field, uploadApiController, uploadPath);
    }

    @Test
    public void testGetUpload() throws Exception {

        mockMvc.perform(get("/uploads").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonCommandList));

    }

    @Test
    public void testGetUploadById() throws Exception {

        mockMvc.perform(get("/uploads/0").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonCommand01));

        mockMvc.perform(get("/uploads/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonCommand02));
    }

    @Test
    public void testPostUpload() throws Exception {

        final String filename = "upload.txt";
        MockMultipartFile multipart = new MockMultipartFile("file", filename, MediaType.MULTIPART_FORM_DATA_VALUE,
                new byte[1024]);

        mockMvc.perform(fileUpload("/uploads").file(multipart)).andExpect(status().isOk())
                .andExpect(content().json(jsonCommand02));

        File uploadedFile = new File(testFolder.getRoot(), filename);

        MatcherAssert.assertThat("File written", uploadedFile.exists());
    }

    @Test
    public void testPostUploadEmptyFile() throws Exception {
        thrown.expect(NestedServletException.class);

        MockMultipartFile multipart = new MockMultipartFile("file", "empty.txt", MediaType.MULTIPART_FORM_DATA_VALUE,
                new byte[0]);

        mockMvc.perform(fileUpload("/uploads").file(multipart));
    }
}
