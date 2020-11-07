/**
 * Copyright (c) 2020, Self XDSD Contributors
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

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Unit tests for {@link GithubWebhookEvent}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.4
 * @checkstyle ExecutableStatementCount (1000 lines)
 */
public final class GithubWebhookEventTestCase {

    /**
     * GithubWebhookEvent can return its Project.
     */
    @Test
    public void returnsProject() {
        final Project project = Mockito.mock(Project.class);
        final Event event = new GithubWebhookEvent(
            project, "push", "{\"type\":\"push\"}"
        );
        MatcherAssert.assertThat(
            event.project(),
            Matchers.is(project)
        );
    }

    /**
     * GithubWebhookEvent can return its type.
     */
    @Test
    public void returnsPushType() {
        final Project project = Mockito.mock(Project.class);
        final Event event = new GithubWebhookEvent(
            project, "push", "{\"type\":\"push\"}"
        );
        MatcherAssert.assertThat(
            event.type(),
            Matchers.equalTo("push")
        );
    }

    /**
     * GithubWebhookEvent can return the newIssue type.
     */
    @Test
    public void returnsNewIssueType() {
        final Project project = Mockito.mock(Project.class);
        final Event event = new GithubWebhookEvent(
            project, "issues", "{\"action\":\"opened\"}"
        );
        MatcherAssert.assertThat(
            event.type(),
            Matchers.equalTo(Event.Type.NEW_ISSUE)
        );
    }

    /**
     * GithubWebhookEvent can return the reopened Issue type.
     */
    @Test
    public void returnsReopenedIssueType() {
        final Project project = Mockito.mock(Project.class);
        final Event event = new GithubWebhookEvent(
            project, "issues", "{\"action\":\"reopened\"}"
        );
        MatcherAssert.assertThat(
            event.type(),
            Matchers.equalTo(Event.Type.REOPENED_ISSUE)
        );
    }


    /**
     * GithubWebhookEvent can return the newIssue PR type.
     */
    @Test
    public void returnsNewPullRequestType() {
        final Project project = Mockito.mock(Project.class);
        final Event event = new GithubWebhookEvent(
            project, "pull_request", "{\"action\":\"opened\"}"
        );
        MatcherAssert.assertThat(
            event.type(),
            Matchers.equalTo(Event.Type.NEW_ISSUE)
        );
    }

    /**
     * GithubWebhookEvent can return the reopened PR type.
     */
    @Test
    public void returnsReopenedPullRequestType() {
        final Project project = Mockito.mock(Project.class);
        final Event event = new GithubWebhookEvent(
            project, "pull_request", "{\"action\":\"reopened\"}"
        );
        MatcherAssert.assertThat(
            event.type(),
            Matchers.equalTo(Event.Type.REOPENED_ISSUE)
        );
    }

    /**
     * GithubWebhookEvent can return its null commit.
     */
    @Test
    public void returnsNullCommit() {
        final Event event = new GithubWebhookEvent(
            Mockito.mock(Project.class), "push", "{\"type\":\"push\"}"
        );
        MatcherAssert.assertThat(
            event.commit(),
            Matchers.nullValue()
        );
    }

    /**
     * GithubWebhookEvent can return its Issue.
     */
    @Test
    public void returnsIssue() {
        final Issue issue = Mockito.mock(Issue.class);

        final JsonObject json = Json.createObjectBuilder()
            .add("number", 1).build();
        final JsonObject payload = Json.createObjectBuilder()
            .add("action", "opened")
            .add("issue", json)
            .build();

        final Issues issues = Mockito.mock(Issues.class);
        Mockito.when(issues.received(json)).thenReturn(issue);
        final Repo repo = Mockito.mock(Repo.class);
        Mockito.when(repo.issues()).thenReturn(issues);
        final Provider provider = Mockito.mock(Provider.class);
        Mockito.when(provider.repo("mihai", "test")).thenReturn(repo);
        final ProjectManager manager = Mockito.mock(ProjectManager.class);
        Mockito.when(manager.provider()).thenReturn(provider);

        final Project project = Mockito.mock(Project.class);
        Mockito.when(project.repoFullName()).thenReturn("mihai/test");
        Mockito.when(project.projectManager()).thenReturn(manager);

        final Event event = new GithubWebhookEvent(
            project,
            "issues",
            payload.toString()
        );

        MatcherAssert.assertThat(
            event.issue(),
            Matchers.is(issue)
        );
    }

    /**
     * GithubWebhookEvent can return its Issue.
     */
    @Test
    public void returnsPullRequest() {
        final Issue pull = Mockito.mock(Issue.class);

        final JsonObject json = Json.createObjectBuilder()
            .add("number", 1).build();
        final JsonObject payload = Json.createObjectBuilder()
            .add("action", "opened")
            .add("pull_request", json)
            .build();

        final Issues issues = Mockito.mock(Issues.class);
        Mockito.when(issues.received(json)).thenReturn(pull);
        final Repo repo = Mockito.mock(Repo.class);
        Mockito.when(repo.issues()).thenReturn(issues);
        final Provider provider = Mockito.mock(Provider.class);
        Mockito.when(provider.repo("mihai", "test")).thenReturn(repo);
        final ProjectManager manager = Mockito.mock(ProjectManager.class);
        Mockito.when(manager.provider()).thenReturn(provider);

        final Project project = Mockito.mock(Project.class);
        Mockito.when(project.repoFullName()).thenReturn("mihai/test");
        Mockito.when(project.projectManager()).thenReturn(manager);

        final Event event = new GithubWebhookEvent(
            project,
            "pull_request",
            payload.toString()
        );

        MatcherAssert.assertThat(
            event.issue(),
            Matchers.is(pull)
        );
    }

    /**
     * GithubWebhookEvent can return its Comment.
     */
    @Test
    public void returnsComment() {
        final Comment comment = Mockito.mock(Comment.class);

        final Comments comments = Mockito.mock(Comments.class);
        final JsonObject jsonComment = Json.createObjectBuilder()
            .add("body", "test comment").build();
        Mockito.when(comments.received(jsonComment)).thenReturn(comment);

        final Issue issue = Mockito.mock(Issue.class);
        Mockito.when(issue.comments()).thenReturn(comments);
        final JsonObject json = Json.createObjectBuilder()
            .add("number", 1).build();
        final JsonObject payload = Json.createObjectBuilder()
            .add("action", "opened")
            .add("issue", json)
            .add("comment", jsonComment)
            .build();

        final Issues issues = Mockito.mock(Issues.class);
        Mockito.when(issues.received(json)).thenReturn(issue);
        final Repo repo = Mockito.mock(Repo.class);
        Mockito.when(repo.issues()).thenReturn(issues);
        final Provider provider = Mockito.mock(Provider.class);
        Mockito.when(provider.repo("mihai", "test")).thenReturn(repo);
        final ProjectManager manager = Mockito.mock(ProjectManager.class);
        Mockito.when(manager.provider()).thenReturn(provider);

        final Project project = Mockito.mock(Project.class);
        Mockito.when(project.repoFullName()).thenReturn("mihai/test");
        Mockito.when(project.projectManager()).thenReturn(manager);

        final Event event = new GithubWebhookEvent(
            project,
            "issues",
            payload.toString()
        );

        MatcherAssert.assertThat(
            event.comment(),
            Matchers.is(comment)
        );
    }
}