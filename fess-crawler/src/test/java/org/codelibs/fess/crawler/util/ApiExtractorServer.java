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
package org.codelibs.fess.crawler.util;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.codelibs.core.io.FileUtil;
import org.codelibs.fess.crawler.exception.CrawlerSystemException;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;

/**
 * @author ma2tani
 *
 */
public class ApiExtractorServer {
    private int port = 8080;

    private File docRoot;

    private Server server;

    private boolean tempDocRoot = false;

    public ApiExtractorServer(final int port) {
        this(port, createDocRoot(3));
        tempDocRoot = true;
        server = new Server(port);

        PostHandler post_handler = new PostHandler();
        //        Log.getLog().info("serving " + post_handler.getBaseResource());
        server.setHandler(post_handler);
    }

    public ApiExtractorServer(final int port, final File docRoot) {
        this.port = port;
        this.docRoot = docRoot;

        server = new Server(port);

        //         String[] configuration = new String[] {
        //         "org.eclipse.jetty.webapp.WebInfConfiguration",
        //         "org.eclipse.jetty.webapp.WebXmlConfiguration",
        //         "org.eclipse.jetty.webapp.MetaInfConfiguration",
        //         "org.eclipse.jetty.webapp.FragmentConfiguration",
        //         "org.eclipse.jetty.plus.webapp.EnvConfiguration",
        //         "org.eclipse.jetty.plus.webapp.PlusConfiguration",
        //         "org.eclipse.jetty.annotations.AnnotationConfiguration",
        //         "org.eclipse.jetty.webapp.JettyWebXmlConfiguration" };
        //         server.setAttribute("org.eclipse.jetty.webapp.configuration", configuration);

        PostHandler post_handler = new PostHandler();
        //        Log.getLog().info("serving " + post_handler.getBaseResource());
        server.setHandler(post_handler);
        //        startjoin();
    }

    @MultipartConfig
    public static class PostHandler extends ContextHandler {
        public PostHandler() {
            super("/post");
        }

        @Override
        public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            System.out.print(request.getContentType());
            Part p = request.getPart("content");
            System.out.print(p.getSize());
            baseRequest.setHandled(true);
        }
    }

    //    public void doPost(String target) {
    //        System.out.println(target);
    //    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        System.out.println("target = " + target);

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("<h1>Hello Jetty!!</h1>");
    }

    public void start() {
        try {
            server.start();
            //            server.join();
        } catch (final Exception e) {
            throw new CrawlerSystemException(e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (final Exception e) {
            throw new CrawlerSystemException(e);
        } finally {
            if (tempDocRoot) {
                docRoot.delete();
            }
        }
    }

    protected static File createDocRoot(final int count) {
        try {
            final File tempDir = File.createTempFile("extracterDocRoot", "");
            tempDir.delete();
            tempDir.mkdirs();

            // robots.txt
            StringBuilder buf = new StringBuilder();
            buf.append("User-agent: *").append('\n');
            buf.append("Disallow: /admin/").append('\n');
            buf.append("Disallow: /websvn/").append('\n');
            final File robotTxtFile = new File(tempDir, "robots.txt");
            FileUtil.writeBytes(robotTxtFile.getAbsolutePath(), buf.toString().getBytes("UTF-8"));
            robotTxtFile.deleteOnExit();

            // sitemaps.xml
            buf = new StringBuilder();
            buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append('\n');
            buf.append("<urlset ").append("xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">").append('\n');
            buf.append("<url>").append('\n');
            buf.append("<loc>http://localhost:7070/index.html</loc>").append('\n');
            buf.append("<loc>http://localhost:7070/file").append(count).append("-1.html").append("</loc>").append('\n');
            buf.append("</url>").append('\n');
            buf.append("</urlset>").append('\n');
            File sitemapsFile = new File(tempDir, "sitemaps.xml");
            FileUtil.writeBytes(sitemapsFile.getAbsolutePath(), buf.toString().getBytes("UTF-8"));
            robotTxtFile.deleteOnExit();

            // sitemaps.txt
            buf = new StringBuilder();
            buf.append("http://localhost:7070/index.html").append('\n');
            buf.append("http://localhost:7070/file").append(count).append("-1.html").append('\n');
            sitemapsFile = new File(tempDir, "sitemaps.txt");
            FileUtil.writeBytes(sitemapsFile.getAbsolutePath(), buf.toString().getBytes("UTF-8"));
            robotTxtFile.deleteOnExit();

            generateContents(tempDir, count);

            return tempDir;
        } catch (final Exception e) {
            throw new CrawlerSystemException(e);
        }
    }

    private static void generateContents(final File dir, final int count) throws Exception {
        if (count <= 0) {
            return;
        }

        final String content = getHtmlContent(count);

        final File indexFile = new File(dir, "index.html");
        indexFile.deleteOnExit();
        FileUtil.writeBytes(indexFile.getAbsolutePath(), content.getBytes("UTF-8"));

        for (int i = 1; i <= 10; i++) {
            final File file = new File(dir, "file" + count + "-" + i + ".html");
            file.deleteOnExit();
            FileUtil.writeBytes(file.getAbsolutePath(), content.getBytes("UTF-8"));
            final File childDir = new File(dir, "dir" + count + "-" + i);
            childDir.mkdirs();
            generateContents(childDir, count - 1);
        }
    }

    private static String getHtmlContent(final int count) {
        final StringBuilder buf = new StringBuilder();
        buf.append("<html><head><title>Title ");
        buf.append(count);
        buf.append("</title></head><body><h1>Content ");
        buf.append(count);
        buf.append("</h1><br>");
        buf.append("<a href=\"index.html\">Index</a><br>");
        for (int i = 1; i <= 10; i++) {
            buf.append("<a href=\"file");
            buf.append(count);
            buf.append("-");
            buf.append(i);
            buf.append(".html\">File ");
            buf.append(count);
            buf.append("-");
            buf.append(i);
            buf.append("</a><br>");
            buf.append("<a href=\"dir");
            buf.append(count);
            buf.append("-");
            buf.append(i);
            buf.append("/index.html\">Dir ");
            buf.append(count);
            buf.append("-");
            buf.append(i);
            buf.append("</a><br>");
        }
        buf.append("</body></html>");
        return buf.toString();
    }
}
