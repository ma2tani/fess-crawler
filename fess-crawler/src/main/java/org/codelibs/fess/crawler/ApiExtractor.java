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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codelibs.core.lang.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Crawler manages/controls a crawling information.
 *
 * @author ma2tani
 *
 */
public class ApiExtractor implements Runnable {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(Crawler.class);

    protected CrawlerContext crawlerContext;

    protected boolean background = false;

    protected boolean daemon = false;

    protected int threadPriority = Thread.NORM_PRIORITY;

    protected Thread parentThread;

    protected ThreadGroup extractorThreadGroup;


    public ApiExtractor() {
        crawlerContext = new CrawlerContext();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS",
                Locale.ENGLISH);
        crawlerContext.sessionId = sdf.format(new Date());
    }

    public void doPost() {
        HelloServlet hs = new HelloServlet();
        //        hs.doPost(request, response);
    }

    @SuppressWarnings("serial")
    public static class HelloServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>Hello from HelloServlet</h1>");
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>Hello from HelloServlet</h1>");
        }
    }

    public void addUrl(final String url) {
        try {
            //urlQueueService.add(crawlerContext.sessionId, url);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getSessionId() {
        return crawlerContext.sessionId;
    }

    public void setSessionId(final String sessionId) {
        if (StringUtil.isNotBlank(sessionId)
                && !sessionId.equals(crawlerContext.sessionId)) {
            //urlQueueService.updateSessionId(crawlerContext.sessionId, sessionId);
            crawlerContext.sessionId = sessionId;
        }
    }

    public String execute() {
        parentThread = new Thread(this, "Crawler-" + crawlerContext.sessionId);
        parentThread.setDaemon(daemon);
        parentThread.start();
        if (!background) {
            awaitTermination();
        }
        return crawlerContext.sessionId;
    }

    public void awaitTermination() {
        awaitTermination(0);
    }

    public void awaitTermination(final long millis) {
        if (parentThread != null) {
            try {
                parentThread.join(millis);
            } catch (final InterruptedException e) {
                logger.warn("Interrupted job at " + parentThread.getName());
            }
        }
    }

    public void stop() {
        crawlerContext.setStatus(CrawlerStatus.DONE);
        try {
            if (extractorThreadGroup != null) {
                extractorThreadGroup.interrupt();
            }
        } catch (final Exception e) {
            // ignore
        }
    }

    public boolean isBackground() {
        return background;
    }

    public void setBackground(final boolean background) {
        this.background = background;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(final boolean daemon) {
        this.daemon = daemon;
    }

    @Override
    public void run() {
    }

    public CrawlerContext getCrawlerContext() {
        return crawlerContext;
    }

    public void setNumOfThread(final int numOfThread) {
        crawlerContext.numOfThread = numOfThread;
    }

    public void setMaxThreadCheckCount(final int maxThreadCheckCount) {
        crawlerContext.maxThreadCheckCount = maxThreadCheckCount;
    }

    public void setMaxDepth(final int maxDepth) {
        crawlerContext.maxDepth = maxDepth;
    }

    public void setMaxAccessCount(final long maxAccessCount) {
        crawlerContext.maxAccessCount = maxAccessCount;
    }

    public void setThreadPriority(final int threadPriority) {
        this.threadPriority = threadPriority;
    }

    public void postMultipart(String url, String filePath) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("enctype", "multipart/form-data");

            File file = new File(filePath);
            FileBody bin = new FileBody(file);;

            HttpEntity postEntity = MultipartEntityBuilder
                    .create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .setCharset(Charset.forName("UTF-8"))
                    .addPart("filedata", bin)
                    .build();
            httpPost.setEntity(postEntity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
                    HttpEntity text = response.getEntity();
                    System.out.println(EntityUtils.toString(text, StandardCharsets.UTF_8));
                } else {
                    logger.warn("Response code is not 200: ", response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
