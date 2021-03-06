/**
 * Copyright (c) 2020-2021, Self XDSD Contributors
 * All rights reserved.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to read the Software only. Permission is hereby NOT GRANTED to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.selfxdsd.selfpm;

import com.selfxdsd.api.*;
import com.selfxdsd.core.Env;
import com.selfxdsd.core.SelfCore;
import com.selfxdsd.storage.MySql;
import com.selfxdsd.storage.SelfJooq;
import org.springframework.stereotype.Component;

/**
 * Self Core component.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.4
 */
@Component
public final class SelfCoreComponent implements Self {

    /**
     * Self's core.
     */
    private final Self core;

    /**
     * Default constructor for Spring.
     */
    public SelfCoreComponent() {
        this(
            new SelfCore(
                new SelfJooq(
                    new MySql(
                        System.getenv(Env.DB_URL),
                        System.getenv(Env.DB_USER),
                        System.getenv(Env.DB_PASSWORD)
                    )
                )
            )
        );
    }

    /**
     * Constructor.
     * @param core Encapsulated core.
     */
    public SelfCoreComponent(final Self core) {
        this.core = core;
    }

    @Override
    public User login(final Login login) {
        return this.core.login(login);
    }

    @Override
    public User authenticate(final String token) {
        return this.core.authenticate(token);
    }

    @Override
    public ProjectManagers projectManagers() {
        return this.core.projectManagers();
    }

    @Override
    public Projects projects() {
        return this.core.projects();
    }

    @Override
    public Contributors contributors() {
        return this.core.contributors();
    }

    @Override
    public void close() throws Exception {
        this.core.close();
    }
}
