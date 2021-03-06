/*
 * (c) Kitodo. Key to digital objects e. V. <contact@kitodo.org>
 *
 * This file is part of the Kitodo project.
 *
 * It is licensed under GNU General Public License version 3 or later.
 *
 * For the full copyright and license information, please read the
 * GPL3-License.txt file that was distributed with this source code.
 */

package org.kitodo.data.database.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Hibernate;

@Entity
@Table(name = "client")
public class Client extends BaseIndexedBean {

    private static final long serialVersionUID = -5538496170333987498L;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.PERSIST)
    private List<Workflow> workflows;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "clients", cascade = CascadeType.PERSIST)
    private List<User> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.PERSIST)
    private List<Role> roles;

    /**
     * Gets name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name
     *            The name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets projects.
     *
     * @return The projects.
     */
    public List<Project> getProjects() {
        Hibernate.initialize(this.projects);
        if (this.projects == null) {
            this.projects = new ArrayList<>();
        }
        return projects;
    }

    /**
     * Sets projects.
     *
     * @param projects
     *            The projects.
     */
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    /**
     * Get workflows.
     *
     * @return list of Workflow objects
     */
    public List<Workflow> getWorkflows() {
        Hibernate.initialize(this.workflows);
        if (Objects.isNull(this.workflows)) {
            this.workflows = new ArrayList<>();
        }
        return this.workflows;
    }

    /**
     * Set workflows.
     *
     * @param workflows
     *            as List of Workflow objects
     */
    public void setWorkflows(List<Workflow> workflows) {
        this.workflows = workflows;
    }

    /**
     * Gets users.
     *
     * @return The users.
     */
    public List<User> getUsers() {
        Hibernate.initialize(this.users);
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        return users;
    }

    /**
     * Sets users.
     *
     * @param users
     *            The users.
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * Get roles.
     *
     * @return list of Role objects
     */
    public List<Role> getRoles() {
        Hibernate.initialize(this.roles);
        if (Objects.isNull(this.roles)) {
            this.roles = new ArrayList<>();
        }
        return this.roles;
    }

    /**
     * Set roles.
     *
     * @param roles
     *            as List of Role objects
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Client client = (Client) o;
        return name != null ? name.equals(client.name) : client.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
