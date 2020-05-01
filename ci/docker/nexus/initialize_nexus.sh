#!/bin/bash

temp_logfile=$(mktemp)

${SONATYPE_DIR}/start-nexus-repository-manager.sh > ${temp_logfile} 2>&1 &
pid=$!
echo "Nexus pid: ${pid}, stdout to ${temp_logfile}, job: $(jobs)"

while IFS= read -r LOGLINE || [[ -n "$LOGLINE" ]]; do
	printf '%s\n' "$LOGLINE"
	if [[ "${LOGLINE}" == *"Started Sonatype Nexus"* ]]; then
		break
	fi; 
done < <(timeout 300 tail -f ${temp_logfile})

pkill tail

echo "Nexus initialized, killing process ${pid}..."
kill -9 $pid

echo "Removing temporary log file ${temp_logfile}..."
rm ${temp_logfile}

exit 0