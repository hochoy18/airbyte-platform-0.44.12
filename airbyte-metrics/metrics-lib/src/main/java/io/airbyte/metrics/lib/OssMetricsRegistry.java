/*
 * Copyright (c) 2023 Airbyte, Inc., all rights reserved.
 */

package io.airbyte.metrics.lib;

import com.google.api.client.util.Preconditions;
import java.util.Arrays;
import java.util.List;

/**
 * Enum source of truth of all Airbyte metrics. Each enum value represent a metric and is linked to
 * an application and contains a description to make it easier to understand.
 * <p>
 * Each object of the enum actually represent a metric, so the Registry name is misleading. The
 * reason 'Registry' is in the name is to emphasize this enum's purpose as a source of truth for all
 * metrics. This also helps code readability i.e. AirbyteMetricsRegistry.metricA.
 * <p>
 * Metric Name Convention (adapted from
 * https://docs.datadoghq.com/developers/guide/what-best-practices-are-recommended-for-naming-metrics-and-tags/):
 * <p>
 * - Use lowercase. Metric names are case-sensitive.
 * <p>
 * - Use underscore to delimit names with multiple words.
 * <p>
 * - No spaces. This makes the metric confusing to read.
 * <p>
 * - Avoid numbers. This makes the metric confusing to read. Numbers should only be used as a
 * <p>
 * - Add units at name end if applicable. This is especially relevant for time units.
 * <p>
 * - Include the time period in the name if the metric is meant to be run at a certain interval.
 */
public enum OssMetricsRegistry implements MetricsRegistry {

  ACTIVITY_DBT_TRANSFORMATION(
      MetricEmittingApps.WORKER,
      "activity_dbt_transformation",
      "increments when we start a dbt transformation activity"),
  ACTIVITY_CHECK_CONNECTION(
      MetricEmittingApps.WORKER,
      "activity_check_connection",
      "increments when we start a check connection activity"),
  ACTIVITY_DISCOVER_CATALOG(
      MetricEmittingApps.WORKER,
      "activity_discover_catalog",
      "increments when we start a discover catalog activity"),
  ACTIVITY_NORMALIZATION(
      MetricEmittingApps.WORKER,
      "activity_normalization",
      "increments when we start a normalization activity"),
  ACTIVITY_NORMALIZATION_SUMMARY_CHECK(
      MetricEmittingApps.WORKER,
      "activity_normalization_summary_check",
      "increments when we start a normalization summary check activity"),
  ACTIVITY_PERSIST_STATE(
      MetricEmittingApps.WORKER,
      "activity_persist_state",
      "increments when we start a persist state activtiy"),
  ACTIVITY_REFRESH_SCHEMA(
      MetricEmittingApps.WORKER,
      "activity_refresh_schema",
      "increments when we start a refresh schema activity"),
  ACTIVITY_REPLICATION(
      MetricEmittingApps.WORKER,
      "activity_replication",
      "increments when we start a replication activity"),
  ACTIVITY_SPEC(
      MetricEmittingApps.WORKER,
      "activity_spec",
      "increments when we start a spec activity"),
  ACTIVITY_SUBMIT_CHECK_DESTINATION_CONNECTION(
      MetricEmittingApps.WORKER,
      "activity_submit_check_destination_connection",
      "increments when we start a submit check connection activity"),
  ACTIVITY_SUBMIT_CHECK_SOURCE_CONNECTION(
      MetricEmittingApps.WORKER,
      "activity_submit_check_source_connection",
      "increments when we start a submit check connection activity"),
  ACTIVITY_WEBHOOK_OPERATION(
      MetricEmittingApps.WORKER,
      "activity_webhook_operation",
      "increments when we start a webhook operation activity"),

