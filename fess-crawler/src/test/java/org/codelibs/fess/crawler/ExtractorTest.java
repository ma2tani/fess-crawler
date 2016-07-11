/*
 * Copyright 2012-2016 CodeLibs Project and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.codelibs.fess.crawler;

import java.io.File;

import org.codelibs.fess.crawler.container.StandardCrawlerContainer;
import org.codelibs.fess.crawler.service.DataService;
import org.codelibs.fess.crawler.service.UrlQueueService;
import org.codelibs.fess.crawler.transformer.impl.FileTransformer;
import org.codelibs.fess.crawler.util.ApiExtractorServer;
import org.dbflute.utflute.core.PlainTestCase;

public class ExtractorTest extends PlainTestCase {

    public ApiExtractor apiExtractor = new ApiExtractor();

    public DataService dataService;

    public UrlQueueService urlQueueService;

    public FileTransformer fileTransformer;

    private StandardCrawlerContainer container;

    public void test_execute_web() throws Exception {
        final ApiExtractorServer server = new ApiExtractorServer(7070);
        server.start();

        final String url = "http://localhost:7070/post/";
        final File file = new File("/apiextractor/src/test/resources/extractor/image/test.jpg");
        try {
            final int maxCount = 50;
            final int numOfThread = 10;

            //            server.doPost(url);

            apiExtractor.postMultipart(url,
                    "/Users/MK/testspace/extractor/fork/apiextractor/fess-crawler/src/test/resources/extractor/test.txt");

        } finally {
            server.stop();
        }
    }

    public void test_execute_xmlSitemaps() throws Exception {
        final ApiExtractorServer server = new ApiExtractorServer(7070);
        server.start();

        final String url = "http://localhost:7070/";
        try {
            //            server.doPost(url);
            apiExtractor.postMultipart(url, "/apiextractor/src/test/resources/extractor/image/test.jpg");
            final int maxCount = 50;
            final int numOfThread = 10;

            final File file = File.createTempFile("crawler-", "");
            file.delete();
            file.mkdirs();
            file.deleteOnExit();
            //            fileTransformer.setPath(file.getAbsolutePath());
        } finally {
            server.stop();
        }
    }

}
