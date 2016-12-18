package com.sunzequn.sdfs.socket.info;

import java.io.Serializable;

/**
 * Created by Sloriac on 2016/12/18.
 */
public class Ask4File implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String localName;

    public Ask4File(String id, String localName) {
        this.id = id;
        this.localName = localName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    @Override
    public String toString() {
        return "Ask4File{" +
                "id='" + id + '\'' +
                ", localName='" + localName + '\'' +
                '}';
    }
}
