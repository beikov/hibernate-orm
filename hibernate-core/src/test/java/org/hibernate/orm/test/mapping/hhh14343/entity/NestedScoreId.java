/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.mapping.hhh14343.entity;

import java.io.Serializable;

public class NestedScoreId implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Integer gameId;

	private Boolean home;

	public NestedScoreId()
	{
	}

	public Integer getGameId()
	{
		return gameId;
	}

	public void setGameId(Integer gameId)
	{
		this.gameId = gameId;
	}

	public Boolean getHome()
	{
		return home;
	}

	public void setHome(Boolean home)
	{
		this.home = home;
	}
}
