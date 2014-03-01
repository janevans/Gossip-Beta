package com.example.gossip;

import com.example.gossip.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "gossipmessageendpoint", namespace = @ApiNamespace(ownerDomain = "example.com", ownerName = "example.com", packagePath = "gossip"))
public class GossipMessageEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listGossipMessage")
	public CollectionResponse<GossipMessage> listGossipMessage(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<GossipMessage> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr
					.createQuery("select from GossipMessage as GossipMessage");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<GossipMessage>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (GossipMessage obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<GossipMessage> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getGossipMessage")
	public GossipMessage getGossipMessage(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		GossipMessage gossipmessage = null;
		try {
			gossipmessage = mgr.find(GossipMessage.class, id);
		} finally {
			mgr.close();
		}
		return gossipmessage;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param gossipmessage the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertGossipMessage")
	public GossipMessage insertGossipMessage(GossipMessage gossipmessage) {
		EntityManager mgr = getEntityManager();
		try {
			/*
			if (containsGossipMessage(gossipmessage)) {
				throw new EntityExistsException("Object already exists");
			}*/
			mgr.persist(gossipmessage);
		} finally {
			mgr.close();
		}
		return gossipmessage;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param gossipmessage the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateGossipMessage")
	public GossipMessage updateGossipMessage(GossipMessage gossipmessage) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsGossipMessage(gossipmessage)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(gossipmessage);
		} finally {
			mgr.close();
		}
		return gossipmessage;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeGossipMessage")
	public void removeGossipMessage(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			GossipMessage gossipmessage = mgr.find(GossipMessage.class, id);
			mgr.remove(gossipmessage);
		} finally {
			mgr.close();
		}
	}

	private boolean containsGossipMessage(GossipMessage gossipmessage) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			GossipMessage item = mgr
					.find(GossipMessage.class, gossipmessage.getKey());
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
