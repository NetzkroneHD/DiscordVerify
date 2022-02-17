package de.netzkronehd.discordverifybot.group;


import net.dv8tion.jda.api.entities.Role;

import java.util.Objects;

public class Group {

    private final String id, name, roleId, permission;
    private final int priority;
    private Role role;

    public Group(String id, String name, String roleId, String permission, int priority) {
        this.id = id;
        this.name = name;
        this.roleId = roleId;
        this.permission = permission;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getPermission() {
        return permission;
    }

    public int getPriority() {
        return priority;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return priority == group.priority && Objects.equals(id, group.id) && Objects.equals(name, group.name) && Objects.equals(roleId, group.roleId) && Objects.equals(permission, group.permission);
    }
}
