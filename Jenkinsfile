Map pipelineParams = [
    mavenDeployGoal: "deploy -DskipTests -DskipITs -Dinvoker.skip=true -Darchetype.test.skip -Dlicense.skip=true -Drevapi.skip -DmuleModule.analyze.skip -Dmaven.resources.skip",
    agentLabel: "windows2019-heavy",
    mavenAdditionalArgs: "-DwhitelistRepositories=false -DruntimeVersion=4.2.0",
]

runtimeExtensionsBuild(pipelineParams)
