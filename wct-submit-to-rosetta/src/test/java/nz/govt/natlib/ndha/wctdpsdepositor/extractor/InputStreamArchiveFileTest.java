/**
 * nz.govt.natlib.ndha.wctdpsdepositor - Software License
 *
 * Copyright 2007/2009 National Library of New Zealand.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * or the file "LICENSE.txt" included with the software.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */

package nz.govt.natlib.ndha.wctdpsdepositor.extractor;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import java.io.*;

public class InputStreamArchiveFileTest {

    private static final String TEST_DIRECTORY = "src/test/resources/WctFiles";
    private final String FILE_NAME = "order.xml";
    @SuppressWarnings("unused")
	private static final String FIXITY = "5b8e0ef130911c544e406f99cb5eb90a";

    @Test
    public void test_toStream_returns_value() throws IOException {
        InputStream inputStream = new FileInputStream(new File(TEST_DIRECTORY + "/" + FILE_NAME));
        ArchiveFile archiveFile = new InputStreamArchiveFile("mime", FILE_NAME, inputStream);

        assertThat(archiveFile.getFileName(), is(equalTo(FILE_NAME)));
        assertThat(archiveFile.toStream(), is(instanceOf(InputStream.class)));
    }

    @Test
    public void test_stream_remains_open_after_multiple_calls_to_toStream() throws IOException {
        File file = new File(TEST_DIRECTORY + "/" + FILE_NAME);
        if (!file.exists())
            throw new RuntimeException("file not found");

        FileInputStream fis = new FileInputStream(file);
        InputStreamArchiveFile archiveFile = new InputStreamArchiveFile("mime", FILE_NAME, fis);
        archiveFile.toStream();
        archiveFile.toStream();
    }


    @Test
    public void test_copy_stream_to_file() throws IOException {
        InputStream inputStream = new FileInputStream(new File(TEST_DIRECTORY + "/" + FILE_NAME));
        ArchiveFile af = new InputStreamArchiveFile("mime", FILE_NAME, inputStream);

        String tempDirectory = "src/test/resources/temp";

        File tempFile = new File(tempDirectory + "/" + FILE_NAME);
        if (tempFile.exists())
            tempFile.delete();

        af.copyStreamToDirectory(tempDirectory);
        assertThat(tempFile.exists(), is(true));
    }

}