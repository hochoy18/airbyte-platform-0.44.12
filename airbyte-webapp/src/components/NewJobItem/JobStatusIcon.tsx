import { JobWithAttempts } from "components/JobItem/types";
import { didJobSucceed, getJobStatus } from "components/JobItem/utils";
import { StatusIcon } from "components/ui/StatusIcon";

import { AttemptInfoRead, JobStatus } from "core/request/AirbyteClient";

import { partialSuccessCheck } from "./NewJobItem";

interface JobStatusIconProps {
  job: JobWithAttempts;
}

export const JobStatusIcon: React.FC<JobStatusIconProps> = ({ job }) => {
  const didSucceed = didJobSucceed(job);
  const jobStatus = getJobStatus(job);
  const isPartialSuccess = job.attempts && partialSuccessCheck(job.attempts);

  if (!isPartialSuccess && !didSucceed) {
    return <StatusIcon status="error" />;
  } else if (jobStatus === JobStatus.cancelled) {
    return <StatusIcon status="cancelled" />;
  } else if (jobStatus === JobStatus.running) {
    return <StatusIcon status="loading" />;
  } else if (jobStatus === JobStatus.succeeded) {
    return <StatusIcon status="success" />;
  } else if (isPartialSuccess) {
    return <StatusIcon status="warning" />;
  }
  return null;
};

interface AttemptStatusIconProps {
  attempt: AttemptInfoRead;
}

export const AttemptStatusIcon: React.FC<AttemptStatusIconProps> = ({ attempt }) => {
  return attempt.attempt.status === JobStatus.failed ? <StatusIcon status="error" /> : <StatusIcon status="success" />;
};
