package com.kentchiu.spring;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ApiBlueprintResultHandler implements ResultHandler {


    private Path document;
    private String action = "";
    private Logger logger = LoggerFactory.getLogger(ApiBlueprintResultHandler.class);

    public ApiBlueprintResultHandler(Path document, String action) {
        this.document = document;
        this.action = action;
    }

    public ApiBlueprintResultHandler(Path document) {
        this.document = document;
    }

    public static void cat(String export, List<String> files) throws URISyntaxException, IOException {
        Path targetDir = getMavenTargetDirectory();
        Path api = targetDir.resolve("api");
        List<String> all = Lists.newArrayList();
        for (String name : files) {
            List<String> strs = Files.readAllLines(api.resolve(name));
            all.addAll(strs);
            all.add("");
        }
        Files.write(api.resolve(export), all);
    }

    public static void process(String markdownFileName) throws Exception {
        Path targetDir = getMavenTargetDirectory();
        ApiBlueprintProcessor processor = new ApiBlueprintProcessor();
        Path input = targetDir.resolve("test-classes/api").resolve(markdownFileName);
        Path api = targetDir.resolve("api");
        Path snippetHome = targetDir.resolve("apiblueprint-snippet");
        Path output = api.resolve(markdownFileName);
        Files.createDirectories(api);
        processor.substitution(snippetHome, input, output);
    }

    private static Path getMavenTargetDirectory() throws URISyntaxException {
        URL resource = ApiBlueprintProcessor.class.getResource("/");
        Path path = Paths.get(resource.toURI());
        return path.getParent();
    }

    public void handle(MvcResult result) throws Exception {
        StringBuilder sb = new StringBuilder();
        String request = getRequest(result);
        sb.append(request);
        if (!StringUtils.isBlank(request)) {
            sb.append("\n");
        }
        sb.append(getResponse(result));
        Files.write(document, sb.toString().getBytes());
        logger.info("snippet file: {}", document);
    }

    private String getRequest(MvcResult result) throws IOException {
        MockHttpServletRequest req = result.getRequest();
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.equals("DELETE", req.getMethod()) && !StringUtils.equals("GET", req.getMethod()) || StringUtils.isNotBlank(action)) {
            sb.append("+ Request ");
            if (StringUtils.isNotBlank(action)) {
                sb.append(action).append(" ");
            }
            sb.append("(").append(req.getContentType()).append(")").append("\n");
            sb.append("\n");
            sb.append(indentJson(IOUtils.toString(req.getInputStream()))).append("\n");
        }
        return sb.toString();
    }

    private String getResponse(MvcResult result) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();
        sb.append("+ Response ").append(response.getStatus());
        if (HttpStatus.NO_CONTENT.value() != response.getStatus()) {
            sb.append(" (").append(response.getContentType()).append(")").append("\n");
            sb.append("\n");
            sb.append(indentJson(content));
        }
        return sb.toString();
    }

    private String indentJson(String content) {
        Iterable<String> split = Splitter.on('\n').split(content);
        return ImmutableList.copyOf(split).stream().map(s -> "\t\t\t" + s).collect(Collectors.joining("\n"));
    }

}
