package com.clood.component;

import java.util.Collections;
import java.util.List;

/**
 * @author David Dossot (david@dossot.net)
 */
public class Md5FileHasherClient {

    private Md5FileHasherService md5hasher;

    public void setMd5hasher(final Md5FileHasherService md5hasher) {
        this.md5hasher = md5hasher;
    }

    public List<String> process(final String fileName) {
        // for the sake of the demonstration, we return something visibly different
        // from the response of the MD5 hasher.
        return Collections.singletonList(md5hasher.hash(fileName));
    }

}
