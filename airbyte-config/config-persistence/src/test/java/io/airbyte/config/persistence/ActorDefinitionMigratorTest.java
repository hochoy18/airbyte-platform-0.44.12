/*
 * Copyright (c) 2023 Airbyte, Inc., all rights reserved.
 */

package io.airbyte.config.persistence;

import static io.airbyte.config.ConfigSchema.STANDARD_DESTINATION_DEFINITION;
import static io.airbyte.config.ConfigSchema.STANDARD_SOURCE_DEFINITION;
import static io.airbyte.featureflag.ContextKt.ANONYMOUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.airbyte.commons.json.Jsons;
import io.airbyte.config.ReleaseStage;
import io.airbyte.config.StandardDestinationDefinition;
import io.airbyte.config.StandardSourceDefinition;
import io.airbyte.config.StandardSourceDefinition.SourceType;
import io.airbyte.config.persistence.ActorDefinitionMigrator.ConnectorInfo;
import io.airbyte.featureflag.FeatureFlagClient;
import io.airbyte.featureflag.SeedActorDefinitionVersions;
import io.airbyte.featureflag.TestClient;
import io.airbyte.featureflag.Workspace;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActorDefinitionMigratorTest extends BaseConfigDatabaseTest {

  public static final String DEFAULT_PROTOCOL_VERSION = "0.2.0";
  protected static final StandardSourceDefinition SOURCE_POSTGRES = new StandardSourceDefinition()
      .withName("Postgres")
      .withSourceDefinitionId(UUID.fromString("decd338e-5647-4c0b-adf4-da0e75f5a750"))
      .withDockerRepository("airbyte/source-postgres")
      .withDockerImageTag("0.3.11")
      .withDocumentationUrl("https://docs.airbyte.io/integrations/sources/postgres")
      .withIcon("postgresql.svg")
      .withSourceType(SourceType.DATABASE)
      .withTombstone(false);
  protected static final StandardSourceDefinition SOURCE_CUSTOM = new StandardSourceDefinition()
      .withName("Custom")
      .withSourceDefinitionId(UUID.fromString("baba338e-5647-4c0b-adf4-da0e75f5a750"))
      .withDockerRepository("airbyte/custom")
      .withDockerImageTag("0.3.11")
      .withDocumentationUrl("https://docs.airbyte.io/integrations/sources/postgres")
      .withIcon("postgresql.svg")
      .withSourceType(SourceType.DATABASE)
      .withCustom(true)
      .withReleaseStage(ReleaseStage.CUSTOM)
      .withTombstone(false);
  protected static final StandardDestinationDefinition DESTINATION_S3 = new StandardDestinationDefinition()
      .withName("S3")
      .withDestinationDefinitionId(UUID.fromString("4816b78f-1489-44c1-9060-4b19d5fa9362"))
      .withDockerRepository("airbyte/destination-s3")
      .withDockerImageTag("0.1.12")
      .withDocumentationUrl("https://docs.airbyte.io/integrations/destinations/s3")
      .withProtocolVersion(DEFAULT_PROTOCOL_VERSION)
      .withTombstone(false);
  protected static final StandardDestinationDefinition DESTINATION_CUSTOM = new StandardDestinationDefinition()
      .withName("Custom")
      .withDestinationDefinitionId(UUID.fromString("baba338e-5647-4c0b-adf4-da0e75f5a750"))
      .withDockerRepository("airbyte/custom")
      .withDockerImageTag("0.3.11")
      .withDocumentationUrl("https://docs.airbyte.io/integrations/sources/postgres")
      .withIcon("postgresql.svg")
      .withCustom(true)
      .withReleaseStage(ReleaseStage.CUSTOM)
      .withTombstone(false);

  private ActorDefinitionMigrator migrator;
  private ConfigRepository configRepository;
  private final FeatureFlagClient featureFlagClient = mock(TestClient.class);

  @BeforeEach
  void setup() throws SQLException {
    truncateAllTables();

    configRepository = new ConfigRepository(database, null, MockData.MAX_SECONDS_BETWEEN_MESSAGE_SUPPLIER);
    migrator = new ActorDefinitionMigrator(configRepository, featureFlagClient);
    when(featureFlagClient.boolVariation(SeedActorDefinitionVersions.INSTANCE, new Workspace(ANONYMOUS))).thenReturn(true);
  }

  private void writeSource(final StandardSourceDefinition source) throws Exception {
    configRepository.writeStandardSourceDefinition(source);
  }

  @Test
  void testGetConnectorRepositoryToInfoMap() throws Exception {
    final String connectorRepository = "airbyte/duplicated-connector";
    final String oldVersion = "0.1.10";
    final String newVersion = DEFAULT_PROTOCOL_VERSION;
    final StandardSourceDefinition source1 = new StandardSourceDefinition()
        .withSourceDefinitionId(UUID.randomUUID())
        .withName("source-1")
        .withDockerRepository(connectorRepository)
        .withDockerImageTag(oldVersion);
    final StandardSourceDefinition source2 = new StandardSourceDefinition()
        .withSourceDefinitionId(UUID.randomUUID())
        .withName("source-2")
        .withDockerRepository(connectorRepository)
        .withDockerImageTag(newVersion);

    final String customConnectorRepository = "airbyte/custom";
    final StandardSourceDefinition customSource = new StandardSourceDefinition()
        .withSourceDefinitionId(UUID.randomUUID())
        .withName("source-3")
        .withDockerRepository(customConnectorRepository)
        .withDockerImageTag(newVersion)
        .withReleaseStage(ReleaseStage.CUSTOM)
        .withCustom(true);
    writeSource(source1);
    writeSource(source2);
    writeSource(customSource);
    final Map<String, ConnectorInfo> result = migrator.getConnectorRepositoryToInfoMap();
    // when there are duplicated connector definitions, the one with the latest version should be
    // retrieved
    assertEquals(newVersion, result.get(connectorRepository).dockerImageTag);
    // custom connectors are excluded
    assertNull(result.get(customConnectorRepository));
  }

  @Test
  void testUpdateIsAvailable() {
    assertTrue(ActorDefinitionMigrator.updateIsAvailable("0.1.99", DEFAULT_PROTOCOL_VERSION));
    assertFalse(ActorDefinitionMigrator.updateIsAvailable("invalid_version", "0.1.2"));
  }

  @Test
  void testUpdateIsPatchOnly() {
    assertFalse(ActorDefinitionMigrator.updateIsPatchOnly("0.1.99", DEFAULT_PROTOCOL_VERSION));
    assertFalse(ActorDefinitionMigrator.updateIsPatchOnly("invalid_version", "0.3.1"));
    assertTrue(ActorDefinitionMigrator.updateIsPatchOnly("0.1.0", "0.1.3"));
  }

  @Test
  void testActorDefinitionReleaseDate() throws Exception {
    final UUID definitionId = UUID.randomUUID();
    final String connectorRepository = "airbyte/test-connector";

    // when the record does not exist, it is inserted
    final StandardSourceDefinition sourceDef = new StandardSourceDefinition()
        .withSourceDefinitionId(definitionId)
        .withDockerRepository(connectorRepository)
        .withDockerImageTag("0.1.2")
        .withName("random-name")
        .withProtocolVersion(DEFAULT_PROTOCOL_VERSION)
        .withTombstone(false)
        .withMaxSecondsBetweenMessages(MockData.DEFAULT_MAX_SECONDS_BETWEEN_MESSAGES);
    writeSource(sourceDef);
    assertEquals(sourceDef, configRepository.getStandardSourceDefinition(sourceDef.getSourceDefinitionId()));
    // TODO assertions on ADVs
  }

  @Test
  void filterCustomSource() {
    final Map<String, ConnectorInfo> sourceMap = new HashMap<>();
    final String nonCustomKey = "non-custom";
    final String customKey = "custom";
    sourceMap.put(nonCustomKey, new ConnectorInfo("id", Jsons.jsonNode(SOURCE_POSTGRES)));
    sourceMap.put(customKey, new ConnectorInfo("id", Jsons.jsonNode(SOURCE_CUSTOM)));

    final Map<String, ConnectorInfo> filteredSrcMap = migrator.filterCustomConnector(sourceMap, STANDARD_SOURCE_DEFINITION);

    assertThat(filteredSrcMap).containsOnlyKeys(nonCustomKey);
  }

  @Test
  void filterCustomDestination() {
    final Map<String, ConnectorInfo> sourceMap = new HashMap<>();
    final String nonCustomKey = "non-custom";
    final String customKey = "custom";
    sourceMap.put(nonCustomKey, new ConnectorInfo("id", Jsons.jsonNode(DESTINATION_S3)));
    sourceMap.put(customKey, new ConnectorInfo("id", Jsons.jsonNode(DESTINATION_CUSTOM)));

    final Map<String, ConnectorInfo> filteredDestMap = migrator.filterCustomConnector(sourceMap, STANDARD_DESTINATION_DEFINITION);

    assertThat(filteredDestMap).containsOnlyKeys(nonCustomKey);
  }

}
