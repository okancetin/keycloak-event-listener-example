package org.keycloak.example;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.events.Event;
import org.keycloak.example.keycloak.EventListenerExampleProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.jpa.entities.UserAttributeEntity;
import org.keycloak.models.jpa.entities.UserEntity;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventListenerExampleProviderTest {

  private static final String PLAY_COUNT = "is_played";

  @Mock
  KeycloakSession keycloakSession;
  EventListenerExampleProvider service;
  @Mock
  JpaConnectionProvider jpaConnectionProvider;
  @Mock
  EntityManager entityManager;
  @Mock
  Event event;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(keycloakSession.getProvider(JpaConnectionProvider.class)).thenReturn(jpaConnectionProvider);
    when(keycloakSession.getProvider(JpaConnectionProvider.class).getEntityManager()).thenReturn(entityManager);
    service = new EventListenerExampleProvider(keycloakSession);
  }

  //TODO add corresponding unit tests. ticket: JRW-12

  @Test
  void testGetPlayCountAtt() {
    UserEntity userEntity = new UserEntity();
    Collection<UserAttributeEntity> attributeEntities = new ArrayList<>();
    UserAttributeEntity playCount = new UserAttributeEntity();
    playCount.setName(PLAY_COUNT);
    playCount.setValue("NA");
    attributeEntities.add(playCount);
    userEntity.setAttributes(attributeEntities);

    when(entityManager.find(any(), any())).thenReturn(userEntity);

    Optional<UserAttributeEntity> userAttributeEntity =  service.getPlayCountAtt(event);
    Assertions.assertTrue(userAttributeEntity.isPresent());
  }
}
