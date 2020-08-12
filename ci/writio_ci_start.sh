#!/bin/bash

ask() {

    local prompt default reply

    if [ "${2:-}" = "Y" ]; then
        prompt="Y/n"
        default=Y
    elif [ "${2:-}" = "N" ]; then
        prompt="y/N"
        default=N
    else
        prompt="y/n"
        default=
    fi

    while true; do

        echo -n "$1 [$prompt] "

        read reply </dev/tty

        if [ -z "$reply" ]; then
            reply=$default
        fi

        case "$reply" in
            Y*|y*) return 0 ;;
            N*|n*) return 1 ;;
        esac
    done
}

if ask "Do you want to pull latest writio ci container image from DockerHub?"; then
	docker pull writio/jenkins:latest
else
	echo "Using the existing writio ci container image on the host..."
fi

docker stack deploy -c ./writio_ci.yaml writio_ci
	
echo "You can access Jenkins on localhost:48080 now!"

if ask "Do you want to attach to StdOut? (can be safely exited with Ctrl+C)"; then
	docker service logs writio_ci_jenkins -f
fi
	
echo "To stop, run ./writio_ci_stop.sh"
exit 0