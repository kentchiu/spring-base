package com.kentchiu.spring;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ApiBlueprintProcessor {

    private Logger logger = LoggerFactory.getLogger(ApiBlueprintProcessor.class);

    public void substitution(Path snippetHome, Path input, Path output) throws IOException {
        List<String> lines = Files.readAllLines(input);
        Map<String, String> map = loadSnippetHomeToMap(snippetHome);

        int i = 1;
        List<String> lines2 = Lists.newArrayList();
        for (String line : lines) {
            String[] tokens = StringUtils.substringsBetween(line, "{{", "}}");
            if (tokens != null && tokens.length > 0) {
                String str = new String();
                for (String token : tokens) {
                    if (map.containsKey(token)) {
                        logger.trace("replace token @ {}:{} : {} ", input, i, token);
                        str = line.replace("{{" + token + "}}", map.getOrDefault(token, "{{" + token + "}}"));
                    } else {
                        logger.warn("token don't mapping @ {}:{} : {} ", input, i, token);
                        str = line;
                    }
                }
                lines2.add(str);
            } else {
                lines2.add(line);
            }
            i++;
        }
        String join = Joiner.on('\n').join(lines2);
        Files.write(output, join.getBytes());
    }

    private Map<String, String> loadSnippetHomeToMap(Path snippetHome) throws IOException {
        Map<String, String> results = Maps.newLinkedHashMap();
        Collection<File> files = FileUtils.listFiles(snippetHome.toFile(), new String[]{"md"}, true);
        for (File file : files) {
            Path relativize = snippetHome.relativize(file.toPath());
            results.put(relativize.toString(), FileUtils.readFileToString(file));
        }
        return results;
    }
}


