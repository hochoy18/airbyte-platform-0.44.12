import React, { useMemo } from "react";

import { useConnectionSyncContext } from "components/connection/ConnectionSync/ConnectionSyncContext";
import { JobItem } from "components/JobItem";
import { JobWithAttempts } from "components/JobItem/types";
import { NewJobItem } from "components/NewJobItem";

import { JobStatus, JobWithAttemptsRead } from "core/request/AirbyteClient";
import { useExperiment } from "hooks/services/Experiment";

interface JobsListProps {
  jobs: JobWithAttemptsRead[];
}

const JobsList: React.FC<JobsListProps> = ({ jobs }) => {
  const { activeJob } = useConnectionSyncContext();
  const searchableJobLogsEnabled = useExperiment("connection.searchableJobLogs", false);

  const sortJobReads: JobWithAttempts[] = useMemo(
    () =>
      jobs
        .filter((job): job is JobWithAttempts => !!job.job && !!job.attempts)
        .sort((a, b) => (a.job.createdAt > b.job.createdAt ? -1 : 1)),
    [jobs]
  );

  return (
    <div>
      {activeJob && activeJob.id !== sortJobReads?.[0]?.job?.id && (
        <JobItem
          key={`${activeJob.id}activeJob`}
          job={{ job: { ...activeJob, status: JobStatus.running }, attempts: [] }}
        />
      )}
      {searchableJobLogsEnabled &&
        sortJobReads.map((jobWithAttempts) => (
          <NewJobItem key={`newJobItem_${jobWithAttempts.job.id}`} jobWithAttempts={jobWithAttempts} />
        ))}
      {!searchableJobLogsEnabled &&
        sortJobReads.map((jobWithAttempts) => <JobItem key={jobWithAttempts.job.id} job={jobWithAttempts} />)}
    </div>
  );
};

export default JobsList;
