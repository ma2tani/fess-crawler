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

    @Override
    public void run() {
        // TODO Auto-generated method stub

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
                    System.err.println(response);
                }
            }

            // System.out.println(httpPost.getRequestLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
