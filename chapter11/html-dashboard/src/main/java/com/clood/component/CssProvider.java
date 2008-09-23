package com.clood.component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.springframework.util.FileCopyUtils;

/**
 * @author David Dossot (david@dossot.net)
 */
class CssProvider implements ContentProvider {

    private final String css;

    public CssProvider() throws IOException {
        css =
                FileCopyUtils.copyToString(new InputStreamReader(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream(
                                "dashboard.css"), Charset.defaultCharset()));
    }

    public String getContent() {
        return css;
    }

}
