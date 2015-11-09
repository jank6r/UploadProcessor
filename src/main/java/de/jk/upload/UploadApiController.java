package de.jk.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.jk.upload.command.ExternalCommand;
import de.jk.upload.command.ExternalCommandFactory;
import de.jk.upload.queue.ProcessQueue;

@RestController
@RequestMapping(value = "/uploads")
public class UploadApiController {

    private static final Logger LOG = LoggerFactory.getLogger(UploadApiController.class);

    @Autowired
    private ExternalCommandFactory factory;

    @Autowired
    private ProcessQueue queue;

    @Value("${command}")
    private String scriptPath;

    @Value("${uploadPath}")
    private String uploadPath;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Collection<ExternalCommand> allUploads() {

        return queue.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ExternalCommand handleNewUpload(@RequestParam("file") MultipartFile multipartFile) {

        if (!multipartFile.isEmpty()) {
            String name = multipartFile.getOriginalFilename();

            try {
                File uploadedFile = storeUploadFile(name, multipartFile.getInputStream());
                ExternalCommand command = factory.create(new String[] { scriptPath, uploadedFile.getAbsolutePath() });

                queue.add(command);

                return command;

            } catch (IOException e) {
                throw new UploadException(String.format("Upload of file '%s' failed", name), e);
            }
        } else {
            throw new UploadException("Received empty file.");
        }
    }

    @RequestMapping("{commandId}")
    @ResponseBody
    public ExternalCommand uploadById(@PathVariable Integer commandId) {

        return queue.getById(commandId);
    }

    private File createFile(String name) {
        File file = new File(uploadPath, name);
        int collisionId = 0;

        while (file.exists()) {
            file = new File(uploadPath, name + '~' + collisionId);
            collisionId++;
        }

        return file;
    }

    private File storeUploadFile(String name, InputStream inputStream) throws IOException {

        File file = createFile(name);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));

        int byteCount = FileCopyUtils.copy(inputStream, outputStream);

        LOG.debug("{} bytes written to '{}'.", byteCount, file.getAbsolutePath());

        return file;
    }
}
