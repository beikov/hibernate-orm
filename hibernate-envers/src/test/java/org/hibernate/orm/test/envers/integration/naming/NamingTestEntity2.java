/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.envers.integration.naming;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "naming_test_entity_2")
@AuditTable("naming_test_entity_2_versions")
public class NamingTestEntity2 {
	@Id
	@GeneratedValue
	@Column(name = "nte_id")
	@Audited(withModifiedFlag = true)
	private Integer id;

	@Column(name = "nte_data")
	@Audited(withModifiedFlag = true, modifiedColumnName = "data_MOD_different")
	private String data;

	public NamingTestEntity2() {
	}

	public NamingTestEntity2(String data) {
		this.data = data;
	}

	public NamingTestEntity2(Integer id, String data) {
		this.id = id;
		this.data = data;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( !(o instanceof NamingTestEntity2) ) {
			return false;
		}

		NamingTestEntity2 that = (NamingTestEntity2) o;

		if ( data != null ? !data.equals( that.data ) : that.data != null ) {
			return false;
		}
		if ( id != null ? !id.equals( that.id ) : that.id != null ) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result;
		result = (id != null ? id.hashCode() : 0);
		result = 31 * result + (data != null ? data.hashCode() : 0);
		return result;
	}
}
