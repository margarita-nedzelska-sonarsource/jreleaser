FROM {{dockerBaseImage}}

{{#dockerLabels}}
LABEL {{.}}
{{/dockerLabels}}

{{#dockerPreCommands}}
{{.}}
{{/dockerPreCommands}}

COPY assembly/{{distributionArtifactFileName}} /{{distributionArtifactFileName}}

RUN unzip {{distributionArtifactFileName}} && \
    rm {{distributionArtifactFileName}} && \
    chmod +x {{distributionArtifactName}}/bin/{{distributionExecutable}}

{{#dockerPostCommands}}
{{.}}
{{/dockerPostCommands}}

ENV PATH="${PATH}:/{{distributionArtifactName}}/bin"

ENTRYPOINT ["/{{distributionArtifactName}}/bin/{{distributionExecutable}}"]
