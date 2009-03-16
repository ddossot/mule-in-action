package com.clood.dao;

import org.apache.commons.lang.RandomStringUtils;

import com.clood.model.Client;

/**
 * @author David Dossot (david@dossot.net)
 */
public class ClientDao {

    public Client findById(final Long id) {
        return new Client(id, RandomStringUtils.randomAscii(5), RandomStringUtils
                .randomAscii(10));
    }

}
