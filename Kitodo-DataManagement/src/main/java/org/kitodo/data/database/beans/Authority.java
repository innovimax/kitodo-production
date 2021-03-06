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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "authority")
public class Authority extends BaseIndexedBean {

    private static final long serialVersionUID = -5187947220333987498L;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @ManyToMany(mappedBy = "authorities", cascade = CascadeType.PERSIST)
    private List<Role> roles;

    /**
     * The constructor for setting title and assignables.
     * 
     * @param title
     *            The title.
     */
    public Authority(String title) {
        this.title = title;
    }

    /**
     * The normal constructor.
     */
    public Authority() {
    }

    /**
     * Gets the title.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the title without suffixes separated by "_" e.g. "admin_global" will
     * become "admin".
     * 
     * @return The title without suffix.
     */
    public String getTitleWithoutSuffix() {
        return title.split("_")[0];
    }

    /**
     * Sets the title.
     *
     * @param title
     *            The title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get all roles in which this authority is used.
     *
     * @return the roles
     */
    public List<Role> getRoles() {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        return roles;
    }

    /**
     * Set the roles.
     *
     * @param roles
     *            the roles
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
        Authority authority = (Authority) o;
        return title != null ? title.equals(authority.title) : authority.title == null;
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Authority{" + "title='" + title + "\'" + '}' + "@" + Integer.toHexString(hashCode());
    }
}
