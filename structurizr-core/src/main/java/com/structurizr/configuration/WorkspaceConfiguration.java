package com.structurizr.configuration;

import java.util.Set;
import java.util.TreeSet;

/**
 * A wrapper for configuration options related to the workspace.
 */
public final class WorkspaceConfiguration {

    private WorkspaceScope scope = null;
    private Visibility visibility = null;
    private Set<User> users = new TreeSet<>();

    WorkspaceConfiguration() {
    }

    /**
     * Gets the scope of this workspace
     *
     * @return      a WorkspaceScope enum, or null if undefined
     */
    public WorkspaceScope getScope() {
        return scope;
    }

    /**
     * Sets the workspace scope.
     *
     * @param scope     a WorkspaceScope enum, or null if undefined
     */
    public void setScope(WorkspaceScope scope) {
        this.scope = scope;
    }

    /**
     * Gets the visibility of this workspace (private or public).
     *
     * @return      a Visibility enum
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility of this workspace (private or public).
     *
     * @param visibility    a Visibility enum, or null to indicate that no changes should be made
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * Gets the set of users should have read-write or read-only access to the workspace.
     *
     * @return   a Set of User objects (could be empty)
     */
    public Set<User> getUsers() {
        return new TreeSet<>(users);
    }

    void setUsers(Set<User> users) {
        if (users != null) {
            this.users = new TreeSet<>(users);
        }
    }

    /**
     * Adds a user.
     *
     * @param   user        a User object representing the username and role
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Adds a user, with the specified username and role.
     *
     * @param username      the username (e.g. an e-mail address)
     * @param role          the user's role
     */
    public void addUser(String username, Role role) {
        users.add(new User(username, role));
    }

    /**
     * Clears all configured users.
     */
    public void clearUsers() {
        this.users = new TreeSet<>();
    }

}