  ATTEMPT_CREATED_BY_RELEASE_STAGE(
      MetricEmittingApps.WORKER,
      "attempt_created_by_release_stage",
      "increments when a new attempt is created. attempts are double counted as this is tagged by release stage."),
  ATTEMPT_FAILED_BY_RELEASE_STAGE(
      MetricEmittingApps.WORKER,
      "attempt_failed_by_release_stage",
      "increments when an attempt fails. attempts are double counted as this is tagged by release stage."),
  ATTEMPT_FAILED_BY_FAILURE_ORIGIN(
      MetricEmittingApps.WORKER,
      "attempt_failed_by_failure_origin",
      "increments for every failure origin a failed attempt has. since a failure can have multiple origins, "
          + "a single failure can be counted more than once. tagged by failure origin and failure type."),
  ATTEMPT_SUCCEEDED_BY_RELEASE_STAGE(
      MetricEmittingApps.WORKER,
      "attempt_succeeded_by_release_stage",
      "increments when an attempts succeeds. attempts are double counted as this is tagged by release stage."),
  AUTHENTICATION_REQUEST(
      MetricEmittingApps.SERVER,
      "authentication_request",
      "increments when an authentication request is attempted."),
  EST_NUM_METRICS_EMITTED_BY_REPORTER(
      MetricEmittingApps.METRICS_REPORTER,
      "est_num_metrics_emitted_by_reporter",
      "estimated metrics emitted by the reporter in the last interval. this is estimated since the count is not precise."),
  JOB_CANCELLED_BY_RELEASE_STAGE(
      MetricEmittingApps.WORKER,
      "job_cancelled_by_release_stage",
      "increments when a job is cancelled. jobs are double counted as this is tagged by release stage."),
  JOB_CREATED_BY_RELEASE_STAGE(
      MetricEmittingApps.WORKER,
      "job_created_by_release_stage",
      "increments when a new job is created. jobs are double counted as this is tagged by release stage."),
  JOB_FAILED_BY_RELEASE_STAGE(
      MetricEmittingApps.WORKER,
      "job_failed_by_release_stage",
      "increments when a job fails. jobs are double counted as this is tagged by release stage."),
  JOB_SUCCEEDED_BY_RELEASE_STAGE(
      MetricEmittingApps.WORKER,
      "job_succeeded_by_release_stage",
      "increments when a job succeeds. jobs are double counted as this is tagged by release stage."),
  JSON_STRING_LENGTH(
      MetricEmittingApps.WORKER,
      "json_string_length",
      "string length of a raw json string"),
  KUBE_POD_PROCESS_CREATE_TIME_MILLISECS(
      MetricEmittingApps.WORKER,
      "kube_pod_process_create_time_millisecs",
      "time taken to create a new kube pod process"),

