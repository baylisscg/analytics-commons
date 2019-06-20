#!/usr/bin/env bash

set -euo pipefail

CHANGE_LIST=""
NEXUS_SERVER="nexus"
NEXUS_URL="https://repo.eresearch.unimelb.edu.au/repository/maven-aurin/"

function run {

  if [[ -z ${BUILDKITE_TAG:-} ]]; then
    CHANGE_LIST="-${BUILDKITE_BUILD_NUMBER}"
  else
    echo "Building release"
  fi

  mvn clean deploy \
      -Pjdk7 \
      -DskipTests \
      -DskipITs \
      -Dchangelist="${CHANGE_LIST}" \
      -DaltDeploymentRepository="${NEXUS_SERVER}::${NEXUS_URL}"
}

run
