#Use GTI Linux Engineering RHEL7 w/ java image

FROM containerregistry-na-.jpmchase.net/container-base/managedbasedimages/oracle-jdk:17-stable

#URL to get source code for building the image(String)

LABEL org.opencontainers.image.source https://${REPO_HOSTNAME}/projects/${PROJECT}/repos/${REPOS}/browse?at=${COMMIT_HASH}

#source control revision identifier for the packaged software . Hash of the commit for this

LABEL org.opencontainers.image.title="java-template-canary"

#Default the target version to 0.0.0
ARG gavVersion=0.0.0

#Override the version from environment , if present (helpful with CI tools)
ENV gavVersion ${gavVersion}

#Copy applicationto container's /app folder

COPY --chown=nobody:nobody target/java-template-canary-${gavaVersion}.jar /app/
COPY --chown=nobody:nobody entrypoint.sh /app/

#change ownership to 'nobody' user aka account wiht least permission
RUN chown nobody /app/java-template-canary-${gavaVersion}.jar && chmod +x /app/java-template-canary-${gavVersion}.jar
RUN chmod +x /app/entrypoint.sh


#Run this container as 'nobody user. GKP does not allow running containers as root

USER 99

#command to run spring boot application
ENTRYPOINT["./app/entrypoint.sh"]