  NORMALIZATION_IN_DESTINATION_CONTAINER(
      MetricEmittingApps.WORKER,
      "normalization_in_destination_container",
      "increments when normalization is run in the destination container",
      MetricTags.CONNECTION_ID),
  NORMALIZATION_IN_NORMALIZATION_CONTAINER(
      MetricEmittingApps.WORKER,
      "normalization_in_normalization_container",
      "increments when normalization is run in the normalization container",
      MetricTags.CONNECTION_ID),
  NUM_ABNORMAL_SCHEDULED_SYNCS_IN_LAST_DAY(
      MetricEmittingApps.METRICS_REPORTER,
      "num_abnormal_scheduled_syncs_last_day",
      "number of abnormal syncs that have skipped at least 1 scheduled run in last day."),
  NUM_ACTIVE_CONN_PER_WORKSPACE(
      MetricEmittingApps.METRICS_REPORTER,
      "num_active_conn_per_workspace",
      "number of active connections per workspace"),
  NUM_PENDING_JOBS(
      MetricEmittingApps.METRICS_REPORTER,
      "num_pending_jobs",
      "number of pending jobs"),
  NUM_ORPHAN_RUNNING_JOBS(
      MetricEmittingApps.METRICS_REPORTER,
      "num_orphan_running_jobs",
      "number of jobs reported as running that as associated to connection inactive or deprecated"),
  NUM_RUNNING_JOBS(
      MetricEmittingApps.METRICS_REPORTER,
      "num_running_jobs",
      "number of running jobs"),
  NUM_DISTINCT_SCHEMA_VALIDATION_ERRORS_IN_STREAMS(MetricEmittingApps.WORKER,
      "record_schema_validation_error",
      "number of validation errors for a given stream"),
  NUM_UNEXPECTED_FIELDS_IN_STREAMS(MetricEmittingApps.WORKER,
      "schemas_unexpected_fields",
      "number of unexpected (top level) fields for a given stream"),
  NUM_TOTAL_SCHEDULED_SYNCS_IN_LAST_DAY(
      MetricEmittingApps.METRICS_REPORTER,
      "num_total_scheduled_syncs_last_day",
      "number of total syncs runs in last day."),
  NUM_UNUSUALLY_LONG_SYNCS(
      MetricEmittingApps.METRICS_REPORTER,
      "num_unusually_long_syncs",
      "number of unusual long syncs compared to their historic performance."),
  OLDEST_PENDING_JOB_AGE_SECS(MetricEmittingApps.METRICS_REPORTER,
      "oldest_pending_job_age_secs",
      "oldest pending job in seconds"),
  OLDEST_RUNNING_JOB_AGE_SECS(MetricEmittingApps.METRICS_REPORTER,
      "oldest_running_job_age_secs",
      "oldest running job in seconds"),
  OVERALL_JOB_RUNTIME_IN_LAST_HOUR_BY_TERMINAL_STATE_SECS(MetricEmittingApps.METRICS_REPORTER,
      "overall_job_runtime_in_last_hour_by_terminal_state_secs",
      "overall job runtime - scheduling and execution for all attempts - for jobs that reach terminal states in the last hour. "
          + "tagged by terminal states."),
  SOURCE_HEARTBEAT_FAILURE(MetricEmittingApps.ORCHESTRATOR,
      "source_hearbeat_failure",
      "Fail a replication because the source missed an heartbeat",
      MetricTags.CONNECTION_ID),
  SOURCE_TIME_SINCE_LAST_HEARTBEAT_MILLIS(MetricEmittingApps.ORCHESTRATOR,
      "source_time_since_last_heartbeat_millis",
      "Time since last heartbeat (message from a source) for a connection.",
      MetricTags.CONNECTION_ID),
  STATE_METRIC_TRACKER_ERROR(MetricEmittingApps.WORKER,
      "state_timestamp_metric_tracker_error",
      "number of syncs where the state timestamp metric tracker ran out of memory or "
          + "was unable to match destination state message to source state message"),
  TEMPORAL_WORKFLOW_ATTEMPT(MetricEmittingApps.WORKER,
      "temporal_workflow_attempt",
      "count of the number of workflow attempts"),
  TEMPORAL_WORKFLOW_SUCCESS(MetricEmittingApps.WORKER,
      "temporal_workflow_success",
      "count of the number of successful workflow syncs."),
  TEMPORAL_WORKFLOW_FAILURE(MetricEmittingApps.WORKER,
      "temporal_workflow_failure",
      "count of the number of workflow failures"),
  REPLICATION_BYTES_SYNCED(MetricEmittingApps.WORKER,
      "replication_bytes_synced",
      "number of bytes synced during replication"),
  REPLICATION_RECORDS_SYNCED(MetricEmittingApps.WORKER,
      "replication_records_synced",
      "number of records synced during replication"),
  RESET_REQUEST(MetricEmittingApps.WORKER,
      "reset_request",
      "number of requested resets"),
  STATE_BUFFERING(MetricEmittingApps.WORKER,
      "state_buffering",
      "number of state messages being buffered before a flush",
      MetricTags.GEOGRAPHY),
  STATE_COMMIT_ATTEMPT(MetricEmittingApps.WORKER,
      "state_commit_attempt",
      "number of attempts to commit states from the orchestrator/workers",
      MetricTags.GEOGRAPHY),
  STATE_COMMIT_ATTEMPT_FAILED(MetricEmittingApps.WORKER,
      "state_commit_attempt_failed",
      "number of failed attempts to commit states from the orchestrator/workers",
      MetricTags.GEOGRAPHY),
  STATE_COMMIT_ATTEMPT_SUCCESSFUL(MetricEmittingApps.WORKER,
      "state_commit_attempt_successful",
      "number of successful attempts to commit states from the orchestrator/workers",
      MetricTags.GEOGRAPHY),
  STATE_COMMIT_NOT_ATTEMPTED(MetricEmittingApps.WORKER,
      "state_commit_not_attempted",
      "number of attempts to commit states dropped due to an early termination",
      MetricTags.GEOGRAPHY),
  STATE_COMMIT_CLOSE_SUCCESSFUL(MetricEmittingApps.WORKER,
      "state_commit_close_successful",
      "number of final to connection exiting with the a successful final state flush",
      MetricTags.GEOGRAPHY),

  STATS_COMMIT_ATTEMPT(MetricEmittingApps.WORKER,
      "stats_commit_attempt",
      "number of attempts to commit stats from the orchestrator/workers",
      MetricTags.GEOGRAPHY),

  STATS_COMMIT_ATTEMPT_FAILED(MetricEmittingApps.WORKER,
      "stats_commit_attempt_failed",
      "number of failed attempts to commit stats from the orchestrator/workers",
      MetricTags.GEOGRAPHY),

