package org.acme;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class GeneratedFile {

    public String name;
    public String content;

    @Override
    public String toString() {
        return "GeneratedFile{" +
            "name='" + name + '\'' +
            ", content='" + content + '\'' +
            '}';
    }
}
