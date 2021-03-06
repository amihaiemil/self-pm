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
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for {@link ReviewAssignedTasks}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @checkstyle ExecutableStatementCount (500 lines)
 */
public final class ReviewAssignedTasksTestCase {

    /**
     * It should work fine if the PM has no assigned Projects.
     */
    @Test
    public void reviewsNoProjects() {
        final ProjectManager manager = Mockito.mock(ProjectManager.class);
        final Projects projects = Mockito.mock(Projects.class);
        Mockito.when(projects.iterator()).thenReturn(
            new ArrayList<Project>().iterator()
        );
        Mockito.when(manager.projects()).thenReturn(projects);

        final ProjectManagers all = Mockito.mock(ProjectManagers.class);
        Mockito.when(all.iterator()).thenReturn(
            Arrays.asList(manager).iterator()
        );
        final Self core = Mockito.mock(Self.class);
        Mockito.when(core.projectManagers()).thenReturn(all);
        final ReviewAssignedTasks review = new ReviewAssignedTasks(core);
        review.reviewAssignedTasks();
        Mockito.verify(core, Mockito.times(1)).projectManagers();
        Mockito.verify(manager, Mockito.times(1)).projects();
    }

    /**
     * It should work fine if the PM has some assigned Projects.
     */
    @Test
    public void reviewsProjects() {
        final List<Project> mocks = new ArrayList<>();
        for(int idx = 0; idx <3; idx++){
            final Project project = Mockito.mock(Project.class);
            Mockito.doNothing().when(project).resolve(Mockito.any());
            mocks.add(project);
        }

        final ProjectManager manager = Mockito.mock(ProjectManager.class);
        final Projects projects = Mockito.mock(Projects.class);
        Mockito.when(projects.iterator()).thenReturn(
            mocks.iterator()
        );
        Mockito.when(manager.projects()).thenReturn(projects);

        final ProjectManagers all = Mockito.mock(ProjectManagers.class);
        Mockito.when(all.iterator()).thenReturn(
            Arrays.asList(manager).iterator()
        );
        final Self core = Mockito.mock(Self.class);
        Mockito.when(core.projectManagers()).thenReturn(all);
        final ReviewAssignedTasks review = new ReviewAssignedTasks(core);
        review.reviewAssignedTasks();
        Mockito.verify(core, Mockito.times(1)).projectManagers();
        Mockito.verify(manager, Mockito.times(1)).projects();
        for(int idx = 0; idx <3; idx++){
            final Project project = mocks.get(idx);
            Mockito.verify(project, Mockito.times(1)).resolve(Mockito.any());
        }
    }

    /**
     * If there's an exception thrown by one of the Projects, it
     * should go on with the rest.
     */
    @Test
    public void reviewsProjectsWithException() {
        final List<Project> mocks = new ArrayList<>();
        for(int idx = 0; idx <3; idx++){
            final Project project = Mockito.mock(Project.class);
            if(idx == 1) {
                Mockito.doThrow(new IllegalStateException("Exception"))
                    .when(project).resolve(Mockito.any());
            } else {
                Mockito.doNothing().when(project).resolve(Mockito.any());
            }
            mocks.add(project);
        }

        final ProjectManager manager = Mockito.mock(ProjectManager.class);
        final Projects projects = Mockito.mock(Projects.class);
        Mockito.when(projects.iterator()).thenReturn(
            mocks.iterator()
        );
        Mockito.when(manager.projects()).thenReturn(projects);

        final ProjectManagers all = Mockito.mock(ProjectManagers.class);
        Mockito.when(all.iterator()).thenReturn(
            Arrays.asList(manager).iterator()
        );
        final Self core = Mockito.mock(Self.class);
        Mockito.when(core.projectManagers()).thenReturn(all);
        final ReviewAssignedTasks review = new ReviewAssignedTasks(core);
        review.reviewAssignedTasks();
        Mockito.verify(core, Mockito.times(1)).projectManagers();
        Mockito.verify(manager, Mockito.times(1)).projects();
        for(int idx = 0; idx <3; idx++){
            final Project project = mocks.get(idx);
            Mockito.verify(project, Mockito.times(1)).resolve(Mockito.any());
        }
    }

    /**
     * Review period has correct format.
     */
    @Test
    public void reviewPeriodHasCorrectFormat() {
        MatcherAssert.assertThat(
            Duration.parse(ReviewAssignedTasks.EVERY_30_MINUTES),
            Matchers.equalTo(Duration.ofMinutes(30))
        );
    }

}
