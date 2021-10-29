package com.github.pihme.docelar.backend;

import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component("unzipper")
public class Unzipper {
    private static final Logger logger = LoggerFactory.getLogger(Unzipper.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.from(ZoneOffset.UTC));


    public void unzip(final MemoryBuffer memoryBuffer, final String mimeType) throws IOException {
        final var fileName = memoryBuffer.getFileName();
        logger.info("Processing " + fileName + " Mime-Type " + mimeType);


        final var folderName = "uploads/" + DATE_TIME_FORMATTER.format(Instant.now()) + "/" + fileName;

        logger.info("Creating folder " + folderName);

        final var folder = new File(folderName);
        folder.mkdirs();

        switch (mimeType) {
            case "application/zip": {
                unzipIt(memoryBuffer, folder);
                break;
            }
        }
    }

    private void unzipIt(final MemoryBuffer memoryBuffer, final File destDir) throws IOException {

        final byte[] buffer = new byte[1024];
        final ZipInputStream zis = new ZipInputStream(memoryBuffer.getInputStream());
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            final File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                final File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                final FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    private File newFile(final File destinationDir, final ZipEntry zipEntry) throws IOException {
        final File destFile = new File(destinationDir, zipEntry.getName());

        final String destDirPath = destinationDir.getCanonicalPath();
        final String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
