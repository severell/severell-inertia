package com.severell.plugins.inertia;

import com.severell.core.container.Container;
import com.severell.core.providers.ServiceProvider;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.file.Files;
import java.nio.file.Paths;


public class InertiaProvider extends ServiceProvider {

    public InertiaProvider(Container c) {
        super(c);
    }

    @Override
    public void register() {
        this.c.bind(Inertia.class, (c) -> new Inertia());
    }

    @Override
    public void boot() throws Exception {
        String version = DigestUtils.md5Hex(Files.readAllBytes(Paths.get("mix-manifest.json"))).toUpperCase();
        Inertia.setVersion(version);
    }
}
