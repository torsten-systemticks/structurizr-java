package com.structurizr.configuration;

import com.structurizr.util.StringUtils;

/**
 * Represents a user, and the role-based access they have to a workspace.
 */
public final class User implements Comparable<User> {

    private String username;
    private Role role;

    User() {
    }

    public User(String username, Role role) {
        if (StringUtils.isNullOrEmpty(username)) {
            throw new IllegalArgumentException("A username must be specified.");
        }

        if (role == null) {
            throw new IllegalArgumentException("A role must be specified.");
        }

        setUsername(username);
        setRole(role);
    }

    /**
     * Gets the username (e.g. e-mail address).
     *
     * @return  the username, as a String
     */
    public String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the role.
     *
     * @return  a Role enum
     */
    public Role getRole() {
        return role;
    }

    void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "User {" +
                "username='" + username + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public int compareTo(User user) {
        return getUsername().compareTo(user.getUsername());
    }

}