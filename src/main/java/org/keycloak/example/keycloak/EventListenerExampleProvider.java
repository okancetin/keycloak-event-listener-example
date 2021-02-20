package org.keycloak.example.keycloak;

import java.util.Collection;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.jboss.logging.Logger;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.jpa.entities.UserAttributeEntity;
import org.keycloak.models.jpa.entities.UserEntity;

public class EventListenerExampleProvider implements EventListenerProvider {

  private static final Logger log = Logger.getLogger(EventListenerExampleProvider.class);
  private static final String PLAY_COUNT = "is_played";
  private static final String NA = "NA";

  private KeycloakSession keycloakSession;
  private EntityManager entityManager;

  public EventListenerExampleProvider(KeycloakSession keycloakSession){
    this.keycloakSession = keycloakSession;
    this.entityManager = keycloakSession.getProvider(JpaConnectionProvider.class).getEntityManager();
  }

  @Override
  public void onEvent(Event event) {
    if (event.getType().equals(EventType.LOGIN)) {
      storeInUserAttributes(event);
    }
  }

  @Override
  public void onEvent(AdminEvent event, boolean includeRepresentation) {
    //This is triggered when Admin event occurs
  }

  @Override
  public void close() {
    this.entityManager.close();
  }

  public Optional<UserAttributeEntity> getPlayCountAtt(Event event) {
    UserEntity userEntity = entityManager.find(UserEntity.class, event.getUserId());

    Collection<UserAttributeEntity> userAttributeEntities = userEntity.getAttributes();
    return userAttributeEntities.stream()
        .filter(e -> e.getName().equalsIgnoreCase(PLAY_COUNT))
        .findFirst();
  }

  public void updatePlayCountAtt(Optional<UserAttributeEntity> playCountAttribute) {
    if (playCountAttribute.isPresent()) {
      playCountAttribute.get().setValue(NA);
      merge(playCountAttribute);
    }
  }

  public void storeInUserAttributes(Event event) {

    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      updatePlayCountAtt(getPlayCountAtt(event));
    } catch (Exception ex) {
      log.error("If nothing happens something happen");
    } finally {
      transaction.commit();
    }
  }

  private void merge(Object entity) {
    if (entity == null) {
      return;
    }
    entityManager.merge(entity);
  }

}
