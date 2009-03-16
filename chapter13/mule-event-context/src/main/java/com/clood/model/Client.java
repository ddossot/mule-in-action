package com.clood.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author David Dossot (david@dossot.net)
 */
public class Client {

    private final long id;

    private final String title;

    private final String lastName;

    public Client(final long id, final String title, final String lastName) {
        this.id = id;
        this.title = title;
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
