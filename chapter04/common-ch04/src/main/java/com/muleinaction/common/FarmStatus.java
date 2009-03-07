package com.muleinaction.common;

import java.io.Serializable;

public class FarmStatus implements Serializable {

    String name;
    long vmCount;

    public FarmStatus() {
    }

    public FarmStatus(String name, long vmCount) {
        this.name = name;
        this.vmCount = vmCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVmCount() {
        return vmCount;
    }

    public void setVmCount(long vmCount) {
        this.vmCount = vmCount;
    }

    public String toString() {
        return name + ": " + vmCount;
    }
}
