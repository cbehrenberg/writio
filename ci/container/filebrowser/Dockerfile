# builds plain filebrowser without authentication
FROM filebrowser/filebrowser:v2.0.16 as filebrowser

FROM alpine:latest
RUN apk --no-cache --update add ca-certificates mailcap bash htop

VOLUME /srv
EXPOSE 80

COPY --from=filebrowser /filebrowser /filebrowser
COPY --from=filebrowser /.filebrowser.json /.filebrowser.json

# see https://github.com/filebrowser/filebrowser/issues/700
RUN /filebrowser config init --port 80 --address 0.0.0.0 --log "stdout" --root="/srv" --auth.method=noauth; \
    RANDOM_PASSWORD=$(tr -cd '[:alnum:]' < /dev/urandom | fold -w30 | head -n1); \
    /filebrowser users add user "${RANDOM_PASSWORD}"

ENTRYPOINT [ "/filebrowser" ]