  STATS_COMMIT_ATTEMPT_SUCCESSFUL(MetricEmittingApps.WORKER,
      "stats_commit_attempt_successful",
      "number of successful attempts to commit stats from the orchestrator/workers",
      MetricTags.GEOGRAPHY),

  STATS_COMMIT_NOT_ATTEMPTED(MetricEmittingApps.WORKER,
      "stats_commit_not_attempted",
      "number of attempts to commit stats dropped due to an early termination",
      MetricTags.GEOGRAPHY),
  STATS_COMMIT_CLOSE_SUCCESSFUL(MetricEmittingApps.WORKER,
      "stats_commit_close_successful",
      "number of final to connection exiting with the a successful final stats flush",
      MetricTags.GEOGRAPHY),
  STREAM_STATS_WRITE_NUM_QUERIES(MetricEmittingApps.WORKER,
      "stream_stats_write_num_queries",
      "number of separate queries to update the stream stats table"),
  @Deprecated
  // To be deleted along with PersistStateActivity
  STATE_COMMIT_ATTEMPT_FROM_PERSIST_STATE(MetricEmittingApps.WORKER,
      "state_commit_attempt_from_persist_state",
      "number of attempts to commit states from the PersistState activity",
      MetricTags.GEOGRAPHY),

  ATTEMPTS_CREATED(
      MetricEmittingApps.WORKER,
      "attempt_created",
      "increments when a new attempt is created. one is emitted per attempt",
      MetricTags.GEOGRAPHY,
      MetricTags.ATTEMPT_NUMBER,
      MetricTags.MIN_CONNECTOR_RELEASE_STATE,
      MetricTags.MAX_CONNECTOR_RELEASE_STATE),
  ATTEMPTS_COMPLETED(
      MetricEmittingApps.WORKER,
      "attempt_completed",
      "increments when a new attempt is completed. one is emitted per attempt",
      MetricTags.GEOGRAPHY,
      MetricTags.ATTEMPT_NUMBER,
      MetricTags.MIN_CONNECTOR_RELEASE_STATE,
      MetricTags.MAX_CONNECTOR_RELEASE_STATE,
      MetricTags.ATTEMPT_QUEUE,
      MetricTags.ATTEMPT_OUTCOME,
      MetricTags.FAILURE_ORIGIN, // only includes the first failure origin
      MetricTags.FAILURE_TYPE), // only includes the first failure type

  BREAKING_SCHEMA_CHANGE_DETECTED(MetricEmittingApps.SERVER,
      "breaking_change_detected",
      "a breaking schema change has been detected",
      MetricTags.CONNECTION_ID),

  NON_BREAKING_SCHEMA_CHANGE_DETECTED(MetricEmittingApps.SERVER,
      "non_breaking_change_detected",
      "a non breaking schema change has been detected",
      MetricTags.CONNECTION_ID),

  SCHEMA_CHANGE_AUTO_PROPAGATED(MetricEmittingApps.SERVER,
      "schema_change_auto_propagated",
      "a schema change have been propagated",
      MetricTags.CONNECTION_ID),

  INCONSISTENT_ACTIVITY_INPUT(MetricEmittingApps.WORKER,
      "inconsistent_activity_input",
      "whenever we detect a mismatch between the input and the actual config"),

  MISSING_APPLY_SCHEMA_CHANGE_INPUT(MetricEmittingApps.SERVER,
      "missing_apply_schema_change_input",
      "one expected value for applying the schema change is missing",
      MetricTags.SOURCE_ID);

  private final MetricEmittingApp application;
  private final String metricName;
  private final String metricDescription;

  // added this field to declare metric attributes, but we never read them.
  @SuppressWarnings("FieldCanBeLocal")
  private final List<String> metricTags;

  OssMetricsRegistry(final MetricEmittingApp application,
                     final String metricName,
                     final String metricDescription,
                     final String... metricTags) {
    Preconditions.checkNotNull(metricDescription);
    Preconditions.checkNotNull(application);

    this.application = application;
    this.metricName = metricName;
    this.metricDescription = metricDescription;
    this.metricTags = Arrays.asList(metricTags);
  }

  @Override
  public MetricEmittingApp getApplication() {
    return application;
  }

  @Override
  public String getMetricName() {
    return metricName;
  }

  @Override
  public String getMetricDescription() {
    return metricDescription;
  }

}
